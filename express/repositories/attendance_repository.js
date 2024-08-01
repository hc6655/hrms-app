const db = require("../lib/db");

module.exports = {
  getMethodSetting: async () => {
    return db.query(
      "SELECT wifi_enable, ble_enable FROM attendance_device_setting WHERE id='HRMS'"
    );
  },

  updateMethodSetting: async (payload) => {
    return db.query(
      "UPDATE attendance_device_setting SET wifi_enable=?, ble_enable=? WHERE id='HRMS'",
      [payload.wifiEnable, payload.bleEnable]
    );
  },

  takeAttendance: async (payload) => {
    return db.query(
      "INSERT INTO attendance " +
        "(employee_id, attend_date, date_time, device, ip, action_type, timeslot_id, success, late, early_leave, overtime) " +
        "VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
      [
        payload.employeeId,
        payload.attendDate,
        payload.dateTime,
        payload.device,
        payload.ip,
        payload.actionType,
        payload.timeslotId,
        payload.success,
        payload.late,
        payload.earlyLeave,
        payload.overtime,
      ]
    );
  },

  getAttendanceByEmployeeId: async (employeeId) => {
    return db.query("SELECT * FROM attendance WHERE employee_id=?", employeeId);
  },

  getAttendanceByEmployeeIdInRange: async (employeeId, startDate, endDate) => {
    return db.query(
      "SELECT a.*, s.name AS timeslot_name, s.total_working_hours  FROM attendance a, attend_timeslot_setting s WHERE a.employee_id=? AND a.timeslot_id=s.id AND attend_date BETWEEN ? AND ?",
      [employeeId, startDate, endDate]
    );
  },
};
