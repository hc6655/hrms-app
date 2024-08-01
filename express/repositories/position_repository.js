const db = require("../lib/db");

module.exports = {
  createPosition: async (payload) => {
    return await db.query(
      "INSERT INTO `position` (department_id, name, access_level, management_feature, access_log) " +
        "VALUE (?, ?, ?, ?, ?)",
      [
        payload.departmentId,
        payload.name,
        payload.accessLevel,
        payload.managementFeature,
        payload.accessLog,
      ]
    );
  },

  removePositionById: async (id) => {
    return await db.query("DELETE FROM position WHERE id=?", [id]);
  },

  updatePositionById: async (payload) => {
    return await db.query(
      "UPDATE position SET name=?, access_level=?, management_feature=?, access_log=? WHERE id=?",
      [
        payload.name,
        payload.accessLevel,
        payload.managementFeature,
        payload.accessLog,
        payload.id,
      ]
    );
  },

  getPositions: async () => {
    return await db.query(
      "SELECT p.*, d.title FROM position p, department d WHERE p.department_id=d.id"
    );
  },

  getPositionsByDepartmentId: async (departmentId) => {
    return await db.query(
      "SELECT p.*, d.title FROM position p, department d WHERE p.department_id=d.id AND d.id=?",
      [departmentId]
    );
  },

  removeAllPositionByDepartmentId: async (departmentId) => {
    return await db.query("DELETE FROM position WHERE department_id=?", [
      departmentId,
    ]);
  },

  getPositionById: async (id) => {
    return await db.query("SELECT * FROM position WHERE id=?", [id]);
  },
};
