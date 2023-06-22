package edu.isel.csee.jchecker2_0.diagram.parser;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import edu.isel.csee.jchecker2_0.diagram.parser.collector.ClassCollector;
import edu.isel.csee.jchecker2_0.diagram.parser.collector.ConstructorCollector;
import edu.isel.csee.jchecker2_0.diagram.parser.collector.FieldCollector;
import edu.isel.csee.jchecker2_0.diagram.parser.collector.MethodCollector;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class for using JavaParser
 */
public class JavaParser {
    private String sourcePath;
    private CompilationUnit compilationUnit;

    // information
    private String className;
    private List<String> classNameCollector = new ArrayList<>();
    private List<String> superClassNameList = new ArrayList<>();
    private List<String> interfaceNameList = new ArrayList<>();
    private List<FieldDeclaration> fieldDeclarationList = new ArrayList<>();
    private List<ConstructorDeclaration> constructorDeclarationList = new ArrayList<>();
    private List<MethodDeclaration> methodDeclarationList = new ArrayList<>();
    private List<String> fieldInfoList = new ArrayList<>();
    private List<String> constructorInfoList = new ArrayList<>();
    private List<String> methodInfoList = new ArrayList<>();
    private List<String> fieldNameList = new ArrayList<>();
    private List<String> constructorNameList = new ArrayList<>();
    private List<String> methodNameList = new ArrayList<>();
    private boolean isClassType = false;
    private boolean isExtended;
    private boolean isImplemented;

    /**
     * Constructor for basic setting
     * @param sourcePath source code path
     */
    public JavaParser(String sourcePath) {
        this.sourcePath = sourcePath;

        Optional<CompilationUnit> optionalCompilationUnit = null;
        try {
            optionalCompilationUnit = Optional.ofNullable(StaticJavaParser.parse(new FileInputStream(this.sourcePath)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Cannot find the path: " + sourcePath);
        } catch (ParseProblemException e) {
            e.printStackTrace();
            System.err.println("JavaParser ParseProblemException: " + sourcePath);
        }

        if (optionalCompilationUnit != null) {
            this.compilationUnit = optionalCompilationUnit.get();
            collectClassBoxInfo();
        } else {
            this.className = "(X) ️" + new File(this.sourcePath).getName() + "\n: Incorrect/Unsupported Java syntax (check your code and Java version)";
            this.isClassType = false;
        }
    }

    /**
     * Method for the entire collecting process
     */
    public void collectClassBoxInfo() {
        collectClass();
        collectFields();
        collectConstructor();
        collectMethods();
    }

    /**
     * Method for saving and preprocessing class information
     */
    private void collectClass() {
        ClassCollector classCollector = new ClassCollector();
        classCollector.visit(compilationUnit, this.classNameCollector);

        // Class or interface
        this.isClassType = classCollector.isClassType();

        // Class name
        if (!classNameCollector.isEmpty())
            this.className = classNameCollector.get(0);
        else {
            this.className = "(X) ️" + new File(this.sourcePath).getName() + "\n: You should complete or remove this file";
            this.isClassType = false;
        }

        // FieldDeclaration list & MethodDeclaration (ConstructorDeclaration) list
        this.fieldDeclarationList = classCollector.getFieldDeclarationList();
        this.constructorDeclarationList = classCollector.getConstructorDeclarationList();
        this.methodDeclarationList = classCollector.getMethodDeclarationList();

        // Field name list & method name list
        for (FieldDeclaration fieldDeclaration : fieldDeclarationList) {
            fieldNameList.add(fieldDeclaration.getVariable(0).getNameAsString());
        }
        for (ConstructorDeclaration constructorDeclaration : constructorDeclarationList) {
            constructorNameList.add(constructorDeclaration.getNameAsString());
        }
        for (MethodDeclaration methodDeclaration : methodDeclarationList) {
            methodNameList.add(methodDeclaration.getNameAsString());
        }

        if (classCollector.isExtended()) {
            this.isExtended = true;
            this.superClassNameList = classCollector.getSuperClassNameList();
        }

        if (classCollector.isImplemented()) {
            this.isImplemented = true;
            this.interfaceNameList = classCollector.getInterfaceNameList();
        }
    }

    /**
     * Method for saving field information
     */
    private void collectFields() {
        FieldCollector fieldCollector = new FieldCollector();
        for (FieldDeclaration fieldDeclaration : this.fieldDeclarationList) {
            fieldCollector.visit(fieldDeclaration, this.fieldInfoList);
        }
    }

    /**
     * Method for saving constructor information
     */
    private void collectConstructor() {
        ConstructorCollector constructorCollector = new ConstructorCollector();
        for (ConstructorDeclaration constructorDeclaration : this.constructorDeclarationList) {
            constructorCollector.visit(constructorDeclaration, this.constructorInfoList);
        }
    }

    /**
     * Method for saving method information
     */
    private void collectMethods() {
        MethodCollector methodCollector = new MethodCollector();
        for (MethodDeclaration methodDeclaration : this.methodDeclarationList) {
            methodCollector.visit(methodDeclaration, this.methodInfoList);
        }
    }

    /**
     * Method for return superclass name list
     * @return superClassNameList
     */
    public List<String> getSuperClassNameList() { return this.superClassNameList; }

    /**
     * Method for return interface name list
     * @return interfaceNameList
     */
    public List<String> getInterfaceNameList() { return this.interfaceNameList; }

    /**
     * Method for return class name
     * @return className
     */
    public String getClassName() { return this.className; }

    /**
     * Method for checking class type (class or interface)
     * @return isClassType
     */
    public boolean isClassType() { return this.isClassType; }

    /**
     * Method for return field name list
     * @return fieldNameList
     */
    public List<String> getFieldNameList() { return this.fieldNameList; }

    /**
     * Method for return constructor name list
     * @return constructorNameList
     */
    public List<String> getConstructorNameList() { return this.constructorNameList; }

    /**
     * Method for return method name list
     * @return methodNameList
     */
    public List<String> getMethodNameList() { return this.methodNameList; }

    /**
     * Method for return field list
     * @return fieldInfoList
     */
    public List<String> getFieldInfoList() { return this.fieldInfoList; }

    /**
     * Method for return constructor list
     * @return constructorInfoList
     */
    public List<String> getConstructorInfoList() { return this.constructorInfoList; }

    /**
     * Method for return method list
     * @return methodInfoList
     */
    public List<String> getMethodInfoList() { return this.methodInfoList; }

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
