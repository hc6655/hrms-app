const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const crypto = require("crypto");
const repository = require("../repositories/user_repository");
const leaveRepository = require("../repositories/leave_repository");
const fcmRepository = require("../repositories/fcm_repository");
const logRepository = require("../repositories/log_repository");
const positionRepository = require("../repositories/position_repository");
const { v4: uuidv4 } = require("uuid");
const dateHelper = require("../lib/date_helper");
const requestIp = require("request-ip");

const genEmployeeId = (latestId) => {
  const currentYear = parseInt(
    new Date().getFullYear().toString().substring(2, 4)
  );
  let year = parseInt(latestId.substring(0, 2));
  let order = parseInt(latestId.substring(2, 5));

  if (year != currentYear) {
    year = currentYear;
    order = 0;
  } else {
    order = order + 1;
  }

  return year.toString() + order.toString().padStart(3, "0");
};

const hashPassword = async (pwd) => {
  const sha = crypto.createHash("sha512").update(pwd).digest("hex");
  return await bcrypt.hash(sha, 10);
};

const createLoginLog = async (data) => {
  if (
    !data ||
    !data.id ||
    data.id.length <= 0 ||
    !data.method ||
    data.method.length <= 0
  ) {
    return;
  }

  const dateTime = dateHelper.getCurrentDateTime();
  const isSuccess = data.errMessage === undefined ? true : false;
  const errMessage = isSuccess ? "" : data.errMessage;
  const ip = requestIp.getClientIp(data.req);

  await logRepository.createLoginLog({
    dateTime: dateTime,
    employeeId: data.id,
    ip: ip,
    method: data.method,
    isSuccess: isSuccess,
    errMessage: errMessage,
  });
};

const createManageEmployeeLog = async (data, appends = undefined) => {
  if (
    !data ||
    !data.operatorId ||
    data.operatedId.length <= 0 ||
    !data.req ||
    data.manageType === undefined ||
    !data.operatedId ||
    data.operatedId.length <= 0
  ) {
    return;
  }

  if (appends !== undefined) {
    data = { ...data, ...appends };
  }

  await logRepository.createManageEmployeeLog({
    dateTime: dateHelper.getCurrentDateTime(),
    operatorId: data.operatorId,
    ip: requestIp.getClientIp(data.req),
    manageType: data.manageType,
    operatedId: data.operatedId,
    message: data.message === undefined ? "" : data.message,
  });

  return data;
};

