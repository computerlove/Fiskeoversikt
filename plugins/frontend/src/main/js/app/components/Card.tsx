import * as React from 'react';
import {LandingsdataByLandingdate, Leveringslinje} from "../domain/domain";

interface CardProps {
    landingsdata: LandingsdataByLandingdate
}
interface CardState {
    showDetails: boolean
}
export default class Card extends React.Component<CardProps, CardState> {
    constructor(props: CardProps) {
        super(props);
        this.state = {
            showDetails: false
        }
    }

    render(){
        const ld = this.props.landingsdata;
        const dato = ld.landingsdato.toString();
        return (
            <article className="card" onClick={(e) => this.setState({showDetails:!this.state.showDetails})} >
                <header>
                    <h1>
                        <span className="fartøy">{ld.fartøy}</span>
                        <time className="landingsdato">{dato}</time>
                    </h1>
                </header>
                <summary className="totalFangst">
                    {totalFangst(ld.leveringslinjer)}kg
                </summary>

                {this.state.showDetails && <Details landingsdata={ld} /> }
            </article>
        );
    }
}

function totalFangst(leveringslinjer: Array<Leveringslinje>): number {
    return leveringslinjer.reduce((acc, curr) => acc + curr.nettovekt, 0);
}

class Details extends React.Component<CardProps, {}> {
    render() {
        const leveringslinjer = this.props.landingsdata.leveringslinjer;
        return (
            <div className="details">
                <ul>
                    {leveringslinjer.map(ll => (
                        <li key={ll.id}>
                            <span className="fiskeslag">{ll.fiskeslag}</span>
                            <span className="tilstand">{ll.tilstand}</span>
                            <span className="størrelse">{ll.storrelse}</span>
                            <span className="kvalitet">{ll.kvalitet}</span>
                            <span className="nettovekt">{ll.nettovekt}</span>
                        </li>
                    ))}
                </ul>
            </div>)
    }
}
