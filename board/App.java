package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {

	public void start() {

		Scanner sc = new Scanner(System.in);

		// JDBC적용
		Connection conn = null; // DB 접속 객체
		PreparedStatement pstat = null; // SQL 구문을 실행하는 역할

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

				int actionResult = doAction(conn, sc, cmd, pstat);

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

			try {
				if (pstat != null && !pstat.isClosed()) {
					pstat.close(); // 연결 종료
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	private int doAction(Connection conn, Scanner sc, String cmd, PreparedStatement pstat) {

		if (cmd.equals("system exit")) {
			System.out.println("== 프로그램 종료 ==");
			return -1;

		} else if (cmd.equals("article write")) {

			System.out.println("== 게시글 작성 ==");

			System.out.print("제목 : ");
			String title = sc.nextLine();
			System.out.print("내용 : ");
			String body = sc.nextLine();

			// JDBC적용
			try {

				String sql = "INSERT INTO article";
				sql += " SET regDate = NOW()";
				sql += ", updateDate = NOW()";
				sql += ", title = \"" + title + "\"";
				sql += ", body = \"" + body + "\"";

				pstat = conn.prepareStatement(sql);
				int affectedRows = pstat.executeUpdate();

				System.out.printf("게시글이 등록되었습니다.\n");

			} catch (Exception e) {
				System.out.println("작업수행 중 오류발생");
			}

		} else if (cmd.equals("article list")) {

			System.out.println("== 게시글 목록 ==");
			List<Article> articles = new ArrayList<>();

			// JDBC적용
			ResultSet rs = null; // Resultset은 executeQuery의 결과값을 저장, next함수를 통해 데이터를 참조

			try {
				String sql = "SELECT * FROM article";
				sql += " ORDER BY id DESC";

				pstat = conn.prepareStatement(sql);
				rs = pstat.executeQuery(sql);

				while (rs.next()) {

					int id = rs.getInt("id");
					String regDate = rs.getString("regDate");
					String updateDate = rs.getString("updateDate");
					String title = rs.getString("title");
					String body = rs.getString("body");

					Article article = new Article(id, regDate, updateDate, title, body);
					articles.add(article);

				}

				if (articles.size() == 0) {
					System.out.println("게시글이 존재하지 않습니다.");
					return 0;
				}

				System.out.println("번호 / 제목 ");
				for (Article article : articles) {
					System.out.printf(" %d / %s \n", article.id, article.title);
				}

			} catch (Exception e) {
				System.out.println("작업수행 중 오류발생");
			}

		} else if (cmd.startsWith("article modify")) {

			System.out.println("== 게시글 수정 ==");

			int id = Integer.parseInt(cmd.split(" ")[2].trim());
			System.out.print("새 제목 : ");
			String title = sc.nextLine();
			System.out.print("새 내용 : ");
			String body = sc.nextLine();

			// JDBC적용
			try {
				String sql = "UPDATE article";
				sql += " SET updateDate = NOW()";
				sql += ", title = \"" + title + "\"";
				sql += ", body = \"" + body + "\"";
				sql += "WHERE id =" + id;

				pstat = conn.prepareStatement(sql);
				pstat.executeUpdate();

				System.out.printf("%d번 게시글이 수정되었습니다.\n", id);
			} catch (Exception e) {
				System.out.println("작업수행 중 오류발생");
			}

		} else {
			System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
		}
		return 0;
	}
}
