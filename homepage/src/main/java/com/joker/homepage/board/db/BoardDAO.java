package com.joker.homepage.board.db;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class BoardDAO {
	
	@Autowired
	SqlSession sqlsession = null;
	
	// 게시판 글 작성
	@Transactional
	public int board_write(BoardDTO board) throws Exception{
		return sqlsession.insert("board.board_write", board);
	}
}