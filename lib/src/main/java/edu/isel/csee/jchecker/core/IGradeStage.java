package edu.isel.csee.jchecker.core;

import java.util.ArrayList;

public interface IGradeStage {

	
	public int compile(String dpath);
	
	
	public boolean build(ArrayList<String> cases, String output, String dpath);
	
}
