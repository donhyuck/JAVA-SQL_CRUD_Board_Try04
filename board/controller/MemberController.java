package board.controller;

import java.sql.Connection;
import java.util.Map;
import java.util.Scanner;

import board.Member;
import board.service.MemberService;
import board.session.Session;
import board.util.DBUtil;
import board.util.SecSql;

public class MemberController {

	Connection conn;
	Scanner sc;
	String cmd;
	Session session;

	MemberService memberService;

	public MemberController(Connection conn, Scanner sc, String cmd, Session session) {
		this.conn = conn;
		this.sc = sc;
		this.cmd = cmd;
		this.session = session;

		memberService = new MemberService(conn);
	}

	public void doJoin() {

		System.out.println("== 회원가입 ==");

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
				return;
			}

			System.out.print("아이디 : ");
			loginId = sc.nextLine();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력하세요");
				blockCnt++;
				continue;
			}

			int memberCnt = memberService.getMemberCntByLoginId(loginId);

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
				return;
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

		memberService.doJoin(loginId, loginPw, name);

		System.out.printf("%s님 환영합니다!\n", name);
	}

	public void doLogin() {

		System.out.println("== 회원 로그인 ==");

		SecSql sql = new SecSql();

		String loginId;
		String loginPw;

		int blockCnt = 0;

		while (true) {

			if (blockCnt >= 3) {
				System.out.println("입력횟수 초과! 다시 시도해주세요.");
				return;
			}

			System.out.print("아이디 : ");
			loginId = sc.nextLine();

			if (loginId.length() == 0) {
				System.out.println("아이디를 입력하세요");
				continue;
			}

			int memberCnt = memberService.getMemberCntByLoginId(loginId);

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
				return;
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

			member = memberService.getMemberByLoginId(loginId);

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
	}

	public void dologout() {

		if (session.loginedMember == null) {
			System.out.println("현재 로그아웃 상태입니다.");
			return;
		}

		System.out.printf("%s님 로그아웃되었습니다.\n", session.loginedMember.name);
		session.loginedMember = null;
		session.loginedMemberId = -1;
	}

	public void ShowWhoAmI() {

		if (session.loginedMember == null) {
			System.out.println("현재 로그아웃 상태입니다.");
			return;
		}

		System.out.println("== 회원정보 조회 ==");
		System.out.printf("아이디 : %s\n", session.loginedMember.loginId);
		System.out.printf("이 름 : %s\n", session.loginedMember.name);

	}

}
