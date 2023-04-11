package edu.isel.csee.jchecker2_0.diagram.parser.collector;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

public class ClassCollector extends VoidVisitorAdapter<List<String>> {
    private String className;
    private List<String> classNameList = new ArrayList<>();
    private List<String> superClassNameList = new ArrayList<>();
    private List<String> interfaceNameList = new ArrayList<>();

    private List<FieldDeclaration> fieldDeclarationList = new ArrayList<>();
    private List<ConstructorDeclaration> constructorDeclarationList = new ArrayList<>();
    private List<MethodDeclaration> methodDeclarationList = new ArrayList<>();

    private boolean isClassType = true;
    private boolean isExtended = false;
    private boolean isImplemented = false;

    @Override
    public void visit(ClassOrInterfaceDeclaration declaration, List<String> collector) {
        super.visit(declaration, collector);

        // Declaration is interface type
        if (declaration.isInterface()) {
            this.isClassType = false;
            this.className = "<< interface >>\n" + declaration.getFullyQualifiedName().get();
        } else {
            this.isClassType = true;
            this.className = declaration.getFullyQualifiedName().get();
        }

        // Save the fieldDeclaration
        this.fieldDeclarationList = declaration.getFields();

        // Save the constructorDeclaration
        this.constructorDeclarationList = declaration.getConstructors();

        // Save the methodDeclaration
        this.methodDeclarationList = declaration.getMethods();

        // Check and save the super class
        if (!declaration.getExtendedTypes().isEmpty()) {
            this.isExtended = true;
            for (ClassOrInterfaceType superClass : declaration.getExtendedTypes()) {
                this.superClassNameList.add(superClass.getNameAsString());
            }
        }

        if (!declaration.getImplementedTypes().isEmpty()) {
            this.isImplemented = true;
            for (ClassOrInterfaceType _interface : declaration.getImplementedTypes()) {
                this.interfaceNameList.add(_interface.getNameAsString());
            }
        }

        // Add a class name in the collector
        collector.add(this.className);
    }

    public void setClassNameList(List<String> classNameList) { this.classNameList = classNameList; }
    public List<String> getClassNameList() { return this.classNameList; }

    public boolean isClassType() { return this.isClassType; }

    public boolean isExtended() { return this.isExtended; }

    public boolean isImplemented() { return this.isImplemented; }

    public List<String> getSuperClassNameList() { return this.superClassNameList; }

    public List<String> getInterfaceNameList() { return this.interfaceNameList; }

    public List<FieldDeclaration> getFieldDeclarationList() { return this.fieldDeclarationList; }

    public List<ConstructorDeclaration> getConstructorDeclarationList() { return this.constructorDeclarationList; }

    public List<MethodDeclaration> getMethodDeclarationList() { return this.methodDeclarationList; }
}
