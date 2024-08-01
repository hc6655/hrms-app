const repository = require('../repositories/fcm_repository')

module.exports = {
    getFCMToken: async (req, res) => {
        const employeeId = req.user.employeeId;
        if (!employeeId || employeeId.length <= 0) {
            return res.status(400).send("Invalid parameters");
        }

        const result = await repository.getFCMToken(employeeId);
        if (!result.isSuccess) {
            return res.status(404).send("Token not found.");
        }

        return res.json(result.results[0]);
    },

    setFCMToken: async (req, res) => {
        const employeeId = req.user.employeeId;
        const token = req.body.token.toString();

        if (!employeeId || employeeId.length <= 0 ||
            !token || token.length <= 0) {
            return res.status(400).send("Invalid parameters");
        }

        const result = await repository.setFCMToken(employeeId, token);
        if (!result.isSuccess) {
            return res.status(404).send("User not found.");
        }

        return res.json({isSuccess: true});
    }
}