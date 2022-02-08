package board.controller;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import board.Article;
import board.service.ArticleService;
import board.session.Session;
import board.util.DBUtil;
import board.util.SecSql;

public class ArticleController {

	Connection conn;
	Scanner sc;
	String cmd;
	Session session;

	ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc, String cmd, Session session) {
		this.conn = conn;
		this.sc = sc;
		this.cmd = cmd;
		this.session = session;

		articleService = new ArticleService(conn);
	}

	public void doWrite() {

		System.out.println("== 게시글 작성 ==");

		System.out.print("제목 : ");
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();

		int id = articleService.doWrite(title, body);

		System.out.printf("%d번 게시글이 등록되었습니다.\n", id);
	}

	public void showList() {

		List<Article> articles = articleService.getArticles();

		if (articles.size() == 0) {
			System.out.println("게시글이 존재하지 않습니다.");
			return;
		}

		System.out.println("== 게시글 목록 ==");
		System.out.println("번호 / 제목 ");
		for (Article article : articles) {
			System.out.printf(" %2d / %s \n", article.id, article.title);
		}
	}

	public void doModify() {

		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");

		if (!isInt) {
			System.out.println("게시글 번호를 숫자로 입력해주세요");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2].trim());

		int foundArticleId = articleService.getArticleCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		System.out.println("== 게시글 수정 ==");
		System.out.print("새 제목 : ");
		String title = sc.nextLine();
		System.out.print("새 내용 : ");
		String body = sc.nextLine();

		articleService.doModify(id, title, body);

		System.out.printf("%d번 게시글이 수정되었습니다.\n", id);
	}

	public void doDelete() {

		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");

		if (!isInt) {
			System.out.println("게시글 번호를 숫자로 입력해주세요");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2].trim());

		int foundArticleId = articleService.getArticleCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		articleService.doDelete(id);

		System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);
	}

	public void showDetail() {

		boolean isInt = cmd.split(" ")[2].matches("-?\\d+");

		if (!isInt) {
			System.out.println("게시글 번호를 숫자로 입력해주세요");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2].trim());

		int foundArticleId = articleService.getArticleCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		Article article = articleService.getArticle(id);

		System.out.printf("== %d번 게시글 상세보기 ==\n", id);
		System.out.printf("번 호 : %d\n", article.id);
		System.out.printf("등록일 : %s\n", article.regDate);
		System.out.printf("수정일 : %s\n", article.updateDate);
		System.out.printf("제 목 : %s\n", article.title);
		System.out.printf("내 용 : %s\n", article.body);
	}

}
