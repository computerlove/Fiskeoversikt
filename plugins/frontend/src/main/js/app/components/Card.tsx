import * as React from 'react';
import {LandingsdataByLandingdate} from "../domain/domain";

interface CardProps {
    landingsdata: LandingsdataByLandingdate
}
export default class Card extends React.Component<CardProps, {}> {
    render(){
        const ld = this.props.landingsdata;
        const dato = ld.landingsdato.toString();
        return (
            <article className="card" >
                <h1>
                    <span className="fartøy">{ld.fartøy}</span>
                    <span className="landingsdato">{dato}</span>
                </h1>
            </article>
        );
    }
}