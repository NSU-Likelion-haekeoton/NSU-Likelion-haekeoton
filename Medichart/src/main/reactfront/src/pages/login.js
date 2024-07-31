import "../pages/login.css";
import { Link } from "react-router-dom";
import kakaologo from "../kakaotalk_sharing_btn_small.png";
import naverlogo from "../naverlogo.png";
import googlelogo from "../btn_google.svg";

import { useAuth } from "../pages/AuthContext"; // useAuth 훅 가져오기
import { useNavigate } from "react-router-dom";

function Login() {
    const { login } = useAuth(); // useAuth 훅 사용
    const navigate = useNavigate();

    const handleLogin = () => {
        // 이메일 로그인 기능을 구현해야 함
        login(); // 사용자 ID를 넘겨줘야 함. 임시로 설정된 값 사용
        navigate("/");
    };

    const K_REST_API_KEY = process.env.REACT_APP_K_REST_API_KEY;
    const K_REDIRECT_URI = `http://localhost:3001/`;
    const kakaoURL = `https://kauth.kakao.com/oauth/authorize?client_id=${K_REST_API_KEY}&redirect_uri=${K_REDIRECT_URI}&response_type=code`;

    const handlekakaoLogin = () => {
        window.location.href = kakaoURL;
    };

    const N_clientID = process.env.REACT_APP_NAVER_CLIENT_ID;
    const N_REDIRECT_URI = "http://localhost:3001/naverlogin";
    const stateString = process.env.REACT_APP_NAVER_STATE;
    const NAVER_URL = `https://nid.naver.com/oauth2.0/authorize?client_id=${N_clientID}&response_type=code&redirect_uri=${N_REDIRECT_URI}&state=${stateString}`;

    const handlenaverLogin = () => {
        window.location.href = NAVER_URL;
    };

    const G_ClientID = process.env.REACT_APP_GOOGLE_KEY;
    const G_REDIRECT_URI = process.env.REACT_APP_GOOGLE_REDIRECT_URI;
    const G_URL = `https://accounts.google.com/o/oauth2/v2/auth?response_type=code&client_id=${G_ClientID}&redirect_uri=${G_REDIRECT_URI}&scope=openid%20email%20profile`;

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
                    required
                />
                <input
                    type="password"
                    id="user_password"
                    placeholder="비밀번호를 입력하세요"
                    className="loginInput"
                    required
                />
            </div>

            <div className="loginForm_bottom">
                <input
                    onClick={handleLogin}
                    type="submit"
                    value="이메일로 로그인"
                    className="button"
                />

                <div className="section_find">
                    <p
                        style={{
                            fontSize: "small",
                            paddingTop: "20px",
                            marginLeft: "10px",
                        }}
                    >
                        <Link
                            to="/login/signup"
                            style={{ color: "black", fontSize: "16px" }}
                        >
                            회원가입
                        </Link>{" "}
                    </p>
                    <p
                        style={{
                            color: "grey",
                            display: "inline",
                            borderBottom: "1px solid grey",
                            marginLeft: "10px",
                        }}
                    >
                        SNS 로그인
                    </p>
                    <div className="SNS">
                        <img onClick={handlenaverLogin} src={naverlogo} alt="Naver login" />
                        <img onClick={handlekakaoLogin} src={kakaologo} alt="Kakao login" />
                        <img onClick={handlegoogleLogin} src={googlelogo} alt="Google login" />
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
