export interface IWellbeingRecord {
    id?: number;
    app: string;
    startTime: string;
    endTime: string;
    type?: string;
}

export const defaultValue: Readonly<IWellbeingRecord> = {
    id: 1,
    app: '',
    startTime: new Date(Date.now()).toISOString(),
    endTime: new Date(Date.now()).toISOString(),
    type: '',
};
