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

export const defaultValue: Readonly<IListenRecord> = {
    id: -1,
    path: '',
    url: '',
    listenTime: new Date(Date.now()).toISOString(),
    type: '',
    externalId: '',
    title: '',
    artist: ''
};