package edu.isel.csee.jchecker.statics;

import java.util.ArrayList;

import java.util.List;

import org.eclipse.jdt.core.dom.*;

import com.google.gson.JsonObject;
import edu.isel.csee.jchecker.score.EvaluationSchemeMapper;



public class OOPChecker extends ASTChecker {
	private EvaluationSchemeMapper policy;
	private List<String> source = null;
	private String unitName;
	private List<IMethodBinding> methods = new ArrayList<>();
	private List<String> packages = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	
	private ArrayList<String> classesViolations = new ArrayList<>();
	private ArrayList<String> spcViolations = new ArrayList<>();
	private ArrayList<String> itfViolations = new ArrayList<>();
	private ArrayList<String> pkgViolations = new ArrayList<>();
	
	private boolean ecpViolation = false;
	
	
	private int classesViolationCount = 0;
	private int pkgViolationCount = 0;
	private int spcViolationCount = 0;
	private int itfViolationCount = 0;
	


	public OOPChecker(EvaluationSchemeMapper policy, List<String> source, String unitName)
	{
		this.policy = policy;
		this.source = source;
		this.unitName = unitName;
	}
	
	
	public JsonObject run(JsonObject scoresheet)
	{
		collect();
		
		test();
		

		if (policy.isEncaps()) {
			JsonObject item_ecp = new JsonObject();
			item_ecp.addProperty("violation", ecpViolation);
			
			if (!ecpViolation)
				policy.setEnc_deduct_point(0);
			
			
			item_ecp.addProperty("deducted", policy.getEnc_deduct_point());
			scoresheet.add("encapsulation", item_ecp);
			
			
			policy.deduct_point(policy.getEnc_deduct_point());
		}
		
		
		
		if (policy.getOverloading() != null && !policy.getOverloading().isEmpty()) {
			JsonObject item_ovl = new JsonObject();
			item_ovl.addProperty("violation-count", policy.getOverloading().size());
			
			
			double deducted = policy.getOvl_deduct_point() * policy.getOverloading().size();
			if (deducted > policy.getOvl_max_deduct())
				deducted = policy.getOvl_max_deduct();
			
			item_ovl.addProperty("deducted", deducted);
			scoresheet.add("overloading", item_ovl);
			
			
			policy.deduct_point(deducted);
		}
		
		
		
		if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()) {
			JsonObject item_ovr = new JsonObject();
			item_ovr.addProperty("violation-count", policy.getOverriding().size());
			
			
			double deducted = policy.getOvr_deduct_point() * policy.getOverriding().size();
			if (deducted > policy.getOvr_max_deduct())
				deducted = policy.getOvr_max_deduct();
			
			item_ovr.addProperty("deducted", deducted);
			scoresheet.add("overriding", item_ovr);
			
			
			policy.deduct_point(deducted);
		}

		
		
