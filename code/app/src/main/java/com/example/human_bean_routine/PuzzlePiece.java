package com.example.human_bean_routine;

public class PuzzlePiece {

    public enum PieceStatus {
        LOCKED, UNLOCKED, REVEALED
    }

    private Integer pieceID;
    private Number xCoord;
    private Number yCoord;
    private Number edgeLength;
    private Integer puzzleID;
    private PieceStatus status;
    private String dateUnlocked;
    private Integer tasksCompleted;
    private String userMessage;

    public PuzzlePiece(Number xCoord, Number yCoord, Number edgeLength, Integer puzzleID,
                       PieceStatus status) {
        this.pieceID = -1;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.edgeLength = edgeLength;
        this.puzzleID = puzzleID;
        this.status = status;
        this.dateUnlocked = "";
        this.tasksCompleted = -1;
        this.userMessage = "";
    }

    public PuzzlePiece(Integer pieceID, Number xCoord, Number yCoord, Number edgeLength,
                       Integer puzzleID, PieceStatus status, String dateUnlocked,
                       Integer tasksCompleted, String userMessage) {
        this.pieceID = pieceID;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.edgeLength = edgeLength;
        this.puzzleID = puzzleID;
        this.status = status;
        this.dateUnlocked = dateUnlocked;
        this.tasksCompleted = tasksCompleted;
        this.userMessage = userMessage;
    }


    public Integer getPieceID() {
        return pieceID;
    }

    public void setPieceID(Integer pieceID) {
        this.pieceID = pieceID;
    }

    public Number getxCoord() {
        return xCoord;
    }

    public void setxCoord(Number xCoord) {
        this.xCoord = xCoord;
    }

    public Number getyCoord() {
        return yCoord;
    }

    public void setyCoord(Number yCoord) {
        this.yCoord = yCoord;
    }

    public Number getEdgeLength() {
        return edgeLength;
    }

    public void setEdgeLength(Number edgeLength) {
        this.edgeLength = edgeLength;
    }

    public Integer getPuzzleID() {
        return puzzleID;
    }

    public void setPuzzleID(Integer puzzleID) {
        this.puzzleID = puzzleID;
    }

    public PieceStatus getStatus() {
        return status;
    }

    public void setStatus(PieceStatus status) {
        this.status = status;
    }

    public String getDateUnlocked() {
        return dateUnlocked;
    }

    public void setDateUnlocked(String dateUnlocked) {
        this.dateUnlocked = dateUnlocked;
    }

    public Integer getTasksCompleted() {
        return tasksCompleted;
    }

    public void setTasksCompleted(Integer tasksCompleted) {
        this.tasksCompleted = tasksCompleted;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

}
