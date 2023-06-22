package edu.isel.csee.jchecker2_0.statics.utils;

import edu.isel.csee.jchecker2_0.statics.ASTChecker;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

/**
 * Class for detecting main class
 */
public class MainClassDetector extends ASTChecker {
	private String mainPath = "";

	/**
	 * Method for finding main class path
	 * @param source source code
	 * @return mainPath
	 */
	public String find(String source) {
		ASTParser parser = this.parserSetProperties(source);
		CompilationUnit unit = (CompilationUnit)parser.createAST(null);

		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(MethodDeclaration node) {
					String qualified = node.resolveBinding().getMethodDeclaration().toString().trim();
					if (qualified.equals("public static void main(java.lang.String[])")) {
						mainPath = node.resolveBinding().getDeclaringClass().getQualifiedName();
					}

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			System.out.println("MainNotFound Error: No main class in the source list");
			e.printStackTrace();
		}

		return mainPath;
	}
}
