const router = require('express').Router();
const controller = require('../../controllers/fcm_controller');
const middleware = require('../../middleware/middleware');
const firebase = require('../../lib/fcm');

router.get('/', middleware.verifyToken, controller.getFCMToken);
router.put('/', middleware.verifyToken, controller.setFCMToken);

/* TEST */

router.post('/', async(req, res) => {
    const title = req.body.title;
    const body = req.body.body;

    const message = {
        notification: {
            title: title,
            body: body
        },
        topic: 'announcement'
    }

    try {
        const response = await firebase.admin.messaging().send(message);
        return res.send(response);
    } catch (e) {
        return res.status(400).send(e);
    }
});

/* ---- */

module.exports = router;