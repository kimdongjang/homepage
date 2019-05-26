package com.joker.homepage.board.db;

import java.sql.Date;

public class BoardDTO {
	private int bod_no;
	private String id;
	private String subject;
	private String content;
	private Date write_date;
	private int read_count;
	public int getBod_no() {
		return bod_no;
	}
	public void setBod_no(int bod_no) {
		this.bod_no = bod_no;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getWrite_date() {
		return write_date;
	}
	public void setWrite_date(Date write_date) {
		this.write_date = write_date;
	}
	public int getRead_count() {
		return read_count;
	}
	public void setRead_count(int read_count) {
		this.read_count = read_count;
	}
	public int getRec_count() {
		return rec_count;
	}
	public void setRec_count(int rec_count) {
		this.rec_count = rec_count;
	}
	public int getComt_count() {
		return comt_count;
	}
	public void setComt_count(int comt_count) {
		this.comt_count = comt_count;
	}
	private int rec_count;
	private int comt_count;
}
