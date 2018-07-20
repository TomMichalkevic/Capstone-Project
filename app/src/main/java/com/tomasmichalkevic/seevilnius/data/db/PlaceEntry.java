package com.tomasmichalkevic.seevilnius.data.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "place")
public class PlaceEntry {

    public static final String TABLE_NAME = "place";
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String place_id;
    private String address;
    private String rating;
    private String telephone;
    private String openingHours;
    private String reviews;
    private Boolean visited;
    @ColumnInfo(name = "visited_time")
    private Date visitedTime;

    @Ignore
    public PlaceEntry(String place_id, String address, String rating, String telephone, String openingHours, String reviews, Boolean visited, Date visitedTime) {
        this.place_id = place_id;
        this.address = address;
        this.rating = rating;
        this.telephone = telephone;
        this.openingHours = openingHours;
        this.reviews = reviews;
        this.visited = visited;
        this.visitedTime = visitedTime;
    }

    public PlaceEntry(int id, String place_id, String address, String rating, String telephone, String openingHours, String reviews, Boolean visited, Date visitedTime) {
        this.id = id;
        this.place_id = place_id;
        this.address = address;
        this.rating = rating;
        this.telephone = telephone;
        this.openingHours = openingHours;
        this.reviews = reviews;
        this.visited = visited;
        this.visitedTime = visitedTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public Boolean getVisited() {
        return visited;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }

    public Date getVisitedTime() {
        return visitedTime;
    }

    public void setVisitedTime(Date visitedTime) {
        this.visitedTime = visitedTime;
    }

}
