package com.EmbersTrial.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.EmbersTrial.Main;
import com.EmbersTrial.S3Manager;

import java.io.File;

public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; //macOS/Windows JVM helper

        //this makes window icons downloaded from S3
        downloadWindowIcons();

        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new Main(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("Ember's Trials");
        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setWindowedMode(1280, 720);

        //set window icons after downloading
        configuration.setWindowIcon(
            "lwjgl3/icons/emberx128.png",
            "lwjgl3/icons/emberx64.png",
            "lwjgl3/icons/emberx32.png",
            "lwjgl3/icons/emberx16.png"
        );

        return configuration;
    }

    private static void downloadWindowIcons() {
        S3Manager s3Manager = new S3Manager();
        String[] icons = {"emberx128.png", "emberx64.png", "emberx32.png", "emberx16.png"};
        String s3Path = "assets/icons/"; //path in the S3 bucket
        String localPath = "lwjgl3/icons/"; //path in the local project directory

        //makes sure the local directory exists
        File directory = new File(localPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        //try to download missing icons
        for (String icon : icons) {
            File iconFile = new File(localPath + icon);
            if (!iconFile.exists()) {
                System.out.println("Icon missing locally, attempting to download: " + icon);
                s3Manager.downloadFile(s3Path + icon, localPath + icon);
                if (iconFile.exists()) {
                    System.out.println("Successfully downloaded: " + icon);
                } else {
                    System.err.println("Failed to download: " + icon + ". Ensure S3 bucket and credentials are correct.");
                }
            }
        }
    }
}
