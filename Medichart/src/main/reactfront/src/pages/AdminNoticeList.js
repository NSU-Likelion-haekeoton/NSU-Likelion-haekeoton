import React, { useState, useEffect } from 'react';
import axios from 'axios';
import styles from './mymedicheck.module.css';
import { Link } from 'react-router-dom';

const AdminNoticeList = () => {
    const [notices, setNotices] = useState([]);
    const [search, setSearch] = useState('');
    const [page, setPage] = useState(0);
    const [size, setSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        fetchNotices();
    }, [search, page, size]);

    const fetchNotices = async () => {
        try {
            const response = await axios.get('/api/admin/notice', {
                params: { page, size, search }
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

    const handleSearchChange = (e) => {
        setSearch(e.target.value);
    };

    const handleSearchSubmit = (e) => {
        e.preventDefault();
        setPage(0); // 검색 시 첫 페이지로 이동
        fetchNotices();
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`/api/admin/notice/${id}`);
            fetchNotices();
        } catch (error) {
            console.error('Failed to delete notice', error);
        }
    };

    const handlePageChange = (newPage) => {
        if (newPage >= 0 && newPage < totalPages) {
            setPage(newPage);
        }
    };

    return (
        <div className={styles.container}>
            <div className={styles.sectionLeft}>
                <h2 className={styles.side}>공지사항 관리</h2>
                <div id="line">
                    <ul>
                        <li>
                            <Link to="/admin/noticeList" style={{ textDecoration: 'none' }} className="link">
                                - 공지사항 목록
                            </Link>
                        </li>
                        <li>
                            <Link to="/admin/notice/new" style={{ textDecoration: 'none' }} className="link">
                                - 공지사항 작성
                            </Link>
                        </li>
                    </ul>
                </div>
            </div>
            <section className={styles.inner}>
                <div className={styles.titleTop}>
                    <h2 className={styles.title}>공지사항 목록</h2>
                    <form onSubmit={handleSearchSubmit} className={styles.searchForm}>
                        <input
                            type="text"
                            value={search}
                            onChange={handleSearchChange}
                            placeholder="검색어 입력"
                            className={styles.searchInput}
                        />
                        <button type="submit" className={styles.searchButton}>검색</button>
                    </form>
                </div>
                <table className={styles.noticeTable}>
                    <thead>
                    <tr>
                        <th>번호</th>
                        <th>제목</th>
                        <th>내용</th>
                        <th>수정</th>
                        <th>삭제</th>
                        <th>작성 일자</th>
                    </tr>
                    </thead>
                    <tbody>
                    {notices.map((notice, index) => (
                        <tr key={notice.id}>
                            <td>{page * size + index + 1}</td>
                            <td>{notice.title}</td>
                            <td>{notice.content}</td>
                            <td>
                                <Link to={`/admin/notice/edit/${notice.id}`}>
                                    <button>수정</button>
                                </Link>
                            </td>
                            <td>
                                <button onClick={() => handleDelete(notice.id)}>삭제</button>
                            </td>
                            <td>{new Date(notice.createdDate).toLocaleString()}</td>
                        </tr>
                    ))}
                    </tbody>
                </table>
                <div className={styles.pagination}>
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
        </div>
    );
};

export default AdminNoticeList;
