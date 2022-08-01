package com.example.data_store_demo.Model;

public class Student {
    private String MSSV;
    private String name;
    private String email;
    private StudentClass studentClass;
    private String objectID;

    public Student(String MSSV, String name, String email, StudentClass studentClass,String objectID) {
        this.MSSV = MSSV;
        this.name = name;
        this.email = email;
        this.studentClass = studentClass;
        this.objectID = objectID;
    }

    public Student() {
        this.MSSV = "";
        this.name = "";
        this.email = "";
        this.studentClass = new StudentClass();
        this.objectID = "";
    }

    public String getMSSV() {
        return MSSV;
    }

    public void setMSSV(String MSSV) {
        this.MSSV = MSSV;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StudentClass getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(StudentClass studentClass) {
        this.studentClass = studentClass;
    }

    public String getObjectID() {
        return objectID;
    }

    public void setObjectID(String objectID) {
        this.objectID = objectID;
    }
}
