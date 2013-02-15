package com.apn.filerenamer.ui;

import com.google.common.base.Function;

import javax.swing.tree.TreePath;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static com.google.common.collect.Collections2.transform;
import static java.util.Arrays.asList;

public class Utils {
    public static final int RETURN_KEYCODE = 10;

    private static final FileSystem fileSystem = FileSystems.getDefault();

    public static File[] buildFileArrayFrom(String baseDir, String[] fileNamesToProcess) {
        Path baseDirPath = Paths.get(baseDir);
        File[] filesToProcess = new File[fileNamesToProcess.length];
        for (int i = 0; i < fileNamesToProcess.length; i++) {
            filesToProcess[i] = baseDirPath.resolve(Paths.get(fileNamesToProcess[i])).toFile();
        }
        return filesToProcess;
    }

    public static String[] fileNamesArrayFrom(TreePath[] selectionPaths) {
        if (selectionPaths == null)
            return new String[0];
        return transform(asList(selectionPaths), new Function<TreePath, String>() {
            @Override
            public String apply(TreePath treePath) {
                return treePath.getLastPathComponent().toString();
            }
        }).toArray(new String[selectionPaths.length]);
    }

    public static String getFileExtension(File f) {
        String[] split = f.getName().split("\\.");
        return split[split.length - 1];
    }

    public static void renameFile(String newDirNameText, Path newBaseDir, String fileType, File[] filesToProcess) {
        int index = 1;
        for (File toProcess : filesToProcess) {
            String newFileName = newDirNameText + "." + fileType + index + "." + getFileExtension(toProcess);
            Path processedFileName = newBaseDir.resolve(Paths.get(newFileName));
            try {
                Files.move(toProcess.toPath(),processedFileName);
            } catch (IOException e) {
                throw new RuntimeException(String.format("Error moving file %s!",toProcess.getName()),e);
            }
            index++;
        }
    }

    public static Path createDirectory(String newDirName, String baseDir) {
        Path newBaseDir = fileSystem.getPath(baseDir, newDirName);
        try {
            return Files.createDirectories(newBaseDir);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create dir:" + newBaseDir.toAbsolutePath());
        }
    }
}
