package edu.isel.csee.jchecker2_0.statics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.jdt.core.dom.*;
import com.google.gson.JsonObject;
import edu.isel.csee.jchecker2_0.score.EvaluationSchemeMapper;

/**
 * Class for checking OOP concepts
 */
public class OOPChecker extends ASTChecker {
	private EvaluationSchemeMapper policy;
	private List<String> source = null;
	private String unitName;
	private String filePath;
	private String libPath;
	private List<IMethodBinding> methods = new ArrayList<>();
	private List<String> packages = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	private HashMap<String, List<ReturnStatement>> methodReturnGroupMap = new HashMap<>();
	private HashMap<String, List<Assignment>> methodAssignmentGroupMap = new HashMap<>();

	private ArrayList<String> classesViolations = new ArrayList<>();
	private ArrayList<String> spcViolations = new ArrayList<>();
	private ArrayList<String> itfViolations = new ArrayList<>();
	private ArrayList<String> pkgViolations = new ArrayList<>();
	private boolean isInterface = false;
	private boolean isSuperclass = false;
	private boolean isOverriding = false;
	private boolean isOverloading = false;
	private boolean isBuild = false;

	private boolean ecpViolation = false;
	private boolean clasViolation = false;
	private boolean pkgViolation = false;
	private boolean itfViolation = false;
	private boolean spcViolation = false;

	private int classesViolationCount = 0;
	private int pkgViolationCount = 0;
	private int spcViolationCount = 0;
	private int itfViolationCount = 0;

	/**
	 * Constructor for OOPChecker class
	 * @param policy EvaluationSchemeMapper
	 * @param source source code
	 * @param unitName unit information
	 * @param filePath file path
	 * @param libPath library path
	 */
	public OOPChecker(EvaluationSchemeMapper policy, List<String> source, String unitName, String filePath, String libPath) {
		this.policy = policy;
		this.source = source;
		this.unitName = unitName;
		this.filePath = filePath;
		this.libPath = libPath;
	}

	/**
	 * Method for scoring OOP concepts and setting result
	 * @param scoresheet JSON data (result)
	 * @param isBuild isBuild
	 * @return scoresheet
	 */
	public JsonObject run(JsonObject scoresheet, boolean isBuild) {
		this.isBuild = isBuild;

		if (!source.isEmpty() && source != null) {
			collect();
			test();

			if (policy.isEncaps()) {
				JsonObject item_ecp = new JsonObject();

				item_ecp.addProperty("violation", ecpViolation);

				if (!ecpViolation) {
					policy.setEnc_deduct_point(0.0D);
				}

				item_ecp.addProperty("deductedPoint", policy.getEnc_deduct_point());
				scoresheet.add("encapsulation", item_ecp);

				policy.deduct_point(policy.getEnc_deduct_point());
			}

			double deducted;
			if (isOverloading) {
				JsonObject item_ovl = new JsonObject();

				if (policy.getOverloading().size() > 0) {
					item_ovl.addProperty("violation", true);
				} else {
					item_ovl.addProperty("violation", false);
				}

				item_ovl.addProperty("violationCount", policy.getOverloading().size());

				deducted = policy.getOvl_deduct_point() * (double)policy.getOverloading().size();
				if (deducted > policy.getOvl_max_deduct()) {
					deducted = policy.getOvl_max_deduct();
				}

				item_ovl.addProperty("deductedPoint", deducted);
				scoresheet.add("overloading", item_ovl);

				policy.deduct_point(deducted);
			}

			if (isOverriding) {
				JsonObject item_ovr = new JsonObject();
				if (policy.getOverriding().size() > 0) {
					item_ovr.addProperty("violation", true);
				} else {
					item_ovr.addProperty("violation", false);
				}

				item_ovr.addProperty("violationCount", policy.getOverriding().size());

				deducted = policy.getOvr_deduct_point() * (double)policy.getOverriding().size();
				if (deducted > policy.getOvr_max_deduct()) {
					deducted = policy.getOvr_max_deduct();
				}

				item_ovr.addProperty("deductedPoint", deducted);
				scoresheet.add("overriding", item_ovr);

				policy.deduct_point(deducted);
			}

			if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()) {
				JsonObject item_class = new JsonObject();

				item_class.addProperty("violation", clasViolation);
				item_class.addProperty("violationCount", classesViolationCount);

				deducted = policy.getClass_deduct_point() * (double)classesViolationCount;
				if (deducted > policy.getClass_max_deduct()) {
					deducted = policy.getClass_max_deduct();
				}

				item_class.addProperty("deductedPoint", deducted);
				scoresheet.add("classes", item_class);

				policy.deduct_point(deducted);
			}

			if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()) {
				JsonObject item_pkg = new JsonObject();
				item_pkg.addProperty("violation", pkgViolation);
				item_pkg.addProperty("violationCount", pkgViolationCount);
				deducted = this.policy.getPackage_deduct_point() * (double)this.pkgViolationCount;
				if (deducted > this.policy.getPackage_max_deduct()) {
					deducted = this.policy.getPackage_max_deduct();
				}

				item_pkg.addProperty("deductedPoint", deducted);
				scoresheet.add("packages", item_pkg);

				policy.deduct_point(deducted);
			}

