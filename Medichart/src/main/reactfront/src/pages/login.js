import "../pages/login.css";
import { Link } from "react-router-dom"; // Link 임포트 추가
import kakaologo from "../kakaotalk_sharing_btn_small.png";
import naverlogo from "../naverlogo.png";
import googlelogo from "../btn_google.svg";
import axios from 'axios';
import { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "./AuthContext";

function Login() {
    const { login } = useContext(AuthContext);
    const navigate = useNavigate();
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

    const handleLogin = async (event) => {
        event.preventDefault();

        try {
            const response = await axios.post('http://localhost:8080/api/users/login', {
                email: email,
                password: password
            });

            if (response.data.status === 'success') {
                login(response.data.data); // 사용자 정보를 AuthContext에 저장
                navigate('/'); // 로그인 성공 후 홈 페이지로 이동
            } else {
                setErrorMessage(response.data.message || '로그인 실패');
            }
        } catch (error) {
            console.error('로그인 오류:', error);
            setErrorMessage('로그인 실패');
        }
    };

    const K_REST_API_KEY = "fce473f392ddd4530306ee0c3531eba0";
    const K_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/kakao";
    const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${K_REST_API_KEY}&redirect_uri=${encodeURIComponent(K_REDIRECT_URI)}&response_type=code&scope=profile_nickname,account_email`;

    const handlekakaoLogin = () => {
        window.location.href = kakaoURL;
    };

    const N_CLIENT_ID =  "J9e7ZObddUCfs_4uICkI"; // 환경 변수에서 가져옴
    const N_REDIRECT_URI = "http://localhost:8080/login/oauth2/code/naver"; // 환경 변수에서 가져옴
    const stateString = generateRandomState(); // 랜덤 상태 생성 함수 사용
    const NAVER_URL = `https://nid.naver.com/oauth2.0/authorize?client_id=${N_CLIENT_ID}&response_type=code&redirect_uri=${encodeURIComponent(N_REDIRECT_URI)}&state=${stateString}`;
    const handlenaverLogin = () => {
        window.location.href = NAVER_URL;
    };
    // 랜덤 상태 문자열 생성 함수
    function generateRandomState() {
        return Math.random().toString(36).substring(2);
    }
    const G_ClientID = process.env.REACT_APP_GOOGLE_KEY;
    const G_REDIRECT_URI = process.env.REACT_APP_GOOGLE_REDIRECT_URI;
    const G_URL = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${G_ClientID}&redirect_uri=${encodeURIComponent(G_REDIRECT_URI)}&scope=openid%20email%20profile`;

    const handlegoogleLogin = () => {
        window.location.href = G_URL;
    };

    return (
        <div className="container">
            <h3>로그인</h3>
            <p style={{ color: "red", margin: "0 0 30px 9px" }}>
                로그인 후 이용해주세요
            </p>
            <div className="loginForm">
                <input
                    type="email"
                    id="user_id"
                    placeholder="이메일을 입력하세요"
                    className="loginInput"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    id="user_password"
                    placeholder="비밀번호를 입력하세요"
                    className="loginInput"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                />
            </div>

            {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}

            <div className="loginForm_bottom">
                <input
                    onClick={handleLogin}
                    type="submit"
                    value="이메일로 로그인"
                    className="button"
                />

                <div className="section_find">
                    <p style={{ fontSize: "small", paddingTop: "20px", marginLeft: "10px" }}>
                        <Link to="/login/signup" style={{ color: "black", fontSize: "16px" }}>회원가입</Link>
                    </p>
                    <p style={{ color: "grey", display: "inline", borderBottom: "1px solid grey", marginLeft: "10px" }}>
                        SNS 로그인
                    </p>
                    <div className="SNS">
                        <img onClick={handlenaverLogin} src={naverlogo} alt="Naver Login" />
                        <img onClick={handlekakaoLogin} src={kakaologo} alt="Kakao Login" />
                        <img onClick={handlegoogleLogin} src={googlelogo} alt="Google Login" />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
