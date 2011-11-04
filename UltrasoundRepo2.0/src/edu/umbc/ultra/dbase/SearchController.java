package edu.umbc.ultra.dbase;

import java.util.ArrayList;
import java.util.Date;

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
		
		return null;
	}

	public ArrayList<DataEntry> searchForEntriesTEST(String firstName, String lastName, Gender gender, String chiefComplaint, String keywords, String userEmail)
	{
//		ArrayList<DataEntry> results = new ArrayList<DataEntry>();
//		results.add(new DataEntry(ArrayList<Comment> comments, 
//				Patient(String firstName, String lastName, Date dob, Gender gender, String id), 
//				new User()));
		return null;
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
