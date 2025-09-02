import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getSales, createSale } from '../redux/saleSlice';
import { getProducts } from '../redux/productSlice';
import {
    Container,
    Box,
    Typography,
    Button,
    TextField,
    Autocomplete,
    Grid,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    CircularProgress,
    Alert
} from '@mui/material';

const SalesPage = () => {
    const dispatch = useDispatch();
    const { sales, isLoading, error } = useSelector((state) => state.sales);
    const { products } = useSelector((state) => state.products);
    const { user } = useSelector((state) => state.auth);

    const [formData, setFormData] = useState({
        productId: '',
        quantitySold: '',
    });
    const [selectedProduct, setSelectedProduct] = useState(null);

    useEffect(() => {
        dispatch(getSales());
        dispatch(getProducts());
    }, [dispatch]);

    const handleProductChange = (event, value) => {
        setSelectedProduct(value);
        setFormData((prevState) => ({
            ...prevState,
            productId: value ? value.id : '',
        }));
    };

    const handleQuantityChange = (e) => {
        setFormData((prevState) => ({
            ...prevState,
            quantitySold: e.target.value,
        }));
    };

    const onSubmit = (e) => {
        e.preventDefault();
        if (formData.productId && formData.quantitySold) {
            dispatch(createSale({
                ...formData,
                quantitySold: parseInt(formData.quantitySold, 10)
            }));
            setFormData({ productId: '', quantitySold: '' });
            setSelectedProduct(null);
        }
    };

    const isAdmin = user && user.roles.includes('ROLE_ADMIN');

    return (
        <Container sx={{ mt: 4 }}>
            <Typography variant="h4" component="h1" gutterBottom>
                Sales
            </Typography>
            <Grid container spacing={4}>
                <Grid item xs={12} md={4}>
                    <Typography variant="h6" component="h2" gutterBottom>
                        Record a Sale
                    </Typography>
                    <Paper sx={{ p: 2 }}>
                        <Box component="form" onSubmit={onSubmit} noValidate>
                            <Autocomplete
                                options={products}
                                getOptionLabel={(option) => option.name}
                                value={selectedProduct}
                                onChange={handleProductChange}
                                renderInput={(params) => <TextField {...params} label="Product" margin="normal" />}
                            />
                            <TextField
                                name="quantitySold"
                                label="Quantity"
                                type="number"
                                value={formData.quantitySold}
                                onChange={handleQuantityChange}
                                fullWidth
                                required
                                margin="normal"
                            />
                            <Button type="submit" variant="contained" sx={{ mt: 2 }} fullWidth disabled={isLoading}>
                                Record Sale
                            </Button>
                        </Box>
                    </Paper>
                </Grid>
                <Grid item xs={12} md={8}>
                    {isAdmin && (
                        <>
                            <Typography variant="h6" component="h2" gutterBottom>
                                Sales History
                            </Typography>
                            {isLoading ? (
                                <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}><CircularProgress /></Box>
                            ) : error ? (
                                <Alert severity="error">{error}</Alert>
                            ) : sales.length === 0 ? (
                                <Typography sx={{ mt: 4, textAlign: 'center' }}>No sales recorded yet.</Typography>
                            ) : (
                                <TableContainer component={Paper}>
                                    <Table>
                                        <TableHead>
                                            <TableRow>
                                                <TableCell>Product</TableCell>
                                                <TableCell>Quantity</TableCell>
                                                <TableCell>Sale Date</TableCell>
                                                <TableCell>Sold By</TableCell>
                                            </TableRow>
                                        </TableHead>
                                        <TableBody>
                                            {sales.map((sale) => {
                                                const product = products.find(p => p.id === sale.productId);
                                                return (
                                                    <TableRow key={sale.id}>
                                                        <TableCell>{product ? product.name : 'N/A'}</TableCell>
                                                        <TableCell>{sale.quantitySold}</TableCell>
                                                        <TableCell>{new Date(sale.saleDate).toLocaleString()}</TableCell>
                                                        <TableCell>{sale.userId}</TableCell>
                                                    </TableRow>
                                                )
                                            })}
                                        </TableBody>
                                    </Table>
                                </TableContainer>
                            )}
                        </>
                    )}
                </Grid>
            </Grid>
        </Container>
    );
};

export default SalesPage;
