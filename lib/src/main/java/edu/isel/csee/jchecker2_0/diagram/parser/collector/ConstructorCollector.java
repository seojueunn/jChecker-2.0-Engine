package edu.isel.csee.jchecker2_0.diagram.parser.collector;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ConstructorCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> constructorNameList = new ArrayList<>();
    private String constructorName;
    private String constructorModifier;
    private String constructorParamType;
    private String constructorInfo;

    @Override
    public void visit(ConstructorDeclaration declaration, List<String> collector) {
        super.visit(declaration, collector);

        this.constructorName = declaration.getNameAsString();

        if (declaration.getModifiers().size() == 0) { this.constructorModifier = "package"; }
        else { this.constructorModifier = declaration.getModifiers().get(0).toString(); }

        if (declaration.getParameters().size() == 0) { this.constructorParamType = "()"; }
        else {
            if (declaration.getParameters().size() == 1) { this.constructorParamType = "(" + declaration.getParameter(0).getTypeAsString() + ")"; }
            else {
                for (int i = 0; i < declaration.getParameters().size(); i ++) {
                    if (i == 0) { this.constructorParamType = "(" + declaration.getParameter(i).getTypeAsString() + ", "; }
                    else if (i == (declaration.getParameters().size() - 1)) { this.constructorParamType += declaration.getParameter(i).getTypeAsString() + ")"; }
                    else { this.constructorParamType += declaration.getParameter(i).getTypeAsString() + ", "; }
                }
            }
        }

        if (this.constructorModifier.equals("private ")) { this.constructorInfo = "- " + this.constructorName + this.constructorParamType; }
        else if (this.constructorModifier.equals("public ")) { this.constructorInfo = "+ " + this.constructorName + this.constructorParamType; }
        else if (this.constructorModifier.equals("protected ")) { this.constructorInfo = "# " + this.constructorName + this.constructorParamType; }
        else { this.constructorInfo = "~ " + this.constructorName + this.constructorParamType; }

        // Add a constructor information in the collector
        collector.add(this.constructorInfo);
    }
}
