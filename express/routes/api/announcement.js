const router = require("express").Router();
const controller = require("../../controllers/announce_controller");
const middleware = require("../../middleware/middleware");

router.get("/", controller.getAnnouncements);
router.get("/:id", controller.getAnnouncementById);
router.post("/", middleware.verifyToken, controller.createAnnouncement);
router.put("/:id", middleware.verifyToken, controller.updateAnnouncement);
router.delete("/:id", middleware.verifyToken, controller.removeAnnouncement);

module.exports = router;
