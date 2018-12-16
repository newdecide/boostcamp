package com.example.ohdongju.pretaskmovie;

import android.graphics.Bitmap;
import android.widget.RatingBar;

import java.util.BitSet;

public class Movie {
    //리퀘스로 받아 오는 Json 데이터를 참조해서 만들자.
    private String poster, title, pubDate, director, actor, link;
    private float rating;

    public Movie(String title, String poster, float userRating, String pubDate, String director, String actor, String link) {
        this.title = title;
        this.poster = poster;
        this.rating = userRating;
        this.pubDate = pubDate;
        this.director = director;
        this.actor = actor;
        this.link = link;
    }

    public String getPoster() {
        return poster;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getDirector() {
        return director;
    }

    public String getActor() {
        return actor;
    }

    public String getLink() {
        return link;
    }

    public float getRating() {
        return rating;
    }
}
