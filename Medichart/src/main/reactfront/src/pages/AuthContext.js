import React, { createContext, useState, useContext, useEffect } from 'react';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [userEmail, setUserEmail] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/auth/session', { credentials: 'include' })
            .then(response => response.text())
            .then(data => {
                if (data.startsWith("Logged in as: ")) {
                    setIsLoggedIn(true);
                    setUserEmail(data.replace("Logged in as: ", ""));
                } else {
                    setIsLoggedIn(false);
                    setUserEmail(null);
                }
            })
            .catch(() => {
                setIsLoggedIn(false);
                setUserEmail(null);
            });
    }, []);

    const login = (userEmail) => {
        setIsLoggedIn(true);
        setUserEmail(userEmail);
    };

    const logout = () => {
        fetch('http://localhost:8080/auth/logout', {
            method: 'POST',
            credentials: 'include',
        })
            .then(() => {
                setIsLoggedIn(false);
                setUserEmail(null);

                document.cookie = "memberId=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
                document.cookie = "JSESSIONID=; expires=Thu, 01 Jan 1970 00:00:00 GMT; path=/";
            })
            .catch(err => console.error('Failed to log out:', err));
    };

    return (
        <AuthContext.Provider value={{ isLoggedIn, userEmail, login, logout }}>
            {children}
        </AuthContext.Provider>
    );
};

export const useAuth = () => useContext(AuthContext);
