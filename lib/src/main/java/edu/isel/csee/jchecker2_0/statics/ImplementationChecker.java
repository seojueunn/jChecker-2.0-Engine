package edu.isel.csee.jchecker2_0.statics;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import org.eclipse.jdt.core.dom.*;

import edu.isel.csee.jchecker2_0.score.EvaluationSchemeMapper;

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

	private ArrayList<String> customExcViolations = new ArrayList<>();
	private ArrayList<String> customStructViolations = new ArrayList<>();

	private boolean jdocViolation = false;
	private boolean threadViolation = false;
	private boolean countViolation = false;
	private boolean customExcViolation = false;
	private boolean customStructViolation = false;
	private boolean isBuild = false;
	private boolean isRunnable = false;

	private int customExcViolationCount = 0;
	private int customStructViolationCount = 0;
	private String result = "";

	private int numOfEnhancedForStatement = 0;
	private int numOfMethods = 0;
	private int numOfFields = 0;

	public ImplementationChecker(EvaluationSchemeMapper policy, List<String> source, String unitName, String filePath, String libPath) {
		this.policy = policy;
		this.source = source;
		this.unitName = unitName;
		this.filePath = filePath;
		this.libPath = libPath;
	}

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
		}

		return scoresheet;
	}

	private void collect() {
		for (String each: source){
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath, libPath, isBuild).createAST(null);
			getClassNames(unit);
			getInstances(unit);
			getCountInfo(unit);
		}
	}

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
	}

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
}
