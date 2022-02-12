package board.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class Comment {

	private int id;
	private LocalDateTime regDate;
	private LocalDateTime updateDate;
	private int articleId;
	private int memberId;
	private String commentBody;

	public Comment(Map<String, Object> commentMap) {
		this.id = (int) commentMap.get("id");
		this.regDate = (LocalDateTime) commentMap.get("regDate");
		this.updateDate = (LocalDateTime) commentMap.get("updateDate");
		this.articleId = (int) commentMap.get("articleId");
		this.memberId = (int) commentMap.get("memberId");
		this.commentBody = (String) commentMap.get("commentBody");
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDateTime getRegDate() {
		return regDate;
	}

	public void setRegDate(LocalDateTime regDate) {
		this.regDate = regDate;
	}

	public LocalDateTime getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(LocalDateTime updateDate) {
		this.updateDate = updateDate;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getCommentBody() {
		return commentBody;
	}

	public void setCommentBody(String commentBody) {
		this.commentBody = commentBody;
	}
}
