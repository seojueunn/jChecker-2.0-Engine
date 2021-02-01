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
	
	
	public void start(String workpath, String policy)
	{
		IGradeStage grader = preset(workpath);
		
		EvaluationSchemeMapper scheme = new EvaluationSchemeMapper();
		JsonObject policyObject = new Gson().fromJson(policy, JsonObject.class);
		
		new PolicyParser().parse(scheme, policyObject);
		

		if (grader.compile(workpath) == 0) {
			for (int i = 0; i < scheme.getInputs().size(); i++) {
				boolean result = false;
				if (!flag) {
					result = grader.build(grader.getTest(workpath, scheme.getInputs().get(i).split(" ")),
							scheme.getOutputs().get(i),
							workpath);
					
				} else {
					System.out.println(grader.getTest(scheme.getInputs().get(i).toString()));
					result = grader.build(grader.getTest(scheme.getInputs().get(i)), 
							scheme.getOutputs().get(i), 
							workpath);
					
				}
				
				
				if (!result) {
					violations.add(String.valueOf(i));
					violationCount++;
				}
			}
			
			
			JsonObject item_class = new JsonObject();
			item_class.addProperty("violation-count", violationCount);
			item_class.addProperty("deducted", scheme.getRuntime_deduct_point() * violationCount);
			policyObject.add("classes", item_class);
		}
		
		EntireContentParser source = new EntireContentParser();
		
		new OOPChecker(scheme).run(source.getAllFiles(workpath), "", policyObject);
		new ImplementationChecker(scheme).run(source.getAllFiles(workpath), "", policyObject);
		
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String str = gson.toJson(policyObject);
		
		System.out.print(str);
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
