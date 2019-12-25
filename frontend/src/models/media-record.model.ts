export interface IMediaRecord {
    id?: number;
    created: string;
    url: string;
    path?: string;
    type?: number;
}

export const defaultValue: Readonly<IMediaRecord> = {
    id: -1,
    path: '',
    url: '',
    created: new Date(Date.now()).toISOString(),
    type: 0
};