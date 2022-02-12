package board.service;

import java.sql.Connection;
import java.util.List;

import board.dao.ArticleDao;
import board.dto.Article;
import board.dto.Member;

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

	public int likeCheck(int id, int loginedMemberId) {
		return articleDao.likeCheck(id, loginedMemberId);
	}

	public void insertLike(int id, int likeType, int loginedMemberId) {
		articleDao.insertLike(id, likeType, loginedMemberId);
	}

	public void deleteLike(int id, int loginedMemberId) {
		articleDao.deleteLike(id, loginedMemberId);
	}

	public void modifyLike(int id, int likeType, int loginedMemberId) {
		articleDao.modifyLike(id, likeType, loginedMemberId);
	}

	public int getLikeCnt(int id, int likeType) {
		return articleDao.getLikeCnt(id, likeType);
	}

	public int doWriteComment(int id, int loginedMemberId, String commentBody) {
		return articleDao.doWriteComment(id, loginedMemberId, commentBody);
	}

	public int getCommentCheckById(int commentId, int id) {
		return articleDao.getCommentCheckById(commentId, id);
	}

	public void doModifyComment(int commentId, String commentBody) {
		articleDao.doModifyComment(commentId, commentBody);
	}

}
