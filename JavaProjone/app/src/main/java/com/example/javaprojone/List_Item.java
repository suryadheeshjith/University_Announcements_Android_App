package com.example.javaprojone;

import com.google.firebase.Timestamp;

import java.util.Date;

public class List_Item {

    private String Title;
    private String Body;
    private String Footer;
    private Timestamp date;

    public List_Item(String Title, String Body, String Footer) {

        this.Title = Title;
        this.Body = Body;
        this.Footer = Footer;
    }


    public List_Item(String Title, String Body, String Footer,Timestamp date) {

        this.Title = Title;
        this.Body = Body;
        this.Footer = Footer;
        this.date = date;
    }



    public String getTitl() {
        return Title;
    }

    public void setBody(String Body) {
        this.Body = Body;
    }

    public String getBody() {
        return Body;
    }

    public void setTitl(String Title) {
        this.Title = Title;
    }
    public void setFooter(String Footer) {
        this.Footer = Footer;
    }

    public String getFooter() {
        return Footer;
    }

    public Timestamp getDate() {
        return date;
    }
}
