import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import reportingService from '../services/reportingService';

const initialState = {
    stockReport: [],
    salesReport: null,
    isLoading: false,
    error: null,
};

export const getStockReport = createAsyncThunk('reports/stock', async (_, thunkAPI) => {
    try {
        const response = await reportingService.getStockReport();
        return response.data;
    } catch (error) {
        const message =
            (error.response && error.response.data && error.response.data.message) ||
            error.message ||
            error.toString();
        return thunkAPI.rejectWithValue(message);
    }
});

export const getSalesReport = createAsyncThunk('reports/sales', async (_, thunkAPI) => {
    try {
        const response = await reportingService.getSalesReport();
        return response.data;
    } catch (error) {
        const message =
            (error.response && error.response.data && error.response.data.message) ||
            error.message ||
            error.toString();
        return thunkAPI.rejectWithValue(message);
    }
});

const reportingSlice = createSlice({
    name: 'reports',
    initialState,
    reducers: {
        reset: (state) => initialState,
    },
    extraReducers: (builder) => {
        builder
            .addCase(getStockReport.pending, (state) => {
                state.isLoading = true;
            })
            .addCase(getStockReport.fulfilled, (state, action) => {
                state.isLoading = false;
                state.stockReport = action.payload;
            })
            .addCase(getStockReport.rejected, (state, action) => {
                state.isLoading = false;
                state.error = action.payload;
            })
            .addCase(getSalesReport.pending, (state) => {
                state.isLoading = true;
            })
            .addCase(getSalesReport.fulfilled, (state, action) => {
                state.isLoading = false;
                state.salesReport = action.payload;
            })
            .addCase(getSalesReport.rejected, (state, action) => {
                state.isLoading = false;
                state.error = action.payload;
            });
    },
});

export const { reset } = reportingSlice.actions;
export default reportingSlice.reducer;
