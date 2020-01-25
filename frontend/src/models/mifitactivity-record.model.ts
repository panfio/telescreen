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
