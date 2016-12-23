import * as ReactDOM from 'react-dom';
import * as React from "react";
import App from "./app/app";

import { Provider } from 'react-redux';
import { createStore, applyMiddleware, Store } from 'redux';
import thunk from 'redux-thunk';
import { default as reducers } from './app/reducers/reducers';
import {Tilstand} from "./app/domain/domain";

const store : Store<Tilstand> = createStore(reducers, applyMiddleware(thunk));

ReactDOM.render((
    <Provider store={ store } key="provider">
        <App />
    </Provider>
), document.getElementById('app'));
