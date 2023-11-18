import {useEffect, useState} from 'react';
import {ENDPOINTS} from '../services/ENDPOINTS';
import {Link, useNavigate} from "react-router-dom";
import Cookies from "universal-cookie";
import "../styles/_profile.scss";
import {useDispatch} from "react-redux";
interface UserData {
    firstName: string;
    lastName: string;
    email: string;
    role: string;
}

const cookies = new Cookies();

export const Profile: React.FC = () => {
    const dispatch = useDispatch();
    const [userData, setUserData] = useState<UserData | null>(null);
    const navigate = useNavigate();
    const token = cookies.get("token");

    useEffect(() => {
        if (!token) {
            navigate("/");
        } else {
            const fetchUserData = async () => {
                try {
                    const response = await fetch(ENDPOINTS.getUser, {
                        mode: "cors" as RequestMode,
                        headers: {
                            "Content-Type": "application/json",
                            "Authorization": `Bearer ${token}`
                        }
                    });

                    if (response.ok) {
                        const data = await response.json();
                        setUserData(data);
                        dispatch({ type: "LOAD_CLIENT", payload: data });
                    } else {
                        console.log('Failed to fetch user data');
                    }
                } catch (error) {
                    console.error('Error:', error);
                    navigate("/");
                }
            };

            fetchUserData();
        }
    }, [navigate, token]);

    if (!userData) {
        return <div>Loading...</div>;
    }

    return (
        <div className="profile-page">
            <h1>Profile</h1>
            <p>Welcome, {userData.firstName} {userData.lastName}!</p>
            <p>Email: {userData.email}</p>
            <button onClick={() => navigate('/chat-logs')}>Chat Logs</button>
            <Link to="/">
                <div className="userbar__logout" onClick={() => cookies.remove("token")}>
                    <p>Logout</p>
                </div>
            </Link>
        </div>
    );
};