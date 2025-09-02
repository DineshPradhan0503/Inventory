import { configureStore } from '@reduxjs/toolkit';
import { thunk } from 'redux-thunk';
import authReducer from './authSlice';
import productReducer from './productSlice';
import saleReducer from './saleSlice';
import reportingReducer from './reportingSlice';

const store = configureStore({
    reducer: {
        auth: authReducer,
        products: productReducer,
        sales: saleReducer,
        reports: reportingReducer,
    },
    middleware: (getDefaultMiddleware) => getDefaultMiddleware().concat(thunk),
});

export default store;
