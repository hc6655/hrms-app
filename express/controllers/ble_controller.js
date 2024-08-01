const repository = require("../repositories/ble_repository");

module.exports = {
  getBleDevices: async (req, res) => {
    const result = await repository.getBleDevices();
    if (!result.hasRecord()) {
      return res.status(404).send("No device");
    }

    return res.json({ devices: result.toArray() });
  },

  addBleDevice: async (req, res) => {
    const mac = req.body.mac;
    const name = req.body.name;

    if (mac === undefined || name === undefined || mac.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.getBleDevice(mac);
    if (result.isSuccess && result.hasRecord()) {
      return res.status(409).send("Duplicated ID");
    }

    result = await repository.addBleDevice(req.body);
    if (result.isSuccess) {
      return res.json({ isSuccess: true });
    } else {
      return res.status(400).send(result.errString());
    }
  },

  removeBleDevice: async (req, res) => {
    const mac = req.body.mac;
    if (mac === undefined || mac.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.removeBleDevice(mac);
    if (result.isSuccess) {
      return res.json({ isSuccess: true });
    } else {
      return res.status(400).send(result.errString());
    }
  },
};
