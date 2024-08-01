const router = require("express").Router();
const controller = require("../../../controllers/leave_controller");
const middleware = require("../../../middleware/middleware");

router.get(
  "/",
  [middleware.verifyToken, middleware.requireHrManagement],
  controller.getLeaveApplications
);
router.get(
  "/:id",
  [middleware.verifyToken],
  controller.getLeaveApplicationsById
);
router.put(
  "/:id",
  [middleware.verifyToken, middleware.requireHrManagement],
  controller.updateLeaveApplicationStatus
);
router.get(
  "/user/:id",
  middleware.verifyToken,
  controller.getLeaveApplicationsByEmployeeId
);

module.exports = router;
