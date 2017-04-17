import * as React from 'react';
import {LandingsdataByLandingdate} from "../domain/domain";

interface CardProps {
    landingsdata: LandingsdataByLandingdate
}
export default class Card extends React.Component<CardProps, {}> {
    render(){
        return (
            <div className="card">

            </div>
        );
    }
}