package board.controller;

import java.sql.Connection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import board.dto.Article;
import board.dto.Comment;
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
		case "export":
			exportHtml();
			break;
		default:
			System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
		}

	}

	private void exportHtml() {

		articleService.exportHtml();

	}

	private void doDetailAction(int id) {

		while (true) {

			System.out.printf("== %d번 게시글 댓글 및 추천/비추천 ==\n", id);
			System.out.println("가이드 >>[나가기] 0 [작성] 1 [수정] 2 [삭제] 3 [목록] 4 [추천/비추천] 5");

			int actionType;

			while (true) {
				try {
					System.out.print("[article detail] 명령어 : ");
					actionType = sc.nextInt();
					sc.nextLine();
					break;

				} catch (InputMismatchException e) {
					System.out.println("명령어를 숫자로 입력해주세요.");
				}
			}

			if (actionType == 0) {
				System.out.println("== 게시글 상세보기 종료 ==");
				return;
			}

			if (actionType == 1) {

				if (session.loginedMember == null) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}

				System.out.println("== 댓글 작성 ==");

				System.out.print("댓글 내용 : ");
				String commentBody = sc.nextLine();

				int commentId = articleService.doWriteComment(id, session.getLoginedMemberId(), commentBody);

				System.out.printf("%d번 게시글의 %d번 댓글이 등록되었습니다.\n", id, commentId);

			} else if (actionType == 2) {

				if (session.loginedMember == null) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}

				// 수정할 댓글 번호 입력받기
				int commentId;
				while (true) {
					try {
						System.out.print("가이드 >>[나가기] 0 [댓글수정] 댓글번호 입력 : ");
						commentId = sc.nextInt();
						sc.nextLine();
						break;

					} catch (InputMismatchException e) {
						System.out.println("수정할 댓글의 번호를 숫자로 입력해주세요.");
					}
				}

				if (commentId == 0) {
					continue;
				}

				// 댓글 확인
				int commentCheck = articleService.getCommentCheckById(commentId, id);

				if (commentCheck == 0) {
					System.out.printf("%d번 댓글을 찾을 수 없습니다.\n", commentId);
					continue;
				}

				// 댓글 작성자에게 권한부여
				Comment comment = articleService.getCommentById(commentId);

				if (comment.getMemberId() != session.getLoginedMemberId()) {
					System.out.println("해당 댓글에 대한 권한이 없습니다.");
					continue;
				}

				System.out.println("== 댓글 수정 ==");
				System.out.print("새 내용 : ");
				String commentBody = sc.nextLine();

				articleService.doModifyComment(commentId, commentBody);

				System.out.printf("%d번 게시글의 %d번 댓글이 수정되었습니다.\n", id, commentId);

			} else if (actionType == 3) {

				if (session.loginedMember == null) {
					System.out.println("로그인 후 이용해주세요.");
					continue;
				}

				System.out.println("== 댓글 삭제 ==");

				// 삭제할 댓글 번호 입력받기
				int commentId;
				while (true) {
					try {
						System.out.print("가이드 >>[나가기] 0 [댓글삭제] 댓글번호 입력 : ");
						commentId = sc.nextInt();
						sc.nextLine();
						break;

					} catch (InputMismatchException e) {
						System.out.println("삭제할 댓글의 번호를 숫자로 입력해주세요.");
					}
				}

				if (commentId == 0) {
					continue;
				}

				// 댓글 확인
				int commentCheck = articleService.getCommentCheckById(commentId, id);

				if (commentCheck == 0) {
					System.out.printf("%d번 댓글을 찾을 수 없습니다.\n", commentId);
					continue;
				}

				// 댓글 작성자에게 권한부여
				Comment comment = articleService.getCommentById(commentId);

				if (comment.getMemberId() != session.getLoginedMemberId()) {
					System.out.println("해당 댓글에 대한 권한이 없습니다.");
					continue;
				}

				articleService.doDeleteComment(commentId);
				System.out.printf("%d번 게시글의 %d번 댓글이 삭제되었습니다.\n", id, commentId);

			} else if (actionType == 4) {

				int page = 1;
				int itemsPage = 5;

				while (true) {

					List<Comment> pageComments = articleService.getCommentsByPage(id, page, itemsPage);

					if (pageComments.size() == 0) {
						System.out.println("등록된 댓글이 없습니다.");
						break;
					}

					System.out.printf("= %d번 게시글 댓글보기 =\n", id);
					System.out.println("번호 / 작성자 /  등록일 ");

					for (Comment comment : pageComments) {
						System.out.printf(" %2d / %s / %s\n", comment.getId(), comment.getExtra_writer(),
								comment.getRegDate().toLocalDate());
						System.out.println("내용 : " + comment.getCommentBody());
					}

					// 전체 댓글수
					int commentsCnt = articleService.getCommentsCnt(id);
					int lastCommentPage = (int) Math.ceil((commentsCnt / (double) itemsPage));

					System.out.printf("페이지 %d / %d, 댓글 %d건\n", page, lastCommentPage, commentsCnt);
					System.out.println("가이드 >> [나가기] 0 이하 입력 [페이지 이동] 페이지 번호 입력 ");
					System.out.print("[article comment] 명령어 : ");

					// 페이지 번호만 입력받기
					while (!sc.hasNextInt()) {
						sc.nextLine();
						System.out.println("페이지 번호를 숫자로 입력해주세요.");
						System.out.print("[article list] 명령어 : ");
						continue;
					}

					page = sc.nextInt();
					sc.nextLine();

					if (page <= 0) {
						System.out.println("댓글 페이지를 나갑니다.");
						break;
					}

				}

			} else if (actionType == 5) {

				doLike(id);

			} else {
				System.out.println("가이드에 해당하는 숫자를 입력해주세요.");
			}
		}

	}

	private void doLike(int id) {

		if (session.loginedMember == null) {
			System.out.println("로그인 후 이용해주세요.");
			return;
		}

		while (true) {

			System.out.printf("== %d번 게시글 추천/비추천 ==\n", id);
			System.out.println("가이드 >> [나가기] 0 [추천] 1 [비추천] 2 [해제] 3 ");
			System.out.print("[article detail like] 명령어 : ");

			// 숫자만 입력받기
			while (!sc.hasNextInt()) {
				sc.nextLine();
				System.out.println("명령어를 숫자로 입력해주세요.");
				System.out.print("[article detail like] 명령어 : ");
				continue;
			}

			int likeType = sc.nextInt();
			sc.nextLine();

			if (likeType == 0) {
				System.out.println("== 게시글 추천/비추천 종료 ==");
				return;

			}

			// 중복 추천/반대를 막아야함
			// 해당 게시글에 대한 로그인 중인 회원의 추천/비추천 여부를 알아야한다.
			int likeCheck = articleService.likeCheck(id, session.getLoginedMemberId());
			String userMsg = (likeType == 1 ? "추천" : "비추천");

			if (likeCheck == 0) {

				if (likeType == 1 || likeType == 2) {

					articleService.insertLike(id, likeType, session.getLoginedMemberId());

					System.out.printf("%s님이 %d번 글을 %s했습니다.\n", session.loginedMember.getName(), id, userMsg);
					return;

				} else if (likeType == 3) {
					System.out.println("해제할 추천/비추천이 없습니다.");
					return;

				} else {
					System.out.println("가이드에 해당하는 숫자를 입력해주세요.\n");
				}

			} else {

				if (likeType == 3) {
					articleService.deleteLike(id, session.getLoginedMemberId());
					String resultMsg = (likeCheck == 1 ? "추천" : "비추천");
					System.out.printf("%s을 취소합니다.\n", resultMsg);
					return;
				}

				// 사용자 입력한 명령어와 추천/비추천여부 간의 비교
				// 중복 명령방지
				if (likeType == likeCheck) {

					System.out.printf("%s님은 %d번 게시글을 이미 %s했습니다.\n", session.getLoginedMember().getName(), id, userMsg);
					return;

				} else {

					articleService.modifyLike(id, likeType, session.getLoginedMemberId()); // 추천/비추천 변경
					System.out.printf("%d번 글을 %s으로 변경합니다.\n", id, userMsg);
					return;
				}
			}

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

		String[] cmdBits = cmd.split(" ");
		String keyWord = "";
		List<Article> articles;

		// 게시글 목록을 페이지로 구현
		int page = 1; // 시작은 1페이지 부터
		int itemsPage = 5; // 한 페이지 당 게시글 갯수

		while (true) {

			// 검색어가 있는 경우
			if (cmdBits.length >= 3) {
				keyWord = cmd.substring("article list ".length());
				articles = articleService.getArticlesByKeyWord(page, itemsPage, keyWord);
			}

			// 검색어가 없는 경우
			else {
				if (cmd.length() != 12) {
					System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
					return;
				}
				articles = articleService.getArticles(page, itemsPage);
			}

			if (articles.size() == 0) {
				System.out.println("게시글이 존재하지 않습니다.");
				return;
			}

			System.out.println("== 게시글 목록 ==");
			System.out.println("번호 / 제목 / 작성자 ");
			for (Article article : articles) {
				System.out.printf(" %2d / %s / %s \n", article.getId(), article.getTitle(), article.getExtra_writer());
			}

			// 페이지 이동 기능
			int articleCnt = articleService.getArticlesCnt(keyWord); // 총 게시글 수
			int lastPage = (int) Math.ceil(articleCnt / (double) itemsPage); // 마지막 페이지

			System.out.printf("페이지 %d / %d, 게시글 %d건\n", page, lastPage, articleCnt);
			System.out.println("가이드 >> [나가기] 0 이하 입력 [페이지 이동] 페이지 번호 입력 ");
			System.out.print("[article list] 명령어 : ");

			// 페이지 번호만 입력받기
			while (!sc.hasNextInt()) {
				sc.nextLine();
				System.out.println("페이지 번호를 숫자로 입력해주세요.");
				System.out.print("[article list] 명령어 : ");
				continue;
			}

			page = sc.nextInt();
			sc.nextLine();

			if (page <= 0) {
				System.out.println("게시글 페이지를 나갑니다.");
				break;
			}
		}

	}

	private void doModify() {

		int id = Integer.parseInt(cmd.split(" ")[2].trim());

		int foundArticleId = articleService.getArticleCntById(id);

		if (foundArticleId == 0) {
			System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
			return;
		}

		// 권한체크
		Article article = articleService.getArticle(id);

		if (article.getMemberId() != session.getLoginedMemberId()) {
			System.out.printf("%s님은 %d번 게시글의 수정권한이 없습니다.\n", session.loginedMember.getName(), id);
			return;
		}

		System.out.printf("== %d번 게시글 수정 ==\n", id);
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

		// 권한체크
		Article article = articleService.getArticle(id);

		if (article.getMemberId() != session.getLoginedMemberId()) {
			System.out.printf("%s님은 %d번 게시글의 삭제권한이 없습니다.\n", session.loginedMember.getName(), id);
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

		articleService.increaseHit(id);

		Article article = articleService.getArticle(id);

		// 해당 게시글의 추천/비추천 수
		int likeCnt = articleService.getLikeCnt(id, 1);
		int disLikeCnt = articleService.getLikeCnt(id, 2);

		System.out.printf("== %d번 게시글 상세보기 ==\n", id);
		System.out.printf("번 호 : %d\n", article.getId());
		System.out.printf("등록일 : %s\n", article.getRegDate());
		System.out.printf("수정일 : %s\n", article.getUpdateDate());
		System.out.printf("작성자 : %s\n", article.getExtra_writer());
		System.out.printf("제 목 : %s\n", article.getTitle());
		System.out.printf("내 용 : %s\n", article.getBody());
		System.out.printf("조회수 : %d\n", article.getHit());
		System.out.printf(" 추천 [ %d건 ] 비추천 [ %d건 ] \n", likeCnt, disLikeCnt);
		System.out.printf("댓 글 [ %d건 ]\n", articleService.getCommentsCnt(id));

		// comment로 접근을 detail을 거쳐서 수행하도록
		System.out.println();
		doDetailAction(id);

	}
}
