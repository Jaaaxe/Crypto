const { Sequelize } = require("sequelize");

const sequelize = new Sequelize("packle", "root", "root", {
  host: "localhost",
  dialect: "mysql",
});

module.exports = sequelize;
