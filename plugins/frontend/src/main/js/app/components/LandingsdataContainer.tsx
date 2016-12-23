import * as React from 'react';
import { connect } from 'react-redux'
import {Tilstand, Landingsdata} from "../domain/domain";
import LandingsOpplysningerList from "./LandingsOpplysningerList";
import {fetchData} from "../actions/actions";

interface LandingsdataProps {laster: boolean, landingsdata: Landingsdata; init: Function}
class LandingsdataContainer extends React.Component<LandingsdataProps, {}> {

    componentDidMount() {
        this.props.init();
    }
    render() {
        return (
            <section className="landingsdata">
                {this.props.laster ? <div className="loading"/> : null}
                <div>
                    <label>
                        Fra:
                        <input type="date" defaultValue={this.datestring(this.props.landingsdata.fraDato)}/>
                    </label>
                    <label>
                        Til:
                        <input type="date" defaultValue={this.datestring(this.props.landingsdata.tilDato)}/>
                    </label>
                </div>
                <LandingsOpplysningerList data={this.props.landingsdata.leveringslinjer} />
            </section>
        );
    }

    datestring(date: Date) : string {
        return date.toISOString().split('T')[0];
    }
}

const mapStateToProps = (state: Tilstand) => {
    const {laster, landingsdata} = state;
    return {
        laster,
        landingsdata
    }
};
const mapDispatchToProps =  ({
    init: fetchData
});
export default connect(mapStateToProps, mapDispatchToProps)(LandingsdataContainer);
