const router = require("express").Router();
const controller = require("../../controllers/user_controller");
const middleware = require("../../middleware/middleware");

// 1
router.get(
  "/",
  [middleware.verifyToken, middleware.requireHrManagement],
  controller.getUsers
);

router.post(
  "/",
  [middleware.verifyToken, middleware.requireHrManagement],
  controller.createUser
);
router.get("/:id", middleware.verifyToken, controller.getUserInfo);
router.post("/login", controller.login);
router.get(
  "/permission/:id",
  middleware.verifyToken,
  controller.getUserPermission
);
router.put(
  "/:id",
  [middleware.verifyToken, middleware.requireHrManagement],
  controller.updateUser
);

router.post("/auth", controller.loginWithBiometric);
router.put("/auth/:id", middleware.verifyToken, controller.updateUserBiometric);

module.exports = router;
