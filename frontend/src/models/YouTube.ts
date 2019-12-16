export class YouTube {
    id: number;
    title: string;
    url: string;
    time: string;
    constructor(title: string, id: number, url: string, time: string) {
      this.title = title;
      this.id = id;
      this.url = url;
      this.time = time;
    }
}