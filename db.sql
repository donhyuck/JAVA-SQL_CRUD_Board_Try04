DROP DATABASE IF EXISTS DB_board;

CREATE DATABASE DB_board;

USE DB_board;
# DB - 게시글 작성쿼리 try 04
#게시글 테이블 삭제
DELETE FROM article;
DROP TABLE article;

# 게시글 테이블 생성
CREATE TABLE article (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(id),
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	title VARCHAR(100) NOT NULL,
	`body` TEXT NOT NULL
);

DESC article;
SELECT * FROM article;

# 게시글 목록보기
SELECT * FROM article
ORDER BY id DESC;

#게시글 수정
UPDATE article
SET updateDate = NOW(),
title = 'test222',
`body` = 'test222'
WHERE id = 6;

# 해당 게시글이 없는 경우 처리
SELECT COUNT(*)
FROM article
WHERE id = 5;

# 게시글 삭제
DELETE FROM article
WHERE id = 10;

# 게시글 상세보기
SELECT * FROM article
WHERE id = 5;

#회원 삭제
DROP TABLE `member`;

# 회원 테이블 생성
CREATE TABLE `member` (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(id),
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	loginId VARCHAR(50) NOT NULL,
	loginPw VARCHAR(50) NOT NULL,
	`name` VARCHAR(50) NOT NULL
);

DESC `member`;
SELECT * FROM `member`;

# 아이디 일치 확인/ 회원 중복방지
SELECT COUNT(*)
FROM `member`
WHERE loginId = 'admin';

# 관리자 회원 추가
INSERT INTO `member`
SET regDate = NOW(),
updateDate = NOW(),
loginId = 'admin',
loginPw = 'admin',
`name` = '관리자';

# 로그인
SELECT * FROM `member`
WHERE loginId = 'test3';