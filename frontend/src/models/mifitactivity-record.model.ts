export interface IMiFitActivityRecord {
  id: string;
  date: string;
  sleepStart: string;
  sleepEnd: string;
  inBedMin: number;
  deepSleepMin: number;
  lightSleepMin: number;
  awakeMin: number;
  dailyDistanceMeter: number;
  dailySteps: number;
  dailyBurnCalories: number;
  walkDistance: number;
  walkTimeMin: number;
  walkBurnCalories: number;
  runDistanceMeter: number;
  runTimeMin: number;
  runBurnCalories: number;
}

export const defaultValue: Readonly<IMiFitActivityRecord> = {
  id: '',
  date: new Date(Date.now()).toISOString(),
  sleepStart: new Date(Date.now()).toISOString(),
  sleepEnd: new Date(Date.now()).toISOString(),
  inBedMin: 0,
  deepSleepMin: 0,
  lightSleepMin: 0,
  awakeMin: 0,
  dailyDistanceMeter: 0,
  dailySteps: 0,
  dailyBurnCalories: 0,
  walkDistance: 0,
  walkTimeMin: 0,
  walkBurnCalories: 0,
  runDistanceMeter: 0,
  runTimeMin: 0,
  runBurnCalories: 0,
};
