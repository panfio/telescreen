export interface IAutoTimerRecord {
    id?: number;
    name: string;
    type?: number;
    startTime: string;
    endTime: string;
}

export const defaultValue: Readonly<IAutoTimerRecord> = {
    id: 0,
    name: '',
    startTime: new Date(Date.now()).toISOString(),
    endTime: new Date(Date.now()).toISOString(),
    type: 0
};
