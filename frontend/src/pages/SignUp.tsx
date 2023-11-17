import React, { useState } from 'react';
import { ENDPOINTS } from "../services/ENDPOINTS";
import {useNavigate} from "react-router-dom";

export const SignUp = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();
    const handleSignUp = async () => {
        setLoading(true);
        setError('');

        if (password !== confirmPassword) {
            setError("Passwords do not match");
            setLoading(false);
            return;
        }

        try {
            const resp = await fetch(ENDPOINTS.signUp, {
                method: "POST",
                body: JSON.stringify({ firstName, lastName, email, password }),
                ...ENDPOINTS.params
            });

            const data = await resp.json();

            if (resp.ok) {
                navigate("/");
            } else {
                setError(data.message || "Sign Up failed");
            }
        } catch (error) {
            setError("Server error happened!");
        } finally {
            setLoading(false);
        }
    };

    const handleLoginRedirect = () => {
        navigate("/");
    };

    return (
        <div>
            <h1>Sign Up Page</h1>
            <form>
                <label>First Name:</label>
                <input
                    type="text"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                />

                <label>Last Name:</label>
                <input
                    type="text"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                />

                <label>Email:</label>
                <input
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <label>Password:</label>
                <input
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <label>Confirm Password:</label>
                <input
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                />

                <button type="button" onClick={handleSignUp} disabled={loading}>
                    {loading ? 'Signing Up...' : 'Sign Up'}
                </button>

                {error && <p style={{ color: 'red' }}>{error}</p>}

                <p>Already have an account? <button type="button" onClick={handleLoginRedirect}>Login</button></p>
            </form>
        </div>
    );
};

