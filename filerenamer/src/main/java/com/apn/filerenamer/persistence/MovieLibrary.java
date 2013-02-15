package com.apn.filerenamer.persistence;

import com.apn.filerenamer.entities.Movie;

import java.util.List;

public interface MovieLibrary {
    void addMovie(Movie movie);

    List<Movie> movieList();
}
