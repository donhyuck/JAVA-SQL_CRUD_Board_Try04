package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.Scanner;

import board.controller.ArticleController;
import board.controller.MemberController;
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

		ArticleController articleController = new ArticleController(conn, sc, cmd, session);
		MemberController membereController = new MemberController(conn, sc, cmd, session);

		if (cmd.equals("system exit")) {
			System.out.println("== 프로그램 종료 ==");
			return -1;

		} else if (cmd.equals("article write")) {

			articleController.doWrite();

		} else if (cmd.equals("article list")) {

			articleController.showList();

		} else if (cmd.startsWith("article modify")) {

			articleController.doModify();

		} else if (cmd.startsWith("article delete")) {

			articleController.doDelete();

		} else if (cmd.startsWith("article detail")) {

			articleController.showDetail();

		} else if (cmd.equals("member join")) {

			membereController.doJoin();

		} else if (cmd.equals("member login")) {

			membereController.doLogin();

		} else if (cmd.equals("member logout")) {

			membereController.dologout();

		} else if (cmd.equals("member whoami")) {

			membereController.ShowWhoAmI();

		} else {
			System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
		}
		return 0;
	}
}
