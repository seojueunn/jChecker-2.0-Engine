package edu.isel.csee.jchecker2_0.diagram.utils;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import edu.isel.csee.jchecker2_0.diagram.parser.JavaParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for diagram box (class)
 */
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

    /**
     * Constructor for ClassBox class
     * @param javaParser JavaParser
     */
    public ClassBox(JavaParser javaParser) {
        this.javaParser = javaParser;

        nameBoxInfo = new Box();
        fieldsBoxInfo = new Box();
        methodsBoxInfo = new Box();

        this.setEntireClassInfo();
        this.setWidthHeight();
    }

    /**
     * Method for setting width and height
     */
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

    /**
     * Method for setting coordinate of subclass
     * @param index index value
     * @param totalWidth total width
     * @param startY y value for start
     * @param maxHeight max height
     */
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

    /**
     * Method for setting coordinate of superclass
     * @param centerX x value for center
     * @param startY y value for start
     * @param width width value
     * @param height height value
     */
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

    /**
     * Method for setting class, field, constructor, method information
     */
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

    /**
     * Method for setting name box
     * @param nameBoxInfo name box information
     */
    public void setNameBoxInfo(Box nameBoxInfo) { this.nameBoxInfo = nameBoxInfo; }

    /**
     * Method for return name box
     * @return nameBoxInfo
     */
    public Box getNameBoxInfo() { return this.nameBoxInfo; }

    /**
     * Method for setting field box
     * @param fieldsBoxInfo field box information
     */
    public void setFieldsBoxInfo(Box fieldsBoxInfo) { this.fieldsBoxInfo = fieldsBoxInfo; }

    /**
     * Method for return field box
     * @return fieldsBoxInfo
     */
    public Box getFieldsBoxInfo() { return this.fieldsBoxInfo; }

    /**
     * Method for setting method box
     * @param methodsBoxInfo method box information
     */
    public void setMethodsBoxInfo(Box methodsBoxInfo) { this.methodsBoxInfo = methodsBoxInfo; }

    /**
     * Method for method box
     * @return methodsBoxInfo
     */
    public Box getMethodsBoxInfo() { return this.methodsBoxInfo; }

    /**
     * Method for setting extension information
     * @param _extends extension information
     */
    public void setExtends(String _extends) { this._extends = _extends; }

    /**
     * Method for return extension information
     * @return _extends
     */
    public String getExtends() { return this._extends; }

    /**
     * Method for setting implementation information
     * @param _interface implementation information
     */
    public void setInterface(String _interface) { this._interface = _interface; }

    /**
     * Method for return implementation information
     * @return _interface
     */
    public String getInterface() { return this._interface; }

    /**
     * Method for setting ID of class box
     * @param classId ID of class box
     */
    public void setClassId(int classId) { this.classId = classId; }

    /**
     * Method for return ID of class box
     * @return classId
     */
    public int getClassId() { return this.classId; }

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
     * Method for return field information list
     * @return fieldInfoList
     */
    public List<String> getFieldInfoList() { return this.fieldInfoList; }

    /**
     * Method for return FieldDeclaration list
     * @return fieldDeclarationList
     */
    public List<FieldDeclaration> getFieldDeclarationList() { return this.fieldDeclarationList; }

    /**
     * Method for return constructor information list
     * @return constructorInfoList
     */
    public List<String> getConstructorInfoList() { return this.constructorInfoList;}

    /**
     * Method for return ConstructorDeclaration list
     * @return constructorDeclarationList
     */
    public List<ConstructorDeclaration> getConstructorDeclarationList() { return this.constructorDeclarationList; }

    /**
     * Method for return method information list
     * @return methodInfoList
     */
    public List<String> getMethodInfoList() { return this.methodInfoList; }

    /**
     * Method for return MethodDeclaration list
     * @return methodDeclarationList
     */
    public List<MethodDeclaration> getMethodDeclarationList() { return this.methodDeclarationList; }
}
