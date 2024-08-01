const moment = require("moment");

module.exports = {
  getCurrentDateTime: () => {
    return moment().format("YYYY-MM-DD HH:mm:ss");
  },

  getCurrentDateObj: () => {
    const tzOffset = new Date().getTimezoneOffset() * 60000;
    return new Date(Date.now() - tzOffset);
  },
};
