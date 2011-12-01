package edu.umbc.ultra.logic;

import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;

public class DataEntry {
	private User author;
	private Date timestamp;
	private ArrayList<Comment> comments;
	private Patient patient;
	private BlobKey videoKey;
	private String entryKey;

	public DataEntry(ArrayList<Comment> comments, Patient patient, User author,
					 BlobKey blobKey, String entryKey) {
		this.comments = comments;
		this.patient = patient;
		this.author = author;
		this.timestamp = new Date();
		this.entryKey = entryKey;
		this.videoKey = blobKey;
	}

	public DataEntry(ArrayList<Comment> comments, Patient patient, User author,
					 BlobKey blobKey, String entrykey, Date timestamp) {
		this.author = author;
		this.timestamp = timestamp;
		this.comments = comments;
		this.patient = patient;
		this.videoKey = blobKey;
		this.entryKey = entrykey;
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
		return videoKey;
	}

	public String getKey() {
		return entryKey;
	}
}
