const repository = require("../repositories/log_repository");

module.exports = {
  getLoginLog: async (req, res) => {
    const result = await repository.getLoginLog();
    if (!result.isSuccess) {
      return res.status(501).send("Server Error");
    }

    return res.json({ logs: result.toArray() });
  },

  getManageEmployeeLog: async (req, res) => {
    const result = await repository.getManageEmployeeLog();
    if (!result.isSuccess) {
      return res.status(501).send("Server Error");
    }

    return res.json({ logs: result.toArray() });
  },

  getManageLeaveLog: async (req, res) => {
    const result = await repository.getManageLeaveLog();
    if (!result.isSuccess) {
      return res.status(501).send("Server Error");
    }

    return res.json({ logs: result.toArray() });
  },

  getTakeAttendanceLog: async (req, res) => {
    const result = await repository.getTakeAttendanceLog();
    if (!result.isSuccess) {
      return res.status(501).send("Server Error");
    }

    return res.json({ logs: result.toArray() });
  },
};
