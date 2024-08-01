const firebase = require("firebase-admin");

firebase.initializeApp({
  credential: firebase.credential.applicationDefault(),
});

module.exports = {
  admin: firebase,

  pushAnnouncement: async (title, content) => {
    const message = {
      notification: {
        title: title,
        body: content,
      },
      topic: "announcement",
    };

    return await firebase.messaging().send(message);
  },
};
