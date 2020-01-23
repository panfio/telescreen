import { initialIntervalStartTime, initialIntervalEndTime } from '../config/config';

export const ACTION_TYPES = {
  SET_INTERVAL_START: 'timeInterval/SET_INTERVAL_START',
  SET_INTERVAL_END: 'timeInterval/SET_INTERVAL_END',
};


const initialState = {
  from: initialIntervalStartTime,
  to: initialIntervalEndTime,
};

export type TimeIntervalState = Readonly<typeof initialState>;

// Reducer

export default (state: TimeIntervalState = initialState, action: any): TimeIntervalState => {
  switch (action.type) {
    case ACTION_TYPES.SET_INTERVAL_START:
      return {
        ...state,
        from: action.date
      };
    case ACTION_TYPES.SET_INTERVAL_END:
      return {
        ...state,
        to: action.date
      };
    default:
      return state;
  }
};

// Actions

export const setIntervalStart = (date: Date) => ({
  type: ACTION_TYPES.SET_INTERVAL_START,
  date: date
});

export const setIntervalEnd = (date: Date) => ({
  type: ACTION_TYPES.SET_INTERVAL_END,
  date: date
});