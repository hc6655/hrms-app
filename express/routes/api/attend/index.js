const router = require("express").Router();
const middleware = require("../../../middleware/middleware");
const controller = require("../../../controllers/attent_controller");
const attendController = require("../../../controllers/attendance_controller");

router.use("/timeslot", require("./timeslot"));
router.post("/time/:id", controller.getAttendTime);
router.post("/", middleware.verifyToken, attendController.takeAttendance);
router.post("/:id", attendController.getAttendance);
router.get("/method", attendController.getMethodSetting);
router.put("/method", attendController.updateMethodSetting);

module.exports = router;
