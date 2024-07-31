import React, { useState, useEffect } from 'react';
import axios from 'axios';
import '../pages/Notice.css';

const Notice = () => {
  const [notices, setNotices] = useState([]);
  const [activeIndex, setActiveIndex] = useState(null);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(10);
  const [totalPages, setTotalPages] = useState(0);

  useEffect(() => {
    fetchNotices();
  }, [page]);

  const fetchNotices = async () => {
    try {
      const response = await axios.get('/api/admin/notice', {
        params: { page, size }
      });
      if (response.data && response.data.content) {
        setNotices(response.data.content);
        setTotalPages(response.data.totalPages);
      } else {
        console.error('Unexpected response structure', response.data);
      }
    } catch (error) {
      console.error('Failed to fetch notices', error);
    }
  };

  const handleToggle = (index) => {
    setActiveIndex(activeIndex === index ? null : index);
  };

  const handlePageChange = (newPage) => {
    if (newPage >= 0 && newPage < totalPages) {
      setPage(newPage);
    }
  };

  return (
      <section className="notice">
        <h2>공지사항</h2>
        <table className="notice-table">
          <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성 일자</th>
          </tr>
          </thead>
          <tbody>
          {notices.map((item, index) => (
              <React.Fragment key={item.id}>
                <tr onClick={() => handleToggle(index)} className="notice-header">
                  <td>{page * size + index + 1}</td> {/* 페이지 내 항목 번호 */}
                  <td className="notice-title">{item.title}</td>
                  <td className="notice-date">{new Date(item.createdDate).toLocaleDateString()}</td>
                </tr>
                {activeIndex === index && (
                    <tr>
                      <td colSpan="3" className="notice-content">
                        {item.content}
                      </td>
                    </tr>
                )}
              </React.Fragment>
          ))}
          </tbody>
        </table>
        <div className="pagination">
          <button onClick={() => handlePageChange(page - 1)} disabled={page === 0}>
            이전
          </button>
          {[...Array(totalPages).keys()].map((num) => (
              <button
                  key={num}
                  onClick={() => handlePageChange(num)}
                  disabled={num === page}
              >
                {num + 1}
              </button>
          ))}
          <button onClick={() => handlePageChange(page + 1)} disabled={page === totalPages - 1}>
            다음
          </button>
        </div>
      </section>
  );
};

export default Notice;
