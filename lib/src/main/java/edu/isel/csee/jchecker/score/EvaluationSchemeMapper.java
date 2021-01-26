package edu.isel.csee.jchecker.score;

import java.util.ArrayList;

public class EvaluationSchemeMapper {

	private String assignmentName;
	private String assignmentGroup;
	private String packageName;
	private ArrayList<String> inputs = null;
	private ArrayList<String> outputs = null;
	private ArrayList<String> reqClass = null;
	private ArrayList<String> reqMethod = null;
	private ArrayList<String> reqCustExc = null;
	private ArrayList<String> reqCusStruct = null;
	private ArrayList<String> superclass_pair = null;
	private ArrayList<String> interface_pair = null;
	private boolean recursion = false;
	private boolean overriding = false;
	private boolean overloading = false;
	private boolean threads = false;
	private boolean javadoc = false;
	private boolean encaps = false;
	
	
	private double point = 0;
	private double runtime_deduct_point = 0;
	private double package_deduct_point = 0;
	private double class_deduct_point = 0;
	private double method_deduct_point = 0;
	private double customExc_deduct_point = 0;
	private double customStr_deduct_point = 0;
	private double spc_deduct_point = 0;
	private double itf_deduct_point = 0;
	private double recur_deduct_point = 0;
	private double ovr_deduct_point = 0;
	private double ovl_deduct_point = 0;
	private double thr_deduct_point = 0;
	private double jvd_deduct_point = 0;
	private double enc_deduct_point = 0;
	
	
	
