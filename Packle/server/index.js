const express = require("express");
const bodyParser = require("body-parser");
const users = require("./routes/User");
const app = express();
const cors = require("cors");

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use("/user", users);

app.listen(3001, () => {
  console.log("Server Started");
});
