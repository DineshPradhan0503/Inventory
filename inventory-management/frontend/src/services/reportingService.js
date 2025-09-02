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

const download = async (url, filename, responseType = 'blob') => {
    const token = getAuthToken();
    const response = await axios.get(API_URL + url, {
        responseType,
        headers: { Authorization: `Bearer ${token}` },
    });
    const link = document.createElement('a');
    link.href = window.URL.createObjectURL(new Blob([response.data]));
    link.download = filename;
    document.body.appendChild(link);
    link.click();
    link.remove();
};

const exportStockExcel = () => download('stock/export', 'stock_report.xlsx');
const exportStockPdf = () => download('stock/export/pdf', 'stock_report.pdf');
const exportSalesPdf = () => download('sales/export', 'sales_report.pdf');
const exportSalesExcel = () => download('sales/export/xlsx', 'sales_report.xlsx');

const reportingService = {
    getStockReport,
    getSalesReport,
    exportStockExcel,
    exportStockPdf,
    exportSalesPdf,
    exportSalesExcel,
};

export default reportingService;
