import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { createProduct, updateProduct } from '../redux/productSlice';
import { Modal, Box, TextField, Button } from '@mui/material';

const style = {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 400,
    bgcolor: 'background.paper',
    border: '2px solid #000',
    boxShadow: 24,
    p: 4,
};

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
    }, [product]);

    const onChange = (e) => {
        setFormData((prevState) => ({
            ...prevState,
            [e.target.name]: e.target.value,
        }));
    };

    const onSubmit = (e) => {
        e.preventDefault();
        if (product) {
            dispatch(updateProduct({ id: product.id, productData: formData }));
        } else {
            dispatch(createProduct(formData));
        }
        handleClose();
    };

    return (
        <Modal open={open} onClose={handleClose}>
            <Box sx={style}>
                <h2>{product ? 'Edit Product' : 'Add Product'}</h2>
                <form onSubmit={onSubmit}>
                    <TextField name="name" label="Name" value={formData.name} onChange={onChange} fullWidth required />
                    <TextField name="category" label="Category" value={formData.category} onChange={onChange} fullWidth required />
                    <TextField name="description" label="Description" value={formData.description} onChange={onChange} fullWidth />
                    <TextField name="price" label="Price" type="number" value={formData.price} onChange={onChange} fullWidth required />
                    <TextField name="stockQuantity" label="Stock Quantity" type="number" value={formData.stockQuantity} onChange={onChange} fullWidth required />
                    <TextField name="threshold" label="Threshold" type="number" value={formData.threshold} onChange={onChange} fullWidth required />
                    <Button type="submit" variant="contained" sx={{ mt: 2 }}>
                        {product ? 'Update' : 'Create'}
                    </Button>
                </form>
            </Box>
        </Modal>
    );
};

export default ProductForm;
