import React from 'react';
import "react-datepicker/dist/react-datepicker.css";

import TimelineWrapper from './components/TimelineWrapper';
import DoughnutsWrapper from './components/DoughnutsWrapper';
import HeaderBar from './components/HeaderBar';

export const App = () => {
  return (
    <React.Fragment>
      <HeaderBar />
      <TimelineWrapper />
      <DoughnutsWrapper />
    </React.Fragment>
  );
};

export default App;
