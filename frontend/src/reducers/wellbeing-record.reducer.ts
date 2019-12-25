import { IWellbeingRecord } from '../models/wellbeing-record.model';
import { apiService } from '../services/ApiService';

export const ACTION_TYPES = {
    FETCH_WELLBEINGRECORD_LIST: 'wellbeingRecord/FETCH_WELLBEINGRECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<IWellbeingRecord>
};

export type WellbeingRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: WellbeingRecordState = initialState, action: any): WellbeingRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_WELLBEINGRECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<IWellbeingRecord>) => ({
    type: ACTION_TYPES.FETCH_WELLBEINGRECORD_LIST,
    items: items
});

export const fetchWellbeingRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getWellbeingHistory(startDate, endDate).then((data: ReadonlyArray<IWellbeingRecord>) => {
            dispath(setEntities(data))
        });
    }
}