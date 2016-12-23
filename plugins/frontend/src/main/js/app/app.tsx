import * as React from 'react';
import LandingsdataContainer from "./components/LandingsdataContainer";

interface AppProps {}
export default class App extends React.Component<AppProps, {}> {
    render(){
        return (
            <div>
                <header className="mainHeader">
                    <h1>Landingsdata</h1>
                </header>
                <section className="container main-container">
                    <div className="main-content">
                        <LandingsdataContainer />
                    </div>
                </section>
            </div>
        );
    }
}