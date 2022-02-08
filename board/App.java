package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

import board.controller.ArticleController;
import board.controller.Controller;
import board.controller.MemberController;
import board.session.Session;

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
				} else if (cmd.equals("system exit")) {
					System.out.println("== 프로그램 종료 ==");
					break;
				}

				// 프론트 컨트롤러 구현
				String[] cmdBits = cmd.split(" ");

				if (cmdBits.length < 2) {
					System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
					continue;
				}

				String controllerName = cmdBits[0];
				Controller controller = null;

				ArticleController articleController = new ArticleController(conn, sc, cmd, session);
				MemberController memberController = new MemberController(conn, sc, cmd, session);

				if (controllerName.equals("article")) {
					controller = articleController;
				} else if (controllerName.equals("member")) {
					controller = memberController;
				} else {
					System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
					continue;
				}

				// 해당 컨트롤러로 명령수행
				controller.doAction();
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
}
