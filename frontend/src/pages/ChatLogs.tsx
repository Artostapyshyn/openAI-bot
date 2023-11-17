import React, {useState, useEffect} from 'react';
import {ENDPOINTS} from "../services/ENDPOINTS";
import Cookies from "universal-cookie";
import {Link} from "react-router-dom";
import "../styles/_chat-logs.scss";

interface ChatLog {
    id: number;
    chatId: string;
    message: string;
    timestamp: string;
}

const cookies = new Cookies();
export const ChatLogs: React.FC = () => {
    const [chatLogs, setChatLogs] = useState<ChatLog[]>([]);
    const [selectedChatId, setSelectedChatId] = useState(null);
    const [newMessage, setNewMessage] = useState('');
    const [showModal, setShowModal] = useState(false);
    const token = cookies.get("token");

    useEffect(() => {
        fetchChatLogs();
    }, []);

    const fetchChatLogs = async () => {
        try {
            const resp = await fetch(ENDPOINTS.allMessages, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                method: 'GET'
            });
            const data = await resp.json();

            if (resp.ok) {
                setChatLogs(data);
            } else {
                console.error('Error fetching chat logs:', data.message);
            }
        } catch (error) {
            console.error('Server error happened!', error);
        }
    };

    const handleOpenModal = (chatId: any) => {
        setSelectedChatId(chatId);
        setShowModal(true);
    };

    const handleCloseModal = () => {
        setSelectedChatId(null);
        setShowModal(false);
    };

    const handleSendMessage = async () => {
        try {
            const resp = await fetch(`${ENDPOINTS.sendMessage}?chatId=${selectedChatId}&message=${newMessage}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
            });

            const data = await resp.json();

            if (resp.ok) {
                fetchChatLogs();
                handleCloseModal();
            } else {
                console.error('Error sending message:', data.message);
            }
        } catch (error) {
            console.error('Server error happened!', error);
        }
    };

    function formatDateString(dateString: string): string {
        const date = new Date(dateString);

        const day = date.getDate().toString().padStart(2, '0');
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');

        return `${day}-${month} ${hours}:${minutes}`;
    }

    return (
        <div className="chat-logs-container">
            <h1>Chat Logs</h1>
            <table>
                <thead>
                <tr>
                    <th>Chat ID</th>
                    <th>Message</th>
                    <th>Date</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                {chatLogs.map(chat => (
                    <tr key={chat.id}>
                        <td>{chat.chatId}</td>
                        <td>{chat.message}</td>
                        <td>{formatDateString(chat.timestamp)}</td>
                        <td>
                            <button onClick={() => handleOpenModal(chat.chatId)}>Send Message</button>
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
            <Link to="/profile" className="profile-link">Go to Profile</Link>
            {showModal && (
                <>
                    <div className="modal-overlay" onClick={handleCloseModal}></div>
                    <div className="modal">
                        <h2>Send Message to chat ID {selectedChatId}</h2>
                        <textarea
                            value={newMessage}
                            onChange={(e) => setNewMessage(e.target.value)}
                            placeholder="Type your message here..."
                        />
                        <div className="modal-buttons">
                            <button onClick={handleCloseModal}>Close</button>
                            <button onClick={handleSendMessage}>Send</button>
                        </div>
                    </div>
                </>
            )}
        </div>
    );
};