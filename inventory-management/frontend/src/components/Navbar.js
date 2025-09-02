import React, { useState } from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { logoutUser } from '../redux/authSlice';
import {
    AppBar,
    Toolbar,
    Typography,
    Button,
    Box,
    IconButton,
    Menu,
    MenuItem,
    useTheme,
    useMediaQuery
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import InventoryIcon from '@mui/icons-material/Inventory';

const Navbar = () => {
    const dispatch = useDispatch();
    const navigate = useNavigate();
    const { user, isAuthenticated } = useSelector((state) => state.auth);
    const [anchorEl, setAnchorEl] = useState(null);
    const theme = useTheme();
    const isMobile = useMediaQuery(theme.breakpoints.down('sm'));

    const handleMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleClose = () => {
        setAnchorEl(null);
    };

    const onLogout = () => {
        dispatch(logoutUser());
        navigate('/login');
    };

    const isAdmin = user && user.roles && user.roles.includes('ROLE_ADMIN');

    const authLinks = (
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <Button color="inherit" component={RouterLink} to="/products">Products</Button>
            <Button color="inherit" component={RouterLink} to="/sales">Sales</Button>
            {isAdmin && <Button color="inherit" component={RouterLink} to="/reports">Reports</Button>}
            <Typography sx={{ mx: 2 }}>Welcome, {user ? user.username : ''}</Typography>
            <Button color="inherit" onClick={onLogout}>Logout</Button>
        </Box>
    );

    const guestLinks = (
        <Box>
            <Button color="inherit" component={RouterLink} to="/login">Login</Button>
            <Button color="inherit" component={RouterLink} to="/register">Register</Button>
        </Box>
    );

    const mobileMenu = (
        <Box>
            <IconButton
                size="large"
                edge="start"
                color="inherit"
                aria-label="menu"
                onClick={handleMenu}
            >
                <MenuIcon />
            </IconButton>
            <Menu
                id="menu-appbar"
                anchorEl={anchorEl}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                keepMounted
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
                open={Boolean(anchorEl)}
                onClose={handleClose}
            >
                {isAuthenticated ? (
                    <div>
                        <MenuItem component={RouterLink} to="/products" onClick={handleClose}>Products</MenuItem>
                        <MenuItem component={RouterLink} to="/sales" onClick={handleClose}>Sales</MenuItem>
                        {isAdmin && <MenuItem component={RouterLink} to="/reports" onClick={handleClose}>Reports</MenuItem>}
                        <MenuItem onClick={() => { onLogout(); handleClose(); }}>Logout</MenuItem>
                    </div>
                ) : (
                    <div>
                        <MenuItem component={RouterLink} to="/login" onClick={handleClose}>Login</MenuItem>
                        <MenuItem component={RouterLink} to="/register" onClick={handleClose}>Register</MenuItem>
                    </div>
                )}
            </Menu>
        </Box>
    );

    return (
        <AppBar position="static">
            <Toolbar>
                <InventoryIcon sx={{ mr: 1 }} />
                <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                    InventoryPro
                </Typography>
                {isMobile ? mobileMenu : (isAuthenticated ? authLinks : guestLinks)}
            </Toolbar>
        </AppBar>
    );
};

export default Navbar;
