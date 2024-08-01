const router = require('express').Router();
const controller = require('../../../controllers/leave_controller')
const middleware = require('../../../middleware/middleware')

router.get('/', middleware.verifyToken, controller.getLeaveTypes);
router.use('/user', require('./leave_user_balance'));
router.use('/applications', require('./leave_applications'));

module.exports = router;