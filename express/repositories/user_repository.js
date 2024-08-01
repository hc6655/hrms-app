const db = require("../lib/db");

module.exports = {
  getUserAuth: async (id) => {
    return await db.query(
      "SELECT e.id, e.nick_name, a.password, p.access_level, p.management_feature, p.access_log " +
        "FROM employee e, authentication a, position p " +
        "WHERE e.id=? AND a.employee_id=e.id AND e.role_id=p.id",
      [id]
    );
  },

  getUserBiometricAuth: async (id) => {
    return await db.query(
      "SELECT e.id, e.nick_name, a.biometric_token, p.access_level, p.management_feature, p.access_log " +
        "FROM employee e, authentication a, position p " +
        "WHERE e.id=? AND a.employee_id=e.id AND e.role_id=p.id",
      [id]
    );
  },

  getUserPermission: async (id) => {
    return await db.query(
      "SELECT p.system_admin, p.hr_management, p.department_management " +
        "FROM permission p, employee e, role r " +
        "WHERE e.id=? AND e.role_id=r.id AND p.id=r.permission_id",
      [id]
    );
  },

  getUserInfo: async (id) => {
    return await db.query(
      "SELECT e.*, d.title AS department_title, p.name AS position_name, p.access_level, IFNULL(t.name, '') AS timeslot_name " +
        "FROM department d, position p, employee e left join attend_timeslot_setting t " +
        "ON e.fixed_working_timeslot_id=t.id " +
        "WHERE e.id=? AND e.department_id=d.id AND e.role_id=p.id",
      [id]
    );
  },

  getLastEmployeeId: async () => {
    return await db.query("SELECT id from employee ORDER BY id DESC LIMIT 1");
  },

  getUsers: async () => {
    return await db.query(
      "SELECT e.*, d.title AS department_title, p.name AS position_name, IFNULL(t.name, '') AS timeslot_name " +
        "FROM department d, position p, employee e left join attend_timeslot_setting t " +
        "ON e.fixed_working_timeslot_id=t.id " +
        "WHERE e.department_id=d.id AND e.role_id=p.id"
    );
  },

  createUser: async (data) => {
    return await db.query(
      "INSERT INTO employee value (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
      [
        data.id,
        data.first_name,
        data.last_name,
        data.role_id,
        data.department_id,
        data.phone,
        data.nick_name,
        data.fixed_working_timeslot_id,
        data.working_type,
        data.salary_type,
        data.salary,
        data.ot_allowance,
        data.ot_allowance_type,
      ]
    );
  },

  createUserAuth: async (id, pwd) => {
    return await db.query(
      "INSERT INTO authentication (employee_id, password) value (?, ?)",
      [id, pwd]
    );
  },

  updateUser: async (id, payload) => {
    return await db.query(
      "UPDATE employee SET first_name=?, last_name=?, role_id=?, department_id=?, phone=?, nick_name=?, fixed_working_timeslot_id=?, working_type=?, salary_type=?, salary=?, ot_allowance=?, ot_allowance_Type=? WHERE id=?",
      [
        payload.first_name,
        payload.last_name,
        payload.role_id,
        payload.department_id,
        payload.phone,
        payload.nick_name,
        payload.fixed_working_timeslot_id,
        payload.working_type,
        payload.salary_type,
        payload.salary,
        payload.ot_allowance,
        payload.ot_allowance_type,
        id,
      ]
    );
  },
  updateUserBiometricToken: async (id, token) => {
    return await db.query(
      "UPDATE authentication SET biometric_token=? WHERE employee_id=?",
      [token, id]
    );
  },

  getUserAccessLevel: async (id) => {
    return await db.query(
      "SELECT p.access_level FROM position p, employee e WHERE p.id=e.role_id AND e.id=?",
      [id]
    );
  },
};
