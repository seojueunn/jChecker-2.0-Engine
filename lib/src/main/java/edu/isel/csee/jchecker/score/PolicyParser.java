package edu.isel.csee.jchecker.score;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class PolicyParser {
	

	public void parse(EvaluationSchemeMapper policyTable, JsonObject policy)
	{

		JsonObject obj = null;
		
		policyTable.setAssignmentName(policy.get("name").getAsString());
		policyTable.setAssignmentGroup(policy.get("group").getAsString());
		policyTable.setPoint(policy.get("point").getAsDouble());
		
	
		
		obj = new Gson().fromJson(policy.get("runtime-compare"), JsonObject.class);
		policyTable.setInputs(new Gson().fromJson(obj.get("input"), new TypeToken<ArrayList<String>>() {}.getType()));
		policyTable.setOutputs(new Gson().fromJson(obj.get("output"), new TypeToken<ArrayList<String>>() {}.getType()));
		policyTable.setRuntime_deduct_point(obj.get("deduct-point").getAsDouble());
		

		
		obj = new Gson().fromJson(policy.get("package"), JsonObject.class);
		policyTable.setPackageName(obj.get("path").getAsString());
		policyTable.setPackage_deduct_point(obj.get("deduct-point").getAsDouble());
		
		

		
		obj = new Gson().fromJson(policy.get("classes"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setReqClass(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
			policyTable.setClass_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		

		
		obj = new Gson().fromJson(policy.get("methods"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setReqMethod(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
			policyTable.setMethod_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		

		
		obj = new Gson().fromJson(policy.get("custom-exception"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setReqCustExc(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
			policyTable.setCustomExc_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		

		
		obj = new Gson().fromJson(policy.get("custom-structure"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setReqCusStruct(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
			policyTable.setCustomStr_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		

		
		obj = new Gson().fromJson(policy.get("inherit-super"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setSuperclass_pair(new Gson().fromJson(obj.get("class-super"), new TypeToken<HashMap<String, String>>() {}.getType()));
			policyTable.setSpc_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		
		
		obj = new Gson().fromJson(policy.get("inherit-interface"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setInterface_pair(new Gson().fromJson(obj.get("class-interface"), new TypeToken<HashMap<String, String>>() {}.getType()));
			policyTable.setItf_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		
		
		obj = new Gson().fromJson(policy.get("recursion"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setRecursion(true);
			policyTable.setRecur_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		
		
		obj = new Gson().fromJson(policy.get("overriding"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setOverriding(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
			policyTable.setOvr_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		
		
		obj = new Gson().fromJson(policy.get("overloading"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setOverloading(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
			policyTable.setOvl_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		
		
		obj = new Gson().fromJson(policy.get("threads"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setThreads(true);
			policyTable.setThr_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		
		
		obj = new Gson().fromJson(policy.get("javadoc"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setJavadoc(true);
			policyTable.setJvd_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		
		
		obj = new Gson().fromJson(policy.get("encapsulation"), JsonObject.class);
		if (obj.get("state").getAsBoolean()) {
			policyTable.setEncaps(true);
			policyTable.setEnc_deduct_point(obj.get("deduct-point").getAsDouble());
		}
		
	}
}
