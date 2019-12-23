function id() {
  let i = 1;
  return function () {
    return i++;
  }
}
const genId = id();

class ApiService {

  env: string = process.env.NODE_ENV
  host: string = (process.env.REACT_APP_API_URL == null) ? "" : process.env.REACT_APP_API_URL;

  constructUrl(path: string, params: any): string {
    let dummy = 'http://dummyurl:8080';
    let url: URL;
    if (this.env === "production") {
      url = new URL(dummy + path);
    } else {
      url = new URL(this.host + path);
    }
    Object.keys(params).forEach((key: any) => url.searchParams.append(key, params[key]));
    return (this.env === "production")
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
  getMessageHistory(startDate: Date, endDate: Date): any {
    return this.getWrapper('/app/message', startDate, endDate);
  }
  processAll() {
    fetch(this.constructUrl("/process/all", {}));
  }

  async getItems(startDate: Date, endDate: Date) {
    let items: any = [];
    await this.getAutotimers(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 1,
        title: e.name,
        start_time: Date.parse(e.startTime),
        end_time: Date.parse(e.endTime),
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          style: {
            overflow: "hidden"
          }
        }
      }))
    );

    await this.getTimeLogs(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 3,
        title: e.tags[0],
        start_time: Date.parse(e.startDate),
        end_time: Date.parse(e.endDate),
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          style: {
            overflow: "hidden"
          }
        }
      })
      )
    );
    await this.getListenHistory(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 5,
        title: e.artist + e.title,
        start_time: Date.parse(e.listenTime),
        end_time: Date.parse(e.listenTime + 10000),
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          onMouseDown: () => {
            window.open(e.url, '_blank');
          },
          style: {
            overflow: "hidden"
          }
        }
      }))
    );
    await this.getYouTubes(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 6,
        title: e.title,
        start_time: Date.parse(e.time),
        end_time: Date.parse(e.time) + 120000,
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          onMouseDown: () => {
            window.open(e.url, '_blank');
          },
          style: {
            overflow: "hidden"
          }
        }
      }))
    );
    await this.getMediaHistory(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 7,
        title: e.path,
        start_time: Date.parse(e.created),
        end_time: Date.parse(e.created) + 10000,
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          onMouseDown: () => {
            window.open(this.host + e.url, '_blank');
          },
          style: {
            overflow: "hidden",
            background: 'fuchsia'
          }
        }
      }))
    );
    await this.getWellbeingHistory(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 2,
        title: e.app,
        start_time: Date.parse(e.startTime),
        end_time: Date.parse(e.endTime),
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          onMouseDown: () => {
            window.open("https://play.google.com/store/apps/details?id=" + e.app, '_blank');
          },
          style: {
            overflow: "hidden"
          }
        }
      }))
    );
    await this.getMessageHistory(startDate, endDate).then(
      (e: any) => e.map((e: any) => items.push({
        id: genId(),
        group: 4,
        title: e.author + " " + e.content,
        start_time: Date.parse(e.created),
        end_time: Date.parse(e.created),
        canMove: false,
        canResize: false,
        canChangeGroup: false,
        itemProps: {
          style: {
            overflow: "hidden"
          }
        }
      }))
    );
    return items;
  }
}

export default ApiService;