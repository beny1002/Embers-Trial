package com.EmbersTrial.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.EmbersTrial.Main;
import com.EmbersTrial.S3Manager;

import java.io.File;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        // check if we need to restart the JVM (macOS/Windows helper)
        if (StartupHelper.startNewJvmIfRequired()) return;

        // download window icons from S3 (if they’re missing locally)
        downloadWindowIcons();

        // create and launch the application
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        // create the main application with default configuration
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        // set up the application configuration
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Ember's Trials"); // app title
        configuration.useVsync(true); // enable vsync
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1); // limit fps to refresh rate
        configuration.setWindowedMode(1280, 720); // default window size

        // set window icons (previously downloaded from S3)
        configuration.setWindowIcon(
            "lwjgl3/icons/emberx128.png",
            "lwjgl3/icons/emberx64.png",
            "lwjgl3/icons/emberx32.png",
            "lwjgl3/icons/emberx16.png"
        );

        return configuration;
    }

    private static void downloadWindowIcons() {
        // initialize the S3 manager
        S3Manager s3Manager = new S3Manager();

        // define the icons to download
        String[] icons = {"emberx128.png", "emberx64.png", "emberx32.png", "emberx16.png"};
        String s3Path = "assets/icons/"; // s3 bucket path
        String localPath = "lwjgl3/icons/"; // local directory for icons

        // make sure the local directory exists
        File directory = new File(localPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // try downloading each icon
        for (String icon : icons) {
            File iconFile = new File(localPath + icon);
            if (!iconFile.exists()) {
                System.out.println("icon missing locally, attempting to download: " + icon);
                s3Manager.downloadFile(s3Path + icon, localPath + icon);
                if (iconFile.exists()) {
                    System.out.println("successfully downloaded: " + icon);
                } else {
                    System.err.println("failed to download: " + icon + ". check s3 bucket and credentials.");
                }
            }
        }
    }
}
