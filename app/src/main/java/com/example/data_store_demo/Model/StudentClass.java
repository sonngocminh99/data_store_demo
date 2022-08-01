package com.example.data_store_demo.Model;

public class StudentClass {
    private String className;

    public StudentClass(String className) {
        this.className = className;
    }

    public StudentClass() {
        this.className = "";
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
