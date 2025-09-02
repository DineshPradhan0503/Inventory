import React, { useState, useEffect } from 'react';
import { useDispatch } from 'react-redux';
import { createProduct, updateProduct } from '../redux/productSlice';
import {
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    TextField,
    Button,
    Box
} from '@mui/material';

const ProductForm = ({ open, handleClose, product }) => {
    const [formData, setFormData] = useState({
        name: '',
        category: '',
        description: '',
        price: '',
        stockQuantity: '',
        threshold: '',
    });

    const dispatch = useDispatch();

    useEffect(() => {
        if (product) {
            setFormData(product);
        } else {
            setFormData({
                name: '',
                category: '',
                description: '',
                price: '',
                stockQuantity: '',
                threshold: '',
            });
        }
    }, [product, open]);

    const onChange = (e) => {
        setFormData((prevState) => ({
            ...prevState,
            [e.target.name]: e.target.value,
        }));
    };

    const onSubmit = (e) => {
        e.preventDefault();
        const productData = {
            ...formData,
            price: parseFloat(formData.price),
            stockQuantity: parseInt(formData.stockQuantity, 10),
            threshold: parseInt(formData.threshold, 10),
        };

        if (product) {
            dispatch(updateProduct({ id: product.id, productData }));
        } else {
            dispatch(createProduct(productData));
        }
        handleClose();
    };

    return (
        <Dialog open={open} onClose={handleClose} maxWidth="sm" fullWidth>
            <DialogTitle>{product ? 'Edit Product' : 'Add Product'}</DialogTitle>
            <Box component="form" onSubmit={onSubmit}>
                <DialogContent>
                    <TextField
                        name="name"
                        label="Name"
                        value={formData.name}
                        onChange={onChange}
                        fullWidth
                        required
                        margin="normal"
                    />
                    <TextField
                        name="category"
                        label="Category"
                        value={formData.category}
                        onChange={onChange}
                        fullWidth
                        required
                        margin="normal"
                    />
                    <TextField
                        name="description"
                        label="Description"
                        value={formData.description}
                        onChange={onChange}
                        fullWidth
                        multiline
                        rows={4}
                        margin="normal"
                    />
                    <TextField
                        name="price"
                        label="Price"
                        type="number"
                        value={formData.price}
                        onChange={onChange}
                        fullWidth
                        required
                        margin="normal"
                        inputProps={{ step: "0.01" }}
                    />
                    <TextField
                        name="stockQuantity"
                        label="Stock Quantity"
                        type="number"
                        value={formData.stockQuantity}
                        onChange={onChange}
                        fullWidth
                        required
                        margin="normal"
                    />
                    <TextField
                        name="threshold"
                        label="Threshold"
                        type="number"
                        value={formData.threshold}
                        onChange={onChange}
                        fullWidth
                        required
                        margin="normal"
                    />
                </DialogContent>
                <DialogActions>
                    <Button onClick={handleClose}>Cancel</Button>
                    <Button type="submit" variant="contained">
                        {product ? 'Update' : 'Create'}
                    </Button>
                </DialogActions>
            </Box>
        </Dialog>
    );
};

export default ProductForm;
