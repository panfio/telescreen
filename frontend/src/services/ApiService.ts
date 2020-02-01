export const host: string = (process.env.REACT_APP_API_URL == null) ? "" : process.env.REACT_APP_API_URL;

export const apiService = {

  processAll() {
    fetch(`${host}/handler/process/all`);
  },

  async getYouTubes(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/video/search/findByTimeBetween?startTime=${startDate.toISOString()}&endTime=${endDate.toISOString()}`)
    return await response.json();
  },

  async getTimeLogs(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/timelog/search/findByStartDateBetween?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`)
    return await response.json();
  },

  async getAutotimers(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/autotimer/search/findByStartTimeBetween?startTime=${startDate.toISOString()}&endTime=${endDate.toISOString()}`)
    return await response.json();
  },

  async getListenHistory(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/music/search/findByListenTimeBetween?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`)
    return await response.json();
  },

  async getMediaHistory(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/media/search/findByCreatedBetween?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`)
    return await response.json();
  },

  async getWellbeingHistory(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/wellbeing/search/findByStartTimeBetween?startTime=${startDate.toISOString()}&endTime=${endDate.toISOString()}`)
    return await response.json();
  },

  async getMessageHistory(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/message/search/findByCreatedBetween?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`)
    return await response.json();
  },

  async getCallHistory(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/call/search/findByDateBetween?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`)
    return await response.json();
  },

  async getMiFitActivity(startDate: Date, endDate: Date): Promise<any> {
    const response = await fetch(`${host}/data/mifit/search/findByDateBetween?startDate=${startDate.toISOString()}&endDate=${endDate.toISOString()}`)
    return await response.json();
  }
}

export default apiService;