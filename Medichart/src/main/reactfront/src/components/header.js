// src/components/Header.js

import React, { useState } from "react";
import styles from "./header.module.css";
import { Link, useNavigate } from "react-router-dom";
import logo from "../MediChart_clear.png";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser } from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "../pages/AuthContext"; // useAuth 훅을 가져옵니다.

function Header() {
  const [dropdownOpen, setDropdownOpen] = useState([false, false]);
  const navigate = useNavigate(); // useNavigate 훅 추가
  const { isLoggedIn, logout } = useAuth(); // useAuth 훅을 통해 AuthContext를 사용합니다.

  const toggleDropdown = (index) => {
    const newDropdownOpen = dropdownOpen.map((isOpen, i) =>
        i === index ? !isOpen : false
    );
    setDropdownOpen(newDropdownOpen);
  };

  const handleLogout = () => {
    logout(); // 로그아웃 수행
    navigate('/login'); // 로그아웃 후 로그인 페이지로 리디렉션
  };

  return (
      <header className={styles.header}>
        <div className={styles.contents}>
          <div>
            <Link to="/" className={styles.link1}>
              <img className={styles.logo} src={logo} alt="Logo" />
              <p>MediChart</p>
            </Link>
          </div>

          <nav className={styles.navigation}>
            <ul>
              <li
                  className={styles.titleHeader}
                  onClick={() => toggleDropdown(0)}
              >
                건강진단서 해석
                <span
                    className={`${styles.triangle} ${
                        dropdownOpen[0] ? styles.open : ""
                    }`}
                ></span>
                {dropdownOpen[0] && (
                    <ul className={styles.dropdown}>
                      <li>
                        <Link to="/Korean" className={styles.link}>
                          - 한국어
                        </Link>
                      </li>
                      <li>
                        <Link to="/Japanese" className={styles.link}>
                          - 日本語
                        </Link>
                      </li>
                      <li>
                        <Link to="/Chinese" className={styles.link}>
                          - 汉文
                        </Link>
                      </li>
                    </ul>
                )}
              </li>
              <li
                  className={styles.titleHeader}
                  onClick={() => toggleDropdown(1)}
              >
                나의 메디체크
                <span
                    className={`${styles.triangle} ${
                        dropdownOpen[1] ? styles.open : ""
                    }`}
                ></span>
                {dropdownOpen[1] && (
                    <ul className={styles.dropdown}>
                      <li>
                        <Link to="/MedicalInform" className={styles.link}>
                          - 건강검진정보
                        </Link>
                      </li>
                      <li>
                        <Link to="/Prediction" className={styles.link}>
                          - 질병 예측
                        </Link>
                      </li>
                      <li>
                        <Link to="/SearchHospital" className={styles.link}>
                          - 검진센터 찾기
                        </Link>
                      </li>
                    </ul>
                )}
              </li>
              <li className={styles.titleHeader}>
                <Link to="/CustomerService" className={styles.link}>
                  고객센터
                </Link>
              </li>
              {isLoggedIn ? (
                  <li className={styles.titleHeader}>
                    <Link to="/Mypage" className={styles.link}>
                      마이페이지
                      <FontAwesomeIcon icon={faUser} id={styles.faUser} />
                    </Link>
                    <button className={styles.logout} onClick={handleLogout}>
                      로그아웃
                    </button>
                  </li>
              ) : (
                  <Link to="/login" className={styles.link}>
                    <li className={styles.titleHeader}>로그인/회원가입</li>
                  </Link>
              )}
            </ul>
          </nav>
        </div>
      </header>
  );
}

export default Header;
