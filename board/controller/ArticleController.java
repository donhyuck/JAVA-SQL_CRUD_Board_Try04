package board.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import board.dto.Article;
import board.service.ArticleService;
import board.session.Session;

public class ArticleController extends Controller {

	private Scanner sc;
	private String cmd;
	private Session session;

	private ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc, String cmd, Session session) {
		this.sc = sc;
		this.cmd = cmd;
		this.session = session;

		articleService = new ArticleService(conn);
	}

	@Override
	public void doAction() {

		String[] cmdBits = cmd.split(" ");

		String actionName = cmdBits[1];

		switch (actionName) {
		case "modify":
		case "delete":
		case "detail":

			if (cmdBits.length == 3) {
				boolean isInt = cmd.split(" ")[2].matches("-?\\d+");

				if (!isInt) {
					System.out.println("게시글 번호를 숫자로 입력해주세요");
					return;
				}
				
			} else if (cmdBits.length == 2) {
				System.out.println("게시글의 번호를 입력해주세요.");
				return;

			} else {
				System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
			}
		}

		switch (actionName) {
		case "write":
		case "modify":
		case "delete":
			if (session.loginedMember == null) {
				System.out.println("로그인 후 이용해주세요.");
				return;
			}
		}

		switch (actionName) {
		case "write":
			doWrite();
			break;
		case "list":
			showList();
			break;
		case "modify":
			doModify();
			break;
		case "delete":
			doDelete();
			break;
		case "detail":
			showDetail();
			break;
		default:
			System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
		}

	}

	private void doWrite() {

		System.out.println("== 게시글 작성 ==");

		System.out.print("제목 : ");
		String title = sc.nextLine();
		System.out.print("내용 : ");
		String body = sc.nextLine();

		int id = articleService.doWrite(title, body, session.getLoginedMemberId());

		System.out.printf("%d번 게시글이 등록되었습니다.\n", id);
	}

	private void showList() {

		List<Article> articles = articleService.getArticles();

		if (articles.size() == 0) {
			System.out.println("게시글이 존재하지 않습니다.");
			return;
		}

		System.out.println("== 게시글 목록 ==");
		System.out.println("번호 / 제목 / 작성자 ");
		for (Article article : articles) {
			System.out.printf(" %2d / %s / %s \n", article.getId(), article.getTitle(), article.getExtra_writer());
		}
	}

	private void doModify() {

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

	private void doDelete() {

		int id = Integer.parseInt(cmd.split(" ")[2].trim());

		int foundArticleId = articleService.getArticleCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		articleService.doDelete(id);

		System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);
	}

	private void showDetail() {

		int id = Integer.parseInt(cmd.split(" ")[2].trim());

		int foundArticleId = articleService.getArticleCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		Article article = articleService.getArticle(id);

		System.out.printf("== %d번 게시글 상세보기 ==\n", id);
		System.out.printf("번 호 : %d\n", article.getId());
		System.out.printf("등록일 : %s\n", article.getRegDate());
		System.out.printf("수정일 : %s\n", article.getUpdateDate());
		System.out.printf("제 목 : %s\n", article.getTitle());
		System.out.printf("내 용 : %s\n", article.getBody());
	}
}
