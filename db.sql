DROP DATABASE IF EXISTS DB_board;

CREATE DATABASE DB_board;

USE DB_board;

#게시글 테이블 삭제
DELETE FROM article;
DROP TABLE article;

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
