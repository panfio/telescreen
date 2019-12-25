import { combineReducers } from 'redux';
import youTubeRecord, { YouTubeRecordState } from './youtube-record.reducer';
import timeLogRecord, { TimeLogRecordState } from './timelog-record.reducer';
import autoTimerRecord, { AutoTimerRecordState } from './autotimer-record.reducer';
import messageRecord, { MessageRecordState } from './message-record.reducer';
import wellbeingRecord, { WellbeingRecordState } from './wellbeing-record.reducer';
import listenRecord, { ListenRecordState } from './listen-record.reducer';
import mediaRecord, { MediaRecordState } from './media-record.reducer';

export interface IRootState {
  readonly youTubeRecord: YouTubeRecordState;
  readonly timeLogRecord: TimeLogRecordState;
  readonly autoTimerRecord: AutoTimerRecordState;
  readonly messageRecord: MessageRecordState;
  readonly wellbeingRecord: WellbeingRecordState;
  readonly listenRecord: ListenRecordState;
  readonly mediaRecord: MediaRecordState;
}

const rootReducer = combineReducers<IRootState>({
  youTubeRecord,
  timeLogRecord,
  autoTimerRecord,
  messageRecord,
  wellbeingRecord,
  mediaRecord,
  listenRecord
});

export default rootReducer;