package edu.isel.csee.jchecker2_0.statics;

import java.util.Map;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

/**
 * Class for AST analysis
 */
public class ASTChecker {
	private ASTParser parser;
	private String filePath;

	/**
	 * Method for setting parser properties and return parser
	 * @param source source code
	 * @param unit unit information
	 * @param filePath file path
	 * @param libPath library path
	 * @param isBuild isBuild
	 * @return parser
	 */
	public ASTParser parserSetProperties(String source, String unit, String filePath, String libPath, boolean isBuild) {
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

		if (isBuild) {
			if (libPath != null) {
				classpath = new String[]{"/usr/lib/jvm/java-14-openjdk-amd64/lib/jrt-fs.jar", filePath + libPath};
				// classpath = new String[]{"/Library/Java/JavaVirtualMachines/adoptopenjdk-15.jdk/Contents/Home/lib/jrt-fs.jar", filePath + libPath};
			} else {
				classpath = new String[]{"/usr/lib/jvm/java-14-openjdk-amd64/lib/jrt-fs.jar"};
				// classpath = new String[]{"/Library/Java/JavaVirtualMachines/adoptopenjdk-15.jdk/Contents/Home/lib/jrt-fs.jar"};
			}
		} else {
			classpath = new String[]{"/usr/lib/jvm/java-14-openjdk-amd64/lib/jrt-fs.jar", filePath + "bin"};
			// classpath = new String[]{"/Library/Java/JavaVirtualMachines/adoptopenjdk-15.jdk/Contents/Home/lib/jrt-fs.jar", filePath + "bin"};
		}

		parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);

		parser.setResolveBindings(true);
		parser.setCompilerOptions(options);
		parser.setIgnoreMethodBodies(false);
		parser.setBindingsRecovery(true);

		return parser;
	}

	/**
	 * Method for setting parser properties and return parser
	 * @param source source code
	 * @return parser
	 */
	public ASTParser parserSetProperties(String source) {
		char[] content = source.toCharArray();

		parser = ASTParser.newParser(AST.JLS15);
		parser.setUnitName("any_name");
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(content);

		Map<String, String> options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);

		String[] sources = { this.filePath };
		// String[] classpath = new String[]{"/Library/Java/JavaVirtualMachines/adoptopenjdk-15.jdk/Contents/Home/lib/jrt-fs.jar"};
		String[] classpath = new String[]{"/usr/lib/jvm/java-14-openjdk-amd64/lib/jrt-fs.jar"};

		parser.setEnvironment(classpath, sources, new String[]{"UTF-8"}, true);

		parser.setResolveBindings(true);
		parser.setCompilerOptions(options);
		parser.setIgnoreMethodBodies(false);
		parser.setBindingsRecovery(true);

		return parser;
	}

	/**
	 * Method for setting file path
	 * @param filePath file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
