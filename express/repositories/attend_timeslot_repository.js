const db = require("../lib/db");

module.exports = {
  createTimeslotSetting: async (payload) => {
    return await db.query(
      "INSERT INTO attend_timeslot_setting " +
        "(name, require_clock_in, require_clock_out, count_absent, count_late, count_leave_early, count_overtime, total_working_hours, lateness_grace, early_leave_grace, " +
        "overtime_start, valid_clock_in_range, range_before_work, range_after_work, range_before_leave, range_after_leave) " +
        "VALUE (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
      [
        payload.name,
        payload.require_clock_in,
        payload.require_clock_out,
        payload.count_absent,
        payload.count_late,
        payload.count_leave_early,
        payload.count_overtime,
        payload.total_working_hours,
        payload.lateness_grace,
        payload.early_leave_grace,
        payload.overtime_start,
        payload.valid_clock_in_range,
        payload.range_before_work,
        payload.range_after_work,
        payload.range_before_leave,
        payload.range_after_leave,
      ]
    );
  },

  updateTimeslotSetting: async (payload) => {
    return await db.query(
      "UPDATE attend_timeslot_setting SET " +
        "name=?, require_clock_in=?, require_clock_out=?, count_absent=?, count_late=?, count_leave_early=?, count_overtime=?, total_working_hours=?, " +
        "lateness_grace=?, early_leave_grace=?, overtime_start=?, valid_clock_in_range=?, " +
        "range_before_work=?, range_after_work=?, range_before_leave=?, range_after_leave=? " +
        "WHERE id=?",
      [
        payload.name,
        payload.require_clock_in,
        payload.require_clock_out,
        payload.count_absent,
        payload.count_late,
        payload.count_leave_early,
        payload.count_overtime,
        payload.total_working_hours,
        payload.lateness_grace,
        payload.early_leave_grace,
        payload.overtime_start,
        payload.valid_clock_in_range,
        payload.range_before_work,
        payload.range_after_work,
        payload.range_before_leave,
        payload.range_after_leave,
        payload.id,
      ]
    );
  },

  createTimeslot: async (payload) => {
    return await db.query(
      "INSERT INTO attend_timeslot (setting_id, start, end) VALUE (? ,? ,?)",
      [payload.settingId, payload.start, payload.end]
    );
  },

  removeTimeslotBySettingId: async (settingId) => {
    return await db.query("DELETE FROM attend_timeslot WHERE setting_id=?", [
      settingId,
    ]);
  },

  getTimeslotSettings: async () => {
    return await db.query("SELECT * FROM attend_timeslot_setting");
  },

  getTimeslotSettingsById: async (id) => {
    return await db.query("SELECT * FROM attend_timeslot_setting WHERE id=?", [
      id,
    ]);
  },

  getAllTimeslots: async () => {
    return await db.query("SELECT * FROM attend_timeslot");
  },

  getTimeslots: async (id) => {
    return await db.query(
      "SELECT id, start, end FROM attend_timeslot WHERE setting_id=?",
      [id]
    );
  },

  getTimeslotName: async () => {
    return await db.query("SELECT id, name FROM attend_timeslot_setting");
  },

  getTimeslotsWithName: async (id) => {
    return await db.query(
      "SELECT t.start, t.end, s.name, s.id, s.total_working_hours FROM attend_timeslot t, attend_timeslot_setting s WHERE t.setting_id=? AND t.setting_id=s.id",
      [id]
    );
  },

  addEmployeeCustomShift: async (payload) => {
    return db.query(
      "INSERT INTO attendance_custom_shift (employee_id, date, timeslot_id) VALUE (?, ?, ?)",
      [payload.employeeId, payload.date, payload.timeslotId]
    );
  },

  removeEmployeeCustomShift: async (id) => {
    return db.query("DELETE FROM attendance_custom_shift WHERE id=?", [id]);
  },

  getEmployeeCustomShift: async (employeeId) => {
    return db.query(
      "SELECT cs.*, ts.name AS timeslot_name, t.start, t.end " +
        "FROM attendance_custom_shift cs, attend_timeslot_setting ts, attend_timeslot t " +
        "WHERE cs.employee_id=? AND cs.timeslot_id=ts.id AND ts.id=t.setting_id",
      [employeeId]
    );
  },
};
