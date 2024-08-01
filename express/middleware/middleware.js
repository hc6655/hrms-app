const jwt = require("jsonwebtoken");
const repository = require("../repositories/user_repository");

module.exports = {
  verifyToken: async (req, res, next) => {
    const token = req.headers["x-access-token"];

    if (!token) {
      return res.status(403).send("Authentication failed");
    }

    try {
      const decoded = jwt.verify(token, process.env.TOKEN_SECRET);
      req.user = decoded;
    } catch (err) {
      return res.status(401).send("Authentication failed");
    }

    return next();
  },

  requireHrManagement: async (req, res, next) => {
    /*const employeeId = req.user.employeeId;
        const result = await repository.getUserPermission(employeeId);
        if (!result.hasRecord()) {
            return res.status(401).send("Authentication failed");
        }

        const userPermission = result.results[0];
        if (userPermission.hr_management != 1) {
            return res.status(403).send("No Access Permission");
        }*/

    if (req.user.managementFeature != 1) {
      return res.status(403).send("No Access Permission");
    }

    return next();
  },
};
