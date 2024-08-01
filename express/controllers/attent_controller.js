const repository = require("../repositories/attend_timeslot_repository");
const userRepository = require("../repositories/user_repository");
const holidayRepository = require("../repositories/hoilday_repository");
const leaveRepository = require("../repositories/leave_repository");
const moment = require("moment");

module.exports = {
  getAttendTime: async (req, res) => {
    const id = req.params.id;
    const startDate = moment(req.body.startDate);
    const endDate = moment(req.body.endDate);

    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await userRepository.getUserInfo(id);
    if (!result.hasRecord()) {
      return res.status(404).send("User not found");
    }

    const user = result.results[0];
    if (user.fixed_working_timeslot_id === undefined) {
      return res.status(404).send("Timeslot setting not found");
    }

    let customShift = [];
    result = await repository.getEmployeeCustomShift(id);
    if (result.hasRecord()) {
      customShift = result.toArray();
    }

    const timeslotId = user.fixed_working_timeslot_id;

    result = await repository.getTimeslotsWithName(timeslotId);
    if (!result.hasRecord()) {
      return res.status(404).send("Timeslot not found");
    }

    const timeslot = result.results[0];

    let holidays = [];

    result = await holidayRepository.getHoildaysByYear(
      startDate.year().toString()
    );
    if (result.hasRecord()) {
      holidays = result.toArray();
    }

    let leave = [];

    result = await leaveRepository.getLeaveApplicationsByEmployeeId(id);
    if (result.isSuccess) {
      leave = result.toArray();
    }

    const slots = [];

    const current = moment(startDate);
    while (current.isSameOrBefore(endDate)) {
      const obj = {
        id: timeslot.id,
        date: current.format("YYYY-MM-DD"),
        start: timeslot.start.substring(0, 5),
        end: timeslot.end.substring(0, 5),
      };

      const leaveApplication = leave.find(
        ({ start_date, end_date }) =>
          current.isSameOrAfter(moment(start_date)) &&
          current.isSameOrBefore(moment(end_date))
      );

      const foundHoliday = holidays.find(({ date }) =>
        moment(date).isSame(current)
      );

      if (foundHoliday !== undefined) {
        obj.id = -1;
        obj.name = "Public Holiday";
      } else if (leaveApplication !== undefined) {
        obj.id = -1;
        obj.name = leaveApplication.name;
      } else if (current.day() == 0 || current.day() == 6) {
        obj.id = -1;
        obj.name = "Day-off";
      } else {
        const customShiftFound = customShift.find(({ date }) =>
          moment(date).isSame(current)
        );
        if (customShiftFound !== undefined) {
          result = await repository.getTimeslotsWithName(
            customShiftFound.timeslot_id
          );
          if (result.hasRecord()) {
            const customslot = result.results[0];
            obj.id = customslot.id;
            obj.name = customslot.name;
            obj.start = customslot.start.substring(0, 5);
            obj.end = customslot.end.substring(0, 5);
          }
        } else {
          obj.name = timeslot.name;
        }
      }

      slots.push(obj);
      current.add(1, "days");
    }

    return res.json({ slots: slots });
  },
};
