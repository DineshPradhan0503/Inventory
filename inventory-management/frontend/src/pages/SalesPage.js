import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getSales, createSale } from '../redux/saleSlice';
import { getProducts } from '../redux/productSlice';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, TextField, Autocomplete } from '@mui/material';

const SalesPage = () => {
    const dispatch = useDispatch();
    const { sales, isLoading, error } = useSelector((state) => state.sales);
    const { products } = useSelector((state) => state.products);
    const { user } = useSelector((state) => state.auth);

    const [formData, setFormData] = useState({
        productId: '',
        quantitySold: '',
    });

    useEffect(() => {
        dispatch(getSales());
        dispatch(getProducts());
    }, [dispatch]);

    const onChange = (e, value) => {
        if (value) {
            setFormData((prevState) => ({
                ...prevState,
                productId: value.id,
            }));
        } else {
            setFormData((prevState) => ({
                ...prevState,
                [e.target.name]: e.target.value,
            }));
        }
    };

    const onSubmit = (e) => {
        e.preventDefault();
        dispatch(createSale(formData));
    };

    if (isLoading) {
        return <h1>Loading...</h1>;
    }

    if (error) {
        return <h1>{error}</h1>;
    }

    const isAdmin = user && user.roles.includes('ROLE_ADMIN');

    return (
        <div>
            <h1>Sales</h1>
            <h2>Record a Sale</h2>
            <form onSubmit={onSubmit}>
                <Autocomplete
                    options={products}
                    getOptionLabel={(option) => option.name}
                    onChange={onChange}
                    renderInput={(params) => <TextField {...params} label="Product" />}
                />
                <TextField
                    name="quantitySold"
                    label="Quantity"
                    type="number"
                    value={formData.quantitySold}
                    onChange={onChange}
                    fullWidth
                    required
                />
                <Button type="submit" variant="contained" sx={{ mt: 2 }}>
                    Record Sale
                </Button>
            </form>

            {isAdmin && (
                <>
                    <h2>Sales History</h2>
                    <TableContainer component={Paper}>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell>Product ID</TableCell>
                                    <TableCell>Quantity</TableCell>
                                    <TableCell>Date</TableCell>
                                    <TableCell>Sold By</TableCell>
                                </TableRow>
                            </TableHead>
                            <TableBody>
                                {sales.map((sale) => (
                                    <TableRow key={sale.id}>
                                        <TableCell>{sale.productId}</TableCell>
                                        <TableCell>{sale.quantitySold}</TableCell>
                                        <TableCell>{new Date(sale.saleDate).toLocaleString()}</TableCell>
                                        <TableCell>{sale.userId}</TableCell>
                                    </TableRow>
                                ))}
                            </TableBody>
                        </Table>
                    </TableContainer>
                </>
            )}
        </div>
    );
};

export default SalesPage;
