const express = require("express");
const bodyParser = require("body-parser");
const users = require("./routes/User");
const app = express();
const cors = require("cors");
const whitelist = ["http://localhost:3000"];
const corsOptions = {
  credentials: true, // This is important.
  origin: (origin, callback) => {
    if (whitelist.includes(origin)) return callback(null, true);

    callback(new Error("Not allowed by CORS"));
  },
};

app.use(cors(corsOptions));

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use("/user", users);

app.listen(3001, () => {
  console.log("Server Started");
});
