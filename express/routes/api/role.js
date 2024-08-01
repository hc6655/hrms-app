const router = require('express').Router();
const controller = require('../../controllers/role_controller')
const middleware = require('../../middleware/middleware')

router.get('/', middleware.verifyToken, controller.getRoles);
router.get('/:id', middleware.verifyToken, controller.getRole)
router.post('/', [middleware.verifyToken, middleware.requireHrManagement], controller.createRole);
router.put('/:id', [middleware.verifyToken, middleware.requireHrManagement], controller.updateRole);
router.delete('/:id', [middleware.verifyToken, middleware.requireHrManagement], controller.deleteRole)

module.exports = router;