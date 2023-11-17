import * as React from 'react';
import { Provider, useDispatch, useSelector } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import {combineReducer} from "./services/store/store";
import {Router} from "./services/Router";

const combinedStore = configureStore({reducer:combineReducer});

export const App:React.FC = ():React.ReactElement => {

    return (
        <Provider store={combinedStore}>
            <Router/>
        </Provider>);
}
