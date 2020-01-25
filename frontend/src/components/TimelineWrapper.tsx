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
import { initialIntervalStartTime, initialIntervalEndTime } from '../config/config';

import { IYouTubeRecord } from '../models/youtube-record.model';
import { ITimeLogRecord } from '../models/timelog-record.model';
import { IAutoTimerRecord } from '../models/autotimer-record.model';
import { IListenRecord } from '../models/listen-record.model';
import { IMediaRecord } from '../models/media-record.model';
import { IWellbeingRecord } from '../models/wellbeing-record.model';
import { IMessageRecord } from '../models/message-record.model';
import { IMiFitActivityRecord } from '../models/mifitactivity-record.model';
import { ICallRecord } from '../models/call-record.model';

function id() {
  let i = 1;
  return function () {
    return i++;
  }
}
const genId = id();

export interface ITimelineProps extends StateProps, DispatchProps { }

export const TimelineWrapper = (props: ITimelineProps) => {

  const callRecordEntities: any[] = props.callRecord.entities.map(
    (e: ICallRecord) => ({
      id: genId(),
      group: 9,
      title: e.name + " " + e.number,
      start_time: Date.parse(e.date),
      end_time: Date.parse(e.date) + e.duration * 1000,
      canMove: false,
      canResize: false,
      canChangeGroup: false,
      itemProps: {
        style: {
          overflow: "hidden",
          background: 'green'
        }
      }
    })
  )

  const autoTimerRecordEntities: any[] = props.autoTimerRecord.entities.map(
    (e: IAutoTimerRecord) => ({
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

  const wellbeingRecordEntities: any[] = props.wellbeingRecord.entities.map(
    (e: IWellbeingRecord) => ({
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

  const timeLogRecordEntities: any[] = props.timeLogRecord.entities.map(
    (e: ITimeLogRecord) => ({
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

  const messageRecordEntities = props.messageRecord.entities.map(
    (e: IMessageRecord) => ({
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

  const listenRecordEntities = props.listenRecord.entities.map(
    (e: IListenRecord) => ({
      id: genId(),
      group: 5,
      title: e.artist + e.title,
      start_time: Date.parse(e.listenTime),
      end_time: Date.parse(e.listenTime) + 10000,
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

  const youTubeRecordEntities = props.youTubeRecord.entities.map(
    (e: IYouTubeRecord) => ({
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

  const mediaRecordEntities = props.mediaRecord.entities.map(
    (e: IMediaRecord) => ({
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
          window.open(host + "/api/media/file?filename=" + e.path, '_blank');
        },
        style: {
          overflow: "hidden",
          background: 'fuchsia'
        }
      }
    })
  )

  const toHHMM = (time: number) => {
    return Math.floor(time / 60) + "h" + Math.floor(time % 60) + "m";
  }

  const miFitActivityRecordEntities = props.miFitActivityRecord.entities.map(
    (e: IMiFitActivityRecord) => ({
      id: genId(),
      group: 8,
      title: "In bed: " + toHHMM(e.inBedMin) + "\nDeep Sleep: " + toHHMM(e.deepSleepMin) + "\nLight Sleep: " + toHHMM(e.lightSleepMin),
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


  const mapRecordsToItems = () => {
    let items: any[] = [
      ...callRecordEntities,
      ...autoTimerRecordEntities,
      ...wellbeingRecordEntities,
      ...timeLogRecordEntities,
      ...messageRecordEntities,
      ...listenRecordEntities,
      ...youTubeRecordEntities,
      ...mediaRecordEntities,
      ...miFitActivityRecordEntities
    ]
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
    if (props.callRecord.entities.length > 0) {
      groups.push({ id: 9, title: 'Calls' })
    }
    return groups
  }

  return (
    <div>
      <Timeline
        groups={mapGroups()}
        items={mapRecordsToItems()}
        defaultTimeStart={initialIntervalStartTime}
        defaultTimeEnd={initialIntervalEndTime}
        minZoom={60000}>
        <TimelineMarkers>
          <CustomMarker date={initialIntervalEndTime} />
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
    miFitActivityRecord,
    callRecord
  }: IRootState) => ({
    youTubeRecord,
    timeLogRecord,
    autoTimerRecord,
    messageRecord,
    wellbeingRecord,
    mediaRecord,
    listenRecord,
    miFitActivityRecord,
    callRecord
  });


const mapDispatchToProps = {};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(TimelineWrapper);
