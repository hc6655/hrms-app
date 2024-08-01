const router = require("express").Router();
const controller = require("../../controllers/log_controller");
const middleware = require("../../middleware/middleware");

router.get("/login", controller.getLoginLog);
router.get("/memployee", controller.getManageEmployeeLog);
router.get("/mleave", controller.getManageLeaveLog);
router.get("/attend", controller.getTakeAttendanceLog);

module.exports = router;
