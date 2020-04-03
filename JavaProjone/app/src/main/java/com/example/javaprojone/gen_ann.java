package com.example.javaprojone;


import com.google.firebase.Timestamp;

public class gen_ann{

    public String Title;
    public Timestamp Date;
    public String Posted_By;
    public String Body;

    gen_ann()
    {
    }
    gen_ann(String Title,Timestamp Date,String postedBy,String body)
    {
        this.Title = Title;
        this.Body = body;
        this.Date=Date;
        this.Posted_By=postedBy;

    }
}