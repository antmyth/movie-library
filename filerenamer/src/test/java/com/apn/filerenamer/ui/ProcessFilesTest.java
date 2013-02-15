package com.apn.filerenamer.ui;

import org.junit.Test;

import java.io.File;

import static com.apn.fixtures.TestDataFixtures.someString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ProcessFilesTest {

    @Test
    public void testFileNameExtension() throws Exception{
        String fileName = someString();
        File f = new File(fileName+".mp4");
        String ext = Utils.getFileExtension(f);
        assertThat(ext, is("mp4"));
    }


}
