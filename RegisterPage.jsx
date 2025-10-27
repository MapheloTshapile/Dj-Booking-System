import React, { useState } from 'react';
import { useNavigate, Link, useLocation } from 'react-router-dom';
import './Auth.css';
import request from '../utils/api';

function RegisterPage() {
    const location = useLocation();
    const returnTo = location.state?.returnTo;

    const [formData, setFormData] = useState({
        fullName: '',
        email: '',
        password: '',
        phone: ''
    });
    const [message, setMessage] = useState('');
    const [messageType, setMessageType] = useState('error'); // 'success' or 'error'
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Register via new auth endpoint with JWT
            const payload = { 
                fullName: formData.fullName,
                email: formData.email,
                password: formData.password,
                phone: formData.phone,
                role: 'EVENT_ORGANIZER' 
            };
            
            console.log('Sending registration request to /api/auth/register...');
            const response = await request('/api/auth/register', { 
                method: 'POST', 
                body: JSON.stringify(payload) 
            });
            console.log('Registration successful! Response received:', response);
            
            // Backend returns: { message, user, token }
            const { user, token } = response;
            
            // Store user data with token (auto-login after registration)
            const userDataWithToken = { ...user, token };
            localStorage.setItem('user', JSON.stringify(userDataWithToken));
            
            setMessageType('success');
            setMessage('✅ Registration successful! Redirecting...');
            
            // Redirect to landing page or returnTo location
            setTimeout(() => {
                if (returnTo) {
                    navigate(returnTo);
                } else {
                    navigate('/');
                }
            }, 1500);
        } catch (error) {
            setMessageType('error');
            setMessage('❌ Registration failed. Please try again.');
            console.error('Registration error:', error);
        }
    };

    return (
        <div className="auth-container">
            <h2>Event Organizer Registration</h2>
            <form onSubmit={handleSubmit} className="auth-form">
                <input type="text" name="fullName" placeholder="Full name" value={formData.fullName} onChange={handleChange} required />
                <input type="email" name="email" placeholder="Email" value={formData.email} onChange={handleChange} required />
                <input type="password" name="password" placeholder="Password" value={formData.password} onChange={handleChange} required />
                <input type="tel" name="phone" placeholder="Phone" value={formData.phone} onChange={handleChange} />
                <button type="submit">Register</button>
            </form>
            {message && <p className={`auth-message ${messageType}`}>{message}</p>}
            <p style={{ marginTop: '1rem' }}>
                Already have an account? <Link to="/login" state={{ returnTo }}>Login</Link>
            </p>
        </div>
    );
}

export default RegisterPage;