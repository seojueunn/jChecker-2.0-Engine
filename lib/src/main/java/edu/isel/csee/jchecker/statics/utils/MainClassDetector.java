package edu.isel.csee.jchecker.statics.utils;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import edu.isel.csee.jchecker.statics.ASTChecker;



public class MainClassDetector extends ASTChecker 
{
	private String mainPath = "";
	
	public String find(String source)
	{
		
		ASTParser parser = parserSetProperties(source);
		CompilationUnit unit = (CompilationUnit) parser.createAST(null);
		
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(MethodDeclaration node)
				{
					String qualified = node.resolveBinding().getMethodDeclaration().toString().trim();
					
					if (qualified.equals("public static void main(java.lang.String[])"))
						mainPath = node.resolveBinding().getDeclaringClass().getQualifiedName();
					
					
					return super.visit(node);
				}
			});
		} 
		catch (Exception e) 
		{ 
			System.out.println("MainNotFound Error: No main class in the source list");
			e.printStackTrace(); 
		}
		
		
		return mainPath;
	}
}
