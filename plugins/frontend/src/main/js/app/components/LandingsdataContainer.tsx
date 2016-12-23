import * as React from 'react';
import { connect } from 'react-redux'
import {Tilstand, Landingsdata, Timespan} from '../domain/domain';
import LandingsOpplysningerList from './LandingsOpplysningerList';
import {fetchData, updateTimespan} from '../actions/actions';
import {LocalDate} from 'js-joda';

interface LandingsdataProps {
    laster: boolean,
    landingsdata: Landingsdata;
    init: Function
    updateTimespan: Function
}
interface LandingsdataState {
    timespan: Timespan
}
class LandingsdataContainer extends React.Component<LandingsdataProps, LandingsdataState> {

    constructor(props) {
        super(props);
        this.state = {
            timespan: {
                from: LocalDate.now(),
                to: LocalDate.now()
            }
        }
    }
    componentDidMount() {
        this.props.init();
    }

    componentWillReceiveProps(nextProps){
        this.setState({
            timespan: {
                from: nextProps.landingsdata.fraDato,
                to: nextProps.landingsdata.tilDato
            }
        })
    }

    toChanged(event) {
        let value : string = event.target.value;
        let toDate = LocalDate.parse(value);
        this.props.updateTimespan({
            from: this.state.timespan.from,
            to: toDate
        })
    }

    fromChanged(event) {
        let value : string = event.target.value;
        let fromDate = LocalDate.parse(value);
        this.props.updateTimespan({
            from: fromDate,
            to: this.state.timespan.to
        })
    }
    render() {
        return (
            <section className="landingsdata">
                {this.props.laster ? <div className="loading"/> : null}
                <div>
                    <label>
                        Fra:
                        <input type="date"
                               value={this.state.timespan.from.toString()}
                               onChange={(e) => this.fromChanged(e)}
                        />
                    </label>
                    <label>
                        Til:
                        <input type="date"
                               value={this.state.timespan.to.toString()}
                               onChange={(e) => this.toChanged(e)}
                        />
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
    init: fetchData,
    updateTimespan: updateTimespan
});
export default connect(mapStateToProps, mapDispatchToProps)(LandingsdataContainer);
