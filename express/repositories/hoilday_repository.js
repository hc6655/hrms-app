const db = require("../lib/db");

module.exports = {
  getHoildaysByYear: async (year) => {
    return await db.query(
      "SELECT date FROM public_holiday WHERE YEAR(date)=?",
      [year]
    );
  },
};
