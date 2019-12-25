import { IListenRecord } from '../models/listen-record.model';
import { apiService } from '../services/ApiService';

export const ACTION_TYPES = {
    FETCH_LISTENRECORD_LIST: 'listenRecord/FETCH_LISTENRECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<IListenRecord>
};

export type ListenRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: ListenRecordState = initialState, action: any): ListenRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_LISTENRECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<IListenRecord>) => ({
    type: ACTION_TYPES.FETCH_LISTENRECORD_LIST,
    items: items
});

export const fetchListenRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getListenHistory(startDate, endDate).then((data: ReadonlyArray<IListenRecord>) => {
            dispath(setEntities(data))
        });
    }
}