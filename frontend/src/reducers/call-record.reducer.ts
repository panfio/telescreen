import { ICallRecord } from '../models/call-record.model';
import { apiService } from '../services/ApiService';

export const ACTION_TYPES = {
    FETCH_CALLRECORD_LIST: 'callRecord/FETCH_CALLRECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<ICallRecord>
};

export type CallRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: CallRecordState = initialState, action: any): CallRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_CALLRECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<ICallRecord>) => ({
    type: ACTION_TYPES.FETCH_CALLRECORD_LIST,
    items: items
});

export const fetchCallRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getCallHistory(startDate, endDate).then((data: any) => {
            dispath(setEntities(data._embedded.calls))
        });
    }
}