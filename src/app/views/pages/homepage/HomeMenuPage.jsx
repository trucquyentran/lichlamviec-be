import React, { useState } from 'react'
import Footer from 'app/components/Footer';
import Sidebar from 'app/components/Sidebar';
import { Route, Routes } from 'react-router-dom';
import DonViPage from '../admin/donvi/DonViPage';
import ProtectedRoute from 'app/common/ProtectedRoute';
import DashboardPage from '../dashboardpage/DashboardPage';
import NavbarMenu from './NavbarMenu';
import LichPage from '../admin/lich/LichPage';
import NguoiDungPage from '../admin/nguoidung/NguoiDungPage';
import TodoPage from '../admin/todo/TodoPage';
import NotificationComponent from 'app/components/NotificationComponent';


const HomeMenuPage = ({ user }) => {
 
    return (
        <div className="">
            <div className='banner-top'>
            </div>

            <div className=''>
                <div className=' main-s'>
                    <Sidebar  user={user}  />
                    <div className='body-main'>
                        <NavbarMenu />
                        
                        <NotificationComponent />
                        <Routes>
                            <Route path="/dashboard" element={<ProtectedRoute user={user} role="Admin"> <DashboardPage /> </ProtectedRoute>} />
                            <Route path="/donvi" element={<ProtectedRoute user={user} role="Admin"> <DonViPage /> </ProtectedRoute>} />
                            <Route path="/" element={<ProtectedRoute user={user} > <LichPage  role="User" user={user} /> </ProtectedRoute>} />
                            <Route path="/nguoidung" element={<ProtectedRoute user={user} role="Admin"> <NguoiDungPage/> </ProtectedRoute>} />
                            <Route path="/todo" element={<ProtectedRoute user={user} role="User"> <TodoPage/> </ProtectedRoute>} />
                        </Routes>
                        {/* <Footer></Footer> */}
                    </div>
                </div>
            </div>
        </div>

    );
};


export default HomeMenuPage;
