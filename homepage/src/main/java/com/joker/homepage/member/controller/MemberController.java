package com.joker.homepage.member.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.joker.homepage.member.db.MemberDTO;
import com.joker.homepage.member.service.MemberServiceImp;

@Controller
@RequestMapping("/member/*")
public class MemberController {
	
	@Autowired
	private MemberServiceImp service;
	
	// 위 내용은 /member/memberJoinForm.do로 요청이 들어오면 /member/memberJoinForm.jsp 파일을 응답해주는 내용
	@RequestMapping(value = "/memberJoinForm.do")
	public String memberJoinForm() throws Exception{
		return "/member/memberJoinForm";
	}
	
	// 아이디 중복 검사(AJAX)
	@RequestMapping(value = "/check_id.do", method = RequestMethod.POST)
	public void check_id(@RequestParam("id") String id, HttpServletResponse response) throws Exception {
		service.check_id(id, response);
	}

	// 이메일 중복 검사(AJAX)
	@RequestMapping(value = "/check_email.do", method = RequestMethod.POST)
	public void check_email(@RequestParam("email") String email, HttpServletResponse response) throws Exception{
		service.check_email(email, response);
	}
	
	// 회원 가입
	@RequestMapping(value = "/join_member.do", method = RequestMethod.POST)
	public String join_member(@ModelAttribute MemberDTO member, RedirectAttributes rttr, HttpServletResponse response) throws Exception{
		rttr.addFlashAttribute("result", service.join_member(member, response));
		return "redircet:./memberJoinForm.do";
	}
	
	// 회원 인증
	@RequestMapping(value="/approval_member.do", method=RequestMethod.POST)
	public void approval_member(@ModelAttribute MemberDTO member, HttpServletResponse response) throws Exception{
		service.approval_member(member, response);
	}
	
	// 로그인 폼 이동
	@RequestMapping(value="/login_form.do", method=RequestMethod.GET)
	public String login_form() throws Exception{
		return "/member/loginForm";
	}	

	// 로그인
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String login(@ModelAttribute MemberDTO member, HttpSession session, HttpServletResponse response) throws Exception{
		member = service.login(member, response);
		session.setAttribute("member", member);
		return "index";
	}
	
	//controller에서  session을 제거(removeAttribute 또는 invalidate) 해주고 위에서 생성한 logout 메서드를 호출
	// 로그아웃
	@RequestMapping(value = "/logout.do", method = RequestMethod.GET)
	public void logout(HttpSession session, HttpServletResponse response) throws Exception{
		session.invalidate();
		//session.removeAttribute("member");
		service.logout(response);
	}
	
	// 아이디 찾기 폼
	@RequestMapping(value = "/find_id_form.do")
	public String find_id_form() throws Exception{
		return "/member/find_id_form";
	}	
	
	// @RequestParam 및 @ModelAttribute 어노테이션 관련 정보
	// https://heavenly-appear.tistory.com/302
	// 아이디 찾기
	@RequestMapping(value = "/find_id.do", method = RequestMethod.POST)
	public String find_id(HttpServletResponse response, @RequestParam("email") String email, Model md) throws Exception{
		md.addAttribute("id", service.find_id(response, email));
		return "/member/find_id";
	}
}
