package edu.isel.csee.jchecker2_0.diagram.parser.collector;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for collecting class information
 */
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

    /**
     * Visit method for searching and collecting class information
     * @param declaration ClassOrInterfaceDeclaration
     * @param collector collector list
     */
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

    /**
     * Method for setting class name list
     * @param classNameList classNameList
     */
    public void setClassNameList(List<String> classNameList) { this.classNameList = classNameList; }

    /**
     * Method for return class name list
     * @return classNameList
     */
    public List<String> getClassNameList() { return this.classNameList; }

    /**
     * Method for checking class type (class or interface)
     * @return isClassType
     */
    public boolean isClassType() { return this.isClassType; }

    /**
     * Method for checking extension
     * @return isExtended
     */
    public boolean isExtended() { return this.isExtended; }

    /**
     * Method for checking implementation
     * @return isImplemented
     */
    public boolean isImplemented() { return this.isImplemented; }

    /**
     * Method for return super class name list
     * @return superClassNameList
     */
    public List<String> getSuperClassNameList() { return this.superClassNameList; }

    /**
     * Method for return interface name list
     * @return interfaceNameList
     */
    public List<String> getInterfaceNameList() { return this.interfaceNameList; }

    /**
     * Method for return FieldDeclaration list
     * @return fieldDeclarationList
     */
    public List<FieldDeclaration> getFieldDeclarationList() { return this.fieldDeclarationList; }

    /**
     * Method for return ConstructorDeclaration list
     * @return constructorDeclarationList
     */
    public List<ConstructorDeclaration> getConstructorDeclarationList() { return this.constructorDeclarationList; }

    /**
     * Method for return MethodDeclaration list
     * @return methodDeclarationList
     */
    public List<MethodDeclaration> getMethodDeclarationList() { return this.methodDeclarationList; }
}
