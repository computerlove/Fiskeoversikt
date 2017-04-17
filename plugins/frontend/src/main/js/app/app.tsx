import * as React from 'react';
import Loader from "./components/Loader";
import {Tilstand} from "./domain/domain";
import {connect} from "react-redux";
import Cards from "./components/Cards";
import LoadMore from "./components/LoadMore";

interface AppProps {
    laster: boolean
}
export class App extends React.Component<AppProps, {}> {
    render(){
        return (
            <div>
                <header className="header">
                    <h1 className="header__title">Landingsopplysninger</h1>
                    <button id="butRefresh" className="headerButton" aria-label="Refresh"></button>
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
    return {
        laster: state.laster
    }
};
const mapDispatchToProps =  ({
});
export default connect(mapStateToProps, mapDispatchToProps)(App);
