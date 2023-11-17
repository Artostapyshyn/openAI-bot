import React, {useState, useEffect} from 'react';
import {ENDPOINTS} from "../services/ENDPOINTS";
import {Modal} from "../components/Modal";
import Cookies from "universal-cookie";
import {Link} from "react-router-dom";

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

    return (
        <div>
            <h1>Chat Logs Page</h1>
            <ul>
                {chatLogs.map(chat => (
                    <li key={chat.id}>
                        <div>Chat ID: {chat.chatId}</div>
                        <div>Message: {chat.message}</div>
                        <div>Timestamp: {chat.timestamp}</div>
                        <button onClick={() => handleOpenModal(chat.chatId)}>Send Message</button>
                    </li>
                ))}
            </ul>
            <Link to="/profile">Go to Profile</Link>
            {showModal && (
                <Modal onClose={handleCloseModal}>
                    <h2>Send Message</h2>
                    <textarea
                        value={newMessage}
                        onChange={(e) => setNewMessage(e.target.value)}
                        placeholder="Type your message here..."
                    />
                    <button onClick={handleSendMessage}>Send</button>
                </Modal>
            )}
        </div>
    );
};