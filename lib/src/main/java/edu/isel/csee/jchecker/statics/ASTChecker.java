package edu.isel.csee.jchecker.statics;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

public class ASTChecker {

	private ASTParser parser;

	
	public ASTParser parserSetProperties(String source, String unit)
	{
		char[] content = source.toCharArray();
		
		parser = ASTParser.newParser(AST.JLS15);
		parser.setUnitName(unit);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(content);
		
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		
		String[] sources = { System.getProperty("user.dir") };
		String[] classpath = { };
		
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
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(content);
		
		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		
		String[] sources = { System.getProperty("user.dir") };
		String[] classpath = { };
		
		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
		
		
		parser.setResolveBindings(true);
		parser.setCompilerOptions(options);
		parser.setIgnoreMethodBodies(false);
		parser.setBindingsRecovery(true);		
		
		
		return parser;
	}
}
