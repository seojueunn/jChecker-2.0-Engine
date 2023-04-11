package edu.isel.csee.jchecker2_0.diagram.parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import edu.isel.csee.jchecker2_0.diagram.parser.collector.ClassCollector;
import edu.isel.csee.jchecker2_0.diagram.parser.collector.ConstructorCollector;
import edu.isel.csee.jchecker2_0.diagram.parser.collector.FieldCollector;
import edu.isel.csee.jchecker2_0.diagram.parser.collector.MethodCollector;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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

    public JavaParser(String sourcePath) {
        this.sourcePath = sourcePath;
        try {
            this.compilationUnit = StaticJavaParser.parse(new FileInputStream(this.sourcePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.err.println("Cannot find the path: " + sourcePath);
        }
    }

    public void collectClassBoxInfo() {
        collectClass();

        collectFields();

        collectConstructor();

        collectMethods();
    }


    private void collectClass() {
        ClassCollector classCollector = new ClassCollector();
        classCollector.visit(compilationUnit, this.classNameCollector);

        // Class or interface
        this.isClassType = classCollector.isClassType();

        // Class name
        this.className = classNameCollector.get(0);

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

    private void collectFields() {
        FieldCollector fieldCollector = new FieldCollector();
        for (FieldDeclaration fieldDeclaration : this.fieldDeclarationList) {
            fieldCollector.visit(fieldDeclaration, this.fieldInfoList);
        }
    }

    private void collectConstructor() {
        ConstructorCollector constructorCollector = new ConstructorCollector();
        for (ConstructorDeclaration constructorDeclaration : this.constructorDeclarationList) {
            constructorCollector.visit(constructorDeclaration, this.constructorInfoList);
        }
    }

    private void collectMethods() {
        MethodCollector methodCollector = new MethodCollector();
        for (MethodDeclaration methodDeclaration : this.methodDeclarationList) {
            methodCollector.visit(methodDeclaration, this.methodInfoList);
        }
    }


    public List<String> getSuperClassNameList() { return this.superClassNameList; }
    public List<String> getInterfaceNameList() { return this.interfaceNameList; }

    public String getClassName() { return this.className; }
    public boolean isClassType() { return this.isClassType; }

    public List<String> getFieldNameList() { return this.fieldNameList; }
    public List<String> getConstructorNameList() { return this.constructorNameList; }
    public List<String> getMethodNameList() { return this.methodNameList; }

    public List<String> getFieldInfoList() { return this.fieldInfoList; }
    public List<String> getConstructorInfoList() { return this.constructorInfoList; }
    public List<String> getMethodInfoList() { return this.methodInfoList; }

    public List<FieldDeclaration> getFieldDeclarationList() { return this.fieldDeclarationList; }
    public List<ConstructorDeclaration> getConstructorDeclarationList() { return this.constructorDeclarationList; }
    public List<MethodDeclaration> getMethodDeclarationList() { return this.methodDeclarationList; }
}
