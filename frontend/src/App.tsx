import React from 'react';
import TimelineWrapper from './components/TimelineWrapper';
import RangePicker from "react-range-picker"
import ApiService from './services/ApiService';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';

const apiClient = new ApiService();

function id() {
  let i = 1;
  return function () {
    return i++;
  }
}
const genId = id();

interface State {
  groups: any,
  items: any
}

class App extends React.Component<{}, State> {

  constructor(props: any) {
    super(props);
    const groups = [
      { id: 1, title: 'PC time log' },
      { id: 2, title: 'YouTube' },
      { id: 3, title: 'TimeLog' },
      { id: 4, title: 'Music' },
      { id: 5, title: 'Media files' },
      { id: 6, title: 'Android phone' }
    ]
    this.state = {
      groups: groups,
      items: []
    };
  }

  componentDidMount() {
    const { items } = this.getItems(new Date(Date.now() - 12000 * 12 * 1000), new Date(Date.now() + 12000 * 24 * 1000));
    this.setState((state) => {
      return {
        items: items
      }
    });
    console.log("componentDidMount()")
  }

  getItems(startDate: Date, endDate: Date) {
    let items: any = [];
    apiClient.getAutotimers(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 1,
        title: e.name,
        start_time: Date.parse(e.startTime),
        end_time: Date.parse(e.endTime),
        canMove: false,
        canResize: false,
        canChangeGroup: false,
      }))
    );
    apiClient.getTimeLogs(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 3,
        title: e.tags[0] + e.description,
        start_time: Date.parse(e.startDate),
        end_time: Date.parse(e.endDate),
        canMove: false,
        canResize: false,
        canChangeGroup: false,

      }))
    );
    apiClient.getListenHistory(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 4,
        title: e.artist + e.title,
        start_time: Date.parse(e.listenTime),
        end_time: Date.parse(e.listenTime + 10000),
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          onMouseDown: () => {
            window.open(e.url, '_blank');
          }
        }
      }))
    );
    apiClient.getYouTubes(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 2,
        title: e.title,
        start_time: Date.parse(e.time),
        end_time: Date.parse(e.time) + 120000,
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          onMouseDown: () => {
            window.open(e.url, '_blank');
          }
        }
      }))
    );
    apiClient.getMediaHistory(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 5,
        title: e.path,
        start_time: Date.parse(e.created),
        end_time: Date.parse(e.created) + 10000,
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          onMouseDown: () => {
            window.open(apiClient.host + e.url, '_blank');
          },
          style: {
            background: 'fuchsia'
          }
        }
      }))
    );
    apiClient.getWellbeingHistory(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 6,
        title: e.app,
        start_time: Date.parse(e.startTime),
        end_time: Date.parse(e.endTime),
        canMove: false,
        canResize: false,
        canChangeGroup: false,
      }))
    );
    return { items };
  }

  setDate(start: Date, end: Date) {
    console.log("setDate()" + start + "   " + end)
    const { items } = this.getItems(start, end);
    this.setState((state) => {
      return {
        items: items
      }
    })
  }

  render() {
    return (
      <div>
        <TimelineWrapper items={this.state.items} groups={this.state.groups} />
        <RangePicker
          onClose={(start: Date, end: Date) => this.setDate(start, end)} />
        <Button variant="outlined" color="primary" href="/process/all">
          Process All
        </Button>
      </div>
    );
  }
}

export default App;
