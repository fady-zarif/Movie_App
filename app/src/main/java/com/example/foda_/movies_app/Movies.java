package com.example.foda_.movies_app;

/**
 * Created by foda_ on 2016-03-25.
 */
public class Movies {
    String  id;
    String JsonPath;
    String overview;
    String release_date;
    String original_title;
    String backdrop_path;
    String vote_average;

    public Movies(String id,String JsonPath, String overview, String release_date, String original_title, String backdrop_path, String vote_average) {
        this.id=id;
        this.JsonPath=JsonPath;
        this.overview=overview;
        this.release_date=release_date;
        this.original_title=original_title;
        this.backdrop_path=backdrop_path;
        this.vote_average=vote_average;
    }
    public Movies(String JsonPath, String overview, String release_date, String original_title ,String vote_average) {
        this.JsonPath=JsonPath;
        this.overview=overview;
        this.release_date=release_date;
        this.original_title=original_title;
        this.vote_average=vote_average;
    }
}

