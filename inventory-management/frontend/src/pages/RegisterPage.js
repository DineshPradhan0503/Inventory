import React, { useState, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { registerUser, reset } from '../redux/authSlice';
import { useNavigate } from 'react-router-dom';

const RegisterPage = () => {
    const [formData, setFormData] = useState({
        username: '',
        email: '',
        password: '',
    });

    const { username, email, password } = formData;

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { user, isLoading, isError, isSuccess, message } = useSelector((state) => state.auth);

    useEffect(() => {
        if (isError) {
            alert(message);
            dispatch(reset());
        }

        if (isSuccess || user) {
            navigate('/login');
        }

        dispatch(reset());
    }, [user, isError, isSuccess, message, navigate, dispatch]);

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
            email,
            password,
        };
        dispatch(registerUser(userData));
    };

    if (isLoading) {
        return <h1>Loading...</h1>;
    }

    return (
        <div>
            <h1>Register</h1>
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
                    type="email"
                    name="email"
                    value={email}
                    onChange={onChange}
                    placeholder="Email"
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
                <button type="submit">Register</button>
            </form>
        </div>
    );
};

export default RegisterPage;
