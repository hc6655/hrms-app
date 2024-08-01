const db = require("../lib/db");

module.exports = {
  getBleDevices: async () => {
    return db.query("SELECT * FROM ble_devices");
  },

  addBleDevice: async (payload) => {
    return db.query("INSERT INTO ble_devices VALUE (?, ?)", [
      payload.mac,
      payload.name,
    ]);
  },

  removeBleDevice: async (mac) => {
    return db.query("DELETE FROM ble_devices WHERE mac=?", [mac]);
  },

  getBleDevice: async (mac) => {
    return db.query("SELECT * FROM ble_devices WHERE mac=?", mac);
  },
};
