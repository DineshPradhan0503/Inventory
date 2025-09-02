import axios from 'axios';

const API_URL = '/api/auth/';

const register = (userData) => {
    return axios.post(API_URL + 'signup', userData);
};

const login = async (userData) => {
    const response = await axios.post(API_URL + 'signin', userData);
    if (response.data.token) {
        localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
};

const logout = () => {
    localStorage.removeItem('user');
};

const authService = {
    register,
    login,
    logout,
};

export default authService;
