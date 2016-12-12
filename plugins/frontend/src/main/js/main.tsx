import * as ReactDOM from 'react-dom';
import * as React from "react";
import { Router, Route, IndexRoute, hashHistory } from 'react-router';
import App from "./app/app";
import Landingsdata from "./app/landingsdata";
import Aggregert from "./app/aggregert";


ReactDOM.render((
    <Router history={hashHistory}>
        <Route path="/" component={App}>
            <IndexRoute component={Landingsdata}/>
            <Route path="/landingsdata" component={Landingsdata}/>
            <Route path="/aggregert" component={Aggregert} />
        </Route>
    </Router>
), document.getElementById('app'));
