package edu.isel.csee.jchecker.diagram.parser.collector;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class MethodCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> methodNameList = new ArrayList<>();
    private String methodName;
    private String methodModifier;
    private String methodType;
    private String methodParamType;
    private String methodInfo;

    @Override
    public void visit(MethodDeclaration declaration, List<String> collector) {
        super.visit(declaration, collector);

        // Add a method name in the collector
        //collector.add(declaration.getNameAsString());

        // + name(paramType, paramType, ...): methodType
        this.methodName = declaration.getNameAsString();

        if (declaration.getModifiers().size() == 0) { this.methodModifier = "package"; }
        else { this.methodModifier = declaration.getModifiers().get(0).toString(); }

        this.methodType = declaration.getTypeAsString();

        if (declaration.getParameters().size() == 0) { this.methodParamType = "()"; }
        else {
            if (declaration.getParameters().size() == 1) { this.methodParamType = "(" + declaration.getParameter(0).getTypeAsString() + ")"; }
            else {
                for (int i = 0; i < declaration.getParameters().size(); i ++) {
                    if (i == 0) { this.methodParamType = "(" + declaration.getParameter(i).getTypeAsString() + ", "; }
                    else if (i == (declaration.getParameters().size() - 1)) { this.methodParamType += declaration.getParameter(i).getTypeAsString() + ")"; }
                    else { this.methodParamType += declaration.getParameter(i).getTypeAsString() + ", "; }
                }
            }
        }

        if (this.methodModifier.equals("private ")) { this.methodInfo = "- " + this.methodName + this.methodParamType + ": " + this.methodType; }
        else if (this.methodModifier.equals("public ")) { this.methodInfo = "+ " + this.methodName + this.methodParamType + ": " + this.methodType; }
        else if (this.methodModifier.equals("protected ")) { this.methodInfo = "# " + this.methodName + this.methodParamType + ": " + this.methodType; }
        else { this.methodInfo = "~ " + this.methodName + this.methodParamType + ": " + this.methodType; }

        // Add a method information in the collector
        collector.add(this.methodInfo);
    }

    public void setMethodNameList(List<String> methodNameList) { this.methodNameList = methodNameList; }
    public List<String> getMethodNameList() { return this.methodNameList; }
}
