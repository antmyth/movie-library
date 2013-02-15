package com.apn.filerenamer.persistence;

public class AlreadyInLibraryException extends RuntimeException{
    private final String name;

    public AlreadyInLibraryException(String name) {
        super(name);
        this.name = name;
    }

    public String name() {
        return name;
    }
}
