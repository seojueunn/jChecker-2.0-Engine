package edu.isel.csee.jchecker.statics;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.LambdaExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.google.gson.JsonObject;

import edu.isel.csee.jchecker.score.EvaluationSchemeMapper;


public class ImplementationChecker extends ASTChecker {
	EvaluationSchemeMapper policy;
	private List<String> source = null;
	private String unitName;
	private String filePath;
	private List<String> instances = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	private List<String> superClasses = new ArrayList<>();
	private List<String> threads = new ArrayList<>();
	private List<String> expressions = new ArrayList<>();
	
	private ArrayList<String> customExcViolations = new ArrayList<>();
	private ArrayList<String> customStructViolations = new ArrayList<>();
	
	private boolean jdocViolation = false;
	private boolean threadViolation = false;
	private boolean countViolation = false;
	private boolean customExcViolation = false;
	private boolean customStructViolation = false;
	
	private int customExcViolationCount = 0;
	private int customStructViolationCount = 0;
	private String result ="";
	
	private int numOfEnhancedForStatement = 0;
	private int numOfMethods = 0;
	private int numOfFields = 0;
	
	
	
	public ImplementationChecker(EvaluationSchemeMapper policy, List<String> source, String unitName, String filePath)
	{
		this.policy = policy;
		this.source = source;
		this.unitName = unitName;
		this.filePath = filePath;
	}
	
	
	
	public JsonObject run(JsonObject scoresheet)
	{
		collect();
		
		test();
		
		
		if (policy.isCount()) 
		{
			JsonObject item = new JsonObject();
			item.addProperty("violation", countViolation);
			
			if (!countViolation)
				policy.setCnt_deduct_point(0);
			
			item.addProperty("deductedPoint", policy.getCnt_deduct_point());
			scoresheet.add("count", item);
			
			
			policy.deduct_point(policy.getCnt_deduct_point());
		}
		
		if (policy.isJavadoc()) 
		{
			JsonObject item = new JsonObject();
			item.addProperty("violation", jdocViolation);
			
			if (!jdocViolation)
				policy.setJvd_deduct_point(0);
			
			item.addProperty("deductedPoint", policy.getJvd_deduct_point());
			scoresheet.add("javadoc", item);
			
			
			policy.deduct_point(policy.getJvd_deduct_point());
		}
		
		
		if (policy.isThreads()) 
		{
			JsonObject item = new JsonObject();
			item.addProperty("violation", threadViolation);
			
			if (!threadViolation)
				policy.setThr_deduct_point(0);
			
			item.addProperty("deductedPoint", policy.getThr_deduct_point());
			scoresheet.add("thread", item);
			
			
			policy.deduct_point(policy.getThr_deduct_point());
		}
		
		
		if (policy.getReqCustExc() != null && !policy.getReqCustExc().isEmpty()) 
		{
			JsonObject item = new JsonObject();
			item.addProperty("violation", customExcViolation);
			item.addProperty("violationCount", customExcViolationCount);
			
			
			double deducted = (double)(policy.getCustomExc_deduct_point() * (double)customExcViolationCount);
			if (deducted > policy.getCustomExc_max_deduct())
				deducted = (double)policy.getCustomExc_max_deduct();
			
			item.addProperty("deductedPoint", deducted);
			scoresheet.add("customException", item);
			
			
			policy.deduct_point(deducted);
		}
		
		
		if (policy.getReqCusStruct() != null && !policy.getReqCusStruct().isEmpty()) 
		{
			JsonObject item = new JsonObject();
			
			item.addProperty("violation", customStructViolation);
			item.addProperty("violationCount", customStructViolationCount);
			
			
			double deducted = (double)(policy.getCustomStr_deduct_point() * (double)customStructViolationCount);
			if (deducted > policy.getCustomStr_max_deduct())
				deducted = (double)policy.getCustomStr_max_deduct();
			
			item.addProperty("deductedPoint", deducted);
			scoresheet.add("customStructure", item);
			
			
			policy.deduct_point(deducted);
		}
		
		return scoresheet;
	}
	
	
	
