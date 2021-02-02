package edu.isel.csee.jchecker.core;

import java.io.File;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.isel.csee.jchecker.core.stage.*;
import edu.isel.csee.jchecker.score.*;
import edu.isel.csee.jchecker.statics.*;
import edu.isel.csee.jchecker.statics.utils.EntireContentParser;


public class CoreGrader {
	private boolean flag = false;
	private ArrayList<String> violations = new ArrayList<>();
	private int violationCount = 0;
	
	
	public String start(String workpath, String policy)
	{
		IGradeStage grader = preset(workpath);
		
		EvaluationSchemeMapper scheme = new EvaluationSchemeMapper();
		JsonObject policyObject = new Gson().fromJson(policy, JsonObject.class);
		JsonObject score = new JsonObject();
		
		new PolicyParser().parse(scheme, policyObject);
		
		
		score.addProperty("name", scheme.getAssignmentName());
		score.addProperty("group", scheme.getAssignmentGroup());
		score.addProperty("point", scheme.getPoint());
		
		
		if (grader.compile(workpath) == 0) {
			JsonObject item = new JsonObject();
			item.addProperty("compiled", true);
			item.addProperty("deduct", 0);
			score.add("compile", item);
			
			for (int i = 0; i < scheme.getInputs().size(); i++) {
				boolean result = false;
				if (!flag) {
					result = grader.build(grader.getTest(workpath, scheme.getInputs().get(i).split(" ")),
							scheme.getOutputs().get(i),
							workpath);
					
				} else {
					result = grader.build(grader.getTest(scheme.getInputs().get(i)), 
							scheme.getOutputs().get(i), 
							workpath);
					
				}
				
				
				if (!result) {
					violations.add(String.valueOf(i + 1));
					violationCount++;
				}
			}
			
			
			JsonObject item_class = new JsonObject();
			item_class.add("violation-number", new Gson().toJsonTree(violations));
			item_class.addProperty("violation-count", violationCount);
			
			double deducted = scheme.getRuntime_deduct_point() * violationCount;
			if (deducted > scheme.getRuntime_max_deduct())
				deducted = scheme.getRuntime_max_deduct();
			
			scheme.deduct_point(deducted);
			item_class.addProperty("deducted", deducted);
			score.add("runtime-result", item_class);
			
		} else {
			JsonObject item = new JsonObject();
			item.addProperty("compiled", false);
			item.addProperty("deduct", scheme.getCompiled_deduct_point());
			score.add("compile", item);
			
			scheme.deduct_point(scheme.getCompiled_deduct_point());
		}
		
		EntireContentParser source = new EntireContentParser();
		
		new OOPChecker(scheme, source.getAllFiles(workpath), "").run(score);
		new ImplementationChecker(scheme, source.getAllFiles(workpath), "").run(score);
		
		
		score.addProperty("result", scheme.getResult_point());
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sheet = gson.toJson(score);
		System.out.println(sheet);
		return sheet;
	}
	
	

	private IGradeStage preset(String path)
	{
		if (new File(path + "\\settings.gradle").exists()) {
			flag = true;
			return new GradleStage();
		}
		
		return new JavaStage();
	}
	
	
}
