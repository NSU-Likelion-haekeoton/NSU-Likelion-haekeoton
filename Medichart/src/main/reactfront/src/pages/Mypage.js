import React from 'react';
import { Link } from 'react-router-dom';
import '../pages/Mypage.css';

function Mypage() {
  // Dummy data for demonstration
  const user = {
    name: "이름",
    birthDate: "2002-01-01",
    contact: "010-1234-1234",
    email: "1234@naver.com"
  };

  // Dummy logout function
  const logout = () => {
    console.log('로그아웃 버튼 클릭됨');
    // Perform logout operation here
  };

  return (
      <div className="my-page">
        <div className="sectionLeft">
          <h2>마이페이지</h2>
          <div id="line">
            <ul>
              <li>
                <Link to="/Mypage" className="link">
                  - 계정 정보
                </Link>
              </li>
            </ul>
          </div>
        </div>
        <div className="UserInformation">
          <div className="account-info">
            <h2>계정 정보</h2>
            <div className="info">
              <label>이름</label>
              <input type="text" value={user.name} readOnly />
            </div>
            <div className="info">
              <label>생년월일</label>
              <input type="text" value={user.birthDate} readOnly />
            </div>
            <div className="info">
              <label>연락처</label>
              <input type="text" value={user.contact} readOnly />
            </div>
            <div className="info">
              <label>이메일</label>
              <input type="text" value={user.email} readOnly />
            </div>
            <button onClick={logout}>로그아웃</button>
          </div>
        </div>
      </div>
  );
}

export default Mypage;