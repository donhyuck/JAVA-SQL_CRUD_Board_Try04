package board;

import java.util.Scanner;

public class boardMain {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		System.out.println("== 프로그램 시작 ==");

		while (true) {
			System.out.print("명령어 : ");
			String cmd = sc.nextLine();

			cmd = cmd.trim();
			
			if (cmd.equals("system exit")) {
				System.out.println("== 프로그램 종료 ==");
				break;
			}

			System.out.println(cmd);
		}

	}

}
