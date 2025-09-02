import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import saleService from '../services/saleService';

const initialState = {
    sales: [],
    isLoading: false,
    error: null,
};

export const getSales = createAsyncThunk('sales/getAll', async (_, thunkAPI) => {
    try {
        const response = await saleService.getSales();
        return response.data;
    } catch (error) {
        const message =
            (error.response && error.response.data && error.response.data.message) ||
            error.message ||
            error.toString();
        return thunkAPI.rejectWithValue(message);
    }
});

export const createSale = createAsyncThunk('sales/create', async (saleData, thunkAPI) => {
    try {
        const response = await saleService.createSale(saleData);
        return response.data;
    } catch (error) {
        const message =
            (error.response && error.response.data && error.response.data.message) ||
            error.message ||
            error.toString();
        return thunkAPI.rejectWithValue(message);
    }
});

const saleSlice = createSlice({
    name: 'sales',
    initialState,
    reducers: {
        reset: (state) => initialState,
    },
    extraReducers: (builder) => {
        builder
            .addCase(getSales.pending, (state) => {
                state.isLoading = true;
            })
            .addCase(getSales.fulfilled, (state, action) => {
                state.isLoading = false;
                state.sales = action.payload;
            })
            .addCase(getSales.rejected, (state, action) => {
                state.isLoading = false;
                state.error = action.payload;
            })
            .addCase(createSale.pending, (state) => {
                state.isLoading = true;
            })
            .addCase(createSale.fulfilled, (state, action) => {
                state.isLoading = false;
                state.sales.push(action.payload);
            })
            .addCase(createSale.rejected, (state, action) => {
                state.isLoading = false;
                state.error = action.payload;
            });
    },
});

export const { reset } = saleSlice.actions;
export default saleSlice.reducer;
