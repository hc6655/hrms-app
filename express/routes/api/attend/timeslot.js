const router = require("express").Router();
const controller = require("../../../controllers/attend_timeslot_controller");
const middleware = require("../../../middleware/middleware");

router.post("/", controller.createAttendTimeslot);
router.put("/", controller.updateAttendTimeslot);
router.get("/", controller.getAttendTimeslots);
router.get("/brief", controller.getAttendTimeslotBrief);
router.get("/:id", controller.getAttendTimeslotById);
router.get("/custom/:id", controller.getCustomShiftByEmployeeId);
router.post("/custom/:id", controller.addCustomShift);
router.delete("/custom/:id", controller.removeCustomShift);

module.exports = router;
