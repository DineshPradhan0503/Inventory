import axios from 'axios';

const API_URL = '/api/sales/';

const getAuthToken = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    return user ? user.token : null;
};

const getSales = () => {
    const token = getAuthToken();
    return axios.get(API_URL, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const getSalesFiltered = (params) => {
    const token = getAuthToken();
    return axios.get(API_URL + 'filter', {
        params,
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const createSale = (saleData) => {
    const token = getAuthToken();
    return axios.post(API_URL, saleData, {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const saleService = {
    getSales,
    getSalesFiltered,
    createSale,
};

export default saleService;
