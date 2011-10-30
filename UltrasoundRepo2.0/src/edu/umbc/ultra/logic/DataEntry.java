package edu.umbc.ultra.logic;

import java.util.ArrayList;
import java.util.Date;

public class DataEntry
{
	private User author;
	private Date timestamp;
	private ArrayList<Comment> comments;
	private Patient patient;
	
	public User getAuthor()
	{
		return author;
	}
	public Date getTimestamp()
	{
		return timestamp;
	}
	public ArrayList<Comment> getComments()
	{
		return comments;
	}
	public Patient getPatient()
	{
		return patient;
	}
}
