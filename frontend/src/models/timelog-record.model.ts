export interface ITimeLogRecord {
    id?: string;
    startDate: string;
    endDate: string;
    description?: string;
    location?: string;
    feeling?: number;
    tags: string[];
}

export const defaultValue: Readonly<ITimeLogRecord> = {
    id: '',
    startDate: new Date(Date.now()).toISOString(),
    endDate: new Date(Date.now()).toISOString(),
    description: '',
    location: '',
    feeling: 0,
    tags: []
};