		if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()) {
			JsonObject item_class = new JsonObject();
			item_class.addProperty("violation-count", classesViolationCount);
			
			
			double deducted = policy.getClass_deduct_point() * classesViolationCount;
			if (deducted > policy.getClass_max_deduct())
				deducted = policy.getClass_max_deduct();
			
			item_class.addProperty("deducted", deducted);
			scoresheet.add("classes", item_class);
			
			
			policy.deduct_point(deducted);
		}
		
		
		
		if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()) {
			JsonObject item_pkg = new JsonObject();
			item_pkg.addProperty("violation-count", pkgViolationCount);
			
			
			double deducted = policy.getPackage_deduct_point() * pkgViolationCount;
			if (deducted > policy.getPackage_max_deduct())
				deducted = policy.getPackage_max_deduct();
			
			item_pkg.addProperty("deducted", deducted);
			scoresheet.add("package", item_pkg);
			
			
			policy.deduct_point(deducted);
		}
		
		
		
		if (policy.getSuperclass_pair() != null && !policy.getSuperclass_pair().isEmpty()) {
			JsonObject item_spc = new JsonObject();
			item_spc.addProperty("violation-count", spcViolationCount);
			
			
			double deducted = policy.getSpc_deduct_point() * spcViolationCount;
			if (deducted > policy.getSpc_max_deduct())
				deducted = policy.getSpc_max_deduct();
			
			item_spc.addProperty("deducted", deducted);
			scoresheet.add("inherit-super", item_spc);
			
			
			policy.deduct_point(deducted);
		}
		
		
		if (policy.getInterface_pair() != null && !policy.getInterface_pair().isEmpty()) {
			JsonObject item_itf = new JsonObject();
			item_itf.addProperty("violation-count", itfViolationCount);
			
			
			double deducted = policy.getItf_deduct_point() * itfViolationCount;
			if (deducted > policy.getItf_max_deduct())
				deducted = policy.getItf_max_deduct();
			
			item_itf.addProperty("deducted", deducted);
			scoresheet.add("inherit-interface", item_itf);
			
			
			policy.deduct_point(deducted);
		}
		
		return scoresheet;
	}

	
	
	private void collect()
	{
		for (String each : source) {
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName).createAST(null);
			
			
			if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()) {
				getMethodDeclarations(unit);
			}
	
			
			if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()) {
				getClassNames(unit);
			}
			
			
			if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()) {
				getPackageNames(unit);
			}
		}
	}
	
	
	
	private void test()
	{
		for (String each : source) {
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName).createAST(null);
			
			
			if (policy.isEncaps()) {
				testEncapsulation(unit);
			}
			
			
			if (policy.getOverloading() != null && !policy.getOverloading().isEmpty()) {
				testOverriding(unit);
			}
			
			
			if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()) {
				testOverriding(unit);
			}
			
			
			if (policy.getSuperclass_pair() != null && !policy.getSuperclass_pair().isEmpty()) {
				testSuperclass(unit);
			}
			
			
			if (policy.getInterface_pair() != null && !policy.getInterface_pair().isEmpty()) {
				testInterface(unit);
			}
			
		}
		
		if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()) {
			testRequiredClass();
		}
		
		
		if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()) {
			testRequiredPackage();
		}
	}
	
	
	
	
	private void testRequiredClass()
	{
		for (String each : policy.getReqClass())
		{
			if (!classes.contains(each)) {
				classesViolations.add(each);
				classesViolationCount++;
			}
				
		}
	}
	
	
	
	
	private void testRequiredPackage()
	{
		for (String each : policy.getPackageName())
		{
			if (!packages.contains(each)) {
				pkgViolations.add(each);
				pkgViolationCount++;
			}
		}
	}
	
	
	
	private void testSuperclass(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node)
				{
					if (!policy.getSuperclass_pair().containsKey(node.getName().toString()))
						return super.visit(node);
					
					if (policy.getSuperclass_pair().get(node.getName().toString()).equals(node.getSuperclassType().toString())) {
						spcViolations.add(policy.getSuperclass_pair().get(node.getName().toString()));
						spcViolationCount++;
					}
					
					
					return super.visit(node);
				}
			});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private void testInterface(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node)
				{
					boolean flag = false;
					if (!policy.getInterface_pair().containsKey(node.getName().toString()))
						return super.visit(node);
					
					for (Object each : node.superInterfaceTypes()) {
						if (policy.getInterface_pair().get(node.getName().toString()).equals(each.toString())) {
							flag = true;
						}
					}
					
					if (!flag) {
						itfViolations.add(policy.getInterface_pair().get(node.getName().toString()));
						itfViolationCount++;
					}
					
					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void testOverriding(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(MethodDeclaration node)
				{
					if (policy.getOverriding().contains(node.getName().toString())) {
						for (IMethodBinding each : methods)
						{
							if (node.resolveBinding().overrides(each)) {
								policy.getOverriding().remove(node.getName().toString());
								return false;
							}
						}
					}
					
					return super.visit(node);
				}
			});
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public void testOverloading(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node)
				{
					MethodDeclaration[] tmp = node.getMethods();
					ArrayList<String> currentMethods = new ArrayList<>();
					
					for (MethodDeclaration each : tmp)
						currentMethods.add(each.toString());
					
					
					for (String ovl : policy.getOverloading()) 
					{
						for (String each : currentMethods)
						{
							if (each.equals(ovl)) {
								currentMethods.remove(each);
								
								
								if (currentMethods.contains(ovl))
									policy.getOverloading().remove(ovl);
								
							}
						}
					}
					
					
					return super.visit(node);
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	private void testEncapsulation(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit (TypeDeclaration node)
				{
					int public_count = 0;
					
					
					if (!classes.contains(node.getName().toString()))
						return false;
					
					
					for (FieldDeclaration eachField : node.getFields())
					{
						if ((eachField.getModifiers() & Modifier.PRIVATE) <= 0)
							ecpViolation = true;
						
					}
					
					
					for (MethodDeclaration eachMethod : node.getMethods())
					{
						if ((eachMethod.getModifiers() & Modifier.PUBLIC) > 0)
							public_count++;
					}
					
					
					if (public_count == 0)
						ecpViolation = true;
					
					
					
					return super.visit(node);
				}
			});
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void getMethodDeclarations(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(MethodDeclaration node)
				{
					methods.add(node.resolveBinding().getMethodDeclaration());
					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

	private void getPackageNames(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(PackageDeclaration node)
				{
					packages.add(node.getName().toString());
					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void getClassNames(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node)
				{
					classes.add(node.getName().toString());
					
					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}