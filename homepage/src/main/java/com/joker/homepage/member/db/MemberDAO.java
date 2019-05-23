package com.joker.homepage.member.db;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

// 해당 클래스가 데이터베이스에 접근하는 클래스임을 명시
// DAO 클래스는 데이터베이스에 엑세스 하는 클래스로, DML을 처리하며 불필요한 연산인 커넥션을 대체함
@Repository
public class MemberDAO {
	
	// 스프링이 빈의 요구 사항과 매칭 되는 애플리케이션 컨텍스트상에서 다른 빈을 찾아 빈 간의 의존성을 자동으로 만족시키도록 하는 수단임
	@Autowired
	SqlSession sqlsession = null;

	// 아이디 중복 검사
	public int check_id(String id) throws Exception{
		return sqlsession.selectOne("member.check_id", id);
	}

	// 이메일 중복 검사
	public int check_email(String email) throws Exception{
		return sqlsession.selectOne("member.check_email", email);
	}
	
	// 회원가입
	// 이 메서드에 트랜잭션 기능이 적용된 프록시 객체가 생성됨(디자인 패턴-프록시 패턴 참고)
	// 위 메서드가 호출될 경우 PlatformTransactionManager을 사용해 트랜잭션 시작, 정상 여부에 따라 commit or Rollback
	@Transactional
	public int join_member(MemberDTO member) throws Exception{
		return sqlsession.insert("member.join_member", member);
	}
	
	// 이메일 인증
	@Transactional
	public int approval_member(MemberDTO member) throws Exception{
		return sqlsession.update("member.approval_member", member);
	}
	
	// 로그인 검사
	public MemberDTO login(String id) throws Exception{
		return sqlsession.selectOne("member.login", id);
	}
	
	// 로그인 접속일자 변경
	@Transactional
	public int update_log(String id) throws Exception{
		return sqlsession.update("member.update_log", id);
	}
}
