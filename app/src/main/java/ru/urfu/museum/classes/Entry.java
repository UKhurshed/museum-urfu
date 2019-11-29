package ru.urfu.museum.classes;

public class Entry {

    public int id;
    public int image;
    public String title;
    public String author;
    public String text;
    public int[] gallery;
    public int floor;

    public Entry(int id, int image, String title, String author, String text, int[] gallery, int floor) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.author = author;
        this.text = text;
        this.gallery = gallery;
        this.floor = floor;
    }

}
