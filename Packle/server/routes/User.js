const express = require('express');
const User = require('../models/User');
const router = express.Router();
const bcrypt = require('bcrypt');
const Op = require('sequelize').Op

router.post('/register', (req,res) => {
    console.log(req.body);
    bcrypt.hash(req.body.password, 10)
    .then(hash => {
        User.findOrCreate({
            where: {
                [Op.or]: [
                    {username: req.body.username},
                    {email: req.body.email}
                ]
            },
            defaults: {
                username: req.body.username,
                email: req.body.email,
                password: hash
            }
        })
        .then(user => {
            if(user){
                res.status(200).json();
            } else {
                res.status(409).end();
            }
        })
    })
})

module.exports = router;