import * as React from 'react';
import MainNavigation from "./main-navigation";

interface AppProps {children?: Array<React.ReactNode>}
export default class App extends React.Component<AppProps, {}> {
    render(){
        return (
            <div>
                <header className="mainHeader">
                    <MainNavigation />
                </header>
                <section className="container main-container">
                    <div className="main-content">
                        {this.props.children}
                    </div>
                </section>
            </div>
        );
    }
}