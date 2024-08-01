const db = require("../lib/db");

module.exports = {
  getRoles: async () => {
    return await db.query(
      "SELECT r.*, d.title AS department_title FROM role r, department d WHERE r.department_id=d.id"
    );
  },

  getRole: async (id) => {
    return await db.query(
      "SELECT r.*, d.title AS department_title FROM role r, department d WHERE r.id=? AND r.department_id=d.id",
      [id]
    );
  },

  createRole: async (title, departmentId, permissionId) => {
    return await db.query(
      "INSERT INTO role (title, department_id, permission_id) value (?, ?)",
      [title, departmentId, permissionId]
    );
  },

  updateRole: async (id, title, departmentId, permissionId) => {
    return await db.query(
      "UPDATE role SET title=?, department_id=?, permission_id=? WHERE id=?",
      [title, departmentId, permissionId, id]
    );
  },

  deleteRole: async (id) => {
    return await db.query("DELETE FROM role WHERE id=?", [id]);
  },
};
