package edu.isel.csee.jchecker.statics;

import java.util.ArrayList;

import java.util.List;

import org.eclipse.jdt.core.dom.*;

import com.google.gson.JsonObject;
import edu.isel.csee.jchecker.score.EvaluationSchemeMapper;



public class OOPChecker extends ASTChecker 
{
	private EvaluationSchemeMapper policy;
	private List<String> source = null;
	private String unitName;
	private String filePath;
	private List<IMethodBinding> methods = new ArrayList<>();
	private List<String> packages = new ArrayList<>();
	private List<String> classes = new ArrayList<>();
	
	private ArrayList<String> classesViolations = new ArrayList<>();
	private ArrayList<String> spcViolations = new ArrayList<>();
	private ArrayList<String> itfViolations = new ArrayList<>();
	private ArrayList<String> pkgViolations = new ArrayList<>();
	private boolean isInterface = false;
	private boolean isSuperclass = false;
	
	private boolean ecpViolation = false;
	private boolean clasViolation = false;
	private boolean pkgViolation = false;
	private boolean itfViolation = false;
	private boolean spcViolation = false;
	private boolean ovlViolation = false;
	private boolean ovrViolation = false;
	
	private int classesViolationCount = 0;
	private int pkgViolationCount = 0;
	private int spcViolationCount = 0;
	private int itfViolationCount = 0;
	


	public OOPChecker(EvaluationSchemeMapper policy, List<String> source, String unitName, String filePath)
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
		

		if (policy.isEncaps()) 
		{
			JsonObject item_ecp = new JsonObject();
			
			item_ecp.addProperty("violation", ecpViolation);
			
			if (!ecpViolation) 
				policy.setEnc_deduct_point(0);
			
			
			item_ecp.addProperty("deductedPoint", policy.getEnc_deduct_point());
			scoresheet.add("encapsulation", item_ecp);
			
			
			policy.deduct_point(policy.getEnc_deduct_point());
		}
		
		
		
		if (policy.getOverloading() != null && !policy.getOverloading().isEmpty()) 
		{
			JsonObject item_ovl = new JsonObject();
			
			if(policy.getOverloading().size() > 0)
				item_ovl.addProperty("violation", ovlViolation);
			item_ovl.addProperty("violationCount", policy.getOverloading().size());
			
			
			double deducted = (double)(policy.getOvl_deduct_point() * (double)policy.getOverloading().size());
			if (deducted > policy.getOvl_max_deduct())
				deducted = (double)policy.getOvl_max_deduct();
			
			item_ovl.addProperty("deductedPoint", deducted);
			scoresheet.add("overloading", item_ovl);
			
			
			policy.deduct_point(deducted);
		}
		
		
		
