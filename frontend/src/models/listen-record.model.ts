export interface IListenRecord {
    id?: number;
    externalId?: string;
    listenTime: string;
    title: string;
    artist: string;
    url: string;
    path?: string;
    type?: string;
}
