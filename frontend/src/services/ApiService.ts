

class ApiService {

    /////   TODO   /////
    host:string = 'http://localhost:8080'
    // host:string = ''

    getWrapper(path: string, startDate: Date, endDate: Date): any {
      let from = startDate.toISOString().substring(0, startDate.toISOString().length - 1);
      let to = endDate.toISOString().substring(0, endDate.toISOString().length - 1);
      let params:any = { from: from , to: to }

      let url = new URL(this.host + path);
      Object.keys(params).forEach((key:any) => url.searchParams.append(key, params[key]))
      return fetch(url.href.substring(this.host.length))
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
}
export default ApiService;