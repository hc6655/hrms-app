const mariadb = require('mariadb');

const pool = mariadb.createPool({
    host: process.env.DB_HOST,
    user: process.env.DB_USER,
    password: process.env.DB_PWD,
    port: process.env.DB_PORT,
    database: process.env.DB_DATABASE,
    metaAsArray: false,
    dateStrings: true,
  });

const QueryResult = class {
    constructor(isSuccess, results, err) {
        this.isSuccess = isSuccess;
        this.results = results;
        this.err = err;
    }

    toArray() {
        return this.results;
    }

    errString() {
        return this.err;
    }

    length() {
        if (this.results == null) {
            return 0;
        } else {
            return this.results.length;
        }
    }

    hasRecord() {
        return this.isSuccess && this.results.length > 0;
    }
}

const queryDB = async (sql, queryOptions) => {
    let conn;
    let rows;
    let errMsg;
    let isSuccess = true;

    try {
        conn = await pool.getConnection();
        rows = await conn.query(sql, queryOptions);
    } catch(err) {
        errMsg = err;
        console.log(err);
        isSuccess = false;
    } finally {
        if (conn) {
            conn.release();
        }
    }

    return new QueryResult(isSuccess, rows, errMsg);
};

module.exports = {
    query: queryDB,
}