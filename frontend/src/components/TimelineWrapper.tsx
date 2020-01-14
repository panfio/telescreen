import React from 'react';
import { connect } from 'react-redux';
import Timeline, {
    TimelineMarkers,
    CustomMarker,
    CursorMarker
} from 'react-calendar-timeline'
import 'react-calendar-timeline/lib/Timeline.css'

import { host } from '../services/ApiService';
import { IRootState } from '../reducers/root-reducer';

import { IYouTubeRecord } from '../models/youtube-record.model';
import { ITimeLogRecord } from '../models/timelog-record.model';
import { IAutoTimerRecord } from '../models/autotimer-record.model';
import { IListenRecord } from '../models/listen-record.model';
import { IMediaRecord } from '../models/media-record.model';
import { IWellbeingRecord } from '../models/wellbeing-record.model';
import { IMessageRecord } from '../models/message-record.model';
import { IMiFitActivityRecord } from '../models/mifitactivity-record.model';

const now = Date.now()

function id() {
    let i = 1;
    return function () {
        return i++;
    }
}
const genId = id();

export interface ITimelineProps extends StateProps, DispatchProps { }

export const TimelineWrapper = (props: ITimelineProps) => {

    const mapRecordsToItems = () => {
        let items: any[] = []

        props.autoTimerRecord.entities.forEach(
            (e: IAutoTimerRecord) => items.push({
                id: genId(),
                group: 1,
                title: e.name,
                start_time: Date.parse(e.startTime),
                end_time: Date.parse(e.endTime),
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                itemProps: {
                    style: {
                        overflow: "hidden"
                    }
                }
            })
        )

        props.wellbeingRecord.entities.forEach(
            (e: IWellbeingRecord) => items.push({
                id: genId(),
                group: 2,
                title: e.app,
                start_time: Date.parse(e.startTime),
                end_time: Date.parse(e.endTime),
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                itemProps: {
                    onMouseDown: () => {
                        window.open("https://play.google.com/store/apps/details?id=" + e.app, '_blank');
                    },
                    style: {
                        overflow: "hidden"
                    }
                }
            })
        )

        props.timeLogRecord.entities.forEach(
            (e: ITimeLogRecord) => items.push({
                id: genId(),
                group: 3,
                title: e.tags[0] + " " + e.description,
                start_time: Date.parse(e.startDate),
                end_time: Date.parse(e.endDate),
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                itemProps: {
                    style: {
                        overflow: "hidden"
                    }
                }
            })
        )

        props.messageRecord.entities.forEach(
            (e: IMessageRecord) => items.push({
                id: genId(),
                group: 4,
                title: e.author + " " + e.content,
                start_time: Date.parse(e.created),
                end_time: Date.parse(e.created),
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                itemProps: {
                    style: {
                        overflow: "hidden"
                    }
                }
            })
        )

        props.listenRecord.entities.forEach(
            (e: IListenRecord) => items.push({
                id: genId(),
                group: 5,
                title: e.artist + e.title,
                start_time: Date.parse(e.listenTime),
                end_time: Date.parse(e.listenTime + 10000),
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                itemProps: {
                    onMouseDown: () => {
                        window.open(e.url, '_blank');
                    },
                    style: {
                        overflow: "hidden"
                    }
                }
            })
        )

        props.youTubeRecord.entities.forEach(
            (e: IYouTubeRecord) => items.push({
                id: genId(),
                group: 6,
                title: e.title,
                start_time: Date.parse(e.time),
                end_time: Date.parse(e.time) + 120000,
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                itemProps: {
                    onMouseDown: () => {
                        window.open(e.url, '_blank');
                    },
                    style: {
                        overflow: "hidden"
                    }
                }
            })
        )

        props.mediaRecord.entities.forEach(
            (e: IMediaRecord) => items.push({
                id: genId(),
                group: 7,
                title: e.path,
                start_time: Date.parse(e.created),
                end_time: Date.parse(e.created) + 10000,
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                itemProps: {
                    onMouseDown: () => {
                        window.open(host + e.url, '_blank');
                    },
                    style: {
                        overflow: "hidden",
                        background: 'fuchsia'
                    }
                }
            })
        )

        props.miFitActivityRecord.entities.forEach(
            (e: IMiFitActivityRecord) => items.push({
                id: genId(),
                group: 8,
                title: "Sleep",
                start_time: Date.parse(e.sleepStart),
                end_time: Date.parse(e.sleepEnd),
                canMove: false,
                canResize: false,
                canChangeGroup: false,
                itemProps: {
                    style: {
                        overflow: "hidden",
                        background: 'tomato'
                    }
                }
            })
        )

        return items;
    }

    const mapGroups = () => {
        let groups: any[] = [];
        if (props.autoTimerRecord.entities.length > 0) {
            groups.push({ id: 1, title: 'PC time log' })
        }
        if (props.wellbeingRecord.entities.length > 0) {
            groups.push({ id: 2, title: 'Android phone' })
        }
        if (props.timeLogRecord.entities.length > 0) {
            groups.push({ id: 3, title: 'Time log' })
        }
        if (props.messageRecord.entities.length > 0) {
            groups.push({ id: 4, title: 'Messages' })
        }
        if (props.listenRecord.entities.length > 0) {
            groups.push({ id: 5, title: 'Music' })
        }
        if (props.youTubeRecord.entities.length > 0) {
            groups.push({ id: 6, title: 'YouTube' })
        }
        if (props.mediaRecord.entities.length > 0) {
            groups.push({ id: 7, title: 'Media files' })
        }
        if (props.miFitActivityRecord.entities.length > 0) {
            groups.push({ id: 8, title: 'Sleep' })
        }
        return groups
    }

    return (
        <div>
            <Timeline
                groups={mapGroups()}
                items={mapRecordsToItems()}
                defaultTimeStart={new Date(now - 86400000)}
                defaultTimeEnd={new Date(now)}
                minZoom={60000}>
                <TimelineMarkers>
                    <CustomMarker date={now} />
                    <CursorMarker />
                </TimelineMarkers>
            </Timeline>
        </div>
    );
}

const mapStateToProps = (
    { youTubeRecord,
        timeLogRecord,
        autoTimerRecord,
        messageRecord,
        wellbeingRecord,
        listenRecord,
        mediaRecord,
        miFitActivityRecord
    }: IRootState) => ({
        youTubeRecord,
        timeLogRecord,
        autoTimerRecord,
        messageRecord,
        wellbeingRecord,
        mediaRecord,
        listenRecord,
        miFitActivityRecord
    });


const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TimelineWrapper);
