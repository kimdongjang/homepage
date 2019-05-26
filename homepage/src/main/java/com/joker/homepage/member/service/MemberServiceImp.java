package com.joker.homepage.member.service;

import java.io.PrintWriter;
import java.util.Random;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.HtmlEmail;
import org.springframework.stereotype.Service;

import com.joker.homepage.member.db.MemberDTO;
import com.joker.homepage.member.db.MemberDAO;

//Repository를 통해 데이터베이스에서 데이터를 가져온 후 컨트롤러에게 전달해 주는 클래스임을 명시.
@Service
public class MemberServiceImp implements MemberService {
	
	@Inject
	private MemberDAO manager;
	
	// 인터페이스 구현
	// 아이디 중복 검사(AJAX)
	@Override
	public void check_id(String id, HttpServletResponse response) throws Exception{
		PrintWriter out = response.getWriter();
		out.println(manager.check_id(id));
		out.close();
	}

	// 이메일 중복 검사(AJAX)
	@Override
	public void check_email(String email, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		out.println(manager.check_email(email));
		out.close();
	}


	// 회원가입
	@Override
	public int join_member(MemberDTO member, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();

		if (manager.check_id(member.getId()) == 1) {
			out.println("<script>");
			out.println("alert('동일한 아이디가 있습니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return 0;
		} else if (manager.check_email(member.getEmail()) == 1) {
			out.println("<script>");
			out.println("alert('동일한 이메일이 있습니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return 0;
		} else {
			// 인증키 set
            System.out.println("\n >>>>>>>>> 회원가입 시도중 \n");
			member.setApproval_key(create_key());
			manager.join_member(member);
			// 인증 메일 발송
			send_mail(member, "join");
			out.println("<script>");
			out.println("alert('회원가입 메일이 전송되었습니다!');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return 1;
		}
	}
	
	@Override
	public String create_key() throws Exception{
		String key = "";
		Random rd = new Random();
		
		for(int i = 0; i<4; i++) {
			key += rd.nextInt(10);
		}
		return key;
	}
	
	
	// 회원가입시 이메일 전송
	@Override
	public void send_mail(MemberDTO member, String div) throws Exception{
		// Mail Server 설정
		String charSet= "utf-8";
		String hostSMTP = "smtp.naver.com";
		String hostSMTPid = "naru_oo";
		String hostSMTPpwd = "exl2y79112t!";
		int SMTPPort = 465;
		int SSLPort = 587;
		
		// 보내는 사람 EMail, 제목, 내용
		String fromEmail = member.getEmail();
		String fromName = "Spring Homepage";
		String subject = "";
		String msg = "";
		// 회원가입일 경우
		if(div.equals("join")) {
	        System.out.println("\n >>>>>>>>> 보내는 메일 "+ member.getEmail() +" \n");
			// 회원가입 메일 내용
			subject = "Spring Homepage 회원가입 인증 메일입니다.";
			msg += "<div align='center' style='border:1px solid black; font-family:verdana'>";
			msg += "<h3 style='color: blue;'>";
			msg += member.getId() + "님 회원가입을 환영합니다.</h3>";
			msg += "<div style='font-size: 130%'>";
			msg += "하단의 인증 버튼 클릭 시 정상적으로 회원가입이 완료됩니다.</div><br/>";
			msg += "<form method='post' action='http://localhost:8080/homepage/member/approval_member.do'>";
			msg += "<input type='hidden' name='email' value='" + member.getEmail() + "'>";
			msg += "<input type='hidden' name='approval_key' value='" + member.getApproval_key() + "'>";
			msg += "<input type='submit' value='인증'></form><br/></div>";			
		}
		else if(div.equals("find_pw")) {
			subject = "Spring Homepage 임시 비밀번호";
			msg += "<div align='center' style='border:1px solid black; font-family:verdana'>";
			msg += "<h3 style='color: blue;'>";
			msg += member.getId() + "님의 임시 비밀번호 입니다.</h3>";
			msg += "<p>임시 비밀번호 : ";
			msg += member.getPw() + "</p></div>";	
		}

		// 받는 사람 E-Mail 주소
		String mail = member.getEmail();
		try {
			HtmlEmail email = new HtmlEmail();
			email.setDebug(true);
			email.setCharset(charSet);
			email.setSSLOnConnect(true);
			email.setHostName(hostSMTP);
			email.setSmtpPort(SSLPort);

			email.setAuthentication(hostSMTPid, hostSMTPpwd);
			email.setStartTLSEnabled(true);
			email.addTo(mail, charSet);
			email.setFrom(fromEmail, fromName, charSet);
			email.setSubject(subject);
			email.setHtmlMsg(msg);
			email.send();
		} catch (Exception e) {
			System.out.println("메일발송 실패 : " + e);
		}
	}
	
	// 이메일 인증
	@Override
	public void approval_member(MemberDTO member, HttpServletResponse response) throws Exception{
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// 이메일 인증에 실패했을 경우
		if(manager.approval_member(member) == 0) {
			out.println("<script>");
			out.println("alert('잘못된 접근입니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
		}
		// 이메일 인증에 성공했을 경우
		else{
			out.println("<script>");
			out.println("alert('인증이 완료되었습니다. 로그인 후 이용하세요.');");
			out.println("location.href='../index.jsp';");
			out.println("</script>");
			out.close();
		}
	}
	
	// 로그인
	@Override
	public MemberDTO login(MemberDTO member, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// 등록된 아이디가 없으면
		if(manager.check_id(member.getId()) == 0) {
			out.println("<script>");
			out.println("alert('등록된 아이디가 없습니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return null;
		} else {
			String pw = member.getPw();
			member = manager.login(member.getId());
			// 비밀번호가 다를 경우
			if(!member.getPw().equals(pw)) {
				out.println("<script>");
				out.println("alert('비밀번호가 다릅니다.');");
				out.println("history.go(-1);");
				out.println("</script>");
				out.close();
				return null;
			// 이메일 인증을 하지 않은 경우
			}else if(!member.getApproval_status().equals("true")) {
				out.println("<script>");
				out.println("alert('이메일 인증 후 로그인 하세요.');");
				out.println("history.go(-1);");
				out.println("</script>");
				out.close();
				return null;
            // 로그인 일자 업데이트 및 회원정보 리턴			
			}else {
				manager.update_log(member.getId());
				return member;
			}
		}
	}
	
	// 로그아웃
	@Override
	public void logout(HttpServletResponse response) throws Exception{
        System.out.println("\n >>>>>>>>> 로그아웃 시도중 \n");
		response.setContentType("text/html;charset=utf-8"); // 응답받은 객체의 캐릭터형 지정
		PrintWriter out = response.getWriter();
		out.println("<script>");
		out.println("location.href=document.referrer;");
		out.println("</script>");
		out.close();
	}
	
	// 아이디 찾기
	@Override
	public String find_id(HttpServletResponse response, String email) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		String id = manager.find_id(email); // 데이터베이스를 통해 이메일과 일치하는 id를 반환해서 리턴해줌 => jsp '#id' 선택자에 반영
		
		if (id == null) {
			out.println("<script>");
			out.println("alert('가입된 아이디가 없습니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return null;
		} else {
			return id;
		}
	}
	
	// 비밀번호 찾기
	@Override
	public void find_pw(HttpServletResponse response, MemberDTO member) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		// 아이디가 없으면
		if(manager.check_id(member.getId()) == 0) {
			out.print("아이디가 없습니다.");
			out.close();
		}
		// 가입에 사용한 이메일이 아니면
		else if(!member.getEmail().equals(manager.login(member.getId()).getEmail())) {
			out.print("잘못된 이메일 입니다.");
			out.close();
		}else {
			// 임시 비밀번호 생성
			String pw = "";
			for (int i = 0; i < 12; i++) {
				pw += (char) ((Math.random() * 26) + 97);
			}
			member.setPw(pw);
			// 비밀번호 변경
			manager.update_pw(member);
			// 비밀번호 변경 메일 발송
			send_mail(member, "find_pw");
			
			out.print("이메일로 임시 비밀번호를 발송하였습니다.");
			out.close();
		}
	}
	
	// 회원정보 수정
	@Override
	public MemberDTO update_mypage(MemberDTO member) throws Exception {
		manager.update_mypage(member);
		return manager.login(member.getId());
	}
	
	// 비밀번호 변경
	@Override
	public MemberDTO update_pw(MemberDTO member, String old_pw, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		if(!old_pw.equals(manager.login(member.getId()).getPw())) {
			out.println("<script>");
			out.println("alert('기존 비밀번호가 다릅니다.');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return null;
		}else {
			manager.update_pw(member);
			return manager.login(member.getId());
		}
	}
	
	// 회원탈퇴
	@Override
	public boolean withdrawal(MemberDTO member, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		if(manager.withdrawal(member) != 1) {
			out.println("<script>");
			out.println("alert('회원탈퇴 실패');");
			out.println("history.go(-1);");
			out.println("</script>");
			out.close();
			return false;
		}else {
			return true;
		}
	}
}
