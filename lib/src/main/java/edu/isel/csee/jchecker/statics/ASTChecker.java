package edu.isel.csee.jchecker.statics;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

public class ASTChecker 
{
	
	private ASTParser parser;
	private String filePath ;
	
	public ASTParser parserSetProperties(String source, String unit, String filePath, boolean isBuild)
	{
		char[] content = source.toCharArray();
		String[] classpath;
		
		this.filePath = filePath;
		
		parser = ASTParser.newParser(AST.JLS15);
		parser.setUnitName("any_name");
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(content);
		
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		
		String[] sources = { this.filePath };
		
		if(isBuild)
		{
			// classpath = new String[] { "/Library/Java/JavaVirtualMachines/jdk-15.0.2.jdk/Contents/Home/lib/jrt-fs.jar" , filePath + "app/build/libs/app.jar" };
			if(filePath.contains("app"))
			{
				System.out.println("Contain app in " + filePath);
				classpath = new String[] { "/usr/lib/jvm/java-14-openjdk-amd64/lib/jrt-fs.jar" , filePath + "build/libs/app.jar" };
			}
			else
			{
				System.out.println("Not contain app in " + filePath);
				classpath = new String[] { "/usr/lib/jvm/java-14-openjdk-amd64/lib/jrt-fs.jar" , filePath + "app/build/libs/app.jar" };
			}
		}
		else
			// classpath = new String[] { "/Library/Java/JavaVirtualMachines/jdk-15.0.2.jdk/Contents/Home/lib/jrt-fs.jar"  , filePath + "bin" };
			classpath = new String[] { "/usr/lib/jvm/java-14-openjdk-amd64/lib/jrt-fs.jar"  , filePath + "bin" };
			
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
		
		
		parser.setResolveBindings(true);
		parser.setCompilerOptions(options);
		parser.setIgnoreMethodBodies(false);
		parser.setBindingsRecovery(true);		
		
		
		return parser;
	}
	
	public ASTParser parserSetProperties(String source)
	{
		char[] content = source.toCharArray();
		
		parser = ASTParser.newParser(AST.JLS15);
		parser.setUnitName("any_name") ;
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(content);
		
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		
		String[] sources = { this.filePath };
		// String[] classpath = { "/usr/lib/jvm/java-14-openjdk-amd64/lib/jrt-fs.jar" };
		String[] classpath = { "/Library/Java/JavaVirtualMachines/jdk-15.0.2.jdk/Contents/Home/lib/jrt-fs.jar" };
		
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
		
		parser.setResolveBindings(true);
		parser.setCompilerOptions(options);
		parser.setIgnoreMethodBodies(false);
		parser.setBindingsRecovery(true);		
		
		
		return parser;
	}
	
	public void setFilePath(String filePath) { this.filePath = filePath ; }
}
