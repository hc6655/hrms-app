const db = require('../lib/db');

module.exports = {
    getDepartments: async () => {
        return await db.query("SELECT * FROM department");
    },

    getDepartment: async (id) => {
        return await db.query("SELECT * FROM department WHERE id=?", [id]);
    },

    createDepartment: async (id, title) => {
        return await db.query("INSERT INTO department value (?, ?)", [id, title])
    },

    updateDepartment: async (id, title) => {
        return await db.query("UPDATE department SET title=? WHERE id=?", [title, id]);
    },

    deleteDepartment: async (id) => {
        return await db.query("DELETE FROM department WHERE id=?", [id]);
    },
};