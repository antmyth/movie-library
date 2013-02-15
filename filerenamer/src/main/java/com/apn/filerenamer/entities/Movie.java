package com.apn.filerenamer.entities;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;

@Entity
public class Movie {
    @Id
    private String name;
    private String location;

    //used by morphia
    @SuppressWarnings("UnusedDeclaration")
    public Movie() {
    }

    public Movie(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String name() {
        return name;
    }

    public String location() {
        return location;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
