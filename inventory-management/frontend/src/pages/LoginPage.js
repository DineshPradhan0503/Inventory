import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { loginUser, reset } from '../redux/authSlice';
import { useNavigate } from 'react-router-dom';

const LoginPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });

    const { username, password } = formData;

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { user, isLoading, isError, isAuthenticated, message } = useSelector((state) => state.auth);

    useEffect(() => {
        if (isError) {
            alert(message);
            dispatch(reset());
        }

        if (isAuthenticated) {
            navigate('/dashboard');
        }
    }, [user, isError, isAuthenticated, message, navigate, dispatch]);

    const onChange = (e) => {
        setFormData((prevState) => ({
            ...prevState,
            [e.target.name]: e.target.value,
        }));
    };

    const onSubmit = (e) => {
        e.preventDefault();
        const userData = {
            username,
            password,
        };
        dispatch(loginUser(userData));
    };

    if (isLoading) {
        return <h1>Loading...</h1>;
    }

    return (
        <div>
            <h1>Login</h1>
            <form onSubmit={onSubmit}>
                <input
                    type="text"
                    name="username"
                    value={username}
                    onChange={onChange}
                    placeholder="Username"
                    required
                />
                <input
                    type="password"
                    name="password"
                    value={password}
                    onChange={onChange}
                    placeholder="Password"
                    required
                />
                <button type="submit">Login</button>
            </form>
        </div>
    );
};

export default LoginPage;
