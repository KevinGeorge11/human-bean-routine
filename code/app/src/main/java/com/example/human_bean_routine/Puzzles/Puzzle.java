package com.example.human_bean_routine.Puzzles;

public class Puzzle {
    private Integer puzzleID;
    private String name;
    private Boolean active;
    private String imagePath;
    private Boolean complete;


    public Puzzle(Integer puzzleID, String name, Boolean active, String imagePath, Boolean complete) {
        this.puzzleID = puzzleID;
        this.name = name;
        this.active = active;
        this.imagePath = imagePath;
        this.complete = complete;
    }

    public Puzzle(String name, Boolean active, String imagePath, Boolean complete) {
        this.puzzleID = -1;
        this.name = name;
        this.active = active;
        this.imagePath = imagePath;
        this.complete = complete;
    }

    public Integer getPuzzleID() {
        return puzzleID;
    }

    public void setPuzzleID(Integer puzzleID) {
        this.puzzleID = puzzleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Boolean getComplete() {
        return complete;
    }

    public void setComplete(Boolean complete) {
        this.complete = complete;
    }
}
