package com.csse3200.game;

import com.badlogic.gdx.Gdx;
import org.junit.Test;

import java.io.File;

public class CleanImages {

    @Test
    public void rejectImagesInRoot() {
        var files = new File("./images").list();
        assert files != null;
        for (String file : files) {
            assert !(file.endsWith(".png") || file.endsWith(".jpeg"));
        }
    }
}
