import rootReducer from '../reducers/root-reducer';
import { createStore, applyMiddleware, compose } from 'redux';
import thunkMiddleware from 'redux-thunk';
import { composeWithDevTools } from 'redux-devtools-extension'

const middlewares = [thunkMiddleware]
const middlewareEnhancer = applyMiddleware(...middlewares)

const composedEnhancers = (process.env.NODE_ENV === 'development')
    ? composeWithDevTools(middlewareEnhancer)
    : compose(middlewareEnhancer);

const initStore = () => createStore(rootReducer, undefined, composedEnhancers)

export default initStore;