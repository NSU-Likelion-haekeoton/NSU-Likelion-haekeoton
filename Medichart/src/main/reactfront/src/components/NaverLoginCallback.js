import React, { useContext, useEffect } from 'react';
import { AuthContext } from '../pages/AuthContext';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const NaverLoginCallback = () => {
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();

    useEffect(() => {
        const queryString = window.location.search;
        const urlParams = new URLSearchParams(queryString);
        const code = urlParams.get('code');
        const state = urlParams.get('state');

        if (code && state) {
            axios.get(`http://localhost:3000/login/oauth2/code/naver?code=${code}&state=${state}`)
                .then(response => {
                    const { token, username } = response.data;
                    localStorage.setItem('authToken', token);
                    login(username); // AuthContext에 로그인 상태 업데이트
                    navigate('/');
                })
                .catch(err => {
                    console.error('네이버 로그인 실패:', err);
                });
        }
    }, [login, navigate]);

    return <div>로그인 중...</div>;
};

export default NaverLoginCallback;
