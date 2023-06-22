package edu.isel.csee.jchecker2_0.score;

import java.util.ArrayList;

/**
 * Class for getting data (grading policy and result)
 */
public class EvaluationSchemeMapper {
	private String token;
	private String itoken;
	private String className;
	private String instructor;
	private String dueDate;

	private ArrayList<String> packageName = null;
	private ArrayList<String> inputs = null;
	private ArrayList<String> checksums = null;
	private ArrayList<String> outputs = null;
	private ArrayList<String> reqClass = null;
	private ArrayList<String> reqFilePath = null;
	private ArrayList<String> reqMethod = null;
	private ArrayList<Integer> reqMethodCount = null;
	private ArrayList<String> reqMethodClass = null;
	private ArrayList<String> reqCustExc = null;
	private ArrayList<String> reqCusStruct = null;
	private ArrayList<String> overriding = null;
	private ArrayList<String> overloading = null;

	private ArrayList<String> ioriginClass = null;
	private ArrayList<String> superClass = null;

	private ArrayList<String> soriginClass = null;
	private ArrayList<String> interfaceClass = null;

	private boolean isBTool = false;
	private boolean isDirect = false;
	private boolean thread = false;
	private boolean javadoc = false;
	private boolean encaps = false;
	private boolean compiled = false;
	private boolean count = false;
	private boolean test = false;

	private double point = 0.0D;
	private double result_point = 0.0D;
	private double compiled_deduct_point = 0.0D;
	private double runtime_deduct_point = 0.0D;
	private double package_deduct_point = 0.0D;
	private double class_deduct_point = 0.0D;
	private double method_deduct_point = 0.0D;
	private double customExc_deduct_point = 0.0D;
	private double customStr_deduct_point = 0.0D;
	private double spc_deduct_point = 0.0D;
	private double itf_deduct_point = 0.0D;
	private double ovr_deduct_point = 0.0D;
	private double ovl_deduct_point = 0.0D;
	private double thr_deduct_point = 0.0D;
	private double jvd_deduct_point = 0.0D;
	private double enc_deduct_point = 0.0D;
	private double cnt_deduct_point = 0.0D;

	private double runtime_max_deduct = 0.0D;
	private double package_max_deduct = 0.0D;
	private double class_max_deduct = 0.0D;
	private double method_max_deduct = 0.0D;
	private double customExc_max_deduct = 0.0D;
	private double customStr_max_deduct = 0.0D;
	private double spc_max_deduct = 0.0D;
	private double itf_max_deduct = 0.0D;
	private double ovr_max_deduct = 0.0D;
	private double ovl_max_deduct = 0.0D;

	private int methodCount;
	private int fieldCount;
	private int enForCount;

	private int feedbackLevel = 1;

	/**
	 * Method for return dueDate
	 * @return dueDate
	 */
	public String getDueDate() {
		return dueDate;
	}

	/**
	 * Method for setting dueDate
	 * @param dueDate due date
	 */
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * Method for return token
	 * @return token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Method for setting token
	 * @param token token value
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * Method for return itoken
	 * @return itoken
	 */
	public String getItoken() {
		return itoken;
	}

	/**
	 * Method for setting itoken
	 * @param itoken itoken value
	 */
	public void setItoken(String itoken) {
		this.itoken = itoken;
	}

	/**
	 * Method for return class name
	 * @return className
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Method for setting class name
	 * @param className class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Method for return instructor name
	 * @return instructor
	 */
	public String getInstructor() {
		return instructor;
	}

	/**
	 * Method for setting instructor name
	 * @param instructor instructor name
	 */
	public void setInstructor(String instructor) {
		this.instructor = instructor;
	}

	/**
	 * Method for return original class name list (interface)
	 * @return ioriginClass
	 */
	public ArrayList<String> getIoriginClass() {
		return ioriginClass;
	}

	/**
	 * Method for setting original class name list (interface)
	 * @param ioriginClass original class name list (interface)
	 */
	public void setIoriginClass(ArrayList<String> ioriginClass) {
		this.ioriginClass = ioriginClass;
	}

	/**
	 * Method for return superclass name list
	 * @return superClass
	 */
	public ArrayList<String> getSuperClass() {
		return superClass;
	}

