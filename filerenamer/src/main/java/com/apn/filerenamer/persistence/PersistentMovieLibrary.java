package com.apn.filerenamer.persistence;

import com.apn.filerenamer.entities.Movie;
import com.google.code.morphia.Datastore;
import com.google.code.morphia.dao.BasicDAO;

import java.util.List;

public class PersistentMovieLibrary extends BasicDAO<Movie,String> implements MovieLibrary {

    public PersistentMovieLibrary(Datastore ds) {
        super(ds);
    }


    @Override
    public void addMovie(Movie movie){
        if(exists(ds.createQuery(Movie.class).field("name").equal(movie.name()))){
            throw new AlreadyInLibraryException(movie.name());
        }
        save(movie);
    }

    @Override
    public List<Movie> movieList() {
        return null;
    }
}
