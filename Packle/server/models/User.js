const {Sequelize, DataTypes} = require('sequelize');
const sequelize = require('../db/db');

const User = sequelize.define('User', {
    username: {
        type: DataTypes.TEXT,
        allowNull: false,
    },
    email: {
        type: DataTypes.TEXT,
        allowNull: false
    },
    password: {
        type: DataTypes.TEXT,
        allowNull: false
    }
}, {
    timestamps: false
})

module.exports = User;
