package com.joker.homepage.board.controller;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.joker.homepage.board.db.BoardDTO;
import com.joker.homepage.board.service.BoardServiceImp;

@Controller // 현재 클래스를 컨트롤러 빈으로 등록
@RequestMapping("/board/*")
public class BoardController {
	
	// 의존관계 주입 = BoardService생성
	@Inject
	private BoardServiceImp service;
	
	// 게시판 목록 페이지 이동(페이징 관련 내용은 추후 구현)
	@RequestMapping(value="/board_list.do")
	public String board_list() throws Exception{
		return "/board/board_list";
	}
	
	// 게시판 글 작성 폼 이동
	@RequestMapping(value="/board_write_form.do")
	public String board_write_form() throws Exception{
		return "/board/board_write_form";
	}
	
	// 게시판 글 작성
	@RequestMapping(value="/board_write.do", method = RequestMethod.POST)
	public String board_write(@ModelAttribute BoardDTO board) throws Exception{
		service.board_write(board);
		return "redirect:/board/board_list.do";
	}
}
