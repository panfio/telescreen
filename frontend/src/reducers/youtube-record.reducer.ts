import { IYouTubeRecord } from '../models/youtube-record.model';
import { apiService } from '../services/ApiService';

export const ACTION_TYPES = {
    FETCH_YOUTUBERECORD_LIST: 'youTubeRecord/FETCH_YOUTUBERECORD_LIST',
};

const initialState = {
    entities: [] as ReadonlyArray<IYouTubeRecord>
};

export type YouTubeRecordState = Readonly<typeof initialState>;

// Reducer

export default (state: YouTubeRecordState = initialState, action: any): YouTubeRecordState => {
    switch (action.type) {
        case ACTION_TYPES.FETCH_YOUTUBERECORD_LIST:
            return {
                ...state,
                entities: [...action.items]
            };
        default:
            return state;
    }
};

// Actions

export const setEntities = (items: ReadonlyArray<IYouTubeRecord>) => ({
    type: ACTION_TYPES.FETCH_YOUTUBERECORD_LIST,
    items: items
});

export const fetchYouTubeRecords = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        apiService.getYouTubes(startDate, endDate).then((data: any) => {
            dispath(setEntities(data._embedded.videos))
        });
    }
}
