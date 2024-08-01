const router = require("express").Router();
const controller = require("../../controllers/wifi_controller");
const middleware = require("../../middleware/middleware");

router.get("/", middleware.verifyToken, controller.getWifiDevices);
router.post("/", middleware.verifyToken, controller.addWifiDevice);
router.delete("/", middleware.verifyToken, controller.removeWifiDevice);

module.exports = router;
