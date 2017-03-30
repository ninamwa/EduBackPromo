package com.example.bruker.edubackpromo.data.models;

public class Fag {

    String name;
    String code;
    String examdate;
    String teachername;

    public Fag(){
        name= null;
        code=null;
        examdate=null;
        teachername=null;
    }

    public Fag(String kode,String fagnavn, String eksamen, String lærer){
        this.name=fagnavn;
        this.code=kode;
        this.examdate=eksamen;
        this.teachername=lærer;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getExamdate() {
        return examdate;
    }

    public void setExamdate(String examdate) {
        this.examdate = examdate;
    }

    public String getTeachername() {
        return teachername;
    }

    public void setTeachername(String teachername) {
        this.teachername = teachername;
    }

}
