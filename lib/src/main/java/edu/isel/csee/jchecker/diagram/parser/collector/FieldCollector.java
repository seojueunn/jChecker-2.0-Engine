package edu.isel.csee.jchecker.diagram.parser.collector;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class FieldCollector extends VoidVisitorAdapter<List<String>> {
    private List<String> fieldNameList = new ArrayList<>();
    private String fieldName;
    private String fieldModifier;
    private String fieldType;
    private String fieldInfo;

    @Override
    public void visit(FieldDeclaration declaration, List<String> collector) {
        super.visit(declaration, collector);

        // Add a field name in the collector
        this.fieldName = declaration.getVariable(0).getNameAsString();

        // - filedName: fieldType
        if (declaration.getModifiers().size() == 0) { this.fieldModifier = "package"; }
        else { this.fieldModifier = declaration.getModifiers().get(0).toString(); }

        this.fieldType = declaration.getVariable(0).getTypeAsString();

        if (this.fieldModifier.equals("private ")) { this.fieldInfo = "- " + this.fieldName + ": " + this.fieldType; }
        else if (this.fieldModifier.equals("public ")) { this.fieldInfo = "+ " + this.fieldName + ": " + this.fieldType; }
        else if (this.fieldModifier.equals("protected ")) { this.fieldInfo = "# " + this.fieldName + ": " + this.fieldType; }
        else { this.fieldInfo = "~ " + this.fieldName + ": " + this.fieldType; }

        // Add a field information in the collector
        collector.add(this.fieldInfo);
    }

    public void setFieldNameList(List<String> fieldNameList) { this.fieldNameList = fieldNameList; }
    public List<String> getFieldNameList() { return this.fieldNameList; }
}