	public String getAssignmentName()
	{
		return assignmentName;
	}
	
	
	public void setAssignmentName(String assignmentName)
	{
		this.assignmentName = assignmentName;
	}
	
	
	public String getAssignmentGroup()
	{
		return assignmentGroup;
	}
	
	
	public void setAssignmentGroup(String assignmentGroup)
	{
		this.assignmentGroup = assignmentGroup;
	}
	
	
	public String getPackageName()
	{
		return packageName;
	}
	
	
	public void setPackageName(String packageName)
	{
		this.packageName = packageName;
	}
	
	
	public ArrayList<String> getInputs()
	{
		return inputs;
	}
	
	
	public void setInputs(ArrayList<String> inputs)
	{
		this.inputs = inputs;
	}
	
	
	public ArrayList<String> getOutputs()
	{
		return outputs;
	}
	
	
	public void setOutputs(ArrayList<String> outputs)
	{
		this.outputs = outputs;
	}
	
	
	public ArrayList<String> getReqClass()
	{
		return reqClass;
	}
	
	
	public void setReqClass(ArrayList<String> reqClass)
	{
		this.reqClass = reqClass;
	}
	
	
	public ArrayList<String> getReqMethod()
	{
		return reqMethod;
	}
	
	
	public void setReqMethod(ArrayList<String> reqMethod)
	{
		this.reqMethod = reqMethod;
	}
	
	
	public ArrayList<String> getReqCustExc()
	{
		return reqCustExc;
	}
	
	
	public void setReqCustExc(ArrayList<String> reqCustExc)
	{
		this.reqCustExc = reqCustExc;
	}
	
	
	public ArrayList<String> getReqCusStruct()
	{
		return reqCusStruct;
	}
	
	
	public void setReqCusStruct(ArrayList<String> reqCusStruct)
	{
		this.reqCusStruct = reqCusStruct;
	}
	
	
	public ArrayList<String> getSuperclass_pair()
	{
		return superclass_pair;
	}
	
	
	public void setSuperclass_pair(ArrayList<String> superclass_pair)
	{
		this.superclass_pair = superclass_pair;
	}
	
	
	public ArrayList<String> getInterface_pair()
	{
		return interface_pair;
	}
	
	
	public void setInterface_pair(ArrayList<String> interface_pair)
	{
		this.interface_pair = interface_pair;
	}
	
	
	public boolean isRecursion()
	{
		return recursion;
	}
	
	
	public void setRecursion(boolean recursion)
	{
		this.recursion = recursion;
	}
	
	
	public boolean isOverriding()
	{
		return overriding;
	}
	
	
	public void setOverriding(boolean overriding)
	{
		this.overriding = overriding;
	}
	
	
	public boolean isOverloading()
	{
		return overloading;
	}
	
	
	public void setOverloading(boolean overloading)
	{
		this.overloading = overloading;
	}
	
	
	public boolean isThreads()
	{
		return threads;
	}
	
	
	public void setThreads(boolean threads)
	{
		this.threads = threads;
	}
	
	
	public boolean isJavadoc()
	{
		return javadoc;
	}
	
	
	public void setJavadoc(boolean javadoc)
	{
		this.javadoc = javadoc;
	}
	
	
	public boolean isEncaps()
	{
		return encaps;
	}
	
	
	public void setEncaps(boolean encaps)
	{
		this.encaps = encaps;
	}
	
	
	public double getPoint()
	{
		return point;
	}
	
	
	public void setPoint(double point)
	{
		this.point = point;
	}
	
	
	public double getRuntime_deduct_point()
	{
		return runtime_deduct_point;
	}
	
	
	public void setRuntime_deduct_point(double runtime_deduct_point)
	{
		this.runtime_deduct_point = runtime_deduct_point;
	}
	
	
	public double getPackage_deduct_point()
	{
		return package_deduct_point;
	}
	
	
	public void setPackage_deduct_point(double package_deduct_point)
	{
		this.package_deduct_point = package_deduct_point;
	}
	
	
	public double getClass_deduct_point()
	{
		return class_deduct_point;
	}
	
	
	public void setClass_deduct_point(double class_deduct_point)
	{
		this.class_deduct_point = class_deduct_point;
	}
	
	
	public double getMethod_deduct_point()
	{
		return method_deduct_point;
	}
	
	
	public void setMethod_deduct_point(double method_deduct_point)
	{
		this.method_deduct_point = method_deduct_point;
	}
	
	
	public double getCustomExc_deduct_point()
	{
		return customExc_deduct_point;
	}
	
	
	public void setCustomExc_deduct_point(double customExc_deduct_point)
	{
		this.customExc_deduct_point = customExc_deduct_point;
	}
	
	
	public double getCustomStr_deduct_point()
	{
		return customStr_deduct_point;
	}
	
	
	public void setCustomStr_deduct_point(double customStr_deduct_point)
	{
		this.customStr_deduct_point = customStr_deduct_point;
	}
	
	
	public double getSpc_deduct_point()
	{
		return spc_deduct_point;
	}
	
	
	public void setSpc_deduct_point(double spc_deduct_point)
	{
		this.spc_deduct_point = spc_deduct_point;
	}
	
	
	public double getItf_deduct_point()
	{
		return itf_deduct_point;
	}
	
	
	public void setItf_deduct_point(double itf_deduct_point)
	{
		this.itf_deduct_point = itf_deduct_point;
	}
	
	
	public double getRecur_deduct_point()
	{
		return recur_deduct_point;
	}
	
	public void setRecur_deduct_point(double recur_deduct_point)
	{
		this.recur_deduct_point = recur_deduct_point;
	}
	
	
	public double getOvr_deduct_point()
	{
		return ovr_deduct_point;
	}
	
	
	public void setOvr_deduct_point(double ovr_deduct_point)
	{
		this.ovr_deduct_point = ovr_deduct_point;
	}
	
	
	public double getOvl_deduct_point()
	{
		return ovl_deduct_point;
	}
	
	
	public void setOvl_deduct_point(double ovl_deduct_point)
	{
		this.ovl_deduct_point = ovl_deduct_point;
	}
	
	
	public double getThr_deduct_point()
	{
		return thr_deduct_point;
	}
	
	
	public void setThr_deduct_point(double thr_deduct_point)
	{
		this.thr_deduct_point = thr_deduct_point;
	}
	
	
	public double getJvd_deduct_point()
	{
		return jvd_deduct_point;
	}
	
	
	public void setJvd_deduct_point(double jvd_deduct_point)
	{
		this.jvd_deduct_point = jvd_deduct_point;
	}
	
	
	public double getEnc_deduct_point()
	{
		return enc_deduct_point;
	}
	
	
	public void setEnc_deduct_point(double enc_deduct_point)
	{
		this.enc_deduct_point = enc_deduct_point;
	}
}
