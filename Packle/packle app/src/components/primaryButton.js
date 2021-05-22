import React from "react";
import { Button } from "reactstrap";
import { Link } from "react-router-dom";

const PrimaryButton = (props) => {
  return (
    <Button
      style={{
        backgroundColor: props.background,
        lineHeight: "100%",
      }}
    >
      <Link to={props.to}>
        <p style={{ color: props.color, marginBottom: "0" }}>{props.label}</p>
      </Link>
    </Button>
  );
};

// const styles = {
//   container: {
//     display: "flex",
//     alignItems: "center",
//     justifyContent: "center",
//   },
// };

export default PrimaryButton;
