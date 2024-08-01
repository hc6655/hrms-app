const router = require("express").Router();
const db = require("../../lib/db.js");

router.use("/user", require("./user"));
router.use("/department", require("./department"));
router.use("/role", require("./role"));
router.use("/leave", require("./leave"));
router.use("/fcm", require("./fcm"));
router.use("/announcement", require("./announcement"));
router.use("/wifi", require("./wifi"));
router.use("/attend", require("./attend"));
router.use("/log", require("./log"));
router.use("/ble", require("./ble"));
router.use("/position", require("./position"));

module.exports = router;
