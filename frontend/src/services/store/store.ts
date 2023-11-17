import {
    legacy_createStore as createStore,
    combineReducers,
    applyMiddleware,
} from "redux";
import {composeWithDevTools} from 'redux-devtools-extension'
import {client} from "../reducers/clientReducer";
import thunk from "redux-thunk";

export const combineReducer = combineReducers({
    client,
})

export const store = createStore(
    combineReducer,
    composeWithDevTools(applyMiddleware(thunk))
);