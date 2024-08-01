const repository = require("../repositories/department_repository");
const positionRepository = require("../repositories/position_repository");

module.exports = {
  getDepartments: async (req, res) => {
    const result = await repository.getDepartments();
    if (!result.hasRecord()) {
      return res.status(404).send("No departments found");
    }

    return res.json({ departments: result.toArray() });
  },

  getDepartment: async (req, res) => {
    const id = req.params.id.toString();
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.getDepartment(id);
    if (!result.hasRecord()) {
      return res.status(404).send("Department not found");
    }

    const department = result.results[0];
    department.positions = [];

    result = await positionRepository.getPositionsByDepartmentId(id);
    if (result.hasRecord()) {
      department.positions = result.toArray();
    }

    return res.json(department);
  },

  createDepartment: async (req, res) => {
    const id = req.body.id.toString();
    const title = req.body.title.toString();
    const positions = req.body.positions;

    if (
      !id ||
      id.length <= 0 ||
      !title ||
      title.length <= 0 ||
      !positions ||
      positions.length <= 0
    ) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.getDepartment(id);
    if (result.isSuccess && result.results.length > 0) {
      return res.status(409).send("Duplicated ID");
    }

    result = await repository.createDepartment(id, title);
    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    for (const position of positions) {
      position.departmentId = id;
      result = await positionRepository.createPosition(position);

      if (!result.isSuccess) {
        break;
      }
    }

    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    return res.json({ isSuccess: true });
  },

  updateDepartment: async (req, res) => {
    const id = req.params.id.toString();
    const title = req.body.title.toString();
    const updatedList = req.body.updatedPositions;
    const deletedList = req.body.deletedPositions;
    const addedList = req.body.addedPositions;

    console.log(req.body);

    if (!id || id.length <= 0 || !title || title.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await repository.updateDepartment(id, title);
    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    if (deletedList !== undefined && deletedList.length > 0) {
      for (const id of deletedList) {
        result = await positionRepository.removePositionById(id);
        if (!result.isSuccess) {
          return res.status(400).send(result.errString());
        }
      }
    }

    if (addedList !== undefined && addedList.length > 0) {
      for (const position of addedList) {
        console.log(position);
        result = await positionRepository.createPosition(position);
        if (!result.isSuccess) {
          return res.status(400).send(result.errString());
        }
      }
    }

    if (updatedList !== undefined && updatedList.length > 0) {
      for (const position of updatedList) {
        result = await positionRepository.updatePositionById(position);
        if (!result.isSuccess) {
          return res.status(400).send(result.errString());
        }
      }
    }

    return res.json({ isSuccess: true });
  },

  deleteDepartment: async (req, res) => {
    const id = req.params.id.toString();
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    let result = await positionRepository.removeAllPositionByDepartmentId(id);
    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    result = await repository.deleteDepartment(id);
    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    return res.json({ isSuccess: true });
  },
};
