import { IYouTubeRecord } from '../models/youtube-record.model';
import { ITimeLogRecord } from '../models/timelog-record.model';
import { IAutoTimerRecord } from '../models/autotimer-record.model';
import { IMessageRecord } from '../models/message-record.model';
import { IWellbeingRecord } from '../models/wellbeing-record.model';
import { IListenRecord } from '../models/listen-record.model';
import { IMediaRecord } from '../models/media-record.model';
import { IMiFitActivityRecord } from '../models/mifitactivity-record.model';

export const host: string = (process.env.REACT_APP_API_URL == null) ? "" : process.env.REACT_APP_API_URL;

export const apiService = {

  processAll() {
    fetch(`${host}/api/process`);
  },

  async getYouTubes(startDate: Date, endDate: Date): Promise<ReadonlyArray<IYouTubeRecord>> {
    const response = await fetch(`${host}/api/video/youtube?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getTimeLogs(startDate: Date, endDate: Date): Promise<ReadonlyArray<ITimeLogRecord>> {
    const response = await fetch(`${host}/api/timelog?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getAutotimers(startDate: Date, endDate: Date): Promise<ReadonlyArray<IAutoTimerRecord>> {
    const response = await fetch(`${host}/api/autotimer?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getListenHistory(startDate: Date, endDate: Date): Promise<ReadonlyArray<IListenRecord>> {
    const response = await fetch(`${host}/api/music?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getMediaHistory(startDate: Date, endDate: Date): Promise<ReadonlyArray<IMediaRecord>> {
    const response = await fetch(`${host}/api/media?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getWellbeingHistory(startDate: Date, endDate: Date): Promise<ReadonlyArray<IWellbeingRecord>> {
    const response = await fetch(`${host}/api/wellbeing?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getMessageHistory(startDate: Date, endDate: Date): Promise<ReadonlyArray<IMessageRecord>> {
    const response = await fetch(`${host}/api/message?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  },

  async getMiFitActivity(startDate: Date, endDate: Date): Promise<ReadonlyArray<IMiFitActivityRecord>> {
    const response = await fetch(`${host}/api/mifit?from=${startDate.toISOString().slice(0, -1)}&to=${endDate.toISOString().slice(0, -1)}`)
    return await response.json();
  }
}

export default apiService;