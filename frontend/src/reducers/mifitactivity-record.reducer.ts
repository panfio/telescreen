import { apiService } from '../services/ApiService';
import { IMiFitActivityRecord } from '../models/mifitactivity-record.model';

export const ACTION_TYPES = {
    FETCH_MIFITACTIVITYRECORD_LIST: 'miFitActivityRecord/FETCH_MIFITACTIVITYRECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<IMiFitActivityRecord>
};

export type MiFitActivityRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: MiFitActivityRecordState = initialState, action: any): MiFitActivityRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_MIFITACTIVITYRECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<IMiFitActivityRecord>) => ({
    type: ACTION_TYPES.FETCH_MIFITACTIVITYRECORD_LIST,
    items: items
});

export const fetchMiFitActivityRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getMiFitActivity(startDate, endDate).then((data: any) => {
            dispath(setEntities(data._embedded.mifits))
        });
    }
}