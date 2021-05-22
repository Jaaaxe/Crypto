import React from "react";
import ModuleCard from "../components/moduleCard"
import { Card, CardImg, CardText, CardBody, CardTitle, CardSubtitle } from 'reactstrap'
import Image from '../assets/images/placeholder.png'
import styled from 'styled-components'
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
import {AuthContext} from '../App';
import PrimaryButton from "../components/primaryButton";

function Homepage() {
    
    const {state,dispatch} = React.useContext(AuthContext);

    return (
        
        <Container>
            <Row><h1 className="display-3">Packle</h1></Row>
            {/*<Row><p>{state ? state.user.name : null}'s Desk</p></Row>*/}
            <PrimaryButton to="" background="#000000" color="#ffffff" label="Create Module" />
            {/* TODO: set link */}
            <Row>
                <Col><ModuleCard to="" moduletitle="Module" numberofpacks="Test"/></Col>
                <Col><ModuleCard to="" moduletitle="Module" numberofpacks="Test"/></Col>
                <Col><ModuleCard to="" moduletitle="Module" numberofpacks="Test"/></Col>
            </Row>
          
            
              

        </Container>
    );
}



export default Homepage;