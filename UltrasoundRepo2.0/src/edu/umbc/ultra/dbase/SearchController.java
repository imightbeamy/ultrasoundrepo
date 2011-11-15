package edu.umbc.ultra.dbase;

import java.util.ArrayList;
import java.util.Date;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;

import edu.umbc.ultra.logic.Comment;
import edu.umbc.ultra.logic.DataEntry;
import edu.umbc.ultra.logic.Patient;
import edu.umbc.ultra.logic.Patient.Gender;
import edu.umbc.ultra.logic.User;

public class SearchController
{
	public SearchController()
	{
		
	}
	
	public ArrayList<DataEntry> searchForEntries(String firstName, String lastName, Gender gender, String chiefComplaint, String keywords, String userEmail)
	{
		Query query = new Query("DataEntry");
		if(userEmail != null)
		{
			query.setAncestor(KeyFactory.createKey("User", userEmail));
		}
		
		
		return null;
	}

	//Gender being null means search for any gender
	public ArrayList<DataEntry> searchForEntriesTEST(String firstName, String lastName, Gender gender, String chiefComplaint, String keywords, String userEmail)
	{
		ArrayList<DataEntry> results = new ArrayList<DataEntry>();
		ArrayList<Comment> comments = new ArrayList<Comment>();
		User user = new User();
		//Patient newPatient = new Patient("Nathan", "Broemm", new Date(), Gender.MALE, "nab0415");
		Comment testComment = new Comment("Turn the Computer off and on!", user);
		comments.add(testComment);
		//results.add(new DataEntry(comments, newPatient, new User()));
		return results;
	}
	
	/* Shameful shameful singleton code */
	private static SearchController instance;
	
	public static SearchController getInstance()
	{
		if(instance == null)
		{
			instance = new SearchController();
		}
		
		return instance;
	}
}
