import React, { useEffect, useMemo } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getStockReport, getSalesReport } from '../redux/reportingSlice';
import {
    Container,
    Grid,
    Paper,
    Typography,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    CircularProgress,
    Alert,
    Box
} from '@mui/material';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';

const ReportingPage = () => {
    const dispatch = useDispatch();
    const { stockReport, salesReport, isLoading, error } = useSelector((state) => state.reports);

    useEffect(() => {
        dispatch(getStockReport());
        dispatch(getSalesReport());
    }, [dispatch]);

    const formattedSalesReport = useMemo(() => {
        if (!salesReport) return [];
        return [
            { name: 'Sales', 'Total Sales Amount': salesReport.totalSalesAmount, 'Total Sales Count': salesReport.totalSalesCount }
        ];
    }, [salesReport]);

    if (isLoading) {
        return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}><CircularProgress /></Box>;
    }

    if (error) {
        return <Container sx={{ mt: 4 }}><Alert severity="error">{error}</Alert></Container>;
    }

    return (
        <Container sx={{ mt: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Reporting
            </Typography>
            <Grid container spacing={4}>
                <Grid item xs={12}>
                    <Paper sx={{ p: 2 }}>
                        <Typography variant="h6" component="h2" gutterBottom>
                            Sales Report
                        </Typography>
                        {salesReport ? (
                            <ResponsiveContainer width="100%" height={300}>
                                <BarChart data={formattedSalesReport}>
                                    <CartesianGrid strokeDasharray="3 3" />
                                    <XAxis dataKey="name" />
                                    <YAxis />
                                    <Tooltip formatter={(value) => typeof value === 'number' ? value.toLocaleString() : value} />
                                    <Legend />
                                    <Bar dataKey="Total Sales Amount" fill="#8884d8" />
                                    <Bar dataKey="Total Sales Count" fill="#82ca9d" />
                                </BarChart>
                            </ResponsiveContainer>
                        ) : (
                            <Typography>No sales data available.</Typography>
                        )}
                    </Paper>
                </Grid>
                <Grid item xs={12}>
                    <Paper sx={{ p: 2 }}>
                        <Typography variant="h6" component="h2" gutterBottom>
                            Stock Report
                        </Typography>
                        {stockReport.length > 0 ? (
                            <TableContainer>
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
                                            <TableRow key={item.productId} sx={{ backgroundColor: item.isLowStock ? 'rgba(255, 0, 0, 0.1)' : 'inherit' }}>
                                                <TableCell>{item.name}</TableCell>
                                                <TableCell>{item.category}</TableCell>
                                                <TableCell>{item.stockQuantity}</TableCell>
                                                <TableCell>{item.threshold}</TableCell>
                                                <TableCell>
                                                    <Typography color={item.isLowStock ? 'error' : 'success.main'}>
                                                        {item.isLowStock ? 'Low Stock' : 'OK'}
                                                    </Typography>
                                                </TableCell>
                                            </TableRow>
                                        ))}
                                    </TableBody>
                                </Table>
                            </TableContainer>
                        ) : (
                            <Typography>No stock data available.</Typography>
                        )}
                    </Paper>
                </Grid>
            </Grid>
        </Container>
    );
};

export default ReportingPage;
