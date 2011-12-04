package edu.umbc.ultra.logic;

import java.util.Date;

public class Comment {
	private String content;
	private Date timestamp;
	private User author;
	private String title;
	
	public Comment(String content, User author, String title) {
		this.content = content;
		this.timestamp = new Date();
		this.author = author;
		this.title = title;
	}

	public Comment(String content, Date timestamp, User author, String title) {
		this.content = content;
		this.timestamp = timestamp;
		this.author = author;
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
	
	public String getContent() {
		return content;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public User getAuthor() {
		return author;
	}
	
	public String toString() {
		
		return "content:" + content + "\nauthor:" + author + "\ntitle:" + title;
	}
}
