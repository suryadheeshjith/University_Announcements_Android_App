package com.example.javaprojone;


import java.util.Date;

public class event_class {

    public String Title;
    public String Body;
    public Date startdate;
    public Date enddate;
    public String Venue;
    public String Posted_By;


    event_class()
    {
    }
    event_class(String Title, String Body, Date startdate, Date enddate, String postedBy,String Venue)
    {
        this.Title = Title;
        this.Body = Body;
        this.startdate=startdate;
        this.enddate=enddate;
        this.Posted_By=postedBy;
        this.Venue = Venue;

    }
}