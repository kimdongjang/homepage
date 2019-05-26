package com.joker.homepage.board.service;

import com.joker.homepage.board.db.BoardDTO;

public interface BoardService {
	public int board_write(BoardDTO board) throws Exception;
}
