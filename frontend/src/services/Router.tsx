import {BrowserRouter, Route, Routes} from "react-router-dom";
import React from "react";
import {SignUp} from "../pages/SignUp";
import {Login} from "../pages/Login";
import {ChatLogs} from "../pages/ChatLogs";
import {Profile} from "../pages/Profile";

export const Router: React.FC = () => {
    return <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login/>}/>
            <Route path="/sign-up" element={<SignUp/>}/>
            <Route path="/chat-logs" element={<ChatLogs/>}/>
            <Route path="/profile" element={<Profile/>}/>
        </Routes>
    </BrowserRouter>
}