import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';

import { Provider } from 'react-redux';
import initStore from './config/store'

const store = initStore();

const rootEl = document.getElementById('root');
const render = (Component: any) =>
    ReactDOM.render(
        <Provider store={store}>
            <div>
                <Component />
            </div>
        </Provider>,
        rootEl
    );

render(App);

