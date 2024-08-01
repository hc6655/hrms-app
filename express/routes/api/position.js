const router = require("express").Router();
const controller = require("../../controllers/position_controller");
const middleware = require("../../middleware/middleware");

router.get("/", controller.getPositions);
router.get("/:id", controller.getPositionByDepartmentId);

module.exports = router;
