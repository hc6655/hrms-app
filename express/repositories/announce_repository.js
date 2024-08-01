const db = require("../lib/db");

module.exports = {
  getAnnouncements: async () => {
    return await db.query(
      "SELECT a.*, e.nick_name AS publisher FROM announcement a, employee e WHERE a.publish_employee_id=e.id AND a.removed=0 ORDER BY a.publish_date DESC"
    );
  },

  getAnnouncementsLimit: async (limit) => {
    return await db.query(
      "SELECT a.*, e.nick_name AS publisher FROM announcement a, employee e WHERE a.publish_employee_id=e.id AND a.removed=0 ORDER BY a.publish_date DESC LIMIT ?",
      [limit]
    );
  },

  getAnnouncementById: async (id) => {
    return await db.query(
      "SELECT a.*, e.nick_name AS publisher FROM announcement a, employee e WHERE a.id=? AND a.publish_employee_id=e.id",
      [id]
    );
  },

  createAnnouncement: async (data) => {
    return await db.query(
      "INSERT INTO announcement (title, content, publish_date, publish_employee_id, is_push_notification) VALUE (?, ?, ?, ?, ?)",
      [
        data.title,
        data.content,
        data.publishDate,
        data.employeeId,
        data.isPushNotification,
      ]
    );
  },

  updateAnnouncement: async (id, data) => {
    return await db.query(
      "UPDATE announcement SET title=?, content=? WHERE id=?",
      [data.title, data.content, id]
    );
  },

  removeAnnouncement: async (id) => {
    return await db.query("UPDATE announcement SET removed=1 WHERE id=?", [id]);
  },
};
