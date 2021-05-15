import React from "react";
import Register from "./views/register";
import Login from "./views/login";
import { Switch, Route, BrowserRouter as Router } from "react-router-dom";

const App = () => {
  return (
    <Router>
      <Switch>
        <Route exact path="/">
          <Register />
        </Route>
        <Route path="/login">
          <Login />
        </Route>
      </Switch>
    </Router>
  );
};

export default App;
