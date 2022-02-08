package board.service;

import java.sql.Connection;
import java.util.List;

import board.Article;
import board.dao.ArticleDao;

public class ArticleService {

	ArticleDao articleDao;

	public ArticleService(Connection conn) {
		articleDao = new ArticleDao(conn);
	}

	public int getArticleCntById(int id) {
		return articleDao.getArticleCntById(id);
	}

	public int doWrite(String title, String body) {
		return articleDao.doWrite(title, body);
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	public void doModify(int id, String title, String body) {
		articleDao.doModify(id, title, body);
	}

	public void doDelete(int id) {
		articleDao.doDelete(id);
	}

	public Article getArticle(int id) {
		return articleDao.getArticle(id);
	}

}
