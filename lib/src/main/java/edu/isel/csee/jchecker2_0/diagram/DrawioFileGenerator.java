package edu.isel.csee.jchecker2_0.diagram;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import edu.isel.csee.jchecker2_0.diagram.parser.JavaParser;
import edu.isel.csee.jchecker2_0.diagram.utils.ClassBox;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawioFileGenerator {
    private String className; // Class name of the submission
    private String studentNum; // Student number of the submission
    private List<String> sourcePathList;
    private String xmlPath;
    private ArrayList<ClassBox> javaClassBoxList; // Rapper class for JavaClassSource
    private Document document;
    private Element root;
    private int maxWidth;
    private int maxHeight;
    private int id;

    private List<String> superClassOrInterfaceNameList = new ArrayList<>();
    private HashMap<String, List<ClassBox>> classBoxGroupMap = new HashMap<>();

    // Constants for drawio XML
    private final String inclassStyleConstant = "text;html=1;strokeColor=none;fillColor=none;align=left;verticalAlign=middle;spacingLeft=4;spacingRight=4;overflow=hidden;rotatable=0;points=[[0,0.5],[1,0.5]];portConstraint=eastwest;";
    private final String classNameStyleConstant = "swimlane;fontStyle=0;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=30;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=0;marginBottom=0;html=1;";
    private final String interfaceNameStyleConstant = "swimlane;fontStyle=0;align=center;verticalAlign=top;childLayout=stackLayout;horizontal=1;startSize=40;horizontalStack=0;resizeParent=1;resizeParentMax=0;resizeLast=0;collapsible=0;marginBottom=0;html=1;";
    private final String separatorLineStyleConstant = "line;strokeWidth=1;fillColor=none;align=left;verticalAlign=middle;spacingTop=-1;spacingLeft=3;spacingRight=3;rotatable=0;labelPosition=right;points=[];portConstraint=eastwest;";
    private final String interfaceLineStyleConstant = "endArrow=block;dashed=1;endFill=0;endSize=12;html=1;exitX=0.5;exitY=0;exitDx=0;exitDy=0;" ;
    private final String extendLineStyleConstant = "endArrow=block;endSize=16;endFill=0;html=1" ;

    public DrawioFileGenerator(String className, String studentNum, List<String> sourcePathList) {
        this.className = className;
        this.studentNum = studentNum;
        this.sourcePathList = sourcePathList;
        this.javaClassBoxList = getJavaClassSources();
        this.id = 0;
    }

    // Main method for generating a drawio XML file
    public void generateDrawioFile() {
        // Set attributes
        initXMLFile();

        // Create the XML file
        createXMLFile();
        System.out.println("Done creating XML file.\n");
    }

    // Set attributes for creating an XML file
    public void initXMLFile() {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();

            Element diagram = document.createElement("diagram");
            addAttribute(diagram, "id", "SNbYQcsz_Utg5FWgZMJS");
            addAttribute(diagram, "name", "Page-1");
            document.appendChild(diagram);

            // Employee element (Constant)
            Element mxGraphModel = document.createElement("mxGraphModel");
            addAttribute(mxGraphModel, "dx", "332");
            addAttribute(mxGraphModel, "dy", "241");
            addAttribute(mxGraphModel, "grid", "1");
            addAttribute(mxGraphModel, "gridSize", "10");
            addAttribute(mxGraphModel, "guides", "1");
            addAttribute(mxGraphModel, "tooltips", "1");
            addAttribute(mxGraphModel, "connect", "1");
            addAttribute(mxGraphModel, "arrows", "1");
            addAttribute(mxGraphModel, "fold", "1");
            addAttribute(mxGraphModel, "page", "1");
            addAttribute(mxGraphModel, "pageScale", "1");
            addAttribute(mxGraphModel, "pageWidth", "5000");
            addAttribute(mxGraphModel, "pageHeight", "5000");
            addAttribute(mxGraphModel, "math", "0");
            addAttribute(mxGraphModel, "shadow", "0");
            diagram.appendChild(mxGraphModel);

            root = document.createElement("root");
            mxGraphModel.appendChild(root);

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    // Create the XML file using ClassBox information
    public void createXMLFile() {
        int index = 1;

        // Draw a ClassBox
        for (ClassBox classBox : javaClassBoxList) {
            drawClassBox(classBox, index);
            index ++;
        }

        // Draw a relationship lines
        for (ClassBox classBox : javaClassBoxList) {
            if (classBox.getExtends() != "") {
                ClassBox superClassBox = null;
                for (ClassBox cb : javaClassBoxList) {
                    if (classBox.getClassId() == cb.getClassId()) { continue; }
                    if (classBox.getExtends().equals(cb.getClassName())) {
                        superClassBox = cb;
                        break;
                    }
                }

                if (superClassBox != null) {
                    drawLines(0, classBox, superClassBox);
                }
            }

            if (classBox.getInterface() != "") {
                ClassBox interfaceBox = null;
                for (ClassBox cb : javaClassBoxList) {
                    if (classBox.getClassId() == cb.getClassId()) { continue; }
                    if (((cb.getClassName()).contains("." + classBox.getInterface())) && ((cb.getClassName()).contains("<< interface >>"))) {
                        interfaceBox = cb;
                        break;
                    }
                }

                if (interfaceBox != null) {
                    drawLines(1, classBox, interfaceBox);
                }
            }
        }

        // Transform the DOM Object to an XML file
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource domSource = new DOMSource(document);

            // String rootDir = "/Users/seojueun/data/";
            String rootDir = "/data/jchecker2.0/";
            String classDir = className + "/";
            String studentDir = studentNum + "/";
            xmlPath = rootDir + classDir + studentDir;

            File xmlDir = new File(xmlPath);
            if (!xmlDir.exists()) {
                boolean result = xmlDir.mkdirs();
                if (result) {
                    System.out.println("Directory created successfully: " + xmlPath);
                } else {
                    System.out.println("Failed to create directory: " + xmlPath);
                }
            }

            StreamResult streamXML = new StreamResult(new File(xmlPath + "/drawio.xml"));
            transformer.transform(domSource, streamXML);

        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ClassBox> getJavaClassSources() {
        javaClassBoxList = new ArrayList<>();

        // Loop for making ClassBox objects
        for (String path : sourcePathList) {
            JavaParser javaParser = new JavaParser(path);
            javaParser.collectClassBoxInfo();

            ClassBox classBox = new ClassBox(javaParser);
            javaClassBoxList.add(classBox);

            // Set "extends" and "interface" relationship
            for (String superClassName : javaParser.getSuperClassNameList()) {
                classBox.setExtends(superClassName);
                if (!superClassOrInterfaceNameList.contains(superClassName)) { superClassOrInterfaceNameList.add(superClassName); }
            }
            for (String interfaceName : javaParser.getInterfaceNameList()) {
                classBox.setInterface(interfaceName);
                if (!superClassOrInterfaceNameList.contains(interfaceName)) { superClassOrInterfaceNameList.add(interfaceName); }
            }
        }

        // Group the class based on relationships
        for (ClassBox classBox : javaClassBoxList) {
            String[] fullyQualifiedName = classBox.getClassName().split("\\.");
            String className = fullyQualifiedName[fullyQualifiedName.length - 1];
            List<ClassBox> classBoxList;
            if (!superClassOrInterfaceNameList.contains(className)) {
                if (classBox.getExtends().isEmpty() && classBox.getInterface().isEmpty()) {
                    // System.out.println("ONLY CLASS: " + className);
                    if (!classBoxGroupMap.containsKey("")) {
                        classBoxList = new ArrayList<>();
                    } else {
                        classBoxList = classBoxGroupMap.get("");
                    }
                    classBoxList.add(classBox);
                    classBoxGroupMap.put("", classBoxList);
                } else {
                    if (!classBox.getExtends().isEmpty()) {
                        // System.out.println("EXTENDS: " + className);
                        String superClassName = classBox.getExtends();
                        if (!classBoxGroupMap.containsKey(superClassName)) {
                            classBoxList = new ArrayList<>();
                        } else {
                            classBoxList = classBoxGroupMap.get(superClassName);
                        }
                        classBoxList.add(classBox);
                        classBoxGroupMap.put(superClassName, classBoxList);
                    } else {
                        // System.out.println("IMPLEMENTS: " + className);
                        String interfaceName = classBox.getInterface();
                        if (!classBoxGroupMap.containsKey(interfaceName)) {
                            classBoxList = new ArrayList<>();
                        } else {
                            classBoxList = classBoxGroupMap.get(interfaceName);
                        }
                        classBoxList.add(classBox);
                        classBoxGroupMap.put(interfaceName, classBoxList);
                    }
                }
            }
        }

        // Loop for getting a max width and height of a ClassBox
        for (int i = 0; i < javaClassBoxList.size(); i ++) {
            maxWidth = Math.max(maxWidth, javaClassBoxList.get(i).getWidth());
            maxHeight = Math.max(maxHeight, javaClassBoxList.get(i).getHeight());
        }

        // Set the position and coordinate of classBoxes
        setClassBoxPosition(classBoxGroupMap);

        return javaClassBoxList;
    }

    public void setClassBoxPosition(HashMap<String, List<ClassBox>> classBoxMap) {
        int centerX;
        int startYForSuper;
        int startYForSub = 80;
        int startYTemp = 80;
        int index = 0;
        int totalWidth = 0;
        int maxHeight = 0;
        int maxHeightTemp = 0;
        int count = 0;
        List<ClassBox> classBoxList;

        for (String superClassName : classBoxMap.keySet()) {
            if (!superClassName.equals("")) {
                classBoxList = classBoxMap.get(superClassName);
                for (int i = 0; i < classBoxList.size(); i++) {
                    if ((index % 5) == 0) {
                        totalWidth = 0;
                        maxHeight = maxHeightTemp;
                        startYForSub = startYTemp;
                        maxHeightTemp = 0;
                    }

                    maxHeightTemp = Math.max(maxHeightTemp, classBoxList.get(i).getHeight());
                    classBoxList.get(i).setSubClassCoordinate(index, totalWidth, startYForSub, maxHeight);
                    if (maxHeightTemp == classBoxList.get(i).getHeight()) { startYTemp = classBoxList.get(i).getY(); }
                    totalWidth += classBoxList.get(i).getWidth();
                    index ++;
                }

                if ((count != classBoxMap.keySet().size() - 2) && ((index % 5) != 0)) { index += (5 - (index % 5)); }

                if (classBoxList.size() <= 5) {
                    if (classBoxList.size() % 2 == 0) {
                        ClassBox firstSubClass = classBoxList.get(0);
                        ClassBox lastSubClass = classBoxList.get(classBoxList.size() - 1);
                        centerX = (firstSubClass.getX() + lastSubClass.getX() + lastSubClass.getWidth()) / 2;
                    } else {
                        ClassBox middleSubClass = classBoxList.get(classBoxList.size() / 2);
                        centerX = middleSubClass.getX() + (middleSubClass.getWidth() / 2);
                    }
                } else {
                    ClassBox middleSubClass = classBoxList.get(2);
                    centerX = middleSubClass.getX() + (middleSubClass.getWidth() / 2);
                }

                startYForSuper = classBoxList.get(0).getY();

                for (ClassBox superClass : javaClassBoxList) {
                    if (superClass.getClassName().contains("." + superClassName)) {
                        int width = superClass.getWidth();
                        int height = superClass.getHeight();
                        superClass.setSuperClassCoordinate(centerX, startYForSuper, width, height);
                    }
                }
            }
            count ++;
        }

        if (classBoxMap.containsKey("")) {
            classBoxList = classBoxMap.get("");
            for (int i = 0; i < classBoxList.size(); i ++) {
                if ((index % 5) == 0) {
                    totalWidth = 0;
                    maxHeight = maxHeightTemp;
                    startYForSub = startYTemp;
                    maxHeightTemp = 0;
                }

                maxHeightTemp = Math.max(maxHeightTemp, classBoxList.get(i).getHeight());
                classBoxList.get(i).setSubClassCoordinate(index, totalWidth, startYForSub, maxHeight);
                if (maxHeightTemp == classBoxList.get(i).getHeight()) { startYTemp = classBoxList.get(i).getY(); }
                totalWidth += classBoxList.get(i).getWidth();
                index ++;
            }
        }
    }

    // Draw a ClassBox
    public void drawClassBox(ClassBox classBox, int index) {
        // XML header
        if (index == 1) {
            Element biggestBox = document.createElement("mxCell");
            addAttribute(biggestBox, "id", Integer.toString(id ++));
            root.appendChild(biggestBox);

            Element biggerBox = document.createElement("mxCell");
            addAttribute(biggerBox, "id", Integer.toString(id ++));
            addAttribute(biggerBox, "parent", "0");
            root.appendChild(biggerBox);
        }

        int classId = id;
        classBox.setClassId(id);

        // Draw a box for class name
        Element classNameBox = document.createElement("mxCell");
        addAttribute(classNameBox, "id", Integer.toString(id ++));
        addAttribute(classNameBox, "value", classBox.getClassName());
        if (classBox.isClassType()) {
            addAttribute(classNameBox, "style", classNameStyleConstant);
        } else {
            addAttribute(classNameBox, "style", interfaceNameStyleConstant);
        }
        addAttribute(classNameBox, "vertex", "1");
        addAttribute(classNameBox, "parent", "1");
        root.appendChild(classNameBox);

        addMxGeometry(classNameBox, classBox.getX(), classBox.getY(), classBox.getWidth(), classBox.getHeight());

        // Draw a FieldsBox
        int y = classBox.getFieldsBoxInfo().getY();
        List<FieldDeclaration> fieldDeclarationList = classBox.getFieldDeclarationList();
        List<String> fieldInfoList = classBox.getFieldInfoList();
        for (int i = 0; i < fieldDeclarationList.size(); i ++) {
            drawFieldBox(fieldDeclarationList.get(i), fieldInfoList.get(i), classId, y, classBox.getWidth());
            y += 26;
        }

        // Draw a SeparatorLine
        drawSeparatorLine(classId, y, classBox.getWidth());
        y += 8;

        // Draw a MethodsBox
        List<ConstructorDeclaration> constructorDeclarationList = classBox.getConstructorDeclarationList();
        List<String> constructorInfoList = classBox.getConstructorInfoList();
        for (int i = 0; i < constructorDeclarationList.size(); i ++) {
            drawMethodBox(constructorDeclarationList.get(i), constructorInfoList.get(i), classId, y, classBox.getWidth());
            y += 26;
        }

        List<MethodDeclaration> methodDeclarationList = classBox.getMethodDeclarationList();
        List<String> methodInfoList = classBox.getMethodInfoList();
        for (int i = 0; i < methodDeclarationList.size(); i ++) {
            drawMethodBox(methodDeclarationList.get(i), methodInfoList.get(i), classId, y, classBox.getWidth());
            y += 26;
        }
    }

    // Draw a FieldsBox
    public void drawFieldBox(FieldDeclaration fieldDeclaration, String fieldInfo, int classId, int y, int width) {
        Element fieldBox = document.createElement("mxCell");
        addAttribute(fieldBox, "id", Integer.toString(id ++));
        addAttribute(fieldBox, "value", fieldInfo);

        // Check whether the field is static or not
        if (fieldDeclaration.isStatic()) {
            if (!fieldDeclaration.isFinal()) { addAttribute(fieldBox, "style", inclassStyleConstant + "fontStyle=4;");}
            else { addAttribute(fieldBox, "style", inclassStyleConstant); }
        }
        else { addAttribute(fieldBox, "style", inclassStyleConstant); }

        addAttribute(fieldBox, "vertex", "1");
        addAttribute(fieldBox, "parent", Integer.toString(classId));
        addMxGeometry(fieldBox, -1, y, width, 26);
        root.appendChild(fieldBox);
    }

    // Draw a MethodsBox (constructor)
    public void drawMethodBox(ConstructorDeclaration constructorDeclaration, String constructorInfo, int classId, int y, int width) {
        Element methodBox = document.createElement("mxCell");
        addAttribute(methodBox, "id", Integer.toString(id ++));
        addAttribute(methodBox, "value", constructorInfo);

        // Check whether the method is static or not
        if (constructorDeclaration.isStatic()){ addAttribute(methodBox, "style", inclassStyleConstant + "fontStyle=4;"); }
        else { addAttribute(methodBox, "style", inclassStyleConstant); }

        addAttribute(methodBox, "vertex", "1");
        addAttribute(methodBox, "parent", Integer.toString(classId));
        addMxGeometry(methodBox, -1, y , width, 26);
        root.appendChild(methodBox);
    }

    // Draw a MethodsBox (method)
    public void drawMethodBox(MethodDeclaration methodDeclaration, String methodInfo, int classId, int y, int width) {
        Element methodBox = document.createElement("mxCell");
        addAttribute(methodBox, "id", Integer.toString(id ++));
        addAttribute(methodBox, "value", methodInfo);

        // Check whether the method is static or not
        if (methodDeclaration.isStatic()){ addAttribute(methodBox, "style", inclassStyleConstant + "fontStyle=4;"); }
        else { addAttribute(methodBox, "style", inclassStyleConstant); }

        addAttribute(methodBox, "vertex", "1");
        addAttribute(methodBox, "parent", Integer.toString(classId));
        addMxGeometry(methodBox, -1, y , width, 26);
        root.appendChild(methodBox);
    }

    // Draw a relationship lines
    public void drawLines(int value, ClassBox sourceClassBox, ClassBox targetClassBox) {
        Element lines = document.createElement("mxCell");
        addAttribute(lines, "id", Integer.toString(id ++));

        if (value == 0) {
            addAttribute(lines, "value", "Extends");
            addAttribute(lines, "style", extendLineStyleConstant);
        } else {
            addAttribute(lines, "value", "");
            addAttribute(lines, "style", interfaceLineStyleConstant);
        }

        addAttribute(lines, "edge", "1");
        addAttribute(lines, "parent", "1");
        addAttribute(lines, "source", Integer.toString(sourceClassBox.getClassId()));
        addAttribute(lines, "target",  Integer.toString(targetClassBox.getClassId()));

        addLineMxGeometry(lines);
        root.appendChild(lines);
    }

    // Draw a SeparatorLine
    public void drawSeparatorLine(int classId, int y, int width) {
        Element separator = document.createElement("mxCell");
        addAttribute(separator, "id", Integer.toString(id++));
        addAttribute(separator, "style", separatorLineStyleConstant);
        addAttribute(separator, "vertex", "1");
        addAttribute(separator, "parent", Integer.toString(classId));
        addMxGeometry(separator, -1, y , width, 8);
        root.appendChild(separator);
    }

    // Add the attribute (helper function)
    public void addAttribute(Element element, String attrName, String attrValue) {
        Attr attr = document.createAttribute(attrName);
        attr.setValue(attrValue);
        element.setAttribute(attrName, attrValue);
    }

    // Add the child attribute (helper function)
    public void addMxGeometry(Element element, int x, int y, int width, int height) {
        Element mxGeometry = document.createElement("mxGeometry");
        if (x != -1) { addAttribute(mxGeometry, "x", Integer.toString(x)); }
        if (y != -1) { addAttribute(mxGeometry, "y", Integer.toString(y)); }
        addAttribute(mxGeometry, "width", Integer.toString(width));
        addAttribute(mxGeometry, "height", Integer.toString(height));
        addAttribute(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }

    // Add the lines (helper function)
    public void addLineMxGeometry(Element element) {
        Element mxGeometry = document.createElement("mxGeometry");
        addAttribute(mxGeometry, "width", "160");
        addAttribute(mxGeometry, "relative", "1");
        addAttribute(mxGeometry, "as", "geometry");
        element.appendChild(mxGeometry);
    }
}
