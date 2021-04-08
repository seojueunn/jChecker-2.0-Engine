package edu.isel.csee.jchecker.core;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import edu.isel.csee.jchecker.core.stage.*;
import edu.isel.csee.jchecker.score.*;
import edu.isel.csee.jchecker.statics.*;
import edu.isel.csee.jchecker.statics.utils.EntireContentParser;
import edu.isel.csee.jchecker.statics.utils.MainClassDetector;


public class CoreGrader {
	private boolean flag = false;
	private ArrayList<String> violations = new ArrayList<>();
	private List<String> srcList = new ArrayList<>();
	private int violationCount = 0;
	private int checkCompile ;
	
	
	public String start(String workpath, String policy)
	{
		System.out.println("workpath : " + workpath);
		System.out.println("policy : " + policy);
		
		IGradeStage grader = preset(workpath);
		
		EvaluationSchemeMapper scheme = new EvaluationSchemeMapper();
		JsonObject policyObject = new Gson().fromJson(policy, JsonObject.class);
		JsonObject score = new JsonObject();
		
		new PolicyParser().parse(scheme, policyObject);
		
		score.addProperty("token", scheme.getToken());
		score.addProperty("isDirect", scheme.isDirect()) ;
		score.addProperty("className", scheme.getClassName());
		score.addProperty("instructor", scheme.getInstructor());
		score.addProperty("point", scheme.getPoint());
		
		checkCompile = grader.compile(workpath) ;
		

		EntireContentParser source = new EntireContentParser();
		MainClassDetector mcd = new MainClassDetector();
		
		srcList = source.getAllFiles(workpath);
		mcd.setFilePath(workpath);
		String mainPath = "";
		
		for (String src : srcList) {

			if (!(mainPath = mcd.find(src)).isBlank()) {
				mainPath.trim();
				break;
			}
		}
		
		
		if (checkCompile == 0) {	
			JsonObject item = new JsonObject();
			item.addProperty("violation", false);
			item.addProperty("deductedPoint", 0);
			score.add("compile", item);

			for (int i = 0; i < scheme.getInputs().size(); i++) {
				boolean result = false;
				if (!flag) {
					// input, output이 없는 경우 에러가 발생할 수 있음 
					// build method를 오버로딩해야 함 (output을 받지 않는 걸로)
					result = grader.build(grader.getTest(mainPath, scheme.getInputs().get(i), scheme.isTest()),
							scheme.getOutputs().get(i),
							workpath);
					
				} else {
					
					result = grader.build(grader.getTest(scheme.getInputs().get(i), scheme.isTest()), 
							scheme.getOutputs().get(i), 
							workpath);
					
				}
				
				
				if (!result) {
					violations.add(String.valueOf(i + 1));
					violationCount++;
				}
			}
			
			JsonObject item_class = new JsonObject();
			
			if(violationCount > 0)
				item_class.addProperty("violation", true);
			else
				item_class.addProperty("violation", false);
			
			item_class.add("violationNumber", new Gson().toJsonTree(violations));
			item_class.addProperty("violationCount", violationCount);
			
			float deducted = (float)(scheme.getRuntime_deduct_point() * (double)violationCount);
			if (deducted > scheme.getRuntime_max_deduct())
				deducted = (float)scheme.getRuntime_max_deduct();
			
			scheme.deduct_point(deducted);
			item_class.addProperty("deductedPoint", deducted);
			score.add("runtimeCompare", item_class);
			
		} else {
			if(scheme.isTest())
			{
				for(int i = 0; i < scheme.getInputs().size(); i++) violations.add(String.valueOf(i + 1));
				
				JsonObject item_class = new JsonObject();
				if(scheme.getInputs().size() > 0)
					item_class.addProperty("violation", true);
				else
					item_class.addProperty("violation", false);
				item_class.add("violationNumber", new Gson().toJsonTree(violations));
				item_class.addProperty("violationCount", scheme.getInputs().size());
				
				double deducted = scheme.getRuntime_max_deduct();
				
				scheme.deduct_point(deducted);
				item_class.addProperty("deductedPoint", deducted);
				score.add("runtimeCompare", item_class);
			}	
			
			JsonObject item = new JsonObject();
			item.addProperty("violation", true);
			item.addProperty("deductedPoint", scheme.getCompiled_deduct_point());
			score.add("compile", item);
			
			scheme.deduct_point(scheme.getCompiled_deduct_point());
		}
				

		new OOPChecker(scheme, srcList, "", workpath).run(score);
		new ImplementationChecker(scheme, srcList, "", workpath).run(score);

		score.addProperty("result", (float)scheme.getResult_point());
		
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String sheet = gson.toJson(score);
		
		return sheet;
	}
	
	

	private IGradeStage preset(String path)
	{
		if (new File(path + "/settings.gradle").exists()) {
			flag = true;
			return new GradleStage();
		}
		
		return new JavaStage();
	}
	
	
}
