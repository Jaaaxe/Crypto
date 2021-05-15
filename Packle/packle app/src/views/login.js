import React from "react";
import {
  Container,
  Col,
  Row,
  Form,
  FormGroup,
  Label,
  Input,
  Button,
} from "reactstrap";

function Login() {
  const [user, setUser] = React.useState({});
  const logIn = () => {
    console.log(user);
    fetch("http://localhost:3001/user/login", {
      method: "POST",
      body: JSON.stringify(user),
      headers: {
        "Content-Type": "application/json",
        Accept: "application/json",
      },
      credentials: "include",
    });
  };

  return (
    <Container>
      <Row>
        <Col>
          <Form>
            <h1 className="display-3">Login</h1>
            <FormGroup>
              <Label for="User">User Name</Label>
              <Input
                type="text"
                name="username"
                id="User"
                onChange={(data) =>
                  setUser({ ...user, username: data.currentTarget.value })
                }
                value={user.username}
              />
              <Label for="Password">Password</Label>
              <Input
                type="text"
                name="password"
                id="Password"
                onChange={(data) =>
                  setUser({ ...user, password: data.currentTarget.value })
                }
                value={user.password}
              />
            </FormGroup>
            <Button color="primary" onClick={logIn}>
              Login
            </Button>{" "}
          </Form>
        </Col>
      </Row>
    </Container>
  );
}

export default Login;