module.exports = {
  login: async (req, res) => {
    const id = req.body.id.toString();
    const pwd = req.body.pwd.toString();

    if (!id || id.length <= 0 || !pwd || pwd.length <= 0) {
      const errMessage = "Invalid parameters";

      createLoginLog({
        req: req,
        method: "password",
        errMessage: errMessage,
        id: id,
      });

      return res.status(400).send(errMessage);
    }

    const result = await repository.getUserAuth(id);
    if (!result.isSuccess || result.results.length <= 0) {
      const errMessage = "ID or password is incorrect.";

      createLoginLog({
        req: req,
        method: "password",
        errMessage: errMessage,
        id: id,
      });

      return res.status(404).send(errMessage);
    }

    const user = result.results[0];
    const isMatch = await bcrypt.compare(pwd, user.password);
    if (!isMatch) {
      const errMessage = "ID or password is incorrect.";

      createLoginLog({
        req: req,
        method: "password",
        errMessage: errMessage,
        id: id,
      });

      return res.status(404).send(errMessage);
    }

    const token = jwt.sign(
      {
        employeeId: user.id,
        nickName: user.nick_name,
        accessLevel: user.access_level,
        managementFeature: user.management_feature,
        accessLog: user.access_log,
      },
      process.env.TOKEN_SECRET,
      {
        expiresIn: "12h",
      }
    );

    createLoginLog({
      req: req,
      method: "password",
      id: id,
    });

    return res.json({ isSuccess: true, token: token });
  },

  loginWithBiometric: async (req, res) => {
    const id = req.body.id.toString();
    const token = req.body.token.toString();

    if (!id || id.length <= 0 || !token || token.length <= 0) {
      const errMessage = "Invalid parameters";

      createLoginLog({
        req: req,
        method: "biometric",
        errMessage: errMessage,
        id: id,
      });

      return res.status(400).send(errMessage);
    }

    const result = await repository.getUserBiometricAuth(id);
    if (!result.isSuccess || result.results.length <= 0) {
      const errMessage = "ID is incorrect";

      createLoginLog({
        req: req,
        method: "biometric",
        errMessage: errMessage,
        id: id,
      });

      return res.status(404).send(errMessage);
    }

    const user = result.results[0];
    const isMatch = await bcrypt.compare(token, user.biometric_token);
    if (!isMatch) {
      const errMessage = "password is incorrect";

      createLoginLog({
        req: req,
        method: "biometric",
        errMessage: errMessage,
        id: id,
      });

      return res.status(404).send(errMessage);
    }

    const jwtToken = jwt.sign(
      {
        employeeId: user.id,
        nickName: user.nick_name,
        accessLevel: user.access_level,
        managementFeature: user.management_feature,
        accessLog: user.access_log,
      },
      process.env.TOKEN_SECRET,
      {
        expiresIn: "12h",
      }
    );

    createLoginLog({
      req: req,
      method: "biometric",
      id: id,
    });

    return res.json({ isSuccess: true, token: jwtToken });
  },

  getUserInfo: async (req, res) => {
    const id = req.params.id;
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const user = req.user;
    if (!user) {
      return res.status(401).send("Authentication failed");
    }

    let logPayload = {
      req: req,
      operatorId: user.employeeId,
      operatedId: id,
      manageType: 0,
    };

    let result = await repository.getUserAccessLevel(user.employeeId);
    if (!result.isSuccess || result.length() <= 0) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: "Permission denied",
      });
      return res.status(403).send(logPayload.message);
    }

    const userAccessLevel = result.results[0].access_level;

    result = await repository.getUserInfo(id);
    if (!result.isSuccess || result.length() <= 0) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: "User not found",
      });
      return res.status(404).send(logPayload.message);
    }

    const { access_level, ...info } = result.results[0];

    if (userAccessLevel < access_level) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: "Permission denied",
      });
      return res.status(403).send(logPayload.message);
    }

    await createManageEmployeeLog(logPayload);
    return res.json(info);
  },

  getUserPermission: async (req, res) => {
    const id = req.params.id;
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.getUserPermission(id);
    if (!result.isSuccess || result.length() <= 0) {
      return res.status(404).send("ID or password is incorrect.");
    }

    return res.json(result.results[0]);
  },

  createUser: async (req, res) => {
    const body = req.body;
    const user = req.user;

    if (!user) {
      return res.status(401).send("Authentication failed");
    }

    let logPayload = {
      req: req,
      operatorId: user.employeeId,
      operatedId: "No assigned",
      manageType: 1,
    };

    if (
      !body.first_name ||
      body.first_name.length <= 0 ||
      !body.last_name ||
      body.last_name.length <= 0 ||
      !body.department_id ||
      body.department_id.length <= 0 ||
      !body.phone ||
      body.phone.length <= 0 ||
      !body.role_id
    ) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: "Invalid parameters",
      });
      return res.status(400).send(logPayload.message);
    }

    let id = "00000";
    let result = await repository.getLastEmployeeId();
    if (result.length() <= 0) {
      if (!result.isSuccess) {
        return res.status(500);
      } else {
        result = await repository.getUsers();
        if (!result.isSuccess || result.length() > 0) {
          return res.status(500);
        }
      }
    } else {
      id = result.results[0].id;
    }

    body.id = genEmployeeId(id);

    result = await repository.createUser(body);
    if (!result.isSuccess) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: result.errString(),
        operatedId: body.id,
      });
      return res.status(400).send(logPayload.message);
    }

    const pwd = await hashPassword(body.id);
    result = await repository.createUserAuth(body.id, pwd);
    if (!result.isSuccess) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: result.errString(),
        operatedId: body.id,
      });
      return res.status(400).send(logPayload.message);
    }

    result = await leaveRepository.getLeaveTypes();
    if (!result.hasRecord()) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: result.errString(),
        operatedId: body.id,
      });
      return res.status(400).send(logPayload.message);
    }

    for (const leaveType of result.results) {
      const json = {
        leaveTypeId: leaveType.id,
        totalDays: leaveType.default_days,
        employeeId: body.id,
      };

      result = await leaveRepository.createUserLeaveBalance(json);
      if (!result.isSuccess) {
        logPayload = await createManageEmployeeLog(logPayload, {
          message: result.errString(),
          operatedId: body.id,
        });
        return res.status(400).send(logPayload.message);
      }
    }

    result = await fcmRepository.createFCMTokenRecord(body.id);
    if (!result.isSuccess) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: result.errString(),
        operatedId: body.id,
      });
      return res.status(400).send(logPayload.message);
    }

    await createManageEmployeeLog(logPayload, { operatedId: body.id });

    return res.json({ employee_id: body.id });
  },

  getUsers: async (req, res) => {
    const result = await repository.getUsers();
    if (!result.hasRecord()) {
      return res.status(404).send("No employees");
    }

    return res.json({ employees: result.toArray() });
  },

  updateUser: async (req, res) => {
    const id = req.params.id;
    const user = req.user;

    if (!user) {
      return res.status(401).send("Authentication failed");
    }

    let logPayload = {
      req: req,
      operatorId: user.employeeId,
      operatedId: id === undefined ? "Unknown" : id,
      manageType: 2,
    };

    if (!id || id.length <= 0) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: "Invalid parameters",
      });
      return res.status(400).send(logPayload.message);
    }

    const body = req.body;
    if (
      !body.first_name ||
      body.first_name.length <= 0 ||
      !body.last_name ||
      body.last_name.length <= 0 ||
      !body.department_id ||
      body.department_id.length <= 0 ||
      !body.phone ||
      body.phone.length <= 0 ||
      !body.role_id
    ) {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: "Invalid parameters",
      });
      return res.status(400).send(logPayload.message);
    }

    if (user.employeeId == id) {
      let result = await repository.getUserInfo(id);
      if (!result.hasRecord()) {
        logPayload = await createManageEmployeeLog(logPayload, {
          message: "User not found",
        });
        return res.status(400).send(logPayload.message);
      }

      const userInfo = result.results[0];

      body.role_id = userInfo.role_id;
      body.department_id = userInfo.department_id;
    }

    const result = await repository.updateUser(id, body);
    if (result.isSuccess) {
      await createManageEmployeeLog(logPayload);
      return res.json({ isSuccess: true });
    } else {
      logPayload = await createManageEmployeeLog(logPayload, {
        message: result.errString(),
      });
      return res.status(400).send(logPayload.message);
    }
  },
  updateUserBiometric: async (req, res) => {
    const id = req.params.id;
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const user = req.user;
    if (!user) {
      return res.status(401).send("Authentication failed");
    }

    if (user.employeeId !== id) {
      return res.status(401).send("Authentication failed");
    }

    const uuid = uuidv4();
    const sha = crypto.createHash("sha512").update(uuid).digest("hex");
    const hashToken = await bcrypt.hash(sha, 10);
    const result = await repository.updateUserBiometricToken(id, hashToken);

    if (!result.isSuccess) {
      return res.status(501).send("Server error");
    }

    return res.json({ token: sha });
  },
};
