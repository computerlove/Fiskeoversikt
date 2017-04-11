import * as React from 'react';
import Loader from "./components/Loader";

interface AppProps {}
export default class App extends React.Component<AppProps, {}> {
    render(){
        return (
            <div>
                <header className="header">
                    <h1 className="header__title">Landingsdata</h1>
                    <button id="butRefresh" className="headerButton" aria-label="Refresh"></button>
                </header>

                <main className="main">

                </main>
                <Loader/>
            </div>
        );
    }
}