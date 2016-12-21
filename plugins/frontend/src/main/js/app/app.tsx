import * as React from 'react';
import Landingsdata from "./landingsdata";

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
                        <Landingsdata />
                    </div>
                </section>
            </div>
        );
    }
}