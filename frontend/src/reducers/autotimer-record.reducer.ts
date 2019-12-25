import { IAutoTimerRecord } from '../models/autotimer-record.model';
import { apiService } from '../services/ApiService';

export const ACTION_TYPES = {
    FETCH_AUTOTIMERRECORD_LIST: 'timeLogRecord/FETCH_AUTOTIMERRECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<IAutoTimerRecord>
};

export type AutoTimerRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: AutoTimerRecordState = initialState, action: any): AutoTimerRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_AUTOTIMERRECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<IAutoTimerRecord>) => ({
    type: ACTION_TYPES.FETCH_AUTOTIMERRECORD_LIST,
    items: items
});

export const fetchAutoTimerRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getAutotimers(startDate, endDate).then((data: ReadonlyArray<IAutoTimerRecord>) => {
            dispath(setEntities(data))
        });
    }
}