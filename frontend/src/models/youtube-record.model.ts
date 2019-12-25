export interface IYouTubeRecord {
    id?: number;
    title?: string;
    url?: string;
    time: string;
}

export const defaultValue: Readonly<IYouTubeRecord> = {
    id: -1,
    title: '',
    url: '',
    time: new Date(Date.now()).toISOString()
};
