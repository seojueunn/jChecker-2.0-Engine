package edu.isel.csee.jchecker2_0.statics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonObject;
import org.eclipse.jdt.core.dom.*;

import edu.isel.csee.jchecker2_0.score.EvaluationSchemeMapper;

/**
 * Class for checking implementation
 */
public class ImplementationChecker extends ASTChecker {
	EvaluationSchemeMapper policy;
	private List<String> source = null;
	private String unitName;
	private String filePath;
	private String libPath;
	private List<String> instances = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	private List<String> superClasses = new ArrayList<>();
	private List<String> threads = new ArrayList<>();
	private List<String> expressions = new ArrayList<>();
	private List<String> methodInvocations = new ArrayList<>();

	private ArrayList<String> customExcViolations = new ArrayList<>();
	private ArrayList<String> customStructViolations = new ArrayList<>();

	private boolean jdocViolation = false;
	private boolean threadViolation = false;
	private boolean countViolation = false;
	private boolean customExcViolation = false;
	private boolean customStructViolation = false;
	private boolean reqMethodViolation = false;
	private boolean isBuild = false;
	private boolean isRunnable = false;

	private int customExcViolationCount = 0;
	private int customStructViolationCount = 0;
	private int reqMethodViolationCount = 0;
	private String result = "";

	private int numOfEnhancedForStatement = 0;
	private int numOfMethods = 0;
	private int numOfFields = 0;

	/**
	 * Constructor for ImplementationChecker class
	 * @param policy EvaluationSchemeMapper
	 * @param source source code
	 * @param unitName unit information
	 * @param filePath file path
	 * @param libPath library path
	 */
	public ImplementationChecker(EvaluationSchemeMapper policy, List<String> source, String unitName, String filePath, String libPath) {
		this.policy = policy;
		this.source = source;
		this.unitName = unitName;
		this.filePath = filePath;
		this.libPath = libPath;
	}

	/**
	 * Method for scoring implementation and setting result
	 * @param scoresheet JSON data (result)
	 * @param isBuild isBuild
	 * @return scoresheet
	 */
	public JsonObject run(JsonObject scoresheet, boolean isBuild) {
		this.isBuild = isBuild;

		JsonObject item;
		if (!source.isEmpty() && source != null) {
			collect();
			test();

			if (policy.isCount()) {
				item = new JsonObject();
				item.addProperty("violation", countViolation);

				if (!countViolation) {
					policy.setCnt_deduct_point(0.0D);
				}

				item.addProperty("deductedPoint", policy.getCnt_deduct_point());
				scoresheet.add("count", item);

				policy.deduct_point(policy.getCnt_deduct_point());
			}

			if (policy.isJavadoc()) {
				item = new JsonObject();
				item.addProperty("violation", jdocViolation);

				if (!jdocViolation) {
					policy.setJvd_deduct_point(0.0D);
				}

				item.addProperty("deductedPoint", policy.getJvd_deduct_point());
				scoresheet.add("javadoc", item);

				policy.deduct_point(policy.getJvd_deduct_point());
			}

			if (policy.isThreads()) {
				item = new JsonObject();
				item.addProperty("violation", threadViolation);

				if (!threadViolation) {
					policy.setThr_deduct_point(0.0D);
				}

				item.addProperty("deductedPoint", policy.getThr_deduct_point());
				scoresheet.add("thread", item);

				policy.deduct_point(policy.getThr_deduct_point());
			}

			double deducted;
			if (policy.getReqCustExc() != null && !policy.getReqCustExc().isEmpty()) {
				item = new JsonObject();
				item.addProperty("violation", customExcViolation);
				item.addProperty("violationCount", customExcViolationCount);

				deducted = policy.getCustomExc_deduct_point() * (double)customExcViolationCount;
				if (deducted > policy.getCustomExc_max_deduct()) {
					deducted = policy.getCustomExc_max_deduct();
				}

				item.addProperty("deductedPoint", deducted);
				scoresheet.add("customException", item);

				policy.deduct_point(deducted);
			}

			if (policy.getReqCusStruct() != null && !policy.getReqCusStruct().isEmpty()) {
				item = new JsonObject();
				item.addProperty("violation", customStructViolation);
				item.addProperty("violationCount", customStructViolationCount);

				deducted = policy.getCustomStr_deduct_point() * (double)customStructViolationCount;
				if (deducted > policy.getCustomStr_max_deduct()) {
					deducted = policy.getCustomStr_max_deduct();
				}

				item.addProperty("deductedPoint", deducted);
				scoresheet.add("customStructure", item);

				 policy.deduct_point(deducted);
			}

			if (policy.getReqMethod() != null && !policy.getReqMethod().isEmpty()) {
				item = new JsonObject();
				item.addProperty("violation", reqMethodViolation);
				item.addProperty("violationCount", reqMethodViolationCount);

				deducted = policy.getMethod_deduct_point() * (double) reqMethodViolationCount;
				if (deducted > policy.getMethod_max_deduct()) {
					deducted = policy.getMethod_max_deduct();
				}

				item.addProperty("deductedPoint", deducted);
				scoresheet.add("methods", item);

				policy.deduct_point(deducted);
			}

		} else {
			if (policy.isCount()) {
				item = new JsonObject();
				item.addProperty("violation", true);

				item.addProperty("deductedPoint", policy.getCnt_deduct_point());
				scoresheet.add("count", item);

				policy.deduct_point(policy.getCnt_deduct_point());
			}

			if (policy.isJavadoc()) {
				item = new JsonObject();
				item.addProperty("violation", true);

				item.addProperty("deductedPoint", policy.getJvd_deduct_point());
				scoresheet.add("javadoc", item);

				policy.deduct_point(policy.getJvd_deduct_point());
			}

			if (policy.isThreads()) {
				item = new JsonObject();
				item.addProperty("violation", true);

				item.addProperty("deductedPoint", policy.getThr_deduct_point());
				scoresheet.add("thread", item);

				policy.deduct_point(policy.getThr_deduct_point());
			}

			if (policy.getReqCustExc() != null && !policy.getReqCustExc().isEmpty()) {
				item = new JsonObject();
				item.addProperty("violation", true);
				item.addProperty("violationCount", policy.getReqCustExc().size());

				item.addProperty("deductedPoint", policy.getCustomExc_max_deduct());
				scoresheet.add("customException", item);

				policy.deduct_point(policy.getCustomExc_max_deduct());
			}

			if (policy.getReqCusStruct() != null && !policy.getReqCusStruct().isEmpty()) {
				item = new JsonObject();
				item.addProperty("violation", true);
				item.addProperty("violationCount", policy.getReqCusStruct().size());

				item.addProperty("deductedPoint", policy.getCustomStr_max_deduct());
				scoresheet.add("customStructure", item);

				policy.deduct_point(policy.getCustomStr_max_deduct());
			}

			if (policy.getReqMethod() != null && !policy.getReqMethod().isEmpty()) {
				item = new JsonObject();
				item.addProperty("violation", true);
				item.addProperty("violationCount", policy.getReqMethod().size());

				item.addProperty("deductedPoint", policy.getMethod_max_deduct());
				scoresheet.add("methods", item);

				policy.deduct_point(policy.getMethod_max_deduct());
			}
		}

		return scoresheet;
	}

