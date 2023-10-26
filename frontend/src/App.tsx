import {Route, Routes, useNavigate} from "react-router-dom";
import { useSelector, useDispatch } from 'react-redux';
import { useEffect } from 'react';
import { RootState } from './store';
import React from "react";
import NotFoundPage from "./pages/NotFoundPage";
import MainPage from "./pages/MainPage";
import LoginPage from "./pages/LoginPage";
import HeaderBar from "./components/HeaderBar";
import ListOrderPage from "./pages/ListOrderPage";
import ActiveOrdersPage from "./pages/ActiveOrdersPage";
import AllOrdersPage from "./pages/AllOrdersPage";
import UserInfoPage from "./pages/UserInfoPage";
import { setAuth } from "./slices/authSlice";
const App: React.FC = () => {
    const user = useSelector((state: RootState) => state.auth.user);
    const dispatch = useDispatch();

    useEffect(() => {
        if(user){
            dispatch(setAuth(true));
        }
      });

    return (
        <div>
            <HeaderBar/>
            <Routes>
                <Route path="/" element={<MainPage/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/orders" element={<ListOrderPage/>}/>
                <Route path="/active-orders" element={<ActiveOrdersPage/>}/>
                <Route path="/all-orders" element={<AllOrdersPage/>}/>
                <Route path="/info" element={<UserInfoPage/>}/>
                <Route path="*" element={<NotFoundPage/>}/>
            </Routes>
        </div>
    );
}

export default App;