	private void collect()
	{
		for (String each : source) 
		{
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath).createAST(null);
			getClassNames(unit);
			getInstances(unit);
			getCountInfo(unit);
		}
	}
	
	
	
	private void test()
	{
		for (String each : source) 
		{
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath).createAST(null);
			
			if (policy.isJavadoc()) testJavadoc(unit);
		}
		
		if(policy.isCount()) testCount();
		
		
		if (policy.isThreads()) testThread();
		
		
		if (policy.getReqCustExc() != null && !policy.getReqCustExc().isEmpty()) testCustomException();
		
		
		if (policy.getReqCusStruct() != null && !policy.getReqCusStruct().isEmpty()) testCustomStructure();
	}
	
	private void testCount()
	{
		if(policy.getMethodCount() > numOfMethods) countViolation = true;
		if(policy.getFieldCount() > numOfFields) countViolation = true;
		if(policy.getEnForCount() > numOfEnhancedForStatement) countViolation = true;
	}
	
	
	private void testJavadoc(CompilationUnit unit)
	{
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(TypeDeclaration node)
				{
					if (policy.getReqClass().contains(node.getName().toString()))
						if (node.getJavadoc() == null) jdocViolation = true;
					
					for (MethodDeclaration each : node.getMethods())
					{
						if ((each.getModifiers() & Modifier.PUBLIC) > 0)
							if (each.getJavadoc() == null) jdocViolation = true;
					}
					
					return super.visit(node);
				}
			});
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	private void testThread()
	{
		boolean checkThread = false;
		
		if(threads != null && !threads.isEmpty()) 
		{
			for(String thread : threads)
				if(instances.contains(thread)) checkThread = true;
			
			if(!checkThread)
				if(!instances.contains("Thread")) threadViolation = true;

		}
		else { if(!instances.contains("Thread")) threadViolation = true; }
	}
	
	
	
	private void testCustomStructure()
	{
		for (String each : policy.getReqCusStruct())
		{
			if (classes.contains(each)) 
			{
				if (!instances.contains(each)) 
				{
					customStructViolations.add(each);
					customStructViolationCount++;
					customStructViolation = true;
				}
			}		
			else 
			{
				customStructViolations.add(each);
				customStructViolationCount++;
				customStructViolation = true;
			}
		}
	}
	
	
	
	private void testCustomException()
	{
		for (String each : policy.getReqCustExc())
		{
			if (classes.contains(each)) 
			{
				if (!superClasses.get(classes.indexOf(each)).equals("Exception") || superClasses.isEmpty()) 
				{
					customExcViolations.add(each);
					customExcViolationCount++;
					customExcViolation = true;
				}
				else 
				{
					if (instances.contains(each)) 
					{
						String result = getStatement(each);
						
						if(!result.isEmpty())
						{
							if(!result.contains("throw"))
							{
								expressions = getExpression();
								
								if(expressions.isEmpty())
								{
									customExcViolations.add(each);
									customExcViolationCount++;
									customExcViolation = true;
								}
								else
								{
									boolean checkExpression = false;
									
									for(String expression : expressions)
									{
										if(result.contains(expression))
										{
											checkExpression = true;
											break;
										}
									}
									
									if(!checkExpression)
									{
										customExcViolations.add(each);
										customExcViolationCount++;
										customExcViolation = true;
									}
								}
							}
						}
						
					}
					else
					{
						customExcViolations.add(each);
						customExcViolationCount++;
						customExcViolation = true;
					}
				}
			}
			else 
			{
				customExcViolations.add(each);
				customExcViolationCount++;
				customExcViolation = true;
			}
		}
	}
	
	
	
	private void getInstances(CompilationUnit unit)
	{
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(ClassInstanceCreation node)
				{
					instances.add(node.getType().toString());
					
					return super.visit(node);
				}
			});
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	private void getClassNames(CompilationUnit unit)
	{	
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(TypeDeclaration node)
				{
					classes.add(node.getName().toString());
					
					if (node.getSuperclassType() != null)
					{
						superClasses.add(node.getSuperclassType().toString());
						
						if(node.getSuperclassType().toString().equals("Thread"))
							threads.add(node.getName().toString());
					}
					else superClasses.add("null");

					return super.visit(node);
				}
			});
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	private void getCountInfo(CompilationUnit unit)
	{
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(EnhancedForStatement node)
				{
					if(policy.getEnForCount() < 0) return false;
					
					numOfEnhancedForStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(TypeDeclaration node)
				{
					if(policy.getMethodCount() < 0 && policy.getFieldCount() < 0) return false;
					
					numOfFields += node.getFields().length;
					numOfMethods += node.getMethods().length;
					
					return super.visit(node);
				}
				
				/*
				public boolean visit(IfStatement node)
				{
					numOfIfStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(BreakStatement node)
				{
					numOfBreakStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(ContinueStatement node)
				{
					numOfContinueStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(DoStatement node)
				{
					numOfDoStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(EmptyStatement node)
				{
					numOfEmptyStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(ForStatement node)
				{
					numOfForStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(SwitchStatement node)
				{
					numOfSwitchStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(TryStatement node)
				{
					numOfTryStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(WhileStatement node)
				{
					numOfWhileStatement++;
					
					return super.visit(node);
				}
				
				public boolean visit(ConditionalExpression node)
				{
					numOfConditionalExpression++;
					
					return super.visit(node);
				}
				
				public boolean visit(LambdaExpression node)
				{
					numOfLambdaExpression++;
					
					return super.visit(node);
				}
				
				public boolean visit(ThisExpression node)
				{
					numOfThisExpression++;
					
					return super.visit(node);
				}
				*/		
			});
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	public String getStatement(String name)
	{
		for (String each : source) 
		{
			if(!result.isEmpty()) break;
			
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath).createAST(null);
				
			try 
			{
				unit.accept(new ASTVisitor() 
				{
					public boolean visit(ClassInstanceCreation node)
					{
						if(node.getType().toString().equals(name))
						{
							result = node.getParent().toString();
							
							
							return false ;
						}
			
						return super.visit(node);
					}
				});
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		return result;
	}
	
	public List<String> getExpression()
	{
		for (String each : source) 
		{
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath).createAST(null);
			
			try 
			{
				unit.accept(new ASTVisitor() 
				{
					public boolean visit(ThrowStatement node)
					{
						expressions.add(node.getExpression().toString());
						
						
						return super.visit(node);
					}
				});
			} catch (Exception e) { e.printStackTrace(); }
		}
		
		return expressions;
	}
}
