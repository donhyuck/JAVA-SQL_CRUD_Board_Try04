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

# 게시글 테이블에 memberId 추가
ALTER TABLE article ADD COLUMN memberId INT(10) UNSIGNED NOT NULL AFTER updateDate;

# 게시글 테이블 수정
CREATE TABLE article (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(id),
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	memberId INT(10) UNSIGNED NOT NULL,
	title VARCHAR(100) NOT NULL,
	`body` TEXT NOT NULL
);

SELECT * FROM article;

# 작성자를 게시글에 표시
SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
ORDER BY a.id DESC;

#게시글 테이블 삭제
DROP TABLE article;

# 게시글 테이블에 memberId 추가
ALTER TABLE article ADD COLUMN hit INT(10) UNSIGNED NOT NULL AFTER `body`;

# 조회수 기능
UPDATE article
SET hit = hit+1
WHERE id = 1;

# 게시글 검색
SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
WHERE a.title LIKE CONCAT('%test1%')
ORDER BY a.id DESC;

# 게시글 검색+페이징
SELECT a.*, m.name AS extra_writer
FROM article AS a
LEFT JOIN `member` AS m
ON a.memberId = m.id
WHERE a.title LIKE CONCAT('%%')
ORDER BY a.id DESC
LIMIT 2,5;

# 게시글 갯수
SELECT COUNT(*)
FROM article;

# 추천/반대 테이블 삭제
DELETE FROM `like`;
DROP TABLE `like`;

# 추천/반대 테이블 생성
# likeType  = 1 추천 / likeType  = 2 반대
CREATE TABLE `like` (
	id INT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
	PRIMARY KEY(id),
	regDate DATETIME NOT NULL,
	updateDate DATETIME NOT NULL,
	articleId INT(10) UNSIGNED NOT NULL,
	memberId INT(10) UNSIGNED NOT NULL,
	likeType TINYINT(1) NOT NULL
);

DESC `like`;
SELECT * FROM `like`;

# 게시글 추천/비추천
INSERT INTO `like`
SET regDate = NOW(),
updateDate = NOW(),
articleId = 1,
memberId = 1,
likeType = 1;

# 로그인 중인 회원의 해당 게시글에 대한 추천/반대 여부
SELECT
CASE WHEN COUNT(*) != 0
THEN likeType ELSE 0 END likeCheck
FROM `like`
WHERE articleId = 1 AND memberId = 2;

# 추천/비추천 해제
DELETE FROM `like`
WHERE id = 1 AND memberId = 2;

# 추천/비추천 변경
UPDATE `like`
SET updateDate = NOW(),
likeType = 2
WHERE articleId = 1 AND memberId = 1;

# 추천/비추천 수
SELECT COUNT(*) likeCnt
FROM `like`
WHERE articleId = 1 AND likeType = 1;