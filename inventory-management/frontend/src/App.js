import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProductPage from './pages/ProductPage';
import SalesPage from './pages/SalesPage';
import ReportingPage from './pages/ReportingPage';
import Navbar from './components/Navbar';
import PrivateRoute from './components/PrivateRoute';

function App() {
  return (
    <Router>
      <div className="App">
        <Navbar />
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route element={<PrivateRoute />}>
            <Route path="/products" element={<ProductPage />} />
            <Route path="/sales" element={<SalesPage />} />
            <Route path="/reports" element={<ReportingPage />} />
          </Route>
        </Routes>
      </div>
    </Router>
  );
}

export default App;
