package edu.umbc.ultra.dbase;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreFailureException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.KeyFactory;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import edu.umbc.ultra.logic.Comment;
import edu.umbc.ultra.logic.DataEntry;
import edu.umbc.ultra.logic.Patient;
import edu.umbc.ultra.logic.Patient.Gender;
import edu.umbc.ultra.logic.User;

/* Making this class and other controller classes singletons...didn't want to, don't really have a choice */
public class UploadController extends HttpServlet {

	private static int MIN_KEYWORD_LENGTH = 4;

	public UploadController() {

	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		RightsManagementController rightsController = RightsManagementController
				.getInstance();
		BlobstoreService blobstoreService = BlobstoreServiceFactory
				.getBlobstoreService();
		Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
		BlobKey blobKey = blobs.get("upload");
		if (blobKey == null) {
			redirect(res, blobKey);
			return;
		}

		Patient patient = null;
		Date DoB = null;
		String first = req.getParameter("first");

		// Makes sure there is a valid value in patient first name.
		if ((first == null) || (first.length() == 0)) {
			redirect(res, blobKey);
			return;
		}
		String last = req.getParameter("last");

		// Makes sure there is a valid value in patient last name.
		if ((last == null) || (last.length() == 0)) {
			redirect(res, blobKey);
			return;
		}
		try {
			DateFormat df = new SimpleDateFormat("mm/dd/yyyy");

			// Make sure there is a value in the DoB field before trying to
			// parse it.
			if ((req.getParameter("DoB") == null)
					|| (req.getParameter("DoB").length() == 0)) {
				redirect(res, blobKey);
				return;
			}
			else {
				DoB = df.parse(req.getParameter("DoB"));
			}
		}
		catch (ParseException e) {
			DoB = new Date();
		}
		Gender gender = Patient.getGenderFromString(req.getParameter("gender"));
		patient = new Patient(first, last, DoB, gender);
		// UserService us = UserServiceFactory.getUserService();

		// Just setting the user to test for testing purposes.
		// TODO: Get the actual current user.
		User user = rightsController.getUser("test@example.com");
		ArrayList<Comment> comments = new ArrayList<Comment>();

		// Makes sure there is a value in complaint.
		if ((req.getParameter("complaint") == null)
				|| (req.getParameter("complaint").length() == 0)) {
			redirect(res, blobKey);
			return;
		}
		else {
			comments.add(new Comment(req.getParameter("complaint"), user));
		}

		// Makes sure there is a value in reason.
		if ((req.getParameter("reason") == null)
				|| (req.getParameter("reason").length() == 0)) {
			redirect(res, blobKey);
			return;
		}
		else {
			comments.add(new Comment(req.getParameter("reason"), user));
		}
		String rInterp = req.getParameter("resInterp");
		String aInterp = req.getParameter("attendInterp");

		// Tests to make sure that there is an entry in the Resident or
		// Attending physician's interpretation textarea.
		if (((aInterp == null) || (aInterp.length() == 0))
				&& ((rInterp == null) || (rInterp.length() == 0))) {
			redirect(res, blobKey);
			return;
		}
		else if ((aInterp == null) || (aInterp.length() == 0)) {
			aInterp = "No Attending Physician interpretation.";
		}
		else if ((rInterp == null) || (rInterp.length() == 0)) {
			rInterp = "No Resident Physician interpretation.";
		}

		comments.add(new Comment(req.getParameter("resInterp"), user));
		comments.add(new Comment(req.getParameter("attendInterp"), user));
		DataEntry data = new DataEntry(comments, patient, user, blobKey, null);
		UploadEntry(data);
		res.sendRedirect("/viewrecord?entry=" + data.getKey());
	}

