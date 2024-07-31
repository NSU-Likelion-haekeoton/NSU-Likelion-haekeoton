import React from 'react';
import { Routes, Route } from "react-router-dom";
import AdminLayout from "./components/AdminLayout";
import Layout from "./components/layout";
import Home from "./pages/home";
import AdminMain from "./pages/AdminMain";
import AdminMonth from "./pages/AdminMonth";
import AdminYear from "./pages/AdminYear";
import AdminNoticeList from "./pages/AdminNoticeList";
import AdminNoticeNew from "./pages/AdminNoticeNew";
import { AuthProvider } from "./pages/AuthContext";
import ProtectedRoute from "./pages/ProtectedRoute";
import Korean from "./pages/korean";
import Japanese from "./pages/Japanese";
import Chinese from "./pages/Chinese";
import Login from "./pages/login";
import Signup from "./pages/signup";
import Email from "./pages/email";
import EmailVerification from "./pages/EmailVerification";
import Mymedicheck from "./pages/mymedicheck";
import SearchHospital from "./pages/SearchHospital";
import Mypage from "./pages/Mypage";
import CustomerService from "./pages/CustomerService";
import Prediction from "./pages/prediction";
import NotFound from "./pages/NotFound";

function App() {
    return (
        <AuthProvider>
            <Routes>
                <Route path="/" element={<Layout><Home /></Layout>} />
                <Route path="/admin/main" element={<AdminLayout><AdminMain /></AdminLayout>} />
                <Route path="/admin/month" element={<AdminLayout><AdminMonth /></AdminLayout>} />
                <Route path="/admin/year" element={<AdminLayout><AdminYear /></AdminLayout>} />
                <Route path="/admin/noticeList" element={<AdminLayout><AdminNoticeList /></AdminLayout>} />
                <Route path="/admin/notice/new" element={<AdminLayout><AdminNoticeNew /></AdminLayout>} />
                <Route path="/admin/notice/edit/:id" element={<AdminLayout><AdminNoticeNew /></AdminLayout>} />
                <Route path="/Korean" element={<Layout><Korean /></Layout>} />
                <Route path="/Japanese" element={<Layout><Japanese /></Layout>} />
                <Route path="/Chinese" element={<Layout><Chinese /></Layout>} />
                <Route path="/login" element={<Layout><Login /></Layout>} />
                <Route path="/login/signup" element={<Layout><Signup /></Layout>} />
                <Route path="/signup/email" element={<Layout><Email /></Layout>} />
                <Route path="/signup/email-verification" element={<Layout><EmailVerification /></Layout>} />
                <Route path="/medicalInform" element={<ProtectedRoute><Layout><Mymedicheck /></Layout></ProtectedRoute>} />
                <Route path="/searchHospital" element={<Layout><SearchHospital /></Layout>} />
                <Route path="/Mypage" element={<ProtectedRoute><Layout><Mypage /></Layout></ProtectedRoute>} />
                <Route path="/CustomerService" element={<Layout><CustomerService /></Layout>} />
                <Route path="/Prediction" element={<Layout><Prediction /></Layout>} />
                <Route path="/*" element={<NotFound />} />
            </Routes>
        </AuthProvider>
    );
}

export default App;
