const db = require("../lib/db");

module.exports = {
  createAnnounceNotiLog: async (data) => {
    return await db.query(
      "INSERT INTO log_push_notification_announce (announcement_id, is_success, error_message, date_time, message_id) VALUE (?, ?, ?, ?, ?)",
      [data.id, data.isSuccess, data.errMessage, data.dateTime, data.messageId]
    );
  },

  createLoginLog: async (data) => {
    return await db.query(
      "INSERT INTO log_login (date_time, employee_id, ip, method, is_success, err_message) " +
        "VALUE (?, ?, ?, ?, ?, ?)",
      [
        data.dateTime,
        data.employeeId,
        data.ip,
        data.method,
        data.isSuccess,
        data.errMessage,
      ]
    );
  },

  getLoginLog: async () => {
    return await db.query("SELECT * FROM log_login ORDER BY id DESC");
  },

  getLoginLogByEmployeeId: async (id) => {
    return await db.query("SELECT * FROM log_login WHERE employee_id=?", [id]);
  },

  createManageEmployeeLog: async (data) => {
    return await db.query(
      "INSERT INTO log_manage_employee (date_time, operator_id, ip, manage_type, operated_id, message) " +
        "VALUE (?, ?, ?, ?, ?, ?)",
      [
        data.dateTime,
        data.operatorId,
        data.ip,
        data.manageType,
        data.operatedId,
        data.message,
      ]
    );
  },

  getManageEmployeeLog: async () => {
    return await db.query("SELECT * FROM log_manage_employee ORDER BY id DESC");
  },

  createManageLeaveLog: async (data) => {
    return await db.query(
      "INSERT INTO log_manage_leave (date_time, ip, operator_id, application_id, status, message, reason) " +
        "VALUE (?, ?, ?, ?, ?, ?, ?)",
      [
        data.dateTime,
        data.ip,
        data.operatorId,
        data.applicationId,
        data.status,
        data.message,
        data.reason,
      ]
    );
  },

  getManageLeaveLog: async () => {
    return await db.query(
      "SELECT l.*, a.employee_id AS applicant FROM log_manage_leave l, leave_application a WHERE l.application_id=a.id ORDER BY l.id DESC"
    );
  },

  getManageLeaveLogById: async (id) => {
    return await db.query(
      "SELECT l.*, a.employee_id AS applicant FROM log_manage_leave l, leave_application a WHERE l.application_id=? AND l.application_id=a.id ORDER BY l.id DESC",
      [id]
    );
  },

  getTakeAttendanceLog: async () => {
    return await db.query(
      "SELECT id, employee_id, date_time, ip, success FROM attendance ORDER BY id DESC"
    );
  },
};
