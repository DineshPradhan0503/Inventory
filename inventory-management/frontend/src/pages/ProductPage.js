import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { getProducts, deleteProduct } from '../redux/productSlice';
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Button, IconButton } from '@mui/material';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ProductForm from '../components/ProductForm';

const ProductPage = () => {
    const dispatch = useDispatch();
    const { products, isLoading, error } = useSelector((state) => state.products);
    const { user } = useSelector((state) => state.auth);

    const [open, setOpen] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState(null);

    useEffect(() => {
        dispatch(getProducts());
    }, [dispatch]);

    const handleOpen = (product = null) => {
        setSelectedProduct(product);
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        setSelectedProduct(null);
    };

    const handleDelete = (id) => {
        if (window.confirm('Are you sure you want to delete this product?')) {
            dispatch(deleteProduct(id));
        }
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
            <h1>Products</h1>
            {isAdmin && <Button variant="contained" onClick={() => handleOpen()}>Add Product</Button>}
            <ProductForm open={open} handleClose={handleClose} product={selectedProduct} />
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Name</TableCell>
                            <TableCell>Category</TableCell>
                            <TableCell>Price</TableCell>
                            <TableCell>Stock</TableCell>
                            <TableCell>Threshold</TableCell>
                            {isAdmin && <TableCell>Actions</TableCell>}
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {products.map((product) => (
                            <TableRow key={product.id}>
                                <TableCell>{product.name}</TableCell>
                                <TableCell>{product.category}</TableCell>
                                <TableCell>{product.price}</TableCell>
                                <TableCell>{product.stockQuantity}</TableCell>
                                <TableCell>{product.threshold}</TableCell>
                                {isAdmin && (
                                    <TableCell>
                                        <IconButton onClick={() => handleOpen(product)}>
                                            <EditIcon />
                                        </IconButton>
                                        <IconButton onClick={() => handleDelete(product.id)}>
                                            <DeleteIcon />
                                        </IconButton>
                                    </TableCell>
                                )}
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>
        </div>
    );
};

export default ProductPage;