	/**
	 * Method for setting superclass name list
	 * @param superClass superclass name list
	 */
	public void setSuperClass(ArrayList<String> superClass) {
		this.superClass = superClass;
	}

	/**
	 * Method for return original class name list (superclass)
	 * @return soriginClass
	 */
	public ArrayList<String> getSoriginClass() {
		return soriginClass;
	}

	/**
	 * Method for setting original class name list (superclass)
	 * @param soriginClass original class name list (superclass)
	 */
	public void setSoriginClass(ArrayList<String> soriginClass) {
		this.soriginClass = soriginClass;
	}

	/**
	 * Method for return interface name list
	 * @return interfaceClass
	 */
	public ArrayList<String> getInterfaceClass() {
		return interfaceClass;
	}

	/**
	 * Method for setting interface name list
	 * @param interfaceClass interface name list
	 */
	public void setInterfaceClass(ArrayList<String> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	/**
	 * Method for return checksum list (oracle)
	 * @return checksums
	 */
	public ArrayList<String> getChecksums() {
		return checksums;
	}

	/**
	 * Method for setting checksum list (oracle)
	 * @param checksums checksum list (oracle)
	 */
	public void setChecksums(ArrayList<String> checksums) {
		this.checksums = checksums;
	}

	/**
	 * Method for return whether to check compilation
	 * @return compiled
	 */
	public boolean isCompiled() {
		return compiled;
	}

	/**
	 * Method for setting whether to check compilation
	 * @param compiled whether to check compilation
	 */
	public void setCompiled(boolean compiled) {
		this.compiled = compiled;
	}

	/**
	 * Method for return deduct point of compile violation
	 * @return compiled_deduct_point
	 */
	public double getCompiled_deduct_point() {
		return compiled_deduct_point;
	}

	/**
	 * Method for setting deduct point of compile violation
	 * @param compiled_deduct_point deduct point of compile violation
	 */
	public void setCompiled_deduct_point(double compiled_deduct_point) {
		this.compiled_deduct_point = compiled_deduct_point;
	}

	/**
	 * Method for return package name list
	 * @return packageName
	 */
	public ArrayList<String> getPackageName() {
		return packageName;
	}

	/**
	 * Method for setting package name list
	 * @param packageName package name list
	 */
	public void setPackageName(ArrayList<String> packageName) {
		this.packageName = packageName;
	}

	/**
	 * Method for return input data list (oracle)
	 * @return inputs
	 */
	public ArrayList<String> getInputs() {
		return inputs;
	}

	/**
	 * Method for setting input data list (oracle)
	 * @param inputs input data list (oracle)
	 */
	public void setInputs(ArrayList<String> inputs) {
		this.inputs = inputs;
	}

	/**
	 * Method for return output data list (oracle)
	 * @return outputs
	 */
	public ArrayList<String> getOutputs() {
		return outputs;
	}

	/**
	 * Method for setting output data list (oracle)
	 * @param outputs output data list (oracle)
	 */
	public void setOutputs(ArrayList<String> outputs) {
		this.outputs = outputs;
	}

	/**
	 * Method for return required class name list
	 * @return reqClass
	 */
	public ArrayList<String> getReqClass() {
		return reqClass;
	}

	/**
	 * Method for setting required class name list
	 * @param reqClass required class name list
	 */
	public void setReqClass(ArrayList<String> reqClass) {
		this.reqClass = reqClass;
	}

	/**
	 * Method for return required method name list
	 * @return reqMethod
	 */
	public ArrayList<String> getReqMethod() {
		return reqMethod;
	}

	/**
	 * Method for setting required method name list
	 * @param reqMethod required method name list
	 */
	public void setReqMethod(ArrayList<String> reqMethod) {
		this.reqMethod = reqMethod;
	}

	/**
	 * Method for return the minimum number of method usage
	 * @return reqMethodCount
	 */
	public ArrayList<Integer> getReqMethodCount() { return reqMethodCount; }

	/**
	 * Method for setting the minimum number of method usage
	 * @param reqMethodCount the minimum number of method usage
	 */
	public void setReqMethodCount(ArrayList<Integer> reqMethodCount) { this.reqMethodCount = reqMethodCount; }

	/**
	 * Method for return class name list of required methods
	 * @return reqMethodClass
	 */
	public ArrayList<String> getReqMethodClass() { return reqMethodClass; }

	/**
	 * Method for setting class name list of required methods
	 * @param reqMethodClass class name list of required methods
	 */
	public void setReqMethodClass(ArrayList<String> reqMethodClass) { this.reqMethodClass = reqMethodClass; }

	/**
	 * Method for return required custom exception class name list
	 * @return reqCustExc
	 */
	public ArrayList<String> getReqCustExc() {
		return reqCustExc;
	}

	/**
	 * Method for setting required custom exception class name list
	 * @param reqCustExc required custom exception class name list
	 */
	public void setReqCustExc(ArrayList<String> reqCustExc) {
		this.reqCustExc = reqCustExc;
	}

	/**
	 * Method for return required custom data structure name list
	 * @return reqCusStruct
	 */
	public ArrayList<String> getReqCusStruct() {
		return reqCusStruct;
	}

	/**
	 * Method for setting required custom data structure name list
	 * @param reqCusStruct required custom data structure name list
	 */
	public void setReqCusStruct(ArrayList<String> reqCusStruct) {
		this.reqCusStruct = reqCusStruct;
	}

	/**
	 * Method for return overriding method name list
	 * @return overriding
	 */
	public ArrayList<String> getOverriding() {
		return overriding;
	}

	/**
	 * Method for setting overriding method name list
	 * @param overriding overriding method name list
	 */
	public void setOverriding(ArrayList<String> overriding) {
		this.overriding = overriding;
	}

	/**
	 * Method for return overloading method name list
	 * @return overloading
	 */
	public ArrayList<String> getOverloading() {
		return overloading;
	}

	/**
	 * Method for setting overloading method name list
	 * @param overloading overloading method name list
	 */
	public void setOverloading(ArrayList<String> overloading) {
		this.overloading = overloading;
	}

	/**
	 * Method for return whether to check thread usage
	 * @return thread
	 */
	public boolean isThreads() {
		return thread;
	}

	/**
	 * Method for setting whether to check thread usage
	 * @param thread whether to check thread usage
	 */
	public void setThreads(boolean thread) {
		this.thread = thread;
	}

	/**
	 * Method for return whether to check javadoc
	 * @return javadoc
	 */
	public boolean isJavadoc() {
		return javadoc;
	}

	/**
	 * Method for setting whether to check javadoc
	 * @param javadoc whether to check javadoc
	 */
	public void setJavadoc(boolean javadoc) {
		this.javadoc = javadoc;
	}

	/**
	 * Method for return whether to check encapsulation
	 * @return encaps
	 */
	public boolean isEncaps() {
		return encaps;
	}

	/**
	 * Method for setting whether to check encapsulation
	 * @param encaps whether to check encapsulation
	 */
	public void setEncaps(boolean encaps) {
		this.encaps = encaps;
	}

	/**
	 * Method for return total point
	 * @return point
	 */
	public double getPoint() {
		return point;
	}

	/**
	 * Method for setting total point
	 * @param point total point
	 */
	public void setPoint(double point) {
		this.point = point;
		this.result_point = point;
	}

	/**
	 * Method for return result point
	 * @return result_point
	 */
	public double getResult_point() {
		return result_point;
	}

	/**
	 * Method for setting result point
	 * @param deduction_point total deduct point
	 */
	public void deduct_point(double deduction_point) {
		this.result_point -= deduction_point;
	}

	/**
	 * Method for return deduct point of oracle violation
	 * @return runtime_deduct_point
	 */
	public double getRuntime_deduct_point() {
		return runtime_deduct_point;
	}

	/**
	 * Method for setting deduct point of oracle violation
	 * @param runtime_deduct_point deduct point of oracle violation
	 */
	public void setRuntime_deduct_point(double runtime_deduct_point) {
		this.runtime_deduct_point = runtime_deduct_point;
	}

	/**
	 * Method for return deduct point of package violation
	 * @return package_deduct_point
	 */
	public double getPackage_deduct_point() {
		return package_deduct_point;
	}

	/**
	 * Method for setting deduct point of package violation
	 * @param package_deduct_point deduct point of package violation
	 */
	public void setPackage_deduct_point(double package_deduct_point) {
		this.package_deduct_point = package_deduct_point;
	}

	/**
	 * Method for return deduct point of class violation
	 * @return class_deduct_point
	 */
	public double getClass_deduct_point() {
		return class_deduct_point;
	}

	/**
	 * Method for setting deduct point of class violation
	 * @param class_deduct_point deduct point of class violation
	 */
	public void setClass_deduct_point(double class_deduct_point) {
		this.class_deduct_point = class_deduct_point;
	}

	/**
	 * Method for return deduct point of method violation
	 * @return method_deduct_point
	 */
	public double getMethod_deduct_point() { return method_deduct_point; }

	/**
	 * Method for setting deduct point of method violation
	 * @param method_deduct_point deduct point of method violation
	 */
	public void setMethod_deduct_point(double method_deduct_point) { this.method_deduct_point = method_deduct_point; }

	/**
	 * Method for return file path data list (oracle)
	 * @return reqFilePath
	 */
	public ArrayList<String> getReqFilePath() {
		return reqFilePath;
	}

	/**
	 * Method for setting file path data list (oracle)
	 * @param reqFilePath file path data list (oracle)
	 */
	public void setReqFilePath(ArrayList<String> reqFilePath) {
		this.reqFilePath = reqFilePath;
	}

	/**
	 * Method for return deduct point of custom exception violation
	 * @return customExc_deduct_point
	 */
	public double getCustomExc_deduct_point() {
		return customExc_deduct_point;
	}

	/**
	 * Method for setting deduct point of custom exception violation
	 * @param customExc_deduct_point deduct point of custom exception violation
	 */
	public void setCustomExc_deduct_point(double customExc_deduct_point) {
		this.customExc_deduct_point = customExc_deduct_point;
	}

	/**
	 * Method for return deduct point of custom data structure violation
	 * @return customStr_deduct_point
	 */
	public double getCustomStr_deduct_point() {
		return customStr_deduct_point;
	}

	/**
	 * Method for setting deduct point of custom data structure violation
	 * @param customStr_deduct_point deduct point of custom data structure violation
	 */
	public void setCustomStr_deduct_point(double customStr_deduct_point) {
		this.customStr_deduct_point = customStr_deduct_point;
	}

	/**
	 * Method for return deduct point of inheritance violation (superclass)
	 * @return spc_deduct_point
	 */
	public double getSpc_deduct_point() {
		return spc_deduct_point;
	}

	/**
	 * Method for setting deduct point of inheritance violation (superclass)
	 * @param spc_deduct_point deduct point of inheritance violation (superclass)
	 */
	public void setSpc_deduct_point(double spc_deduct_point) {
		this.spc_deduct_point = spc_deduct_point;
	}

	/**
	 * Method for return deduct point of inheritance violation (interface)
	 * @return itf_deduct_point
	 */
	public double getItf_deduct_point() {
		return itf_deduct_point;
	}

	/**
	 * Method for setting deduct point of inheritance violation (interface)
	 * @param itf_deduct_point deduct point of inheritance violation (interface)
	 */
	public void setItf_deduct_point(double itf_deduct_point) {
		this.itf_deduct_point = itf_deduct_point;
	}

	/**
	 * Method for return deduct point of overriding violation
	 * @return ovr_deduct_point
	 */
	public double getOvr_deduct_point() {
		return ovr_deduct_point;
	}

	/**
	 * Method for setting deduct point of overriding violation
	 * @param ovr_deduct_point deduct point of overriding violation
	 */
	public void setOvr_deduct_point(double ovr_deduct_point) {
		this.ovr_deduct_point = ovr_deduct_point;
	}

	/**
	 * Method for return deduct point of overloading violation
	 * @return ovl_deduct_point
	 */
	public double getOvl_deduct_point() {
		return ovl_deduct_point;
	}

	/**
	 * Method for setting deduct point of overloading violation
	 * @param ovl_deduct_point deduct point of overloading violation
	 */
	public void setOvl_deduct_point(double ovl_deduct_point) {
		this.ovl_deduct_point = ovl_deduct_point;
	}

	/**
	 * Method for return deduct point of thread violation
	 * @return thr_deduct_point
	 */
	public double getThr_deduct_point() {
		return thr_deduct_point;
	}

	/**
	 * Method for setting deduct point of thread violation
	 * @param thr_deduct_point deduct point of thread violation
	 */
	public void setThr_deduct_point(double thr_deduct_point) {
		this.thr_deduct_point = thr_deduct_point;
	}

	/**
	 * Method for return deduct point of javadoc violation
	 * @return jvd_deduct_point
	 */
	public double getJvd_deduct_point() {
		return jvd_deduct_point;
	}

	/**
	 * Method for setting deduct point of javadoc violation
	 * @param jvd_deduct_point deduct point of javadoc violation
	 */
	public void setJvd_deduct_point(double jvd_deduct_point) {
		this.jvd_deduct_point = jvd_deduct_point;
	}

	/**
	 * Method for return deduct point of encapsulation violation
	 * @return enc_deduct_point
	 */
	public double getEnc_deduct_point() {
		return enc_deduct_point;
	}

	/**
	 * Method for setting deduct point of encapsulation violation
	 * @param enc_deduct_point deduct point of encapsulation violation
	 */
	public void setEnc_deduct_point(double enc_deduct_point) {
		this.enc_deduct_point = enc_deduct_point;
	}

	/**
	 * Method for return deduct point of count violation
	 * @return cnt_deduct_point
	 */
	public double getCnt_deduct_point() {
		return cnt_deduct_point;
	}

	/**
	 * Method for setting deduct point of count violation
	 * @param cnt_deduct_point deduct point of count violation
	 */
	public void setCnt_deduct_point(double cnt_deduct_point) {
		this.cnt_deduct_point = cnt_deduct_point;
	}

	/**
	 * Method for return max deduct point of oracle violation
	 * @return runtime_max_deduct
	 */
	public double getRuntime_max_deduct() {
		return runtime_max_deduct;
	}

	/**
	 * Method for setting max deduct point of oracle violation
	 * @param runtime_max_deduct max deduct point of oracle violation
	 */
	public void setRuntime_max_deduct(double runtime_max_deduct) {
		this.runtime_max_deduct = runtime_max_deduct;
	}

	/**
	 * Method for return max deduct point of package violation
	 * @return package_max_deduct
	 */
	public double getPackage_max_deduct() {
		return package_max_deduct;
	}

	/**
	 * Method for setting max deduct point of package violation
	 * @param package_max_deduct max deduct point of package violation
	 */
	public void setPackage_max_deduct(double package_max_deduct) {
		this.package_max_deduct = package_max_deduct;
	}

	/**
	 * Method for return max deduct point of class violation
	 * @return class_max_deduct
	 */
	public double getClass_max_deduct() {
		return class_max_deduct;
	}

	/**
	 * Method for setting max deduct point of class violation
	 * @param class_max_deduct max deduct point of class violation
	 */
	public void setClass_max_deduct(double class_max_deduct) {
		this.class_max_deduct = class_max_deduct;
	}

	/**
	 * Method for return max deduct point of method violation
	 * @return method_max_deduct
	 */
	public double getMethod_max_deduct() { return method_max_deduct; }

	/**
	 * Method for setting max deduct point of method violation
	 * @param method_max_deduct max deduct point of method violation
	 */
	public void setMethod_max_deduct(double method_max_deduct) { this.method_max_deduct = method_max_deduct; }

	/**
	 * Method for return max deduct point of custom exception violation
	 * @return customExc_max_deduct
	 */
	public double getCustomExc_max_deduct() {
		return customExc_max_deduct;
	}

	/**
	 * Method for setting max deduct point of custom exception violation
	 * @param customExc_max_deduct max deduct point of custom exception violation
	 */
	public void setCustomExc_max_deduct(double customExc_max_deduct) {
		this.customExc_max_deduct = customExc_max_deduct;
	}

	/**
	 * Method for return max deduct point of custom data structure violation
	 * @return customStr_max_deduct
	 */
	public double getCustomStr_max_deduct() {
		return customStr_max_deduct;
	}

	/**
	 * Method for setting max deduct point of custom data structure violation
	 * @param customStr_max_deduct max deduct point of custom data structure violation
	 */
	public void setCustomStr_max_deduct(double customStr_max_deduct) {
		this.customStr_max_deduct = customStr_max_deduct;
	}

	/**
	 * Method for return max deduct point of inheritance violation (superclass)
	 * @return spc_max_deduct
	 */
	public double getSpc_max_deduct() {
		return spc_max_deduct;
	}

	/**
	 * Method for setting max deduct point of inheritance violation (superclass)
	 * @param spc_max_deduct max deduct point of inheritance violation (superclass)
	 */
	public void setSpc_max_deduct(double spc_max_deduct) {
		this.spc_max_deduct = spc_max_deduct;
	}

	/**
	 * Method for return max deduct point of inheritance violation (interface)
	 * @return itf_max_deduct
	 */
	public double getItf_max_deduct() {
		return itf_max_deduct;
	}

	/**
	 * Method for setting max deduct point of inheritance violation (interface)
	 * @param itf_max_deduct max deduct point of inheritance violation (interface)
	 */
	public void setItf_max_deduct(double itf_max_deduct) {
		this.itf_max_deduct = itf_max_deduct;
	}

	/**
	 * Method for return max deduct point of overriding violation
	 * @return ovr_max_deduct
	 */
	public double getOvr_max_deduct() {
		return ovr_max_deduct;
	}

	/**
	 * Method for setting max deduct point of overriding violation
	 * @param ovr_max_deduct max deduct point of overriding violation
	 */
	public void setOvr_max_deduct(double ovr_max_deduct) {
		this.ovr_max_deduct = ovr_max_deduct;
	}

	/**
	 * Method for return max deduct point of overloading violation
	 * @return ovl_max_deduct
	 */
	public double getOvl_max_deduct() {
		return ovl_max_deduct;
	}

	/**
	 * Method for setting max deduct point of overloading violation
	 * @param ovl_max_deduct max deduct point of overloading violation
	 */
	public void setOvl_max_deduct(double ovl_max_deduct) {
		this.ovl_max_deduct = ovl_max_deduct;
	}

	/**
	 * Method for return whether to check count usage
	 * @return count
	 */
	public boolean isCount() {
		return count;
	}

	/**
	 * Method for setting whether to check count usage
	 * @param count whether to check count usage
	 */
	public void setCount(boolean count) {
		this.count = count;
	}

	/**
	 * Method for return the minimum number of method
	 * @return methodCount
	 */
	public int getMethodCount() {
		return methodCount;
	}

	/**
	 * Method for setting the minimum number of method
	 * @param methodCount the minimum number of method
	 */
	public void setMethodCount(int methodCount) {
		this.methodCount = methodCount;
	}

	/**
	 * Method for return the minimum number of field
	 * @return fieldCount
	 */
	public int getFieldCount() {
		return fieldCount;
	}

	/**
	 * Method for setting the minimum number of field
	 * @param fieldCount the minimum number of field
	 */
	public void setFieldCount(int fieldCount) {
		this.fieldCount = fieldCount;
	}

	/**
	 * Method for return the minimum number of enhanced for statement
	 * @return enForCount
	 */
	public int getEnForCount() {
		return enForCount;
	}

	/**
	 * Method for setting the minimum number of enhanced for statement
	 * @param enForCount the minimum number of enhanced for statement
	 */
	public void setEnForCount(int enForCount) {
		this.enForCount = enForCount;
	}

	/**
	 * Method for return whether to use test
	 * @return test
	 */
	public boolean isTest() {
		return test;
	}

	/**
	 * Method for setting whether to use test
	 * @param test whether to use test
	 */
	public void setTest(boolean test) {
		this.test = test;
	}

	/**
	 * Method for return whether to provide feedback
	 * @return isDirect
	 */
	public boolean isDirect() {
		return isDirect;
	}

	/**
	 * Method for setting whether to provide feedback
	 * @param isDirect whether to provide feedback
	 */
	public void setDirect(boolean isDirect) {
		this.isDirect = isDirect;
	}

	/**
	 * Method for return whether to use build tool (gradle)
	 * @return isBTool
	 */
	public boolean isBTool() {
		return isBTool;
	}

	/**
	 * Method for setting whether to use build tool (gradle)
	 * @param isBTool whether to use build tool (gradle)
	 */
	public void setBTool(boolean isBTool) {
		this.isBTool = isBTool;
	}

	/**
	 * Method for return feedback level
	 * @return feedbackLevel
	 */
	public int getFeedbackLevel() { return feedbackLevel; }

	/**
	 * Method for setting feedback level
	 * @param feedbackLevel feedback level
	 */
	public void setFeedbackLevel(int feedbackLevel) { this.feedbackLevel = feedbackLevel; }
}
