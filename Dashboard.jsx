// src/components/Dashboard.jsx

import React, { useState, useEffect } from 'react';
import ManageDJsPage from './ManageDJsPage';
import ManageGigsPage from './ManageGigsPage';
import ManageBookingsPage from './ManageBookingsPage';
import ManagePaymentsPage from './ManagePaymentsPage';
import request from '../utils/api';
import './Dashboard.css';

function Dashboard() {
  const [activeSection, setActiveSection] = useState('overview');
  const [stats, setStats] = useState({
    totalBookings: 0,
    availableDJs: 0,
    totalRevenue: 0,
    loading: true
  });

  // Fetch statistics when component mounts or when activeSection is 'overview'
  useEffect(() => {
    if (activeSection === 'overview') {
      fetchStatistics();
    }
  }, [activeSection]);

  const fetchStatistics = async () => {
    try {
      // Fetch all bookings
      const bookings = await request('/api/booking/all');
      const totalBookings = Array.isArray(bookings) ? bookings.length : 0;

      // Fetch all DJs and count available ones
      const djs = await request('/api/dj/all');
      const availableDJs = Array.isArray(djs) 
        ? djs.filter(dj => dj.availabilityStatus === 'AVAILABLE').length 
        : 0;

      // Fetch all payments and calculate total revenue
      const payments = await request('/api/payment/all');
      const totalRevenue = Array.isArray(payments)
        ? payments.reduce((sum, payment) => sum + (payment.amount || 0), 0)
        : 0;

      setStats({
        totalBookings,
        availableDJs,
        totalRevenue,
        loading: false
      });
    } catch (error) {
      console.error('Error fetching statistics:', error);
      setStats(prev => ({ ...prev, loading: false }));
    }
  };

  // Get logged-in user's name
  const getWelcomeMessage = () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      if (user && user.fullName) {
        return `Welcome ${user.fullName}`;
      }
      if (user && user.firstName && user.lastName) {
        return `Welcome ${user.firstName} ${user.lastName}`;
      }
      if (user && user.firstName) {
        return `Welcome ${user.firstName}`;
      }
      return 'Welcome Admin';
    } catch (error) {
      return 'Welcome Admin';
    }
  };

  const menuItems = [
    { id: 'overview', label: 'Overview', icon: '📊' },
    { id: 'bookings', label: 'Manage Bookings', icon: '📅' },
    { id: 'gigs', label: 'Manage Gigs', icon: '🎵' },
    { id: 'djs', label: 'Manage DJs', icon: '🎧' },
    { id: 'payments', label: 'Payments', icon: '💰' },
    // { id: 'reviews', label: 'Reviews', icon: '⭐' }, // Hidden - feature not implemented yet
  ];

  const handleMenuClick = (sectionId) => {
    setActiveSection(sectionId);
  };

  const renderContent = () => {
    switch (activeSection) {
      case 'overview':
        return (
          <div className="overview-content">
            <h2>Dashboard Overview</h2>
            <p>Welcome to your DJ booking management dashboard. Select a section from the sidebar to get started.</p>
            {stats.loading ? (
              <p>Loading statistics...</p>
            ) : (
              <div className="stats-grid">
                <div className="stat-card">
                  <h3>📅</h3>
                  <p>Total Bookings</p>
                  <span>{stats.totalBookings}</span>
                </div>
                <div className="stat-card">
                  <h3>🎧</h3>
                  <p>Available DJs</p>
                  <span>{stats.availableDJs}</span>
                </div>
                <div className="stat-card">
                  <h3>💰</h3>
                  <p>Total Revenue</p>
                  <span>R {stats.totalRevenue.toFixed(2)}</span>
                </div>
              </div>
            )}
          </div>
        );
      case 'djs':
        return <ManageDJsPage />;
      case 'gigs':
        return <ManageGigsPage />;
      case 'bookings':
        return <ManageBookingsPage />;
      case 'payments':
        return <ManagePaymentsPage />;
      case 'reviews':
        return (
          <div className="section-content">
            <h2>Reviews</h2>
            <p>Review management functionality coming soon...</p>
          </div>
        );
      default:
        return (
          <div className="section-content">
            <h2>Page Not Found</h2>
            <p>The requested section could not be found.</p>
          </div>
        );
    }
  };

  return (
    <div className="dashboard-layout">
      {/* Sidebar */}
      <div className="dashboard-sidebar">
        <div className="sidebar-header">
          <h2>{getWelcomeMessage()}</h2>
        </div>
        
        <nav className="sidebar-nav">
          {menuItems.map(item => (
            <button
              key={item.id}
              className={`nav-item ${activeSection === item.id ? 'active' : ''}`}
              onClick={() => handleMenuClick(item.id)}
            >
              <span className="nav-icon">{item.icon}</span>
              <span className="nav-label">{item.label}</span>
            </button>
          ))}
        </nav>
      </div>

      {/* Main Content */}
      <div className="dashboard-main">
        <div className="dashboard-content">
          {renderContent()}
        </div>
      </div>
    </div>
  );
}

export default Dashboard;