		if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()) 
		{
			JsonObject item_ovr = new JsonObject();
			
			if(policy.getOverriding().size() > 0)
				item_ovr.addProperty("violation", ovrViolation);
			item_ovr.addProperty("violationCount", policy.getOverriding().size());
			
			
			double deducted = (double)(policy.getOvr_deduct_point() * (double)policy.getOverriding().size());
			if (deducted > policy.getOvr_max_deduct())
				deducted = (double)policy.getOvr_max_deduct();
			
			item_ovr.addProperty("deductedPoint", deducted);
			scoresheet.add("overriding", item_ovr);
			
			
			policy.deduct_point(deducted);
		}

		
		
		if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()) 
		{
			JsonObject item_class = new JsonObject();
			
			item_class.addProperty("violation", clasViolation);
			item_class.addProperty("violationCount", classesViolationCount);
			
			
			double deducted = (double)(policy.getClass_deduct_point() * (double)classesViolationCount);
			if (deducted > policy.getClass_max_deduct())
				deducted = (double)policy.getClass_max_deduct();
			
			item_class.addProperty("deductedPoint", deducted);
			scoresheet.add("classes", item_class);
			
			
			policy.deduct_point(deducted);
		}
		
		
		
		if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()) 
		{
			JsonObject item_pkg = new JsonObject();
			
			item_pkg.addProperty("violation", pkgViolation);
			item_pkg.addProperty("violationCount", pkgViolationCount);
			
			
			double deducted = (double)(policy.getPackage_deduct_point() * (double)pkgViolationCount);
			if (deducted > policy.getPackage_max_deduct())
				deducted = (double)policy.getPackage_max_deduct();
			
			item_pkg.addProperty("deductedPoint", deducted);
			scoresheet.add("packages", item_pkg);
			
			
			policy.deduct_point(deducted);
		}
		
		
		
		if (isSuperclass) 
		{
			JsonObject item_spc = new JsonObject();
			
			item_spc.addProperty("violation", spcViolation);
			item_spc.addProperty("violationCount", spcViolationCount + policy.getSoriginClass().size());
			
			
			double deducted = (double)(policy.getSpc_deduct_point() * (double)(spcViolationCount + policy.getSoriginClass().size()));
			if (deducted > policy.getSpc_max_deduct())
				deducted = (double)policy.getSpc_max_deduct();
			
			item_spc.addProperty("deductedPoint", deducted);
			scoresheet.add("inheritSuper", item_spc);
			
			
			policy.deduct_point(deducted);
		}
		
		
		if (isInterface) 
		{
			JsonObject item_itf = new JsonObject();
			
			item_itf.addProperty("violation", itfViolation);
			item_itf.addProperty("violationCount", itfViolationCount + policy.getIoriginClass().size());
			
			
			double deducted = (double)(policy.getItf_deduct_point() * (double)(itfViolationCount + policy.getIoriginClass().size()));
			if (deducted > policy.getItf_max_deduct()) 
				deducted = (double)policy.getItf_max_deduct();
			
			item_itf.addProperty("deductedPoint", deducted);
			scoresheet.add("inheritInterface", item_itf);
			
			
			policy.deduct_point(deducted);
		}
		
		return scoresheet;
	}

	
	
	private void collect()
	{
		for (String each : source) 
		{
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath).createAST(null);
			
			
			if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()) getMethodDeclarations(unit);
	
			
			if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()) getClassNames(unit);
			
			
			if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()) getPackageNames(unit);
		}
	}
	
	
	
	private void test()
	{
		for (String each : source) 
		{
			CompilationUnit unit = (CompilationUnit) parserSetProperties(each, unitName, filePath).createAST(null);
			
			
			if (policy.isEncaps()) testEncapsulation(unit);
			
			
			if (policy.getOverloading() != null && !policy.getOverloading().isEmpty()) testOverriding(unit);
			
			
			if (policy.getOverriding() != null && !policy.getOverriding().isEmpty()) testOverriding(unit);
			
			
			if (policy.getSuperClass() != null && !policy.getSuperClass().isEmpty()) 
			{
				isSuperclass = true;
				testSuperclass(unit);
			}
			
			
			if (policy.getInterfaceClass() != null && !policy.getInterfaceClass().isEmpty()) 
			{
				isInterface = true;
				testInterface(unit);
			}	
		}
		
		if (policy.getReqClass() != null && !policy.getReqClass().isEmpty()) testRequiredClass();
		
		
		if (policy.getPackageName() != null && !policy.getPackageName().isEmpty()) testRequiredPackage();
	}
	
	
	
	
	private void testRequiredClass()
	{
		for (String each : policy.getReqClass())
		{
			if (!classes.contains(each)) 
			{
				classesViolations.add(each);
				classesViolationCount++;
				clasViolation = true;
			}
				
		}
	}
	
	
	
	private void testRequiredPackage()
	{
		for (String each : policy.getPackageName())
		{
			if (!packages.contains(each)) 
			{
				pkgViolations.add(each);
				pkgViolationCount++;
				pkgViolation = true;
			}
		}
	}
	
	
	
	private void testSuperclass(CompilationUnit unit)
	{
		
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(TypeDeclaration node)
				{
					int idx = -1;
					
					if (!policy.getSoriginClass().contains(node.getName().toString()))
						return super.visit(node);
					else 
						idx = policy.getSoriginClass().indexOf(node.getName().toString());
					
					if (!policy.getSuperClass().get(idx).equals(node.getSuperclassType().toString())) 
					{
						spcViolations.add(node.getName().toString());
						spcViolationCount++;
						spcViolation = true;
					}
					else
					{
						policy.getSoriginClass().remove(idx);
						policy.getSuperClass().remove(idx);
					}
					
					
					return super.visit(node);
				}
			});
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	
	private void testInterface(CompilationUnit unit)
	{
		
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(TypeDeclaration node)
				{
					boolean flag = false;
					int idx = -1;
					
					if (!policy.getIoriginClass().contains(node.getName().toString()))
						return super.visit(node);
					else 
						idx = policy.getIoriginClass().indexOf(node.getName().toString());
					
					for (Object each : node.superInterfaceTypes()) 
					{
						if (policy.getInterfaceClass().get(idx).equals(each.toString())) 
						{
							flag = true;
							policy.getIoriginClass().remove(idx);
							policy.getInterfaceClass().remove(idx);
							break;
						}
					}
					
					if (!flag) 
					{
						itfViolations.add(node.getName().toString());
						itfViolationCount++;
						itfViolation = true;
					}
					
					return super.visit(node);
				}
			});
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	public void testOverriding(CompilationUnit unit)
	{
		
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(MethodDeclaration node)
				{
					if (policy.getOverriding().contains(node.getName().toString())) 
					{
						for (IMethodBinding each : methods)
						{
							if (node.resolveBinding().overrides(each)) 
							{
								policy.getOverriding().remove(node.getName().toString());
								return false;
							}
						}
					}
					
					return super.visit(node);
				}
			});
			
			
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	public void testOverloading(CompilationUnit unit)
	{
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(TypeDeclaration node)
				{
					MethodDeclaration[] tmp = node.getMethods();
					ArrayList<String> currentMethods = new ArrayList<>();
					
					for (MethodDeclaration each : tmp) currentMethods.add(each.toString());
					
					
					for (String ovl : policy.getOverloading()) 
					{
						for (String each : currentMethods)
						{
							if (each.equals(ovl)) 
							{
								currentMethods.remove(each);
								
								if (currentMethods.contains(ovl)) policy.getOverloading().remove(ovl);	
							}
						}
					}
					
					
					return super.visit(node);
				}
			});
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	
	
	private void testEncapsulation(CompilationUnit unit)
	{
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit (TypeDeclaration node)
				{
					int public_count = 0;
					
					
					if (!policy.getReqClass().contains(node.getName().toString())) return false;
					
					
					for (FieldDeclaration eachField : node.getFields())
						if ((eachField.getModifiers() & Modifier.PRIVATE) <= 0) ecpViolation = true;		
					
					
					for (MethodDeclaration eachMethod : node.getMethods())
						if ((eachMethod.getModifiers() & Modifier.PUBLIC) > 0) public_count++;
					
					
					if (public_count == 0) ecpViolation = true;
					

					return super.visit(node);
				}
			});
		
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	
	
	private void getMethodDeclarations(CompilationUnit unit)
	{
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(MethodDeclaration node)
				{
					methods.add(node.resolveBinding().getMethodDeclaration());
					
					
					return super.visit(node);
				}
			});
		} catch (Exception e) { e.printStackTrace(); }
	}
	
	

	private void getPackageNames(CompilationUnit unit)
	{
		try 
		{
			unit.accept(new ASTVisitor() 
			{
				public boolean visit(PackageDeclaration node)
				{
					packages.add(node.getName().toString());
					
					
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
					
					
					return super.visit(node);
				}
			});
		} catch (Exception e) { e.printStackTrace(); }
	}
}