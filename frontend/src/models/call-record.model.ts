export interface ICallRecord {
  id: string;
  number: string;
  date: string;
  duration: number;
  name: string;
  type: number;

}

export const defaultValue: Readonly<ICallRecord> = {
  id: '',
  date: new Date(Date.now()).toISOString(),
  type: -1,
  number: '',
  duration: 0,
  name: '',
};