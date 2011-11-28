package edu.umbc.ultra.logic;

import java.util.Calendar;
import java.util.Date;

public class Comment {
	private String content;
	private Date timestamp;
	private User author;

	public Comment(String content, User author) {
		this.content = content;
		this.timestamp = Calendar.getInstance().getTime();
		this.author = author;
	}

	public Comment(String content, Date timestamp, User author) {
		this.content = content;
		this.timestamp = timestamp;
		this.author = author;
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
}
