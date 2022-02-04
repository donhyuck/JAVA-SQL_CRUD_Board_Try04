package board;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class BoardMain {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.println("== 프로그램 시작 ==");

		while (true) {

			System.out.print("명령어 : ");
			String cmd = sc.nextLine();

			cmd = cmd.trim();

			if (cmd.length() == 0) {
				continue;
			}

			if (cmd.equals("system exit")) {
				System.out.println("== 프로그램 종료 ==");
				break;

			} else if (cmd.equals("article write")) {

				System.out.println("== 게시글 작성 ==");

				System.out.print("제목 : ");
				String title = sc.nextLine();
				System.out.print("내용 : ");
				String body = sc.nextLine();

				// JDBC적용
				Connection conn = null; // DB 접속 객체
				PreparedStatement pstat = null; // SQL 구문을 실행하는 역할

				try {
					Class.forName("com.mysql.cj.jdbc.Driver"); // Mysql JDBC 드라이버 로딩
					String url = "jdbc:mysql://127.0.0.1:3306/DB_board?useUnicode=true&characterEncoding=utf8&autoReconnect=true&serverTimezone=Asia/Seoul&useOldAliasMetadataBehavior=true&zeroDateTimeNehavior=convertToNull";

					conn = DriverManager.getConnection(url, "root", "");

					String sql = "INSERT INTO article";
					sql += " SET regDate = NOW()";
					sql += ", updateDate = NOW()";
					sql += ", title = \"" + title + "\"";
					sql += ", body = \"" + body + "\"";

					pstat = conn.prepareStatement(sql);
					int affectedRows = pstat.executeUpdate();

					System.out.printf("게시글이 등록되었습니다.\n");

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

			} else {
				System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
			}
		}
	}
}
