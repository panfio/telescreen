import { IMessageRecord } from '../models/message-record.model';
import { apiService } from '../services/ApiService';

export const ACTION_TYPES = {
    FETCH_MESSAGERECORD_LIST: 'messageRecord/FETCH_MESSAGERECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<IMessageRecord>
};

export type MessageRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: MessageRecordState = initialState, action: any): MessageRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_MESSAGERECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<IMessageRecord>) => ({
    type: ACTION_TYPES.FETCH_MESSAGERECORD_LIST,
    items: items
});

export const fetchMessageRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getMessageHistory(startDate, endDate).then((data: any) => {
            dispath(setEntities(data._embedded.messages))
        });
    }
}