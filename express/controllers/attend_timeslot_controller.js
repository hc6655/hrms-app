const repository = require("../repositories/attend_timeslot_repository");

module.exports = {
  createAttendTimeslot: async (req, res) => {
    const payload = req.body;
    if (!payload || payload.length <= 0) {
      return res.status(400).send("Invalid Parameters");
    }

    if (
      !payload.name ||
      payload.name.length <= 0 ||
      !payload.slots ||
      payload.slots.length <= 0
    ) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.createTimeslotSetting(payload);
    if (!result.isSuccess) {
      return res.status(501).send("Internal server error.");
    }

    const id = result.results.insertId;

    const slots = payload.slots;
    for (const e of slots) {
      result = await repository.createTimeslot({
        settingId: id,
        start: e.start,
        end: e.end,
      });

      if (!result.isSuccess) {
        break;
      }
    }

    if (!result.isSuccess) {
      return res.status(501).send("Internal server error.");
    }

    return res.json({ id: payload.settingId });
  },

  updateAttendTimeslot: async (req, res) => {
    const payload = req.body;
    if (!payload || payload.length <= 0) {
      return res.status(400).send("Invalid Parameters");
    }

    if (
      !payload.name ||
      payload.name.length <= 0 ||
      !payload.slots ||
      payload.slots.length <= 0 ||
      !payload.id
    ) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.removeTimeslotBySettingId(payload.id);
    if (!result.isSuccess) {
      return res.status(501).send("Internal server error.");
    }

    const slots = payload.slots;
    for (const e of slots) {
      result = await repository.createTimeslot({
        settingId: payload.id,
        start: e.start,
        end: e.end,
      });

      if (!result.isSuccess) {
        break;
      }
    }

    if (!result.isSuccess) {
      return res.status(501).send("Internal server error.");
    }

    result = await repository.updateTimeslotSetting(payload);
    if (!result.isSuccess) {
      return res.status(501).send("Internal server error.");
    }

    return res.send("Success");
  },

  getAttendTimeslots: async (req, res) => {
    let result = await repository.getTimeslotSettings();
    if (!result.isSuccess || !result.hasRecord()) {
      return res.start(404).send("No record");
    }

    const settings = result.toArray();
    for (const e of settings) {
      result = await repository.getTimeslots(e.id);
      if (!result.isSuccess) {
        e.slots = [];
      } else {
        e.slots = result.toArray().map((e) => {
          const d = {
            id: e.id,
            start: e.start.substring(0, 5),
            end: e.end.substring(0, 5),
          };

          return d;
        });
      }
    }

    return res.json({ timeslots: settings });
  },

  getAttendTimeslotById: async (req, res) => {
    const id = req.params.id;
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.getTimeslotSettingsById(id);
    if (!result.isSuccess || !result.hasRecord()) {
      return res.status(404).send("No record");
    }

    const settings = result.toArray()[0];
    result = await repository.getTimeslots(id);
    if (!result.isSuccess) {
      settings.slots = [];
    } else {
      settings.slots = result.toArray().map((e) => {
        const d = {
          id: e.id,
          start: e.start.substring(0, 5),
          end: e.end.substring(0, 5),
        };

        return d;
      });
    }

    return res.json(settings);
  },

  getAttendTimeslotBrief: async (req, res) => {
    let result = await repository.getTimeslotName();
    if (!result.isSuccess || !result.hasRecord()) {
      return res.start(404).send("No record");
    }

    const settings = result.toArray();

    result = await repository.getAllTimeslots();
    if (!result.isSuccess || !result.hasRecord()) {
      return res.start(404).send("No record");
    }

    const slots = result.toArray();

    for (const setting of settings) {
      setting.slots = [];
      for (const slot of slots) {
        if (setting.id === slot.setting_id) {
          setting.slots.push({
            id: slot.id,
            start: slot.start.substring(0, 5),
            end: slot.end.substring(0, 5),
          });
        }
      }
    }

    return res.json({ timeslots: settings });
  },

  addCustomShift: async (req, res) => {
    const employeeId = req.params.id;
    const payload = req.body;

    if (
      employeeId == undefined ||
      employeeId.length <= 0 ||
      payload.date === undefined ||
      payload.date.length <= 0 ||
      payload.timeslotId === undefined ||
      payload.timeslotId < 0
    ) {
      return res.status(400).send("Invalid parameters");
    }

    payload.employeeId = employeeId;
    const result = await repository.addEmployeeCustomShift(payload);
    if (!result.isSuccess) {
      return res.status(501).send("Server Error");
    }

    return res.send("Success");
  },

  removeCustomShift: async (req, res) => {
    const id = req.params.id;

    if (id === undefined || id < 0) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.removeEmployeeCustomShift(id);
    if (!result.isSuccess) {
      return res.status(501).send("Server Error");
    }

    return res.send("Success");
  },

  getCustomShiftByEmployeeId: async (req, res) => {
    const employeeId = req.params.id;

    if (employeeId == undefined || employeeId.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.getEmployeeCustomShift(employeeId);
    if (!result.isSuccess) {
      return res.status(501).send("Server Error");
    }

    return res.json({ shifts: result.toArray() });
  },
};
