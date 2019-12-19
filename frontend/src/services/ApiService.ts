

class ApiService {

  env: string = process.env.NODE_ENV
  host: any = process.env.REACT_APP_API_URL;

  constructUrl(path: string, params: any): string {
    console.log(this.env, this.host);
    let dummy = 'http://dummyurl:8080';
    let url: URL;
    if (this.env == "production") {
      url = new URL(dummy + path);
    } else {
      url = new URL(this.host + path);
    }
    Object.keys(params).forEach((key: any) => url.searchParams.append(key, params[key]));
    return (this.env == "production")
      ? url.href.substring(dummy.length)
      : url.href;
  }

  getWrapper(path: string, startDate: Date, endDate: Date): any {
    let from = startDate.toISOString().slice(0, -1);
    let to = endDate.toISOString().slice(0, -1);
    let params: any = { from: from, to: to }
    return fetch(this.constructUrl(path, params))
      .then(res => res.json())
  }

  getYouTubes(startDate: Date, endDate: Date): any {
    return this.getWrapper('/app/youtube', startDate, endDate)
  }
  getAutotimers(startDate: Date, endDate: Date): any {
    return this.getWrapper('/app/autotimer', startDate, endDate)
  }
  getTimeLogs(startDate: Date, endDate: Date): any {
    return this.getWrapper('/app/timelog', startDate, endDate)
  }
  getListenHistory(startDate: Date, endDate: Date): any {
    return this.getWrapper('/app/listenhistory', startDate, endDate);
  }
  getMediaHistory(startDate: Date, endDate: Date): any {
    return this.getWrapper('/media', startDate, endDate);
  }
  getWellbeingHistory(startDate: Date, endDate: Date): any {
    return this.getWrapper('/app/wellbeing', startDate, endDate);
  }
  processAll() {
    fetch(this.constructUrl("/process/all", {}));
  }

}
export default ApiService;