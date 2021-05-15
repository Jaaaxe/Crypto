const jwt = require("jsonwebtoken");
const express = require("express");
const User = require("../models/User");
const router = express.Router();
const bcrypt = require("bcrypt");
const Op = require("sequelize").Op;

router.post("/register", (req, res) => {
  bcrypt.hash(req.body.password, 10).then((hash) => {
    User.findOrCreate({
      where: {
        [Op.or]: [{ username: req.body.username }, { email: req.body.email }],
      },
      defaults: {
        username: req.body.username,
        email: req.body.email,
        password: hash,
      },
    }).then((user) => {
      if (user) {
        res.status(200).json();
      } else {
        res.status(409).end();
      }
    });
  });
});

router.post("/login", (req, res) => {
  User.findOne({
    where: {
      username: req.body.username,
    },
  }).then((user) => {
    if (user) {
      // checks to see if the password matches
      bcrypt.compare(req.body.password, user.password).then((match) => {
        if (match) {
          console.log(user.id);
          jwt.sign(
            { token: user.id },
            "root",
            { algorithm: "HS256" },
            function (err, token) {
              console.log(token, err);

              res
                .status(200)
                .cookie("access_token", `Bearer ${token}`, {
                  expires: new Date(Date.now() + 8 * 3600000),
                })
                .end();
            }
          );
        } else {
          res.status(401).end();
        }
      });
    } else {
      res.status(404).end();
    }
  });
});

module.exports = router;
