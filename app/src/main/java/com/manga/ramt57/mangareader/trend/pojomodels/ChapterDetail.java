package com.manga.ramt57.mangareader.trend.pojomodels;

import java.util.ArrayList;

/**
 * Created by user on 15-09-2017.
 */

public class ChapterDetail {
    String title;
    int chapters_len;
    ArrayList<String> categories;
    String author;
    String artist;
    String image;
    String description;
    int released;
    double last_chaper_date;
    ArrayList<Object> chapters;
    public  ChapterDetail(){

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChapters_len() {
        return chapters_len;
    }

    public void setChapters_len(int chapters_len) {
        this.chapters_len = chapters_len;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getReleased() {
        return released;
    }

    public void setReleased(int released) {
        this.released = released;
    }

    public double getLast_chaper_date() {
        return last_chaper_date;
    }

    public void setLast_chaper_date(double last_chaper_date) {
        this.last_chaper_date = last_chaper_date;
    }

    public ArrayList<Object> getChapters() {
        return chapters;
    }

    public void setChapters(ArrayList<Object> chapters) {
        this.chapters = chapters;
    }
}
