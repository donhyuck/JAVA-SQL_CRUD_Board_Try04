package board.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import board.dto.Article;
import board.dto.Comment;
import board.util.DBUtil;
import board.util.SecSql;
import board.util.Util;

public class ArticleDao {

	Connection conn;

	public ArticleDao(Connection conn) {
		this.conn = conn;
	}

	// 해당 게시글이 있는지 확인
	public int getArticleCntById(int id) {

		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");
		sql.append("WHERE id = ?", id);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public int doWrite(String title, String body, int loginedMemberId) {

		SecSql sql = new SecSql();
		sql.append("INSERT INTO article");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", memberId = ?", loginedMemberId);
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append(", hit = 0");

		return DBUtil.insert(conn, sql);
	}

	public List<Article> getArticles(int limitFrom, int limitTake) {

		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();
		sql.append("SELECT a.*, m.name AS extra_writer");
		sql.append("FROM article AS a");
		sql.append("LEFT JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("ORDER BY a.id DESC");
		sql.append("LIMIT ?,?", limitFrom, limitTake);

		List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public void doModify(int id, String title, String body) {

		SecSql sql = new SecSql();
		sql.append("UPDATE article");
		sql.append("SET updateDate = NOW()");
		sql.append(", title = ?", title);
		sql.append(", body = ?", body);
		sql.append("WHERE id = ?", id);

		DBUtil.update(conn, sql);
	}

	public void doDelete(int id) {

		SecSql sql = new SecSql();
		sql.append("DELETE FROM article");
		sql.append("WHERE id = ?", id);

		DBUtil.delete(conn, sql);
	}

	public Article getArticle(int id) {

		SecSql sql = new SecSql();
		sql.append("SELECT a.*, m.name AS extra_writer");
		sql.append("FROM article AS a");
		sql.append("LEFT JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("WHERE a.id = ?", id);

		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

		return new Article(articleMap);

	}

	public void increaseHit(int id) {

		SecSql sql = new SecSql();
		sql.append("UPDATE article");
		sql.append("SET hit = hit+1");
		sql.append("WHERE id = ?", id);

		DBUtil.update(conn, sql);

	}

	public List<Article> getArticlesByKeyWord(int limitFrom, int limitTake, String keyWord) {

		SecSql sql = new SecSql();
		sql.append("SELECT a.*, m.name AS extra_writer");
		sql.append("FROM article AS a");
		sql.append("LEFT JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("WHERE a.title LIKE CONCAT('%',?,'%')", keyWord);
		sql.append("ORDER BY a.id DESC");
		sql.append("LIMIT ?,?", limitFrom, limitTake);

		List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

		List<Article> articles = new ArrayList<>();

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;
	}

	public int getArticlesCnt(String keyWord) {

		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*)");
		sql.append("FROM article");

		if (keyWord != "") {
			sql.append("WHERE title LIKE CONCAT('%',?,'%')", keyWord);
		}

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public int likeCheck(int id, int loginedMemberId) {

		SecSql sql = new SecSql();
		sql.append("SELECT");
		sql.append("CASE WHEN COUNT(*) != 0");
		sql.append("THEN likeType ELSE 0 END likeCheck");
		sql.append("FROM `like`");
		sql.append("WHERE articleId = ? AND memberId = ?", id, loginedMemberId);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public void insertLike(int id, int likeType, int loginedMemberId) {

		SecSql sql = new SecSql();
		sql.append("INSERT INTO `like`");
		sql.append("SET regDate = NOW()");
		sql.append(", updateDate = NOW()");
		sql.append(", articleId = ?", id);
		sql.append(", memberId = ?", loginedMemberId);
		sql.append(", likeType = ?", likeType);

		DBUtil.insert(conn, sql);
	}

	public void deleteLike(int id, int loginedMemberId) {

		SecSql sql = new SecSql();
		sql.append("DELETE FROM `like`");
		sql.append("WHERE articleId = ? AND memberId = ?", id, loginedMemberId);

		DBUtil.delete(conn, sql);
	}

	public void modifyLike(int id, int likeType, int loginedMemberId) {

		SecSql sql = new SecSql();
		sql.append("UPDATE `like`");
		sql.append("SET updateDate = NOW()");
		sql.append(", likeType = ?", likeType);
		sql.append("WHERE articleId = ? AND memberId = ?", id, loginedMemberId);

		DBUtil.update(conn, sql);
	}

	public int getLikeCnt(int id, int likeType) {

		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*)");
		sql.append("FROM `like`");
		sql.append("WHERE articleId = ? AND likeType = ?", id, likeType);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public int doWriteComment(int id, int loginedMemberId, String commentBody) {

		SecSql sql = new SecSql();
		sql.append("INSERT INTO `comment`");
		sql.append("SET regDate=NOW()");
		sql.append(", updateDate=NOW()");
		sql.append(", articleId = ?", id);
		sql.append(", memberId = ?", loginedMemberId);
		sql.append(", commentBody = ?", commentBody);

		return DBUtil.insert(conn, sql);
	}

	public int getCommentCheckById(int commentId, int id) {

		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*)");
		sql.append("FROM `comment`");
		sql.append("WHERE id = ? AND articleId = ?", commentId, id);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public void doModifyComment(int commentId, String commentBody) {

		SecSql sql = new SecSql();
		sql.append("UPDATE `comment`");
		sql.append("SET updateDate = NOW()");
		sql.append(", commentBody = ?", commentBody);
		sql.append("WHERE id = ?", commentId);

		DBUtil.update(conn, sql);
	}

	public void doDeleteComment(int commentId) {

		SecSql sql = new SecSql();
		sql.append("DELETE FROM `comment`");
		sql.append("WHERE id = ?", commentId);

		DBUtil.delete(conn, sql);
	}

	public Comment getCommentById(int commentId) {

		SecSql sql = new SecSql();
		sql.append("SELECT *");
		sql.append("FROM `comment`");
		sql.append("WHERE id = ?", commentId);

		Map<String, Object> commentMap = DBUtil.selectRow(conn, sql);

		return new Comment(commentMap);
	}

	public List<Comment> getCommentsByPage(int id, int limitFrom, int limitTake) {

		SecSql sql = new SecSql();
		sql.append("SELECT c.*,m.name AS extra_writer");
		sql.append("FROM `comment` c");
		sql.append("INNER JOIN `member` m");
		sql.append("ON c.memberId = m.id");
		sql.append("WHERE c.articleId = ?", id);
		sql.append("ORDER BY c.id DESC");
		sql.append("LIMIT ?,?", limitFrom, limitTake);

		List<Map<String, Object>> commentListMap = DBUtil.selectRows(conn, sql);

		List<Comment> comments = new ArrayList<>();

		for (Map<String, Object> commentMap : commentListMap) {
			comments.add(new Comment(commentMap));
		}

		return comments;
	}

	public int getCommentsCnt(int id) {

		SecSql sql = new SecSql();
		sql.append("SELECT COUNT(*)");
		sql.append("FROM `comment`");
		sql.append("WHERE articleId = ?", id);

		return DBUtil.selectRowIntValue(conn, sql);
	}

	public List<Article> getArticles() {

		List<Article> articles = new ArrayList<>();

		SecSql sql = new SecSql();

		sql.append("SELECT a.*, m.name AS extra_writer");
		sql.append("FROM article AS a");
		sql.append("LEFT JOIN `member` AS m");
		sql.append("ON a.memberId = m.id");
		sql.append("ORDER BY a.id DESC");

		List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

		for (Map<String, Object> articleMap : articleListMap) {
			articles.add(new Article(articleMap));
		}

		return articles;

	}

	public void exprotHtml() {

		System.out.println("== HTML 생성을 시작합니다 ==");

		List<Article> articles = getArticles();

		for (Article article : articles) {

			String fileName = "./html/" + article.getId() + ".html";
			String html = "<meta charset=\"UTF-8\">";
			html += "<div>번호: " + article.getId() + "</div>";
			html += "<div>날짜: " + article.getRegDate() + "</div>";
			html += "<div>작성자: " + article.getExtra_writer() + "</div>";
			html += "<div>제목: " + article.getTitle() + "</div>";
			html += "<div>내용: " + article.getBody() + "</div>";

			html += "<div><a href=\"" + (article.getId() - 1) + ".html\">이전글</a></div>";
			html += "<div><a href=\"" + (article.getId() + 1) + ".html\">다음글</a></div>";

			Util.writeFileContents(fileName, html);

		}

	}

}
