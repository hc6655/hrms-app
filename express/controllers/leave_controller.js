const repository = require("../repositories/leave_repository");
const logRepository = require("../repositories/log_repository");
const dateHelper = require("../lib/date_helper");
const requestIp = require("request-ip");

const createManageLeaveLog = async (payload, appends = undefined) => {
  if (
    payload.req === undefined ||
    payload.req.user === undefined ||
    payload.applicationId === undefined ||
    payload.status === undefined
  ) {
    return;
  }

  if (appends !== undefined) {
    payload = { ...payload, ...appends };
  }

  payload.dateTime = dateHelper.getCurrentDateTime();
  payload.operatorId = payload.req.user.employeeId;
  payload.message = payload.message === undefined ? "" : payload.message;
  payload.reason = payload.reason === undefined ? "" : payload.reason;
  payload.ip = requestIp.getClientIp(payload.req);

  await logRepository.createManageLeaveLog(payload);
};

module.exports = {
  getLeaveTypes: async (req, res) => {
    const result = await repository.getLeaveTypes();
    return res.json({ leaveTypes: result.toArray() });
  },

  getUserLeaveBalance: async (req, res) => {
    const id = req.params.id;
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.getUserLeaveBalance(id);
    if (!result.hasRecord()) {
      return res.status(404).send("User not found");
    }

    return res.json({ balances: result.toArray() });
  },

  createLeaveApplication: async (req, res) => {
    const body = req.body;
    const employeeId = req.params.id;

    if (
      !body.startDate ||
      body.startDate.length <= 0 ||
      !body.endDate ||
      body.endDate.length <= 0 ||
      !body.days ||
      body.days < 0.5 ||
      !body.leaveTypeId ||
      body.leaveTypeId <= 0 ||
      !employeeId ||
      employeeId.length <= 0
    ) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.getUserLeaveBalanceByLeaveType(
      employeeId,
      body.leaveTypeId
    );
    if (!result.isSuccess) {
      return res.status(400).send("Balance less than or equal to zero");
    }

    let balance = result.results[0];
    if (balance.days_left <= 0.5) {
      return res.status(400).send("Balance less than or equal to zero");
    }

    let tzOffset = new Date().getTimezoneOffset() * 60000;
    body.applyDate = new Date(Date.now() - tzOffset)
      .toISOString()
      .slice(0, 19)
      .replace("T", " ");
    body.employeeId = employeeId;

    result = await repository.createLeaveApplication(body);
    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    await createManageLeaveLog({
      req: req,
      applicationId: result.results.insertId,
      status: 0,
    });

    return res.json({ applicationId: result.results.insertId.toString() });
  },

  getLeaveApplicationsByEmployeeId: async (req, res) => {
    const id = req.params.id;
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.getLeaveApplicationsByEmployeeId(id);
    if (!result.hasRecord()) {
      return res.status(404).send("No record");
    }

    return res.json({ applications: result.toArray() });
  },

  getLeaveApplications: async (req, res) => {
    const id = req.user.employeeId;
    if (!id || id.length <= 0) {
      return res.status(403).send("Authentication failed");
    }

    const result = await repository.getLeaveApplicationsWithIgnoreId(id);
    return res.json({ applications: result.toArray() });
  },

  getLeaveApplicationsById: async (req, res) => {
    const id = req.params.id;
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.getLeaveApplicationsById(id);
    if (!result.hasRecord()) {
      return res.status(404).send("Record not found");
    }

    const application = result.results[0];

    result = await logRepository.getManageLeaveLogById(id);
    if (!result.isSuccess) {
      return res.status(501).send("Server error");
    }

    application.logs = result.toArray();

    return res.json(application);
  },

  updateLeaveApplicationStatus: async (req, res) => {
    const id = req.params.id;
    const status = req.body.status;
    const reason = req.body.reason;

    if (!id || id.length <= 0 || !status || status < 0) {
      return res.status(400).send("Invalid parameters");
    }

    if (status === 2 && (reason === undefined || reason.length <= 0)) {
      return res.status(400).send("Reason is required when rejecting");
    }

    let result = await repository.getLeaveApplicationsById(id);
    if (!result.hasRecord()) {
      return res.status(400).send("Application not found");
    }

    let application = result.results[0];

    if (application.status == status) {
      return res.status(400).send("The status is the same");
    }

    result = await repository.getUserLeaveBalanceByLeaveType(
      application.employee_id,
      application.leave_type_id
    );
    if (!result.hasRecord()) {
      return res.status(400).send("Leave balance not found");
    }

    let userBalance = result.results[0];
    let days = userBalance.days_left;
    if (status == 1) {
      days -= application.days;
    } else if (status == 2 && application.status == 1) {
      days += application.days;
    }

    result = await repository.updateUserLeaveBalance(
      application.employee_id,
      application.leave_type_id,
      days
    );
    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    result = await repository.updateLeaveApplicationStatus(id, status);
    if (!result.isSuccess) {
      result = await repository.updateUserLeaveBalance(
        application.employee_id,
        application.leave_type_id,
        userBalance.days_left
      );

      return res.status(400).send(result.errString());
    }

    await createManageLeaveLog({
      req: req,
      applicationId: id,
      status: status,
      reason: reason,
    });

    return res.json({ isSuccess: true });
  },
};
