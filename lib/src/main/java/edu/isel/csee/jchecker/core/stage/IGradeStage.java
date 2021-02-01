package edu.isel.csee.jchecker.core.stage;

import java.util.ArrayList;

public interface IGradeStage {

	
	public int compile(String dpath);
	
	
	public boolean build(ArrayList<String> cases, String output, String dpath);
	
	
	public ArrayList<String> getTest(String argument);
	
	
	public ArrayList<String> getTest(String argument, String[] cases);
}
