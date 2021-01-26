package edu.isel.csee.jchecker.statics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.*;



public class OOPChecker extends ASTChecker {
	private List<IMethodBinding> methods = new ArrayList<>();
	private List<String> invocations = new ArrayList<>();
	private List<String> packages = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	
	
	private boolean violation_cap = false;
	
	
	
	
	
	public void run(List<String> source, String unitName)
	{
		for (String each : source) {
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName).createAST(null);
			getMethodDeclarations(unit);
			getClassNames(unit);
			getPackageNames(unit);
		}
		
		
		
	}
	
	
	
	
	
	
	/* TODO: input / output format 정의 필요
	
	private void testInterface(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node)
				{
					
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	*/
	
	
	public void testOverriding(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(MethodDeclaration node)
				{
					
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
						if ((eachField.getModifiers() & Modifier.PUBLIC) > 0) {
							
							//TODO: 어떻게 violations를 넘겨줄 것인가?
							violation_cap = true;
						}
					}
					
					
					for (MethodDeclaration eachMethod : node.getMethods())
					{
						if ((eachMethod.getModifiers() & Modifier.PUBLIC) > 0)
							public_count++;
					}
					
					
					if (public_count == 0) {
						 violation_cap = true;
					}
					
					
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
