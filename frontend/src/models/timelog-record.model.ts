export interface ITimeLogRecord {
    id?: string;
    startDate: string;
    endDate: string;
    description?: string;
    location?: string;
    feeling?: number;
    tags: string[];
}
