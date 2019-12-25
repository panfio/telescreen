import { ITimeLogRecord } from '../models/timelog-record.model';
import { apiService } from '../services/ApiService';

export const ACTION_TYPES = {
    FETCH_TIMELOGRECORD_LIST: 'timeLogRecord/FETCH_TIMELOGRECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<ITimeLogRecord>
};

export type TimeLogRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: TimeLogRecordState = initialState, action: any): TimeLogRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_TIMELOGRECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<ITimeLogRecord>) => ({
    type: ACTION_TYPES.FETCH_TIMELOGRECORD_LIST,
    items: items
});

export const fetchTimeLogRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getTimeLogs(startDate, endDate).then((data: ReadonlyArray<ITimeLogRecord>) => {
            dispath(setEntities(data))
        });
    }
}