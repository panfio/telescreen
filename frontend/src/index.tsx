import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';

import { Provider } from 'react-redux';
import initStore from './config/store'

import { MuiPickersUtilsProvider } from '@material-ui/pickers';
import DateFnsUtils from "@date-io/date-fns";

const store = initStore();

const rootEl = document.getElementById('root');
const render = (Component: any) =>
  ReactDOM.render(
    <Provider store={store}>
      <MuiPickersUtilsProvider utils={DateFnsUtils}>
        <div>
          <Component />
        </div>
      </MuiPickersUtilsProvider>
    </Provider>,
    rootEl
  );

render(App);

