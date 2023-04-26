package edu.isel.csee.jchecker2_0.diagram.utils;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import edu.isel.csee.jchecker2_0.diagram.parser.JavaParser;

import java.util.ArrayList;
import java.util.List;

public class ClassBox extends Box {
    private Box nameBoxInfo;
    private Box fieldsBoxInfo;
    private Box methodsBoxInfo;

    private String _extends = "";
    private String _interface = "";
    private int classId;

    private int maxLength = 0;

    // For javaParser
    private JavaParser javaParser;
    private String className;
    private boolean isClassType = false;
    private List<String> fieldNameList = new ArrayList<>();
    private List<String> constructorNameList = new ArrayList<>();
    private List<String> methodNameList = new ArrayList<>();
    private List<String> fieldInfoList = new ArrayList<>();
    private List<String> constructorInfoList = new ArrayList<>();
    private List<String> methodInfoList = new ArrayList<>();
    private List<FieldDeclaration> fieldDeclarationList = new ArrayList<>();
    private List<ConstructorDeclaration> constructorDeclarationList = new ArrayList<>();
    private List<MethodDeclaration> methodDeclarationList = new ArrayList<>();

    public ClassBox(JavaParser javaParser) {
        this.javaParser = javaParser;

        nameBoxInfo = new Box();
        fieldsBoxInfo = new Box();
        methodsBoxInfo = new Box();

        this.setEntireClassInfo();
        this.setWidthHeight();
    }

    public void setWidthHeight() {
        // class name, field name list, method name list
        maxLength = javaParser.getClassName().length();
        this.lineCount += 1;
        nameBoxInfo.setLineCount(nameBoxInfo.getLineCount() + 1);

        for (String fieldInfo : javaParser.getFieldInfoList()) {
            maxLength = Math.max(maxLength, fieldInfo.length());
            this.lineCount += 1;
            fieldsBoxInfo.setLineCount(fieldsBoxInfo.getLineCount() + 1);
        }

        for (String constructorInfo : javaParser.getConstructorInfoList()) {
            maxLength = Math.max(maxLength, constructorInfo.length());
            this.lineCount += 1;
            methodsBoxInfo.setLineCount(methodsBoxInfo.getLineCount() + 1);
        }

        for (String methodInfo : javaParser.getMethodInfoList()) {
            maxLength = Math.max(maxLength, methodInfo.length());
            this.lineCount += 1;
            methodsBoxInfo.setLineCount(methodsBoxInfo.getLineCount() + 1);
        }

        int nameBoxHeight = nameBoxInfo.getLineCount() * 26;
        int fieldBoxHeight = fieldsBoxInfo.getLineCount() * 26;
        int methodBoxHeight = methodsBoxInfo.getLineCount() * 26;

        if (!javaParser.isClassType()) {
            nameBoxHeight += 14;
        }

        nameBoxInfo.setHeight(nameBoxHeight);
        fieldsBoxInfo.setHeight(fieldBoxHeight);
        methodsBoxInfo.setHeight(methodBoxHeight);
        this.setHeight(nameBoxHeight + fieldBoxHeight + methodBoxHeight + 8);

        int width = maxLength * 7;
        nameBoxInfo.setWidth(width);
        fieldsBoxInfo.setWidth(width);
        methodsBoxInfo.setWidth(width);
        this.setWidth(width);
    }

    public void setSubClassCoordinate(int index, int totalWidth, int startY, int maxHeight) {
        this.setX(70 + totalWidth + ((index % 5) * 20));
        this.setY(startY + maxHeight + 70);

        nameBoxInfo.setX(this.getX());
        nameBoxInfo.setY(this.getY());

        fieldsBoxInfo.setX(this.getX());
        fieldsBoxInfo.setY(nameBoxInfo.getHeight());

        methodsBoxInfo.setX(this.getX());
        methodsBoxInfo.setY(fieldsBoxInfo.getY() + nameBoxInfo.getHeight() + 8);
    }

    public void setSuperClassCoordinate(int centerX, int startY, int width, int height) {
        this.setX(centerX - (width / 2));
        this.setY(startY - 70 - height);

        nameBoxInfo.setX(this.getX());
        nameBoxInfo.setY(this.getY());

        fieldsBoxInfo.setX(this.getX());
        fieldsBoxInfo.setY(nameBoxInfo.getHeight());

        methodsBoxInfo.setX(this.getX());
        methodsBoxInfo.setY(fieldsBoxInfo.getY() + nameBoxInfo.getHeight() + 8);
    }

    public void setEntireClassInfo() {
        // Class information
        this.className = this.javaParser.getClassName();
        this.isClassType = this.javaParser.isClassType();

        // Field information
        this.fieldNameList = this.javaParser.getFieldNameList();
        this.fieldInfoList = this.javaParser.getFieldInfoList();
        this.fieldDeclarationList = this.javaParser.getFieldDeclarationList();

        // Constructor information
        this.constructorNameList = this.javaParser.getConstructorNameList();
        this.constructorInfoList = this.javaParser.getConstructorInfoList();
        this.constructorDeclarationList = this.javaParser.getConstructorDeclarationList();

        // Method information
        this.methodNameList = this.javaParser.getMethodNameList();
        this.methodInfoList = this.javaParser.getMethodInfoList();
        this.methodDeclarationList = this.javaParser.getMethodDeclarationList();
    }


    public void setNameBoxInfo(Box nameBoxInfo) { this.nameBoxInfo = nameBoxInfo; }
    public Box getNameBoxInfo() { return this.nameBoxInfo; }

    public void setFieldsBoxInfo(Box fieldsBoxInfo) { this.fieldsBoxInfo = fieldsBoxInfo; }
    public Box getFieldsBoxInfo() { return this.fieldsBoxInfo; }

    public void setMethodsBoxInfo(Box methodsBoxInfo) { this.methodsBoxInfo = methodsBoxInfo; }
    public Box getMethodsBoxInfo() { return this.methodsBoxInfo; }

    public void setExtends(String _extends) { this._extends = _extends; }
    public String getExtends() { return this._extends; }

    public void setInterface(String _interface) { this._interface = _interface; }
    public String getInterface() { return this._interface; }

    public void setClassId(int classId) { this.classId = classId; }
    public int getClassId() { return this.classId; }


    public String getClassName() { return this.className; }
    public boolean isClassType() { return this.isClassType; }

    public List<String> getFieldInfoList() { return this.fieldInfoList; }
    public List<FieldDeclaration> getFieldDeclarationList() { return this.fieldDeclarationList; }

    public List<String> getConstructorInfoList() { return this.constructorInfoList;}
    public List<ConstructorDeclaration> getConstructorDeclarationList() { return this.constructorDeclarationList; }

    public List<String> getMethodInfoList() { return this.methodInfoList; }
    public List<MethodDeclaration> getMethodDeclarationList() { return this.methodDeclarationList; }
}
