import { IMediaRecord } from '../models/media-record.model';
import { apiService } from '../services/ApiService';

export const ACTION_TYPES = {
    FETCH_MEDIARECORD_LIST: 'mediaRecord/FETCH_MEDIARECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<IMediaRecord>
};

export type MediaRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: MediaRecordState = initialState, action: any): MediaRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_MEDIARECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<IMediaRecord>) => ({
    type: ACTION_TYPES.FETCH_MEDIARECORD_LIST,
    items: items
});

export const fetchMediaRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getMediaHistory(startDate, endDate).then((data: ReadonlyArray<IMediaRecord>) => {
            dispath(setEntities(data))
        });
    }
}