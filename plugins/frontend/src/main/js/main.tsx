import * as ReactDOM from 'react-dom';
import * as React from "react";
import App from "./app/app";

import { Provider } from 'react-redux';
import { createStore, applyMiddleware } from 'redux';
import thunk from 'redux-thunk';
import { default as reducers } from './app/reducers';

const store = createStore(reducers, applyMiddleware(thunk));

ReactDOM.render((
    <Provider store={ store } key="provider">
        <App />
    </Provider>
), document.getElementById('app'));
