const repository = require("../repositories/announce_repository");
const logRepository = require("../repositories/log_repository");
const dateHelper = require("../lib/date_helper");
const firebase = require("../lib/fcm");

module.exports = {
  getAnnouncements: async (req, res) => {
    let result = null;

    if (req.query.limit) {
      const limit = Math.max(req.query.limit, 1);
      result = await repository.getAnnouncementsLimit(limit);
    } else {
      result = await repository.getAnnouncements();
    }

    return res.json({ announcements: result.toArray() });
  },

  getAnnouncementById: async (req, res) => {
    const id = req.params.id.toString();
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.getAnnouncementById(id);
    if (result.isSuccess) {
      if (result.results.length > 0) {
        return res.json(result.results[0]);
      } else {
        return res.status(404).send("Announcement not found");
      }
    } else {
      return res.status(400).send(result.errString());
    }
  },

  createAnnouncement: async (req, res) => {
    const title = req.body.title.toString();
    const content = req.body.content.toString();
    const isPushNotification = req.body.isPushNotification;
    const employeeId = req.user.employeeId;

    if (
      !title ||
      title.length <= 0 ||
      !content ||
      content.length <= 0 ||
      !employeeId ||
      employeeId.length <= 0 ||
      isPushNotification === null ||
      isPushNotification === undefined
    ) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.createAnnouncement({
      title: title,
      content: content,
      employeeId: employeeId,
      publishDate: dateHelper.getCurrentDateTime(),
      isPushNotification: isPushNotification,
    });

    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    const insertId = result.results.insertId;

    if (isPushNotification) {
      const logPayload = {
        id: insertId,
        dateTime: dateHelper.getCurrentDateTime(),
        isSuccess: false,
        errMessage: "",
        messageId: "",
      };

      firebase
        .pushAnnouncement(title, content)
        .then((response) => {
          logPayload.isSuccess = true;
          logPayload.messageId = response;
          logRepository.createAnnounceNotiLog(logPayload);
        })
        .catch((error) => {
          logPayload.errMessage = error;
          logRepository.createAnnounceNotiLog(logPayload);
        });
    }

    return res.json({ announcementId: insertId.toString() });
  },

  updateAnnouncement: async (req, res) => {
    const id = req.params.id;
    const isPushNotification = req.body.isPushNotification;

    if (
      !id ||
      id.length <= 0 ||
      !req.body.title ||
      req.body.title.length <= 0 ||
      !req.body.content ||
      req.body.content.length <= 0 ||
      isPushNotification === null ||
      isPushNotification === undefined
    ) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.updateAnnouncement(id, req.body);
    if (!result.isSuccess) {
      return res.status(400).send(result.errString());
    }

    if (isPushNotification) {
      const logPayload = {
        id: id,
        dateTime: dateHelper.getCurrentDateTime(),
        isSuccess: false,
        errMessage: "",
        messageId: "",
      };

      firebase
        .pushAnnouncement(req.body.title, req.body.content)
        .then((response) => {
          logPayload.isSuccess = true;
          logPayload.messageId = response;
          logRepository.createAnnounceNotiLog(logPayload);
        })
        .catch((error) => {
          logPayload.errMessage = error;
          logRepository.createAnnounceNotiLog(logPayload);
        });
    }

    return res.send("announcement updated");
  },

  removeAnnouncement: async (req, res) => {
    const id = req.params.id;
    if (!id || id.length <= 0) {
      return res.status(400).send("Invalid parameters");
    }

    const result = await repository.removeAnnouncement(id);
    if (!result.isSuccess) {
      return res.status(501).send(result.errString());
    }

    return res.send("Success");
  },
};
