import {BrowserRouter, Navigate, Route, Routes} from "react-router-dom";
// import {Profile} from "../pages/Profile";
import React from "react";
import {SignUp} from "../pages/SignUp";
import {Login} from "../pages/Login";
import {ChatLogs} from "../pages/ChatLogs";
import {Profile} from "../pages/Profile";
import {ProtectedRouter} from "./ProtectedRouter";

export const Router: React.FC = () => {
    return <BrowserRouter>
        <Routes>
            <Route path="/" element={<Login/>}/>
            <Route path="/sign-up" element={<SignUp/>}/>
            <Route path="/chat-logs" element={<ChatLogs/>}/>
            {/*<Route path="/profile" element={<Profile/>}/>*/}
            <Route path="/profile" element={<ProtectedRouter children={<Profile/>} failed={<Navigate to={"/"}/>}/>}/>
        </Routes>
    </BrowserRouter>
}