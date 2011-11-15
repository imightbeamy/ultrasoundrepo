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
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.users.UserService;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import edu.umbc.ultra.logic.Comment;
import edu.umbc.ultra.logic.DataEntry;
import edu.umbc.ultra.logic.Patient;
import edu.umbc.ultra.logic.Patient.Gender;
import edu.umbc.ultra.logic.User;

/* Making this class and other controller classes singletons...didn't want to, don't really have a choice */
public class UploadController extends HttpServlet {

	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
    
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {
    	RightsManagementController rightsController = RightsManagementController.getInstance();
        Map<String, BlobKey> blobs = blobstoreService.getUploadedBlobs(req);
        BlobKey blobKey = blobs.get("upload");
    	Patient patient = null;
    	Date DoB = null;
    	String first = req.getParameter("first");
    	String last = req.getParameter("last");
    	try {
    		DateFormat df = new SimpleDateFormat("mm/dd/yyyy");
			DoB = df.parse(req.getParameter("DoB"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			DoB = new Date();
			//e.printStackTrace();
		}
    	Gender gender = Patient.getGenderFromString(req.getParameter("gender"));
    	patient = new Patient(first, last, DoB, gender);
    	//UserService us = UserServiceFactory.getUserService();
    	User user = new User();//rightsController.getUser(us.getCurrentUser().getEmail());
    	ArrayList<Comment> comments = new ArrayList<Comment>();
    	comments.add(new Comment(req.getParameter("complaint"), user));
    	comments.add(new Comment(req.getParameter("reason"), user));
    	comments.add(new Comment(req.getParameter("resInterp"), user));
    	comments.add(new Comment(req.getParameter("attendInterp"), user));
    	DataEntry data = new DataEntry(comments, patient, user, blobKey, null);
    	res.sendRedirect("/viewrecord?entry="+blobKey);
    }

	public UploadController()
	{
		
	}
	
	// Returns NULL upon success, otherwise returns the error heading
	public String UploadEntry(DataEntry entry)
	{
		Entity entity = getEntityFromDataEntry(entry);
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		try
		{
			datastore.put(entity);
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace(System.err);
			return e.getMessage();
		}
		catch(ConcurrentModificationException e)
		{
			e.printStackTrace(System.err);
			return e.getMessage();
		}
		catch(DatastoreFailureException e)
		{
			e.printStackTrace(System.err);
			return e.getMessage();
		}
		
		return null;
	}
	
	
	
	/*
	 * The dbase entity heirarchy is as follows:
	 * 	User Entity 1
	 *   -> Patient Entity
	 *   	 -> DataEntry Entity 1
	 *   	 	 -> Comment Entity 1
	 *   	 	 -> Comment Entity 2
	 *   	 -> DataEntry Entity 2
	 *   etc...
	 *   
	 * The advantage here is that restricting a lower level user to only accessing his/her
	 *  own uploads becomes very simple.
	 *  
	 * Therefore, this method takes in a DataEntry and returns a User entity with associated children.	
	 */
	private Entity getEntityFromDataEntry(DataEntry entry)
	{
		User author = entry.getAuthor();
		Patient patient = entry.getPatient();
		ArrayList<Comment> comments = entry.getComments();
		
		// Create and add Patient entity using as a key the unique id assigned upon creation
		Entity patientEntity = new Entity("Patient", patient.getId(), KeyFactory.createKey("User", entry.getAuthor().getGoogleUser()));
		patientEntity.setProperty("FirstName", patient.getFirstName());
		patientEntity.setProperty("LastName", patient.getLastName());
		patientEntity.setProperty("DOB", patient.getDob());
		patientEntity.setProperty("Gender", patient.getGender());
		patientEntity.setProperty("ID", patient.getId());
		
		// Create and add DataEntry entity with a generated unique key, specifying the parent key as the user
		Entity dataEntity = new Entity("DataEntry", patientEntity.getKey());
		dataEntity.setProperty("timestamp", entry.getTimestamp());
		dataEntity.setProperty("blobKey", entry.getBlobKey());
		dataEntity.setProperty("uniqueID", dataEntity.getKey().hashCode());
		// STUB: No facilitation of blob storage yet
		//Half assed attempt at adding blob storage.
//		Entity blobEntity = new Entity("BlobEntry", dataEntity.getKey());
//		blobEntity.setProperty("Video", lastBlobKey);
		
		// Add each comment using a system generated key for each
		for(Comment comment: comments)
		{
			Entity commentEntity = new Entity("Comment", dataEntity.getKey());
			commentEntity.setProperty("Text", comment.getContent());
			// Author entry consists of user's google account
			commentEntity.setProperty("Author", comment.getAuthor().getGoogleUser());
			commentEntity.setProperty("Timestamp", comment.getTimestamp());
		}
		
		return patientEntity;
	}
	
	/* Shameful shameful singleton code */
	private static UploadController instance;
	
	public static UploadController getInstance()
	{
		if(instance == null)
		{
			instance = new UploadController();
		}
		
		return instance;
	}
}
