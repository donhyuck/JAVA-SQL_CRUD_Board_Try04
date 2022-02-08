package board.service;

import java.sql.Connection;

import board.dao.MemberDao;
import board.dto.Member;

public class MemberService {

	MemberDao memberDao;

	public MemberService(Connection conn) {
		memberDao = new MemberDao(conn);
	}

	public int getMemberCntByLoginId(String loginId) {
		return memberDao.getMemberCntByLoginId(loginId);
	}

	public void doJoin(String loginId, String loginPw, String name) {
		memberDao.doJoin(loginId, loginPw, name);
	}

	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}
}
