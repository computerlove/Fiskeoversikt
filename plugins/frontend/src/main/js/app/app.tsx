import * as React from 'react';
import Loader from "./components/Loader";
import {Tilstand} from "./domain/domain";
import {connect} from "react-redux";
import Cards from "./components/Cards";
import LoadMore from "./components/LoadMore";
import {reload, reloadT} from "./actions/actions";

interface AppProps {
    laster: boolean,
    numLoaded: number,
    reload: reloadT
}
export class App extends React.Component<AppProps, {}> {
    render(){
        return (
            <div>
                <header className="header">
                    <h1 className="header__title">Landingsopplysninger</h1>
                    <button id="butRefresh"
                            className="headerButton"
                            aria-label="Refresh"
                            onClick={(e) => this.props.reload(this.props.numLoaded, 0)}></button>
                </header>

                <main className="main">
                    <Cards />
                    <LoadMore />
                </main>
                {this.props.laster && <Loader/>}
            </div>
        );
    }
}

const mapStateToProps = (state: Tilstand) => {
    const datoer = new Set(state.landingsdata
        .map(ld => ld.landingsdato.toString()));
    return {
        laster: state.laster,
        numLoaded: datoer.size
    }
};
const mapDispatchToProps =  ({
    reload: reload
});
export default connect(mapStateToProps, mapDispatchToProps)(App);
