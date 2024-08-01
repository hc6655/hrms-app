const repository = require('../repositories/role_repository')

module.exports = {
    getRoles: async (req, res) => {
        const result = await repository.getRoles();
        return res.json({roles: result.toArray()});
    },

    getRole: async (req, res) => {
        const id = req.params.id.toString();
        if (!id || id.length <= 0) {
            return res.status(400).send("Invalid parameters");
        }

        const result = await repository.getRole(id);
        if (result.isSuccess) {
            if (result.results.length > 0) {
                return res.json(result.results[0]);
            } else {
                return res.status(404).send("Department not found");
            }
        } else {
            return res.status(400).send(result.errString());
        }
    },

    createRole: async (req, res) => {
        const title = req.body.title.toString();
        const departmentId = req.body.departmentId.toString();
        const permissionId = req.body.permissionId.toString();

        if (!permissionId || permissionId.length <= 0 ||
            !departmentId || departmentId.length <= 0 ||
            !title || title.length <= 0) {
            return res.status(400).send("Invalid parameters");
        }

        const result = await repository.createRole(title, departmentId, permissionId);
        if (result.isSuccess) {
            return res.json({isSuccess: true});
        } else {
            return res.status(400).send(result.errString());
        }
    },

    updateRole: async (req, res) => {
        const id = req.params.id.toString();
        const title = req.body.title.toString();
        const departmentId = req.body.departmentId.toString();
        const permissionId = req.body.permissionId.toString();

        if (!id || id.length <= 0 || 
            !title || title.length <= 0 || 
            !departmentId || departmentId.length <= 0 ||
            !permissionId || permissionId.length <= 0) {
            return res.status(400).send("Invalid parameters");
        }

        const result = await repository.updateRole(id, title, departmentId, permissionId);
        if (result.isSuccess) {
            return res.json({isSuccess: true});
        } else {
            return res.status(400).send(result.errString());
        }
    },

    deleteRole: async (req, res) => {
        const id = req.params.id.toString();
        if (!id || id.length <= 0) {
            return res.status(400).send("Invalid parameters");
        }

        const result = await repository.deleteRole(id);
        if (result.isSuccess) {
            return res.json({isSuccess: true});
        } else {
            return res.status(400).send(result.errString());
        }
    }
};