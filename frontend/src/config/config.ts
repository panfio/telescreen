export const oneDay: number = 24 * 60 * 60 * 1000;
export const initialIntervalStartTime: Date = new Date(new Date(Date.now() - oneDay).setHours(0,0,0,0));
export const initialIntervalEndTime: Date = new Date(Date.now());