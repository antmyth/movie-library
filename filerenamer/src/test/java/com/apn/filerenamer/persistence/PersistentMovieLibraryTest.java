package com.apn.filerenamer.persistence;

import com.apn.filerenamer.entities.Movie;
import com.google.code.morphia.Datastore;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.apn.fixtures.TestDataFixtures.someString;
import static com.apn.fixtures.TestDataFixtures.someStringOfLength;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class PersistentMovieLibraryTest {
    private MovieLibrary movieLibrary;
    private Datastore datastore;
    private Movie expected;

    @Before
    public void setUp() throws Exception {
        datastore = new MorphiaDb().getDatastore("movie_db");
        movieLibrary = new PersistentMovieLibrary(datastore);
        String movieName = someStringOfLength(8);
        expected = new Movie(movieName,someString());
    }

    @After
    public void tearDown() throws Exception {
        datastore.delete(expected);
    }

    @Test
    public void insertsNewMovieOnTheLibrary() throws Exception {
        movieLibrary.addMovie(expected);

        assertThat(datastore.exists(expected).getId(), is(notNullValue()));
    }

    @Test
    public void throwsAlreadyInLibraryExceptionWhenInsertingDuplicateMovie() throws Exception {
        datastore.save(expected);
        try {
            movieLibrary.addMovie(expected);
            Assert.fail(String.format("Expected [%s]", AlreadyInLibraryException.class.getSimpleName()));
        } catch (AlreadyInLibraryException success) {
        }
    }
}
