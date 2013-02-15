package com.apn.filerenamer.ui;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.apn.filerenamer.ui.Utils.buildFileArrayFrom;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class UtilsTest {

    private String baseDir;
    private String fileName;
    private File expectedFile;

    @Before
    public void setUp() throws Exception {
        baseDir = "/data/stuff";
        fileName = "test.file";
        expectedFile = new File(baseDir+File.separator+fileName);

        assertTrue("Cannot create test file :"+expectedFile.getAbsolutePath(),expectedFile.createNewFile());
    }

    @After
    public void tearDown() throws Exception {
        assertTrue("Cannot delete test file :"+expectedFile.getAbsolutePath(),expectedFile.delete());
    }

    @Test
    public void buildsFileArrayContainingOneFile() throws Exception {
        File[] actual = buildFileArrayFrom(baseDir, new String[]{fileName});

        assertThat(actual.length,is(1));
        assertThat(actual[0],is(expectedFile));
    }

    @Test
    public void buildsEmptyArrayForEmptyFileNamesArray() throws Exception {
        File[] actual = buildFileArrayFrom(baseDir, new String[0]);

        assertThat(actual.length,is(0));
    }

    @Test
    public void directoryStream() throws Exception {
        Path dir = Paths.get("src/test/java/com/apn/filerenamer/persistence");
        System.out.println("dir=" + dir.toAbsolutePath());
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dir,FileFilters.newDirectoryFilter)){
            for (Path file :stream){
                System.out.println("file = " + file.toAbsolutePath());
            }
        }catch (Exception e){
            e.printStackTrace(System.out);
        }
        
    }
}
