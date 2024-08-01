const router = require('express').Router();
const controller = require('../../../controllers/leave_controller')
const middleware = require('../../../middleware/middleware')

router.get('/:id', middleware.verifyToken, controller.getUserLeaveBalance);
router.post('/:id', middleware.verifyToken, controller.createLeaveApplication);

module.exports = router;