const repository = require("../repositories/attend_timeslot_repository");
const userRepository = require("../repositories/user_repository");
const attendanceRepository = require("../repositories/attendance_repository");
const holidayRepository = require("../repositories/hoilday_repository");
const leaveRepository = require("../repositories/leave_repository");
const dateHelper = require("../lib/date_helper");
const requestIp = require("request-ip");
const moment = require("moment");

module.exports = {
  getMethodSetting: async (req, res) => {
    const result = await attendanceRepository.getMethodSetting();
    if (!result.hasRecord()) {
      return res.status(400).send("No method found");
    }

    return res.json(result.results[0]);
  },

  updateMethodSetting: async (req, res) => {
    const wifiEnable = req.body.wifiEnable;
    const bleEnable = req.body.bleEnable;

    if (wifiEnable === undefined || bleEnable === undefined) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await attendanceRepository.updateMethodSetting({
      wifiEnable: wifiEnable,
      bleEnable: bleEnable,
    });

    if (!result.isSuccess) {
      return res.status(501).send("Server error");
    }

    return res.send("Success");
  },

  takeAttendance: async (req, res) => {
    const payload = req.body;
    const user = req.user;

    if (!payload || !user) {
      return res.status(400).send("Invalid parameters");
    }

    if (payload.actionType < 0 || payload.actionType > 1) {
      return res.status(400).send("Invalid action type");
    }

    const currentDatetime = moment();
    const currentDate = moment(currentDatetime.format("YYYY-MM-DD"));

    if (!currentDate.isSame(moment(payload.attendDate))) {
      return res.status(400).send("Invalid Date");
    }

    let result = await repository.getTimeslots(payload.timeslotId);
    if (!result.hasRecord()) {
      return res.status(404).send("Timeslot not found");
    }

    const slot = result.results[0];

    result = await repository.getTimeslotSettingsById(payload.timeslotId);
    if (!result.hasRecord()) {
      return res.status(404).send("Timeslot not found");
    }

    const slotSetting = result.results[0];
    const attendTime =
      payload.actionType === 0
        ? moment(currentDate.format("YYYY-MM-DD " + slot.end))
        : moment(currentDate.format("YYYY-MM-DD " + slot.start));

    const grace =
      payload.actionType === 0
        ? slotSetting.early_leave_grace
        : slotSetting.lateness_grace;

    const gracedTime = moment(attendTime).add(grace, "m");
    const rangeOfBeforeWork = moment(attendTime).subtract(
      slotSetting.range_before_work,
      "m"
    );
    const rangeOfAfterWork = moment(attendTime).add(
      slotSetting.range_after_work,
      "m"
    );
    const rangeOfBeforeLeave = moment(attendTime).subtract(
      slotSetting.range_before_leave,
      "m"
    );
    const rangeOfAfterLeave = moment(attendTime).add(
      slotSetting.range_after_leave,
      "m"
    );

    payload.late = 0;
    payload.earlyLeave = 0;
    payload.overtime = 0;

    if (payload.actionType === 0) {
      if (
        (slotSetting.range_before_leave >= 0 &&
          currentDatetime.isBefore(rangeOfBeforeLeave)) ||
        (slotSetting.range_after_leave >= 0 &&
          currentDatetime.isAfter(rangeOfAfterLeave))
      ) {
        return res.status(401).send("Invalid time.");
      }

      if (currentDatetime.isBefore(gracedTime)) {
        const earlyTime = moment(attendTime).subtract({
          hours: currentDatetime.hours(),
          minutes: currentDatetime.minutes(),
        });
        payload.earlyLeave = moment
          .duration(earlyTime.format("HH:mm"))
          .asMinutes();

        console.log(payload.earlyLeave);
      }
    } else if (payload.actionType === 1) {
      if (
        (slotSetting.range_before_work >= 0 &&
          currentDatetime.isBefore(rangeOfBeforeWork)) ||
        (slotSetting.range_after_work >= 0 &&
          currentDatetime.isAfter(rangeOfAfterWork))
      ) {
        return res.status(401).send("Invalid time.");
      }

      if (currentDatetime.isAfter(gracedTime)) {
        const lateTime = moment(currentDatetime).subtract({
          hours: attendTime.hours(),
          minutes: attendTime.minutes(),
        });
        payload.late = moment.duration(lateTime.format("HH:mm")).asMinutes();
      }
    }

    payload.ip = requestIp.getClientIp(req);
    payload.success = true;
    payload.employeeId = user.employeeId;
    payload.dateTime = currentDatetime.format("YYYY-MM-DD HH:mm:ss");

    result = await attendanceRepository.takeAttendance(payload);
    if (!result.isSuccess) {
      return res.status(500).send("Internal error");
    }

    return res.send("Success");
  },

  getAttendance: async (req, res) => {
    if (
      !req.body.startDate ||
      req.body.startDate.length <= 0 ||
      !req.body.endDate ||
      req.body.endDate.length <= 0 ||
      !req.params.id ||
      req.params.id.length <= 0
    ) {
      return res.status(400).send("Invalid parameters");
    }

    const startDate = moment(req.body.startDate);
    const endDate = moment(req.body.endDate);
    const employeeId = req.params.id;

    let query = await attendanceRepository.getAttendanceByEmployeeIdInRange(
      employeeId,
      startDate.format("YYYY-MM-DD"),
      endDate.format("YYYY-MM-DD")
    );

    if (!query.isSuccess) {
      return res.status(500).send("Internal server error");
    }

    const attendLogs = query.toArray();

    let holidays = [];
    query = await holidayRepository.getHoildaysByYear(
      startDate.year().toString()
    );

    if (query.isSuccess) {
      holidays = query.toArray();
    }

    let leave = [];
    query = await leaveRepository.getLeaveApplicationsByEmployeeId(employeeId);
    if (query.isSuccess) {
      leave = query.toArray();
    }

    const result = {
      startDate: startDate.format("YYYY-MM-DD"),
      endDate: endDate.format("YYYY-MM-DD"),
      totalAttendDays: 0,
      totalWorkingHours: 0.0,
      totalLateHours: 0.0,
      totalEarlyLeaveHours: 0.0,
      totalOvertimeHours: 0.0,
      logs: attendLogs,
      brief: [],
    };

    const sdate = startDate;
    while (sdate.isSameOrBefore(endDate, "day")) {
      const briefObj = {
        date: sdate.format("YYYY-MM-DD"),
        status: "",
        inTime: "",
        outTime: "",
        lateHour: 0,
        earlyLeaveHour: 0,
        otHour: 0,
        leaveTypeId: 0,
        timeslotName: "",
        workingHours: 0,
      };

      const found = attendLogs.filter(({ date_time }) =>
        moment(date_time).isSame(sdate, "day")
      );

      if (found.length <= 0) {
        const leaveApplication = leave.find(
          ({ start_date, end_date }) =>
            sdate.isSameOrAfter(moment(start_date)) &&
            sdate.isSameOrBefore(moment(end_date))
        );

        if (holidays.find(({ date }) => moment(date).isSame(sdate))) {
          briefObj.status = "Public Holiday";
        } else if (leaveApplication !== undefined) {
          briefObj.status = "Leave";
          briefObj.leaveTypeId = leaveApplication.leave_type_id;
        } else if (sdate.day() == 0 || sdate.day() == 6) {
          briefObj.status = "Day-off";
        } else if (sdate.isBefore(moment(), "days")) {
          briefObj.status = "Absent";
        }
      } else {
        for (const log of found) {
          result.totalWorkingHours += log.total_working_hours;
          if (log.action_type == 1) {
            if (log.late) {
              briefObj.status = "Late";
              briefObj.lateHour = log.late / 60.0;
              result.totalLateHours += briefObj.lateHour;
              result.totalWorkingHours -= briefObj.lateHour;
            }

            briefObj.inTime = moment(log.date_time).format("HH:mm:ss");
            result.totalAttendDays += 1;
          } else if (log.action_type == 0) {
            if (log.early_leave) {
              briefObj.earlyLeaveHour = log.early_leave / 60.0;
              result.totalEarlyLeaveHours += briefObj.earlyLeaveHour;
              result.totalWorkingHours -= briefObj.earlyLeaveHour;
            } else if (log.overtime) {
              briefObj.otHour = log.overtime / 60.0;
              result.totalOvertimeHours += briefObj.otHour;
              result.totalWorkingHours += briefObj.otHour;
            }

            briefObj.outTime = moment(log.date_time).format("HH:mm:ss");
          }

          briefObj.timeslotName = log.timeslot_name;
          briefObj.workingHours = log.total_working_hours;
        }
      }

      result.brief.push(briefObj);
      sdate.add(1, "days");
    }

    return res.json(result);
  },
};
