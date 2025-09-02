import axios from 'axios';

const API_URL = '/api/products/';

const getAuthToken = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    return user ? user.token : null;
};

const getProducts = () => {
    const token = getAuthToken();
    return axios.get(API_URL, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const searchProducts = (params) => {
    const token = getAuthToken();
    return axios.get('/api/products/search', {
        params,
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const createProduct = (productData) => {
    const token = getAuthToken();
    return axios.post(API_URL, productData, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const updateProduct = (id, productData) => {
    const token = getAuthToken();
    return axios.put(API_URL + id, productData, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const deleteProduct = (id) => {
    const token = getAuthToken();
    return axios.delete(API_URL + id, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const getLowStock = () => {
    const token = getAuthToken();
    return axios.get(API_URL + 'low-stock', {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const increaseStock = (id, amount) => {
    const token = getAuthToken();
    return axios.post(`${API_URL}${id}/stock/increase`, null, {
        params: { amount },
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const decreaseStock = (id, amount) => {
    const token = getAuthToken();
    return axios.post(`${API_URL}${id}/stock/decrease`, null, {
        params: { amount },
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const productService = {
    getProducts,
    searchProducts,
    createProduct,
    updateProduct,
    deleteProduct,
    getLowStock,
    increaseStock,
    decreaseStock,
};

export default productService;
