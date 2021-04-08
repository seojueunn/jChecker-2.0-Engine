package edu.isel.csee.jchecker.score;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class PolicyParser {
	

	public void parse(EvaluationSchemeMapper policyTable, JsonObject policy)
	{
		try {
			JsonObject obj = null;
			
			policyTable.setToken(policy.get("token").getAsString());
			policyTable.setItoken(policy.get("itoken").getAsString());
			policyTable.setClassName(policy.get("className").getAsString());
			policyTable.setInstructor(policy.get("instructor").getAsString());
			policyTable.setPoint(policy.get("point").getAsDouble());
			policyTable.setDirect(policy.get("feedback").getAsBoolean());
			
				
			obj = new Gson().fromJson(policy.get("compiled"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setCompiled(true);
				policyTable.setCompiled_deduct_point(obj.get("deductPoint").getAsDouble());
			}
			
			
			obj = new Gson().fromJson(policy.get("runtimeCompare"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setTest(true);
				policyTable.setInputs(new Gson().fromJson(obj.get("input"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setOutputs(new Gson().fromJson(obj.get("output"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setRuntime_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setRuntime_max_deduct(obj.get("maxDeduct").getAsDouble());
			}
			

			obj = new Gson().fromJson(policy.get("packages"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setPackageName(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setPackage_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setPackage_max_deduct(obj.get("maxDeduct").getAsDouble());
			}

			obj = new Gson().fromJson(policy.get("classes"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setReqClass(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setClass_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setClass_max_deduct(obj.get("maxDeduct").getAsDouble());
			}
			

			obj = new Gson().fromJson(policy.get("customException"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setReqCustExc(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setCustomExc_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setCustomExc_max_deduct(obj.get("maxDeduct").getAsDouble());
			}
			

			
			obj = new Gson().fromJson(policy.get("customStructure"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setReqCusStruct(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setCustomStr_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setCustomStr_max_deduct(obj.get("maxDeduct").getAsDouble());
			}
			

			
			obj = new Gson().fromJson(policy.get("inheritSuper"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setSoriginClass(new Gson().fromJson(obj.get("origins"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setSuperClass(new Gson().fromJson(obj.get("inherit"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setSpc_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setSpc_max_deduct(obj.get("maxDeduct").getAsDouble());
			}
			
			
			obj = new Gson().fromJson(policy.get("inheritInterface"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setIoriginClass(new Gson().fromJson(obj.get("origins"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setInterfaceClass(new Gson().fromJson(obj.get("inherit"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setItf_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setItf_max_deduct(obj.get("maxDeduct").getAsDouble());
			}
			
			
			obj = new Gson().fromJson(policy.get("overriding"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setOverriding(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setOvr_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setOvr_max_deduct(obj.get("maxDeduct").getAsDouble());
			}
			
			
			obj = new Gson().fromJson(policy.get("overloading"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setOverloading(new Gson().fromJson(obj.get("required"), new TypeToken<ArrayList<String>>() {}.getType()));
				policyTable.setOvl_deduct_point(obj.get("deductPoint").getAsDouble());
				policyTable.setOvl_max_deduct(obj.get("maxDeduct").getAsDouble());
			}
			
			
			obj = new Gson().fromJson(policy.get("thread"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setThreads(true);
				policyTable.setThr_deduct_point(obj.get("deductPoint").getAsDouble());
			}
			
			
			obj = new Gson().fromJson(policy.get("javadoc"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setJavadoc(true);
				policyTable.setJvd_deduct_point(obj.get("deductPoint").getAsDouble());
			}
			
			
			obj = new Gson().fromJson(policy.get("encapsulation"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setEncaps(true);
				policyTable.setEnc_deduct_point(obj.get("deductPoint").getAsDouble());
			}
			
			obj = new Gson().fromJson(policy.get("count"), JsonObject.class);
			if (obj.get("state").getAsBoolean()) {
				policyTable.setCount(true);
				policyTable.setMethodCount(obj.get("methodCount").getAsInt());
				policyTable.setFieldCount(obj.get("fieldCount").getAsInt());
				policyTable.setMethodCount(obj.get("enForCount").getAsInt());
				policyTable.setCnt_deduct_point(obj.get("deductPoint").getAsDouble());
			}
			
		} catch(Exception e) { e.printStackTrace(); }
	}
}
