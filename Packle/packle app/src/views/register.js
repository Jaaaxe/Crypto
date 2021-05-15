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
import { Link } from "react-router-dom";

function Register() {
  const [user, setUser] = React.useState({});
  const submitInfo = () => {
    console.log(user);
    fetch("http://localhost:3001/user/register", {
      method: "POST",
      body: JSON.stringify(user),
      headers: {
        "Content-Type": "application/json",
        "Access-Control-Allow-Origin": "no-cors",
      },
    });
  };

  return (
    <Container>
      <Row>
        <Col>
          <Form>
            <h1 className="display-3">Register</h1>
            <FormGroup>
              <Label for="Email">Email</Label>
              <Input
                type="email"
                name="email"
                id="Email"
                onChange={(data) =>
                  setUser({ ...user, email: data.currentTarget.value })
                }
                value={user.email}
              />
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
            <Button color="primary" onClick={submitInfo}>
              Submit
            </Button>{" "}
          </Form>
          <Link to="/login">
            <p>Login</p>
          </Link>
        </Col>
      </Row>
    </Container>
  );
}

export default Register;