import { fetchTimeLogRecords } from './timelog-record.reducer';
import { fetchYouTubeRecords } from './youtube-record.reducer';
import { fetchAutoTimerRecords } from './autotimer-record.reducer';
import { fetchMessageRecords } from './message-record.reducer';
import { fetchWellbeingRecords } from './wellbeing-record.reducer';
import { fetchListenRecords } from './listen-record.reducer';
import { fetchMediaRecords } from './media-record.reducer';
import { fetchMiFitActivityRecords } from './mifitactivity-record.reducer';
import { fetchCallRecords } from './call-record.reducer';

export const fetchAll = (startDate: Date, endDate: Date) => {
    return (dispath: any) => {
        dispath(fetchYouTubeRecords(startDate, endDate));
        dispath(fetchTimeLogRecords(startDate, endDate));
        dispath(fetchAutoTimerRecords(startDate, endDate));
        dispath(fetchMessageRecords(startDate, endDate));
        dispath(fetchWellbeingRecords(startDate, endDate));
        dispath(fetchListenRecords(startDate, endDate));
        dispath(fetchMediaRecords(startDate, endDate));
        dispath(fetchMiFitActivityRecords(startDate, endDate));
        dispath(fetchCallRecords(startDate, endDate));
    }
}