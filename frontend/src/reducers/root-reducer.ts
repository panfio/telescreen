import { combineReducers } from 'redux';
import youTubeRecord, { YouTubeRecordState } from './youtube-record.reducer';
import timeLogRecord, { TimeLogRecordState } from './timelog-record.reducer';
import autoTimerRecord, { AutoTimerRecordState } from './autotimer-record.reducer';
import messageRecord, { MessageRecordState } from './message-record.reducer';
import wellbeingRecord, { WellbeingRecordState } from './wellbeing-record.reducer';
import listenRecord, { ListenRecordState } from './listen-record.reducer';
import mediaRecord, { MediaRecordState } from './media-record.reducer';
import miFitActivityRecord, { MiFitActivityRecordState } from './mifitactivity-record.reducer';
import callRecord, { CallRecordState } from './call-record.reducer';
import timenterval, { TimeIntervalState } from './time-interval-reducer';

export interface IRootState {
  readonly youTubeRecord: YouTubeRecordState;
  readonly timeLogRecord: TimeLogRecordState;
  readonly autoTimerRecord: AutoTimerRecordState;
  readonly messageRecord: MessageRecordState;
  readonly wellbeingRecord: WellbeingRecordState;
  readonly listenRecord: ListenRecordState;
  readonly mediaRecord: MediaRecordState;
  readonly miFitActivityRecord: MiFitActivityRecordState;
  readonly callRecord: CallRecordState;
  readonly timenterval: TimeIntervalState;
}

const rootReducer = combineReducers<IRootState>({
  youTubeRecord,
  timeLogRecord,
  autoTimerRecord,
  messageRecord,
  wellbeingRecord,
  mediaRecord,
  listenRecord,
  miFitActivityRecord,
  callRecord,
  timenterval
});

export default rootReducer;