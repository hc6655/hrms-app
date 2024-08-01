const db = require('../lib/db');

module.exports = {
    getFCMToken: async (employeeId) => {
        return await db.query("SELECT token FROM fcm_token WHERE employee_id=?", [employeeId]);
    },

    setFCMToken: async (employeeId, token) => {
        return await db.query("UPDATE fcm_token SET token=? WHERE employee_id=?", [token, employeeId]);
    },

    createFCMTokenRecord: async (employeeId) => {
        return await db.query("INSERT INTO fcm_token (employee_id) VALUE (?)", [employeeId]);
    }
}