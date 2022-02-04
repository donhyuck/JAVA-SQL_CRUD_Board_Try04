package board;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BoardMain {

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		List<Article> articles = new ArrayList<>();

		System.out.println("== 프로그램 시작 ==");

		int lastArticleId = 1;

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

				int id = lastArticleId;
				System.out.print("제목 : ");
				String title = sc.nextLine();
				System.out.print("내용 : ");
				String body = sc.nextLine();

				Article article = new Article(id, title, body);
				articles.add(article);

				System.out.printf("%d번 게시글이 등록되었습니다.\n", id);
				lastArticleId++;

			} else {
				System.out.printf("%s는 잘못된 명령어입니다.\n", cmd);
			}
		}
	}
}