	/**
	 * Method for collecting data of source codes
	 */
	private void collect() {
		for (String each: source){
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath, libPath, isBuild).createAST(null);
			getClassNames(unit);
			getInstances(unit);
			getCountInfo(unit);
			getMethodInvocation(unit);
		}
	}

	/**
	 * Method for checking implementation
	 */
	private void test() {
		for (String each : source){
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath, libPath, isBuild).createAST(null);
			if (policy.isJavadoc()) {
				testJavadoc(unit);
			}
		}

		if (policy.isCount()) {
			testCount();
		}

		if (policy.isThreads()) {
			testThread();
		}

		if (policy.getReqCustExc() != null && !policy.getReqCustExc().isEmpty()) {
			testCustomException();
		}

		if (policy.getReqCusStruct() != null && !policy.getReqCusStruct().isEmpty()) {
			testCustomStructure();
		}

		if (policy.getReqMethod() != null && !policy.getReqMethod().isEmpty()) {
			testMethods();
		}
	}

	/**
	 * Method for checking count
	 */
	private void testCount() {
		if (policy.getMethodCount() > numOfMethods) {
			countViolation = true;
		}

		if (policy.getFieldCount() > numOfFields) {
			countViolation = true;
		}

		if (policy.getEnForCount() > numOfEnhancedForStatement) {
			countViolation = true;
		}
	}

	/**
	 * Method for checking javadoc
	 * @param unit CompilationUnit
	 */
	private void testJavadoc(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node) {
					if (policy.getReqClass().contains(node.getName().toString()) && node.getJavadoc() == null) {
						jdocViolation = true;
						System.out.println(node.getName().toString());
					}

					for (MethodDeclaration each : node.getMethods()){
						if ((each.getModifiers() & Modifier.PUBLIC) > 0 && each.getJavadoc() == null) {
							jdocViolation = true;
							System.out.println(each.toString());
						}
					}

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method for checking thread
	 */
	private void testThread() {
		boolean checkThread = false;

		if (threads != null && !threads.isEmpty()) {
			for (String thread : threads){
				if (instances.contains(thread)) {
					checkThread = true;
				}
			}

			if (!checkThread && !instances.contains("Thread") && !isRunnable) {
				threadViolation = true;
			}

		} else if (!instances.contains("Thread") && !isRunnable) {
			threadViolation = true;
		}
	}

	/**
	 * Method for checking custom data structure
	 */
	private void testCustomStructure() {
		for (String each : policy.getReqCusStruct()){
			if (classes.contains(each)){
				if (!instances.contains(each)){
					customStructViolations.add(each);
					customStructViolationCount ++;
					customStructViolation = true;
				}
			}
			else {
				customStructViolations.add(each);
				customStructViolationCount ++;
				customStructViolation = true;
			}
		}
	}

	/**
	 * Method for checking custom exception
	 */
	private void testCustomException() {

		for (String each : policy.getReqCustExc()){
			if (classes.contains(each)){

				if (!superClasses.get(classes.indexOf(each)).equals("Exception") || superClasses.isEmpty()){
					customExcViolations.add(each);
					customExcViolationCount++;
					customExcViolation = true;
				}
				else {
					if (instances.contains(each)){
						String result = getStatement(each);

						if(!result.isEmpty()){
							if(!result.contains("throw")){
								expressions = getExpression();

								if(expressions.isEmpty()){
									customExcViolations.add(each);
									customExcViolationCount++;
									customExcViolation = true;
								}
								else {
									boolean checkExpression = false;

									for(String expression : expressions){
										if(result.contains(expression)){
											checkExpression = true;
											break;
										}
									}

									if(!checkExpression){
										customExcViolations.add(each);
										customExcViolationCount++;
										customExcViolation = true;
									}
								}
							}
						}

					}
					else {
						customExcViolations.add(each);
						customExcViolationCount++;
						customExcViolation = true;
					}
				}
			}
			else {
				customExcViolations.add(each);
				customExcViolationCount++;
				customExcViolation = true;
			}
		}
	}

	/**
	 * Method for checking method usage
	 */
	private void testMethods() {
		int index = 0;

		ArrayList<Integer> methodCounts = policy.getReqMethodCount();
		ArrayList<String> methodClasses = policy.getReqMethodClass();

		for (String each : policy.getReqMethod()) {
			String reqFullyQualified = methodClasses.get(index) + "." + each;

			int count = Collections.frequency(methodInvocations, reqFullyQualified);

			if (count < methodCounts.get(index)) {
				reqMethodViolation = true;
				reqMethodViolationCount ++;
			}

			index ++;
		}
	}

	/**
	 * Method for getting instances
	 * @param unit CompilationUnit
	 */
	private void getInstances(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(ClassInstanceCreation node) {
					instances.add(node.getType().toString());

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method for getting class names
	 * @param unit CompilationUnit
	 */
	private void getClassNames(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node) {
					classes.add(node.getName().toString());

					if (node.getSuperclassType() != null) {
						superClasses.add(node.getSuperclassType().toString());

						if (node.getSuperclassType().toString().equals("Thread")) {
							threads.add(node.getName().toString());
						}
					} else {
						superClasses.add("null");
					}

					for (Object each : node.superInterfaceTypes()){
						if (each.toString().equals("Runnable")){
							isRunnable = true;
						}
					}

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method for getting count information
	 * @param unit CompilationUnit
	 */
	private void getCountInfo(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(EnhancedForStatement node) {
					if (policy.getEnForCount() < 0) {
						return false;
					} else {
						numOfEnhancedForStatement ++;

						return super.visit(node);
					}
				}

				public boolean visit(TypeDeclaration node) {
					if (policy.getMethodCount() < 0 && policy.getFieldCount() < 0) {
						return false;
					} else {
						numOfFields += node.getFields().length;
						numOfMethods += node.getMethods().length;

						return super.visit(node);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Method for return statement information
	 * @param name target name information
	 * @return result
	 */
	public String getStatement(final String name) {
		for (String each : source){
			if (!result.isEmpty()) break;

			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath, libPath, isBuild).createAST(null);

			try {
				unit.accept(new ASTVisitor() {
					public boolean visit(ClassInstanceCreation node){
						if (node.getType().toString().equals(name)){
							result = node.getParent().toString();

							return false;
						}

						return super.visit(node);
					}
				});
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		return result;
	}

	/**
	 * Method for return expression information
	 * @return expressions
	 */
	public List<String> getExpression() {
		for (String each : source){
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath, libPath, isBuild).createAST(null);

			try {
				unit.accept(new ASTVisitor() {
					@Override
					public boolean visit(ThrowStatement node) {
						expressions.add(node.getExpression().toString());

						return super.visit(node);
					}
				});
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		return expressions;
	}

	/**
	 * Method for getting method invocation
	 * @param unit CompilationUnit
	 */
	public void getMethodInvocation(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(MethodInvocation node) {
					IMethodBinding methodBinding = node.resolveMethodBinding();

					if (methodBinding != null) {
						String className = methodBinding.getDeclaringClass().getQualifiedName();

						String methodName = node.getName().getIdentifier();

						String fullyQualifiedName = className + "." + methodName;

						methodInvocations.add(fullyQualifiedName);
					}

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
