const router = require('express').Router();
const controller = require('../../controllers/department_controller')
const middleware = require('../../middleware/middleware')

router.get('/', middleware.verifyToken, controller.getDepartments);
router.get('/:id', middleware.verifyToken, controller.getDepartment)
router.post('/', [middleware.verifyToken, middleware.requireHrManagement], controller.createDepartment);
router.put('/:id', [middleware.verifyToken, middleware.requireHrManagement], controller.updateDepartment);
router.delete('/:id', [middleware.verifyToken, middleware.requireHrManagement], controller.deleteDepartment)

module.exports = router;