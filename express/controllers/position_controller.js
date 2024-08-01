const repository = require("../repositories/position_repository");

module.exports = {
  getPositions: async (req, res) => {
    const result = await repository.getPositions();
    if (!result.hasRecord()) {
      return res.status(404).send("No positions found");
    }

    return res.json({ positions: result.toArray() });
  },

  getPositionByDepartmentId: async (req, res) => {
    const id = req.params.id;
    if (id === undefined || id.length <= 0) {
      return res.status(400).send("Invalid paramaters");
    }

    const result = await repository.getPositionsByDepartmentId(id);
    if (!result.hasRecord()) {
      return res.status(404).send("No positions found");
    }

    return res.json({ positions: result.toArray() });
  },
};
