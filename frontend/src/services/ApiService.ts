import { IYouTubeRecord } from '../models/youtube-record.model';
import { ITimeLogRecord } from '../models/timelog-record.model';
import { IAutoTimerRecord } from '../models/autotimer-record.model';
import { IMessageRecord } from '../models/message-record.model';
import { IWellbeingRecord } from '../models/wellbeing-record.model';
import { IListenRecord } from '../models/listen-record.model';
import { IMediaRecord } from '../models/media-record.model';

export const host: string = (process.env.REACT_APP_API_URL == null) ? "" : process.env.REACT_APP_API_URL;

export const apiService = {

  processAll() {
    fetch(`${host}/process/all`);
  },

  async getYouTubes(startDate: Date, endDate: Date): Promise<ReadonlyArray<IYouTubeRecord>> {
    const response = await fetch(`${host}/app/youtube?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getTimeLogs(startDate: Date, endDate: Date): Promise<ReadonlyArray<ITimeLogRecord>> {
    const response = await fetch(`${host}/app/timelog?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getAutotimers(startDate: Date, endDate: Date): Promise<ReadonlyArray<IAutoTimerRecord>> {
    const response = await fetch(`${host}/app/autotimer?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getListenHistory(startDate: Date, endDate: Date): Promise<ReadonlyArray<IListenRecord>> {
    const response = await fetch(`${host}/app/listenhistory?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getMediaHistory(startDate: Date, endDate: Date): Promise<ReadonlyArray<IMediaRecord>> {
    const response = await fetch(`${host}/media?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getWellbeingHistory(startDate: Date, endDate: Date): Promise<ReadonlyArray<IWellbeingRecord>> {
    const response = await fetch(`${host}/app/wellbeing?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getMessageHistory(startDate: Date, endDate: Date): Promise<ReadonlyArray<IMessageRecord>> {
    const response = await fetch(`${host}/app/message?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  }
}

export default apiService;