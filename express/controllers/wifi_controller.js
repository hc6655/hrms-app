const repository = require("../repositories/wifi_repository");

module.exports = {
  getWifiDevices: async (req, res) => {
    const result = await repository.getWifiDevices();
    if (result.length() <= 0) {
      return res.status(404).send("No device");
    }

    return res.json({ devices: result.toArray() });
  },

  addWifiDevice: async (req, res) => {
    if (
      !req.body.bssid ||
      req.body.bssid.length <= 0 ||
      !req.body.ssid ||
      req.body.ssid.length <= 0
    ) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.getWifiDevice(req.body.bssid);
    if (result.isSuccess && result.results.length > 0) {
      return res.status(409).send("Duplicated ID");
    }

    result = await repository.addWifiDevice(req.body);
    if (result.isSuccess) {
      return res.json({ isSuccess: true });
    } else {
      return res.status(400).send(result.errString());
    }
  },

  removeWifiDevice: async (req, res) => {
    const bssid = req.body.bssid.toString();
    if (!bssid || bssid.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.removeWifiDevice(bssid);
    if (result.isSuccess) {
      return res.json({ isSuccess: true });
    } else {
      return res.status(400).send(result.errString());
    }
  },
};
