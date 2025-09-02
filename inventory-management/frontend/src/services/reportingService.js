import axios from 'axios';

const API_URL = '/api/reports/';

const getAuthToken = () => {
    const user = JSON.parse(localStorage.getItem('user'));
    return user ? user.token : null;
};

const getStockReport = () => {
    const token = getAuthToken();
    return axios.get(API_URL + 'stock', {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const getSalesReport = () => {
    const token = getAuthToken();
    return axios.get(API_URL + 'sales', {
        headers: {
            Authorization: `Bearer ${token}`,
        },
    });
};

const reportingService = {
    getStockReport,
    getSalesReport,
};

export default reportingService;
