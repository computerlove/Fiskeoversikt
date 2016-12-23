import * as React from 'react';
import {Leveringslinje} from "../domain/domain";

interface LandingsOpplysningerListProps {data: Array<Leveringslinje>}
export default class LandingsOpplysningerList extends React.Component<LandingsOpplysningerListProps, {}> {
    render(){
        return (
            <ol className="leveringslinjer">
                {this.props.data.map((linje) => LandingsOpplysningerList.leveringslinjeContainer(linje))}
            </ol>
        );
    }

    static leveringslinjeContainer(ll: Leveringslinje) {
        return (<li key={ll.id} className="leveringslinje">
            <time dateTime={ll.landingsdato.toISOString()}>{ll.landingsdato.toDateString()}</time>
            <span className="fartøy">{ll.fartoy}</span>
            <span className="mottak">{ll.mottak}</span>
            <span className="fiskeslag">{ll.fiskeslag}</span>
            <span className="tilstand">{ll.tilstand}</span>
            <span className="størrelse">{ll.storrelse}</span>
            <span className="kvalitet">{ll.kvalitet}</span>
            <span className="nettovekt">{ll.nettovekt}</span>
        </li>)
    }
}