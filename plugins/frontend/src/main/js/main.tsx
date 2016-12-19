import * as ReactDOM from 'react-dom';
import * as React from "react";
import { Router, Route, IndexRoute, hashHistory } from 'react-router';
import App from "./app/app";
import Landingsdata from "./app/landingsdata";
import Aggregert from "./app/aggregert";

import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import { connect } from 'react-redux'
import { default as reducers } from './app/reducers';

const store = createStore(reducers, applyMiddleware(thunk));

const mapStateToProps = state => {
    return state;
};

ReactDOM.render((
    <Provider store={ store } key="provider">
        <App />
    </Provider>
), document.getElementById('app'));
