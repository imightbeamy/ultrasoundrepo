package edu.umbc.ultra.logic;

import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;

public class DataEntry {
	private User author;
	private Date timestamp;
	private ArrayList<Comment> comments;
	private Patient patient;
	private BlobKey key;
	private String entrykey;

	public DataEntry(ArrayList<Comment> comments, Patient patient, User author,
			BlobKey key, String entryKey) {
		this.comments = comments;
		this.patient = patient;
		this.author = author;
		this.timestamp = new Date();
		this.entrykey = entryKey;
		this.key = key;
	}

	public DataEntry(ArrayList<Comment> comments, Patient patient, User author,
			BlobKey blobKey, String entrykey, Date timestamp) {
		super();
		this.author = author;
		this.timestamp = timestamp;
		this.comments = comments;
		this.patient = patient;
		this.key = blobKey;
		this.entrykey = entrykey;
	}

	public User getAuthor() {
		return author;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public Patient getPatient() {
		return patient;
	}

	public BlobKey getBlobKey() {
		return key;
	}

	public String getKey() {
		return entrykey;
	}
}
