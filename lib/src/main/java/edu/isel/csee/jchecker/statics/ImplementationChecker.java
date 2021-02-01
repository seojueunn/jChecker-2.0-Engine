package edu.isel.csee.jchecker.statics;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.google.gson.JsonObject;

import edu.isel.csee.jchecker.score.EvaluationSchemeMapper;


public class ImplementationChecker extends ASTChecker {
	EvaluationSchemeMapper policy;
	private List<String> source = null;
	private String unitName;
	private List<String> instances = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	private List<String> superClasses = new ArrayList<>();
	private List<String> threads = new ArrayList<>();
	
	private ArrayList<String> customExcViolations = new ArrayList<>();
	private ArrayList<String> customStructViolations = new ArrayList<>();
	
	private boolean jdocViolation = false;
	private boolean threadViolation = false;
	private int customExcViolationCount = 0;
	private int customStructViolationCount = 0;
	
	
	
	public ImplementationChecker(EvaluationSchemeMapper policy, List<String> source, String unitName)
	{
		this.policy = policy;
		this.source = source;
		this.unitName = unitName;
	}
	
	
	
	public JsonObject run(JsonObject scoresheet)
	{
		collect();
		
		test();
		
		
		if (policy.isJavadoc()) {
			JsonObject item = new JsonObject();
			item.addProperty("violation", jdocViolation);
			
			if (!jdocViolation)
				policy.setJvd_deduct_point(0);
			
			item.addProperty("deducted-point", policy.getJvd_deduct_point());
			scoresheet.add("javadoc", item);
		}
		
		
		if (policy.isThreads()) {
			JsonObject item = new JsonObject();
			item.addProperty("violation", threadViolation);
			
			if (!threadViolation)
				policy.setThr_deduct_point(0);
			
			item.addProperty("deducted-point", policy.getThr_deduct_point());
			scoresheet.add("threads", item);
		}
		
		
		if (policy.getReqCustExc() != null && !policy.getReqCustExc().isEmpty()) {
			JsonObject item = new JsonObject();
			item.addProperty("violation-count", customExcViolationCount);
			
			
			double deducted = policy.getCustomExc_deduct_point() * customExcViolationCount;
			if (deducted > policy.getCustomExc_max_deduct())
				deducted = policy.getCustomExc_max_deduct();
			
			item.addProperty("deducted-point", policy.getCustomExc_deduct_point() * customExcViolationCount);
			scoresheet.add("custom-exception", item);
		}
		
		
		if (policy.getReqCusStruct() != null && !policy.getReqCusStruct().isEmpty()) {
			JsonObject item = new JsonObject();
			item.addProperty("violation-count", customStructViolationCount);
			
			
			double deducted = policy.getCustomStr_deduct_point() * customStructViolationCount;
			if (deducted > policy.getCustomStr_max_deduct())
				deducted = policy.getCustomStr_max_deduct();
			
			item.addProperty("deducted-point", policy.getCustomStr_deduct_point() * customStructViolationCount);
			scoresheet.add("custom-structure", item);
		}
		
		return scoresheet;
	}
	
	
	
	private void collect()
	{
		for (String each : source) {
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName).createAST(null);
			getClassNames(unit);
			getInstances(unit);
		}
	}
	
	
	
	private void test()
	{
		for (String each : source) {
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName).createAST(null);
			
			if (policy.isJavadoc()) {
				testJavadoc(unit);
			}

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
	
	
	private void testJavadoc(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(TypeDeclaration node)
				{
					if (policy.getReqClass().contains(node.getName().toString())) {
						if (node.getJavadoc() == null)
							jdocViolation = true;
						
					}
					
					
					
					for (MethodDeclaration each : node.getMethods())
					{
						if ((each.getModifiers() & Modifier.PUBLIC) > 0) {
							if (each.getJavadoc() == null)
								jdocViolation = true;
						}
					}
					
					
					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void testThread()
	{
		boolean checkThread = false;
		
		if(threads != null && !threads.isEmpty()) {
			for(String thread : threads)
				if(instances.contains(thread)) 
					checkThread = true;
			
			if(!checkThread)
				if(!instances.contains("Thread"))
					threadViolation = true;

		}
		
		
		else {
			if(!instances.contains("Thread"))
				threadViolation = true;
		}
	}
	
	
	
	private void testCustomStructure()
	{
		for (String each : policy.getReqCusStruct())
		{
			if (classes.contains(each)) {
				if (!instances.contains(each)) {
					customStructViolations.add(each);
					customStructViolationCount++;
				}
			}
			
			else {
				customStructViolations.add(each);
				customStructViolationCount++;
			}
		}
	}
	
	
	
	private void testCustomException()
	{
		for (String each : policy.getReqCustExc())
		{
			if (classes.contains(each)) {
				if (!superClasses.get(classes.indexOf(each)).equals("Exception") || superClasses.isEmpty()) {
					customExcViolations.add(each);
					customExcViolationCount++;
				}
				
				else {
					if (!instances.contains(each)) {
						customExcViolations.add(each);
						customExcViolationCount++;
					}
				}
			}
			
			else {
				customExcViolations.add(each);
				customExcViolationCount++;
			}
		}
	}
	
	
	
	private void getInstances(CompilationUnit unit)
	{
		
		try {
			unit.accept(new ASTVisitor() {
				public boolean visit(ClassInstanceCreation node)
				{
					instances.add(node.getType().toString());
					
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
					
					if (node.getSuperclassType() != null)
					{
						superClasses.add(node.getSuperclassType().toString());
						
						if(node.getSuperclassType().toString().equals("Thread"))
							threads.add(node.getName().toString());
					}
					else
						superClasses.add("null");

					return super.visit(node);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
