package ru.urfu.museum.classes;

public class Entry {

    public int id;
    public int image;
    public String title;
    public String author;
    public String description;
    public int floor;

    public Entry(int id, int image, String title, String author, String description, int floor) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.author = author;
        this.description = description;
        this.floor = floor;
    }

}
