package board.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import board.dto.Article;
import board.util.DBUtil;
import board.util.SecSql;

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

		return DBUtil.insert(conn, sql);
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
		sql.append("SELECT * FROM article");
		sql.append("WHERE id = ?", id);

		Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);

		return new Article(articleMap);

	}

}
