import React from 'react';
import { Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logoutUser } from '../redux/authSlice';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';

const Navbar = () => {
    const dispatch = useDispatch();
    const { user, isAuthenticated } = useSelector((state) => state.auth);

    const onLogout = () => {
        dispatch(logoutUser());
    };

    const isAdmin = user && user.roles.includes('ROLE_ADMIN');

    return (
        <AppBar position="static">
            <Toolbar>
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    Inventory Management
                </Typography>
                {isAuthenticated ? (
                    <>
                        <Button color="inherit" component={Link} to="/products">Products</Button>
                        <Button color="inherit" component={Link} to="/sales">Sales</Button>
                        {isAdmin && <Button color="inherit" component={Link} to="/reports">Reports</Button>}
                        <Button color="inherit" onClick={onLogout}>Logout</Button>
                    </>
                ) : (
                    <>
                        <Button color="inherit" component={Link} to="/login">Login</Button>
                        <Button color="inherit" component={Link} to="/register">Register</Button>
                    </>
                )}
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
