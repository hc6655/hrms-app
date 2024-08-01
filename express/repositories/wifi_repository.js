const db = require("../lib/db");

module.exports = {
  getWifiDevices: async () => {
    return db.query("SELECT * FROM wifi_devices");
  },

  getWifiDevice: async (bssid) => {
    return db.query("SELECT * FROM wifi_devices WHERE bssid=?", bssid);
  },

  addWifiDevice: async (payload) => {
    return db.query("INSERT INTO wifi_devices value (?, ?)", [
      payload.bssid,
      payload.ssid,
    ]);
  },

  removeWifiDevice: async (bssid) => {
    return db.query("DELETE FROM wifi_devices WHERE bssid=?", [bssid]);
  },
};
