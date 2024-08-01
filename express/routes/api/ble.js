const router = require("express").Router();
const controller = require("../../controllers/ble_controller");
const middleware = require("../../middleware/middleware");

router.get("/", middleware.verifyToken, controller.getBleDevices);
router.post("/", middleware.verifyToken, controller.addBleDevice);
router.delete("/", middleware.verifyToken, controller.removeBleDevice);

module.exports = router;
