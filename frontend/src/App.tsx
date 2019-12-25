import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import RangePicker from "react-range-picker"
import Button from '@material-ui/core/Button';

import TimelineWrapper from './components/TimelineWrapper';
import DoughnutsWrapper from './components/DoughnutsWrapper';
import { apiService } from './services/ApiService';
import { IRootState } from './reducers/root-reducer';
import { fetchAll } from './reducers/actions';

export interface IAppProps extends StateProps, DispatchProps { }

export const App = (props: IAppProps) => {
  useEffect(() => {
    const from = new Date(Date.now() - 86400000)
    const to = new Date(Date.now())
    props.fetchAll(from, to);
  }, []);

  return (
    <div>
      <TimelineWrapper />
      <RangePicker
        onClose={(start: Date, end: Date) => props.fetchAll(start, end)} />
      <Button variant="outlined" color="primary" onClick={() => apiService.processAll()}>
        Process All
      </Button>
      <DoughnutsWrapper />
    </div>
  );
};

const mapStateToProps = ({ }: IRootState) => ({});

const mapDispatchToProps: any = { fetchAll };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(App);
