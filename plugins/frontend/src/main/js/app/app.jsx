import React from 'react';
import ReactDOM from 'react-dom';
import data from './data';
import observer from 'omnipotent/decorator/observer';
import { Router, Route, RouteHandler, IndexRoute, hashHistory } from 'react-router';
import Landingsdata from './landingsdata.jsx'
import MainNavigation from './main-navigation.jsx'
import Aggregert from './aggregert.jsx'

var App = (props) => {
    return (
        <div>
            <header className="mainHeader">
                <MainNavigation />
            </header>
            <section className="container main-container">
                <div className="main-content">
                    {props.children}
                </div>
            </section>
        </div>
    );
};

const RouterList = observer(data, {
    cursor: ['landingsdata']
}, Landingsdata);

const RouterChart = observer(data, {
    cursor: ['aggregert']
}, Aggregert);


ReactDOM.render((
    <Router  history={hashHistory}>
        <Route path="/" component={App}>
            <IndexRoute component={RouterList}/>
            <Route path="/landingsdata" component={RouterList}/>
            <Route path="/aggregert" component={RouterChart} />
        </Route>
    </Router>
), document.getElementById('app'));
