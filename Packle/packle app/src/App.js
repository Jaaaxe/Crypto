import React from "react";
import Register from "./views/register";
import Login from "./views/login";
import Homepage from './views/homepage';
import { Switch, Route, BrowserRouter as Router } from "react-router-dom";
export const AuthContext = React.createContext()

const App = () => {

  const [state, dispatch] = React.useReducer(
    (prevState,action)=>{
      switch(action.type){
        case "LOG_IN":
          return {
            ...prevState,
            isLoggedIn: true,
            user: action.user
          }
        case "LOG_OUT":
          return {
            ...prevState,
            isLoggedIn: false,
            user: null
          }
      }
    },{
      isLoggedIn: true,
      user:null
  })

  React.useEffect(()=>{
    fetch('http://localhost:3001/user/get-user-details', {
      method: "GET",
      headers: {
          'Content-Type': 'application/json'
      },
      credentials: 'include'
  })
  .then(res => {
      if(res.status === 200){
        return res.json()
      }
  })
  .then(ret => {
      dispatch({type:"LOG_IN",user: ret})
      console.log(ret)
  })
  },[])

  return (
    <AuthContext.Provider value={{state:state,dispatch:dispatch}}>
      <Router>
        <Switch>
          {
            state.isLoggedIn ? 
            (
            <Route exact path="/">
              <Homepage />
            </Route>
            )
            :
            (
            <>
              <Route exact path="/">
                <Register />
              </Route>
              <Route path="/login">
                <Login />
              </Route>
            </>
            )
          }
        </Switch>
      </Router>
    </AuthContext.Provider>
  );
};

export default App;
