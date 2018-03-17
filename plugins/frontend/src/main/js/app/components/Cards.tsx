import * as React from 'react';
import {connect} from "react-redux";
import {LandingsdataByLandingdate, Tilstand} from "../domain/domain";
import Card from "./Card";
import {fetchData, fetchDataT} from "../actions/actions";

interface CardsProps {
    landingsdata: LandingsdataByLandingdate[],
    fetchData: fetchDataT
}
class Cards extends React.Component<CardsProps, {}> {
    componentDidMount() {
        this.props.fetchData();
    }
    render(){
        return (
            <section className="cards">
                {this.props.landingsdata
                    .map(ld =>
                        <Card
                            landingsdata={ld}
                            key={ld.landingsdato.toString() + ld.fartøy} />)}
            </section>
        );
    }
}

const mapStateToProps = (state: Tilstand) => {
    return {
        landingsdata: state.landingsdata
    }
};
const mapDispatchToProps =  ({
    fetchData: fetchData
});
export default connect(mapStateToProps, mapDispatchToProps)(Cards);
