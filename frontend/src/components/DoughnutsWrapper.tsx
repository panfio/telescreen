import React from 'react';
import { connect } from 'react-redux';
import { Doughnut } from 'react-chartjs-2';
import randomColor from "randomcolor";

import { IRootState } from '../reducers/root-reducer';
import { ITimeLogRecord } from '../models/timelog-record.model';
import { IWellbeingRecord } from '../models/wellbeing-record.model';

export interface IDoughnutsWrapperProps extends StateProps, DispatchProps { }

export const DoughnutsWrapper = (props: IDoughnutsWrapperProps) => {

    const collectDoughnuts = () => {
        // todo  fetchAll() 
        // console.log("collectDoughnuts() has been invoked")

        const timeLogKV: Map<string, number> = new Map();
        const andriodKV: Map<string, number> = new Map();

        props.timeLogRecord.entities.forEach(
            (el: ITimeLogRecord) => {
                const time = timeLogKV.get(el.tags[0]);
                const period = Date.parse(el.endDate) - Date.parse(el.startDate);
                if (time === undefined) {
                    timeLogKV.set(el.tags[0], period);
                } else {
                    timeLogKV.set(el.tags[0], time + period);
                }
            }
        )
        props.wellbeingRecord.entities.forEach(
            (el: IWellbeingRecord) => {
                const time = andriodKV.get(el.app);
                const period = Date.parse(el.endTime) - Date.parse(el.startTime);
                if (time === undefined) {
                    andriodKV.set(el.app, period);
                } else {
                    andriodKV.set(el.app, time + period);
                }
            }
        )

        let timeLogDoughnut = fillDoughnut(timeLogKV);
        let andriodDoughnut = fillDoughnut(andriodKV);
        return [andriodDoughnut, timeLogDoughnut]
    };

    const fillDoughnut = (data: Map<string, number>) => {
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

    return (
        <div style={{ display: 'grid', gridTemplateColumns: 'repeat(2,1fr)' }}>
            {
                collectDoughnuts().map((d: any) => <div><Doughnut data={d} /></div>)
            }
        </div>
    );
}

const mapStateToProps = (
    { timeLogRecord, wellbeingRecord, }: IRootState) => ({
        timeLogRecord,
        wellbeingRecord,
    });

const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(DoughnutsWrapper);
