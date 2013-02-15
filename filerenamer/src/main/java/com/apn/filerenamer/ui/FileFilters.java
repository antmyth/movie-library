package com.apn.filerenamer.ui;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileFilters {

    public static final DirectoryStream.Filter<Path> newDirectoryFilter =
        new DirectoryStream.Filter<Path>() {
        public boolean accept(Path path){
                return (Files.isDirectory(path));
        }
    };
    public static final DirectoryStream.Filter<Path> newFileFilter =
        new DirectoryStream.Filter<Path>() {
        public boolean accept(Path path){
                return (Files.isRegularFile(path));
        }
    };
}
