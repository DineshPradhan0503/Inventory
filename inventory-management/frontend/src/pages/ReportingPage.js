import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getStockReport, getSalesReport } from '../redux/reportingSlice';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper } from '@mui/material';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const ReportingPage = () => {
    const dispatch = useDispatch();
    const { stockReport, salesReport, isLoading, error } = useSelector((state) => state.reports);

    useEffect(() => {
        dispatch(getStockReport());
        dispatch(getSalesReport());
    }, [dispatch]);

    if (isLoading) {
        return <h1>Loading...</h1>;
    }

    if (error) {
        return <h1>{error}</h1>;
    }

    return (
        <div>
            <h1>Reporting</h1>
            <h2>Stock Report</h2>
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Product</TableCell>
                            <TableCell>Category</TableCell>
                            <TableCell>Stock</TableCell>
                            <TableCell>Threshold</TableCell>
                            <TableCell>Status</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {stockReport.map((item) => (
                            <TableRow key={item.productId}>
                                <TableCell>{item.name}</TableCell>
                                <TableCell>{item.category}</TableCell>
                                <TableCell>{item.stockQuantity}</TableCell>
                                <TableCell>{item.threshold}</TableCell>
                                <TableCell>{item.isLowStock ? 'Low Stock' : 'OK'}</TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <h2>Sales Report</h2>
            {salesReport && (
                <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={[salesReport]}>
                        <CartesianGrid strokeDasharray="3 3" />
                        <XAxis dataKey="name" />
                        <YAxis />
                        <Tooltip />
                        <Legend />
                        <Bar dataKey="totalSalesAmount" fill="#8884d8" name="Total Sales Amount" />
                        <Bar dataKey="totalSalesCount" fill="#82ca9d" name="Total Sales Count" />
                    </BarChart>
                </ResponsiveContainer>
            )}
        </div>
    );
};

export default ReportingPage;
