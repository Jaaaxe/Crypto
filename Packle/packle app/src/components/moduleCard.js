import React from "react";
import { Card, CardBody, CardTitle, CardSubtitle } from 'reactstrap'
import { Link } from "react-router-dom";
import styled from 'styled-components'
    
const ModuleCard = (props) => {
    return (
        <div>
            <Links to={props.modulelink}>
                <Card>
                    <CardBody>
                        <CardTitle tag="h5">{props.moduletitle}</CardTitle>
                        <CardSubtitle tag="h6" className="mb-2 text-muted">{props.numberofpacks}</CardSubtitle>
                    </CardBody>
                </Card>
                </Links>
        </div>
    )
}


const Links = styled(Link)`  
  font-size:14px;
  text-decoration:none;
  color: #000;
  &:hover{
    color:#5a616a;
  }
`;


export default ModuleCard;