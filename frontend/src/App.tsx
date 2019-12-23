import React from 'react';
import TimelineWrapper from './components/TimelineWrapper';
import RangePicker from "react-range-picker"
import ApiService from './services/ApiService';
import Button from '@material-ui/core/Button';
import randomColor from "randomcolor";
import { Doughnut } from 'react-chartjs-2';

const apiClient = new ApiService();

interface State {
  groups: any,
  items: any[],
  doughnuts: any[]
}

class App extends React.Component<{}, State> {

  constructor(props: any) {
    super(props);
    const groups = [
      { id: 1, title: 'PC time log' },
      { id: 2, title: 'Android phone' },
      { id: 3, title: 'Time log' },
      { id: 4, title: 'Messages' },
      { id: 5, title: 'Music' },
      { id: 6, title: 'YouTube' },
      { id: 7, title: 'Media files' }
    ]
    this.state = {
      groups: groups,
      items: [],
      doughnuts: []
    };
  }

  componentDidMount() {
    this.setDate(new Date(Date.now() - 1000 * 60 * 60 * 24), //one day
      new Date(Date.now()));
  }

  setDate(start: Date, end: Date) {
    apiClient.getItems(start, end).then((items: any) => {

      const timeLogKV: Map<string, number> = new Map();
      const andriodKV: Map<string, number> = new Map();

      items.map((el: any) => {
        if (el.group === 3) {
          timeLogKV.set(el.title, (timeLogKV.get(el.title) == null)
            ? el.end_time - el.start_time
            : timeLogKV.get(el.title) + el.end_time - el.start_time);
        }
        if (el.group === 2) {
          andriodKV.set(el.title, (andriodKV.get(el.title) == null)
            ? el.end_time - el.start_time
            : andriodKV.get(el.title) + el.end_time - el.start_time);
        }
        return el;
      })

      let timeLogDoughnut = this.fillDoughnut(timeLogKV);
      let andriodDoughnut = this.fillDoughnut(andriodKV);

      this.setState(() => {
        return {
          doughnuts: [andriodDoughnut, timeLogDoughnut],
          items: items
        }
      })
    });
  }

  fillDoughnut(data: Map<string, number>){
    let doughnut: any = {
      labels: [],
      datasets: [{
        data: [],
        backgroundColor: []
      }]
    };
    
    data.forEach((val: number, key: string) => {
      let diffDays = Math.floor(val / 86400000); // days
      let diffHrs = Math.floor((val % 86400000) / 3600000); // hours
      let diffMins = Math.round(((val % 86400000) % 3600000) / 60000); // minutes
      let diffSec = Math.floor(diffMins / 60000); // seconds
      let period = ((diffDays > 0) ? diffDays + "d " : '') +
        ((diffHrs > 0) ? diffHrs + "h " : '') +
        ((diffMins > 0) ? diffMins + "m " : '') +
        diffSec + "s";

      doughnut.labels.push(key + " " + period);
      doughnut.datasets[0].data.push(val);
      doughnut.datasets[0].backgroundColor.push(randomColor({
        luminosity: "random",
        seed: Math.floor(Math.random() * 1000)
      }));
    })
    return doughnut;
  }

  render() {
    return (
      <div>
        <TimelineWrapper items={this.state.items} groups={this.state.groups} />
        <RangePicker
          onClose={(start: Date, end: Date) => this.setDate(start, end)} />
        <Button variant="outlined" color="primary" onClick={() => apiClient.processAll()}>
          Process All
        </Button>
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3,1fr)' }}>
          {
            this.state.doughnuts.map((d:any) => <div><Doughnut data={d} /></div>)
          }
        </div>
      </div>
    );
  }
}

export default App;
