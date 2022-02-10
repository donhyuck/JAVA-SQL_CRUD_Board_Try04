package board.service;

import java.sql.Connection;
import java.util.List;

import board.dao.ArticleDao;
import board.dto.Article;

public class ArticleService {

	ArticleDao articleDao;

	public ArticleService(Connection conn) {
		articleDao = new ArticleDao(conn);
	}

	public int getArticleCntById(int id) {
		return articleDao.getArticleCntById(id);
	}

	public int doWrite(String title, String body, int loginedMemberId) {
		return articleDao.doWrite(title, body, loginedMemberId);
	}

	public List<Article> getArticles(int page, int itemsPage) {
		int limitFrom = (page - 1) * itemsPage;
		int limitTake = itemsPage;
		return articleDao.getArticles(limitFrom, limitTake);
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

	public void increaseHit(int id) {
		articleDao.increaseHit(id);
	}

	public List<Article> getArticlesByKeyWord(int page, int itemsPage, String keyWord) {
		int limitFrom = (page - 1) * itemsPage;
		int limitTake = itemsPage;
		return articleDao.getArticlesByKeyWord(limitFrom, limitTake, keyWord);
	}

	public int getArticlesCnt(String keyWord) {
		return articleDao.getArticlesCnt(keyWord);
	}

}
