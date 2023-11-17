import {useEffect, useState} from 'react';
import {ENDPOINTS} from '../services/ENDPOINTS';
import {Link, useNavigate} from "react-router-dom";
import Cookies from "universal-cookie";

interface UserData {
    firstName: string;
    lastName: string;
    email: string;
    role: string;
}

const cookies = new Cookies();

export const Profile: React.FC = () => {
    const [userData, setUserData] = useState<UserData | null>(null);
    const navigate = useNavigate();
    const token = cookies.get("token");
    const getUser = async () => {
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
            } else {
                console.error('Error fetching user data:', response.statusText);
            }
        } catch (error) {
            console.error('Server error happened:', error);
        }
    };


    const goToChatLogs = () => {
        navigate('/chat-logs');
    };

    useEffect(() => {
        const fetchData = async () => {
            const data: any = await getUser();
            if (data) {
                setUserData(data);
            }
        };

        fetchData();
    }, []);

    if (!userData) {
        return <div>Loading...</div>;
    }

    return (
        <div>
            <h1>Profile Page</h1>
            <p>
                Welcome, {userData.firstName} {userData.lastName}!
            </p>
            <p>Email: {userData.email}</p>
            <p>Role: {userData.role}</p>
            <button onClick={goToChatLogs}>Go to Chat Logs</button>

            <Link to="/">
                <div className="userbar__logout" onClick={() => {
                    cookies.remove("token");
                }}>
                    <p>Logout</p>
                </div>
            </Link>
        </div>
    );
};