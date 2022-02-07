package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import board.session.Session;
import board.util.DBUtil;
import board.util.SecSql;

public class App {

	public void start() {

		Scanner sc = new Scanner(System.in);

		// 세션 적용
		Session session = new Session();

		// JDBC적용
		Connection conn = null; // DB 접속 객체

		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Mysql JDBC 드라이버 로딩
			String url = "jdbc:mysql://127.0.0.1:3306/DB_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

			conn = DriverManager.getConnection(url, "root", "");

			System.out.println("== 프로그램 시작 ==");

			while (true) {

				System.out.print("명령어 : ");
				String cmd = sc.nextLine();

				cmd = cmd.trim();

				if (cmd.length() == 0) {
					continue;
				}

				int actionResult = doAction(conn, sc, cmd, session);

				// 프로그램 종료
				if (actionResult == -1) {
					break;
				}
			}

		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패");
		} catch (SQLException e) {
			System.out.println("에러: " + e);
		} finally { // 예외 상황이든 아니든 무조건 마지막에 실행하는 finally
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close(); // 연결 종료
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private int doAction(Connection conn, Scanner sc, String cmd, Session session) {

		if (cmd.equals("system exit")) {
			System.out.println("== 프로그램 종료 ==");
			return -1;

		} else if (cmd.equals("article write")) {

			System.out.println("== 게시글 작성 ==");

			System.out.print("제목 : ");
			String title = sc.nextLine();
			System.out.print("내용 : ");
			String body = sc.nextLine();

			// DBUtil 적용
			SecSql sql = new SecSql();
			sql.append("INSERT INTO article");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", body = ?", body);

			int id = DBUtil.insert(conn, sql);

			System.out.printf("%d번 게시글이 등록되었습니다.\n", id);

		} else if (cmd.equals("article list")) {

			List<Article> articles = new ArrayList<>();

			// DBUtil 적용
			SecSql sql = new SecSql();
			sql.append("SELECT * FROM article");
			sql.append("ORDER BY id DESC");

			List<Map<String, Object>> articleListMap = DBUtil.selectRows(conn, sql);

			for (Map<String, Object> articleMap : articleListMap) {
				articles.add(new Article(articleMap));
			}

			if (articles.size() == 0) {
				System.out.println("게시글이 존재하지 않습니다.");
				return 0;
			}

			System.out.println("== 게시글 목록 ==");
			System.out.println("번호 / 제목 ");
			for (Article article : articles) {
				System.out.printf(" %2d / %s \n", article.id, article.title);
			}

		} else if (cmd.startsWith("article modify")) {

			// 수정, 삭제, 상세보기의 경우 두번째 index자리에 들어오는 숫자로 명령을 수행할 게시글을 골라낸다.
			// 이때, 그 자리에 문자가 입력될 경우 에러가 발생한다.
			// 해당 문제를 해결하는 정규표현식이있다.

			// 정수이외는 예외처리(정규표현식)
			boolean isInt = cmd.split(" ")[2].matches("-?\\d+");

			if (!isInt) {
				System.out.println("게시글 번호를 숫자로 입력해주세요");
				return 0;
			}

			int id = Integer.parseInt(cmd.split(" ")[2].trim());

			// 해당 게시글이 있는지 확인
			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			int foundArticleId = DBUtil.selectRowIntValue(conn, sql);

			if (foundArticleId == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}

			System.out.println("== 게시글 수정 ==");
			System.out.print("새 제목 : ");
			String title = sc.nextLine();
			System.out.print("새 내용 : ");
			String body = sc.nextLine();

			// DBUtil 적용
			sql = new SecSql();
			sql.append("UPDATE article");
			sql.append("SET updateDate = NOW()");
			sql.append(", title = ?", title);
			sql.append(", body = ?", body);
			sql.append("WHERE id = ?", id);

			DBUtil.update(conn, sql);

			System.out.printf("%d번 게시글이 수정되었습니다.\n", id);

		} else if (cmd.startsWith("article delete")) {

			// 정수이외는 예외처리(정규표현식)
			boolean isInt = cmd.split(" ")[2].matches("-?\\d+");

			if (!isInt) {
				System.out.println("게시글 번호를 숫자로 입력해주세요");
				return 0;
			}

			int id = Integer.parseInt(cmd.split(" ")[2].trim());

			// 해당 게시글이 있는지 확인
			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			int foundArticleId = DBUtil.selectRowIntValue(conn, sql);

			if (foundArticleId == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}

			System.out.println("== 게시글 삭제 ==");

			// DBUtil 적용
			sql = new SecSql();
			sql.append("DELETE FROM article");
			sql.append("WHERE id = ?", id);

			DBUtil.delete(conn, sql);

			System.out.printf("%d번 게시글이 삭제되었습니다.\n", id);

		} else if (cmd.startsWith("article detail")) {

			// 정수이외는 예외처리(정규표현식)
			boolean isInt = cmd.split(" ")[2].matches("-?\\d+");

			if (!isInt) {
				System.out.println("게시글 번호를 숫자로 입력해주세요");
				return 0;
			}

			int id = Integer.parseInt(cmd.split(" ")[2].trim());

			// 해당 게시글이 있는지 확인
			SecSql sql = new SecSql();
			sql.append("SELECT COUNT(*)");
			sql.append("FROM article");
			sql.append("WHERE id = ?", id);

			int foundArticleId = DBUtil.selectRowIntValue(conn, sql);

			if (foundArticleId == 0) {
				System.out.printf("%d번 게시글이 존재하지 않습니다.\n", id);
				return 0;
			}

			// DBUtil 적용
			sql = new SecSql();
			sql.append("SELECT * FROM article");
			sql.append("WHERE id = ?", id);

			Map<String, Object> articleMap = DBUtil.selectRow(conn, sql);
			Article article = new Article(articleMap);

			System.out.printf("== %d번 게시글 상세보기 ==\n", id);
			System.out.printf("번 호 : %d\n", article.id);
			System.out.printf("등록일 : %s\n", article.regDate);
			System.out.printf("수정일 : %s\n", article.updateDate);
			System.out.printf("제 목 : %s\n", article.title);
			System.out.printf("내 용 : %s\n", article.body);

		} else if (cmd.equals("member join")) {

			System.out.println("== 회원가입 ==");

			SecSql sql;

			String loginId;
			String loginPw;
			String loginPwConfirm;
			String name;

			// 입력횟수 제한
			int blockCnt = 0;

			// 아이디 입력
			while (true) {

				if (blockCnt >= 3) {
					System.out.println("입력횟수 초과! 다시 시도해주세요.");
					return 0;
				}

				System.out.print("아이디 : ");
				loginId = sc.nextLine();

				if (loginId.length() == 0) {
					System.out.println("아이디를 입력하세요");
					blockCnt++;
					continue;
				}

				sql = new SecSql();
				sql.append("SELECT COUNT(*)");
				sql.append("FROM `member`");
				sql.append("WHERE loginId = ?", loginId);

				int memberCnt = DBUtil.selectRowIntValue(conn, sql);

				if (memberCnt > 0) {
					System.out.println("이미 존재하는 아이디입니다.");
					blockCnt++;
					continue;
				}
				break;
			}

			blockCnt = 0;

			// 비밀번호 입력
			while (true) {

				if (blockCnt >= 3) {
					System.out.println("입력횟수 초과! 다시 시도해주세요.");
					return 0;
				}

				System.out.print("비밀번호 : ");
				loginPw = sc.nextLine();

				if (loginPw.length() == 0) {
					System.out.println("비밀번호를 입력하세요");
					blockCnt++;
					continue;
				}

				while (true) {

					System.out.print("비밀번호 재입력 : ");
					loginPwConfirm = sc.nextLine();

					if (loginPwConfirm.length() == 0) {
						System.out.println("비밀번호를 입력하세요");
						continue;
					}

					break;
				}

				if (!loginPw.equals(loginPwConfirm)) {
					System.out.println("비밀번호가 일치하지 않습니다.");
					blockCnt++;
					continue;
				}
				break;
			}

			// 닉네임 입력
			while (true) {

				System.out.print("이름 : ");
				name = sc.nextLine();

				if (name.length() == 0) {
					System.out.println("이름를 입력하세요");
					continue;
				}
				break;
			}

			// DBUtil 적용
			sql = new SecSql();
			sql.append("INSERT INTO `member`");
			sql.append("SET regDate = NOW()");
			sql.append(", updateDate = NOW()");
			sql.append(", loginId = ?", loginId);
			sql.append(", loginPw = ?", loginPw);
			sql.append(", name = ?", name);

			DBUtil.insert(conn, sql);

			System.out.printf("%s님 환영합니다!\n", name);

		} else if (cmd.equals("member login")) {

			System.out.println("== 회원 로그인 ==");

			SecSql sql = new SecSql();

			String loginId;
			String loginPw;

			int blockCnt = 0;

			while (true) {

				if (blockCnt >= 3) {
					System.out.println("입력횟수 초과! 다시 시도해주세요.");
					return 0;
				}

				System.out.print("아이디 : ");
				loginId = sc.nextLine();

				if (loginId.length() == 0) {
					System.out.println("아이디를 입력하세요");
					continue;
				}

				sql = new SecSql();
				sql.append("SELECT COUNT(*)");
				sql.append("FROM `member`");
				sql.append("WHERE loginId = ?", loginId);

				int memberCnt = DBUtil.selectRowIntValue(conn, sql);

				if (memberCnt == 0) {
					System.out.println("등록되지 않은 아이디입니다.");
					blockCnt++;
					continue;
				}
				break;
			}

			Member member;
			blockCnt = 0;

			while (true) {

				if (blockCnt >= 3) {
					System.out.println("입력횟수 초과! 다시 시도해주세요.");
					return 0;
				}

				while (true) {

					System.out.print("비밀번호 : ");
					loginPw = sc.nextLine();

					if (loginPw.length() == 0) {
						System.out.println("비밀번호를 입력하세요");
						continue;
					}
					break;
				}

				sql = new SecSql();
				sql.append("SELECT * FROM `member`");
				sql.append("WHERE loginId = ?", loginId);

				Map<String, Object> memberMap = DBUtil.selectRow(conn, sql);
				member = new Member(memberMap);

				if (!member.loginPw.equals(loginPw)) {
					System.out.println("비밀번호가 일치하지 않습니다.");
					blockCnt++;
					continue;
				}
				break;
			}

			// 로그인 처리
			session.loginedMemberId = member.id;
			session.loginedMember = member;
			System.out.printf("%s님 로그인되었습니다.\n", member.name);

		} else {
			System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
		}
		return 0;
	}
}