			if (isSuperclass) {
				JsonObject item_spc = new JsonObject();

				if (spcViolationCount + policy.getSoriginClass().size() > 0) {
					item_spc.addProperty("violation", true);
				} else {
					item_spc.addProperty("violation", false);
				}

				item_spc.addProperty("violationCount", spcViolationCount + policy.getSoriginClass().size());

				deducted = policy.getSpc_deduct_point() * (double)(spcViolationCount + policy.getSoriginClass().size());
				if (deducted > policy.getSpc_max_deduct()) {
					deducted = policy.getSpc_max_deduct();
				}

				item_spc.addProperty("deductedPoint", deducted);
				scoresheet.add("inheritSuper", item_spc);

				policy.deduct_point(deducted);
			}

			if (isInterface) {
				JsonObject item_itf = new JsonObject();

				if (itfViolationCount + policy.getIoriginClass().size() > 0) {
					item_itf.addProperty("violation", true);
				} else {
					item_itf.addProperty("violation", false);
				}

				item_itf.addProperty("violationCount", itfViolationCount + policy.getIoriginClass().size());
				deducted = policy.getItf_deduct_point() * (double)(itfViolationCount + policy.getIoriginClass().size());
				if (deducted > policy.getItf_max_deduct()) {
					deducted = policy.getItf_max_deduct();
				}

				item_itf.addProperty("deductedPoint", deducted);
				scoresheet.add("inheritInterface", item_itf);

				policy.deduct_point(deducted);
			}
		} else {
			if (policy.isEncaps()) {
				JsonObject item_ecp = new JsonObject();

				item_ecp.addProperty("violation", true);

				item_ecp.addProperty("deductedPoint", policy.getEnc_deduct_point());
				scoresheet.add("encapsulation", item_ecp);

				policy.deduct_point(policy.getEnc_deduct_point());
			}

			if (policy.getOverloading() != null && !policy.getOverloading().isEmpty()) {
				JsonObject item_ovl = new JsonObject();

				item_ovl.addProperty("violation", true);
				item_ovl.addProperty("violationCount", policy.getOverloading().size());

				item_ovl.addProperty("deductedPoint", policy.getOvl_max_deduct());
				scoresheet.add("overloading", item_ovl);

				policy.deduct_point(policy.getOvl_max_deduct());
			}

			if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()) {
				JsonObject item_ovr = new JsonObject();

				item_ovr.addProperty("violation", true);
				item_ovr.addProperty("violationCount", policy.getOverriding().size());

				item_ovr.addProperty("deductedPoint", policy.getOvr_max_deduct());
				scoresheet.add("overriding", item_ovr);

				policy.deduct_point(policy.getOvr_max_deduct());
			}

			if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()) {
				JsonObject item_class = new JsonObject();

				item_class.addProperty("violation", true);
				item_class.addProperty("violationCount", policy.getReqClass().size());

				item_class.addProperty("deductedPoint", policy.getClass_max_deduct());
				scoresheet.add("classes", item_class);

				policy.deduct_point(policy.getClass_max_deduct());
			}

			if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()) {
				JsonObject item_pkg = new JsonObject();

				item_pkg.addProperty("violation", true);
				item_pkg.addProperty("violationCount", policy.getPackageName().size());

				item_pkg.addProperty("deductedPoint", policy.getPackage_max_deduct());
				scoresheet.add("packages", item_pkg);

				policy.deduct_point(policy.getPackage_max_deduct());
			}

			if (policy.getSuperClass() != null && !policy.getSuperClass().isEmpty()) {
				JsonObject item_spc = new JsonObject();

				item_spc.addProperty("violation", true);
				item_spc.addProperty("violationCount", policy.getSoriginClass().size());

				item_spc.addProperty("deductedPoint", policy.getSpc_max_deduct());
				scoresheet.add("inheritSuper", item_spc);

				policy.deduct_point(policy.getSpc_max_deduct());
			}

			if (policy.getInterfaceClass() != null && !policy.getInterfaceClass().isEmpty()) {
				JsonObject item_itf = new JsonObject();

				item_itf.addProperty("violation", true);
				item_itf.addProperty("violationCount", policy.getIoriginClass().size());

				item_itf.addProperty("deductedPoint", policy.getItf_max_deduct());
				scoresheet.add("inheritInterface", item_itf);

				policy.deduct_point(policy.getItf_max_deduct());
			}
		}

		return scoresheet;
	}

	/**
	 * Method for collecting data of source codes
	 */
	private void collect() {
		for (String each : source){
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath, libPath, isBuild).createAST(null);

			if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()){
				getMethodDeclarations(unit);
			}

			if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()){
				getClassNames(unit);
			}

			if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()){
				getPackageNames(unit);
			}
		}
	}

	/**
	 * Method for checking OOP concepts
	 */
	private void test() {
		for (String each : source){
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath, libPath, isBuild).createAST(null);

			if (policy.isEncaps()){
				getReturnStatements(unit);
				getAssignments(unit);
				testEncapsulation(unit);
			}

			if (policy.getOverloading() != null && !policy.getOverloading().isEmpty()){
				isOverloading = true;
				testOverloading(unit);
			}

			if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()){
				isOverriding = true;
				testOverriding(unit);
			}

			if (policy.getSuperClass() != null && !policy.getSuperClass().isEmpty()){
				isSuperclass = true;
				testSuperclass(unit);
			}

			if (policy.getInterfaceClass() != null && !policy.getInterfaceClass().isEmpty()){
				isInterface = true;
				testInterface(unit);
			}
		}

		if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()){
			testRequiredClass();
		}

		if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()){
			testRequiredPackage();
		}
	}

	/**
	 * Method for checking required class
	 */
	private void testRequiredClass() {
		for (String each : policy.getReqClass()){
			if (!classes.contains(each)){
				classesViolations.add(each);
				classesViolationCount ++;
				clasViolation = true;
			}
		}
	}

	/**
	 * Method for checking required package
	 */
	private void testRequiredPackage() {
		for (String each : policy.getPackageName()){
			if (!packages.contains(each)){
				pkgViolations.add(each);
				pkgViolationCount ++;
				pkgViolation = true;
			}
		}
	}

	/**
	 * Method for checking inheritance (superclass)
	 * @param unit CompilationUnit
	 */
	private void testSuperclass(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration node) {
					int idx = -1;
					if (!policy.getSoriginClass().contains(node.getName().toString())){
						return super.visit(node);
					}
					else {
						idx = policy.getSoriginClass().indexOf(node.getName().toString());
					}

					if(!policy.getSuperClass().get(idx).equals(node.getSuperclassType().toString())){
						spcViolations.add(node.getName().toString());
						spcViolationCount ++;
					}
					else {
						policy.getSoriginClass().remove(idx);
						policy.getSuperClass().remove(idx);
					}

					return super.visit(node);
				}
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Method for checking inheritance (interface)
	 * @param unit CompilationUnit
	 */
	private void testInterface(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration node) {
					boolean flag = false;
					int idx = -1;

					if (!policy.getIoriginClass().contains(node.getName().toString())){
						return super.visit(node);
					}
					else {
						idx = policy.getIoriginClass().indexOf(node.getName().toString());
					}

					for (Object each : node.superInterfaceTypes()){
						if (policy.getInterfaceClass().get(idx).equals(each.toString())){
							flag = true;
							policy.getIoriginClass().remove(idx);
							policy.getInterfaceClass().remove(idx);
							break;
						}
					}

					if (!flag){
						itfViolations.add(node.getName().toString());
						itfViolationCount ++;
					}

					return super.visit(node);
				}
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Method for checking overriding method
	 * @param unit CompilationUnit
	 */
	public void testOverriding(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(MethodDeclaration node) {
					if (policy.getOverriding().contains(node.getName().toString())){
						// e.g. subclass method()
						IMethodBinding methodBinding = node.resolveBinding();

						// e.g. subclass
						ITypeBinding classBinding = methodBinding.getDeclaringClass();

						// e.g. super class
						ITypeBinding superclassBinding = classBinding.getSuperclass();

						if (superclassBinding != null){
							for (IMethodBinding parentBinding : superclassBinding.getDeclaredMethods()){
								if (methodBinding.overrides(parentBinding)){
									policy.getOverriding().remove(node.getName().toString());
								}
							}
						}
					}
					return super.visit(node);
				}
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Method for checking overloading method
	 * @param unit CompilationUnit
	 */
	public void testOverloading(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration node) {
					MethodDeclaration[] tmp = node.getMethods();
					ArrayList<String> currentMethods = new ArrayList<>();

					for (MethodDeclaration each : tmp){
						currentMethods.add(each.toString());
					}

					for (String ovl : policy.getOverloading()){
						for (String each : currentMethods){
							if (each.equals(ovl)){
								currentMethods.remove(each);
								if (currentMethods.contains(ovl)){
									policy.getOverloading().remove(ovl);
								}
							}
						}
					}

					return super.visit(node);
				}
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Method for checking encapsulation
	 * @param unit CompilationUnit
	 */
	private void testEncapsulation(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(TypeDeclaration node) {
					if (!policy.getReqClass().contains(node.resolveBinding().getQualifiedName())){
						return false;
					}

					for (FieldDeclaration eachField : node.getFields()){
						if ((eachField.getModifiers() & Modifier.FINAL) > 0) {
							continue;
						}

						// Get field name
						VariableDeclarationFragment fieldFragment = (VariableDeclarationFragment) eachField.fragments().get(0);
						String fieldName = fieldFragment.getName().getIdentifier();

						// Private field
						if ((eachField.getModifiers() & Modifier.PRIVATE) > 0) {
							boolean hasGetter = false;
							boolean hasSetter = false;

							// Check the method (getter & setter)
							for (MethodDeclaration eachMethod : node.getMethods()) {
								// Check the public method
								if ((eachMethod.getModifiers() & Modifier.PUBLIC) > 0) {
									String methodName = eachMethod.getName().getIdentifier();

									// Check the return statement (for the getter method)
									if (methodReturnGroupMap.containsKey(methodName)) {
										for (ReturnStatement returnStatement : methodReturnGroupMap.get(methodName)) {
											Expression expression = returnStatement.getExpression();

											if (expression instanceof SimpleName && ((SimpleName) expression).getIdentifier().equals(fieldName)
													|| expression instanceof FieldAccess && ((FieldAccess) expression).getName().getIdentifier().equals(fieldName)
													|| expression instanceof QualifiedName && ((QualifiedName) expression).getName().getIdentifier().equals(fieldName)) {
												hasGetter = true;
											}
										}
									}

									// Check the assignment statement (for the setter method & constructor)
									if (methodAssignmentGroupMap.containsKey(methodName)) {
										for (Assignment assignment : methodAssignmentGroupMap.get(methodName)) {
											Expression leftHandSide = assignment.getLeftHandSide();

											if (leftHandSide.toString().equals(fieldName) || leftHandSide.toString().equals("this." + fieldName)) {
												if (eachMethod.isConstructor()) {
													hasSetter = true;
												} else {
													hasSetter = true;
												}
											}
										}
									}
								}
							}

							if (!hasGetter || !hasSetter ) {
								ecpViolation = true;
							}

						} else if ((eachField.getModifiers() & Modifier.PUBLIC) > 0) {
							if ((eachField.getModifiers() & Modifier.STATIC) <= 0) {
								ecpViolation = true;
							}
						} else {
							if ((eachField.getModifiers() & Modifier.PROTECTED) <= 0) {
								ecpViolation = true;
							}
						}
					}

					return super.visit(node);
				}
			});


		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Method for getting return statements
	 * @param unit CompilationUnit
	 */
	private void getReturnStatements(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(ReturnStatement node) {
					List<ReturnStatement> returnStatements;

					ASTNode parent = node.getParent();
					while (parent != null) {
						if (parent.getNodeType() == ASTNode.METHOD_DECLARATION) {
							MethodDeclaration method = (MethodDeclaration) parent;
							String methodName = method.getName().getIdentifier();

							if (!methodReturnGroupMap.containsKey(methodName)) {
								returnStatements = new ArrayList<>();
							} else {
								returnStatements = methodReturnGroupMap.get(methodName);
							}

							returnStatements.add(node);
							methodReturnGroupMap.put(methodName, returnStatements);

							break;
						}
						parent = parent.getParent();
					}

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for getting assignment statements
	 * @param unit CompilationUnit
	 */
	private void getAssignments(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(Assignment node) {
					List<Assignment> assignments;

					ASTNode parent = node.getParent();
					while (parent != null) {
						if (parent.getNodeType() == ASTNode.METHOD_DECLARATION) {
							MethodDeclaration method = (MethodDeclaration) parent;
							String methodName = method.getName().getIdentifier();

							if (!methodAssignmentGroupMap.containsKey(methodName)) {
								assignments = new ArrayList<>();
							} else {
								assignments = methodAssignmentGroupMap.get(methodName);
							}

							assignments.add(node);
							methodAssignmentGroupMap.put(methodName, assignments);

							break;
						}
						parent = parent.getParent();
					}

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Method for getting MethodDeclaration
	 * @param unit CompilationUnit
	 */
	private void getMethodDeclarations(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(MethodDeclaration node) {
					methods.add(node.resolveBinding().getMethodDeclaration());

					return super.visit(node);
				}
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Method for getting package name
	 * @param unit CompilationUnit
	 */
	private void getPackageNames(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				@Override
				public boolean visit(PackageDeclaration node) {
					packages.add(node.getName().toString());

					return super.visit(node);
				}
			});
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Method for getting class name
	 * @param unit CompilationUnit
	 */
	private void getClassNames(CompilationUnit unit) {
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node) {
					String fqn = node.resolveBinding().getPackage().toString().replace("package ", "") + "." + node.getName().toString();
					fqn = fqn.replace("The Default Package.", "");
					classes.add(fqn);

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
