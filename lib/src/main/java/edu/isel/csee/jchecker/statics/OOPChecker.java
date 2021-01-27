package edu.isel.csee.jchecker.statics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.*;
import com.google.gson.JsonObject;
import edu.isel.csee.jchecker.score.EvaluationSchemeMapper;



public class OOPChecker extends ASTChecker {
	private EvaluationSchemeMapper policy;
	private List<IMethodBinding> methods = new ArrayList<>();
	private List<String> invocations = new ArrayList<>();
	private List<String> packages = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	private Map<String, String> superclasses = new HashMap<>();
	private Map<String, String> interfaces = new HashMap<>();
	
	private ArrayList<String> runtimeViolations = null;
	private ArrayList<String> classesViolations = null;
	private ArrayList<String> methodsViolations = null;
	private ArrayList<String> customExcViolations = null;
	private ArrayList<String> customStrViolations = null;
	private ArrayList<String> spcViolations = null;
	private ArrayList<String> itfViolations = null;
	
	private boolean pkgViolation = false;
	private boolean ovrViolation = false;
	private boolean ovlViolation = false;
	private boolean jvdViolation = false;
	private boolean ecpViolation = false;
	
	
	private int runtimeViolationCount = 0;
	private int classesViolationCount = 0;
	private int methodsViolationCount = 0;
	private int spcViolationCount = 0;
	private int itfViolationCount = 0;
	
	
	private JsonObject scoresheet;
	
	
	
	
	public OOPChecker(EvaluationSchemeMapper policy)
	{
		this.policy = policy;
		
	}
	
	
	public void run(List<String> source, String unitName)
	{
		for (String each : source) {
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName).createAST(null);
			getMethodDeclarations(unit);
			getClassNames(unit);
			getPackageNames(unit);
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
					if (!policy.getOverriding().isEmpty()) {
						if (policy.getOverriding().contains(node.getName().toString())) {
							for (IMethodBinding each : methods)
							{
								if (node.resolveBinding().overrides(each)) {
									policy.getOverriding().remove(node.getName().toString());
									return false;
								}
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
						if ((eachField.getModifiers() & Modifier.PUBLIC) > 0)
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