	// Returns NULL upon success, otherwise returns the error heading
	public String UploadEntry(DataEntry entry) {
		// Get a datastore entity populated with the data entry to upload
		Entity entity = getEntityFromDataEntry(entry);

		// Get an instance of the data store controller
		DatastoreService datastore = DatastoreServiceFactory
				.getDatastoreService();
		try {
			// Attempt to upload the entry
			datastore.put(entity);
		}
		catch (IllegalArgumentException e) {
			e.printStackTrace(System.err);
			return e.getMessage();
		}
		catch (ConcurrentModificationException e) {
			e.printStackTrace(System.err);
			return e.getMessage();
		}
		catch (DatastoreFailureException e) {
			e.printStackTrace(System.err);
			return e.getMessage();
		}

		return null;
	}

	/*
	 * The dbase entity heirarchy is as follows: User Entity 1 -> Patient Entity
	 * -> DataEntry Entity 1 -> Comment Entity 1 -> Comment Entity 2 ->
	 * DataEntry Entity 2 etc...
	 * 
	 * The advantage here is that restricting a lower level user to only
	 * accessing his/her own uploads becomes very simple.
	 * 
	 * Therefore, this method takes in a DataEntry and returns a User entity
	 * with associated children.
	 */
	private Entity getEntityFromDataEntry(DataEntry entry) {
		User author = entry.getAuthor();
		Patient patient = entry.getPatient();
		ArrayList<Comment> comments = entry.getComments();

		// Create and add Patient entity using as a key the unique id assigned
		// upon creation
		Entity patientEntity = new Entity("Patient", patient.getId(),
				KeyFactory.createKey("User", entry.getAuthor().getGoogleUser()));
		patientEntity.setProperty("FirstName", patient.getFirstName());
		patientEntity.setProperty("LastName", patient.getLastName());
		patientEntity.setProperty("DOB", patient.getDob());
		patientEntity.setProperty("Gender",
				Patient.getGenderAsString(patient.getGender()));
		patientEntity.setProperty("ID", patient.getId());

		// Create and add DataEntry entity with a generated unique key,
		// specifying the parent key as the user
		Entity dataEntity = new Entity("DataEntry", patientEntity.getKey());
		dataEntity.setProperty("timestamp", entry.getTimestamp());
		dataEntity.setProperty("blobKey", entry.getBlobKey());
		dataEntity.setProperty("uniqueID", dataEntity.getKey().hashCode());

		// Add each comment using a system generated key for each
		for (int i = 0; i < comments.size(); i++) {
			Entity commentEntity = new Entity("Comment", dataEntity.getKey());
			commentEntity.setProperty("Text", comments.get(i).getContent());
			// Author entry consists of user's google account
			commentEntity.setProperty("Author", comments.get(i).getAuthor()
					.getGoogleUser());
			commentEntity.setProperty("Timestamp", comments.get(i)
					.getTimestamp());
			ArrayList<String> keywords = extractKeywords(comments.get(i)
					.getContent());
			for (String keyword : keywords) {
				Entity kwEntity = new Entity("Keyword", commentEntity.getKey());
				kwEntity.setProperty("Word", keyword);
				// If this is a chief complaint (first comment), set it as such
				if (i == 0) {
					kwEntity.setProperty("Type", "CC");
				} else {
					kwEntity.setProperty("Type", "KW");
				}
				System.out.println("Adding keyword \"" + keyword + "\"");
			}
		}

		return patientEntity;
	}

	private ArrayList<String> extractKeywords(String commentText) {
		ArrayList<String> results = new ArrayList<String>();

		String[] words = commentText.split("\\s+");
		for (String word : words) {
			if (word.length() >= MIN_KEYWORD_LENGTH) {
				results.add(word.toLowerCase());
			}
		}

		return results;
	}

	private void redirect(HttpServletResponse res, BlobKey blobKey)
			throws IOException {
		BlobstoreService blobstoreService = BlobstoreServiceFactory
				.getBlobstoreService();
		if (blobKey != null)
			blobstoreService.delete(blobKey);
		res.sendRedirect("/upload");
	}

	/* Shameful shameful singleton code */
	private static UploadController instance;

	public static UploadController getInstance() {
		if (instance == null) {
			instance = new UploadController();
		}

		return instance;
	}
}
