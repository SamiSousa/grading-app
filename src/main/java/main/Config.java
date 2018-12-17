package main;


import database.SQLConnection;

import java.io.*;
import java.util.Properties;

public class Config {


    private static Properties config;
    private static boolean needsConfiguring;
    private static String configPath;

    static {
        String rootPath = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
        configPath = rootPath + "config.properties";
        Config.config = new Properties();
        try {
            config.load(new FileInputStream(configPath));
            if(config.getProperty("havesettingsbeenset").equals("true")) {
                needsConfiguring = false;
                SQLConnection.initialize(getMySQLUser(), getMySQLPassword());
            }
            needsConfiguring = !config.getProperty("havesettingsbeenset").equals("true");
        } catch (IOException e) {
            needsConfiguring = true;
            System.out.println("Properties file failed to load.");
            e.printStackTrace();
        }
    }

    public static boolean isPasswordCorrect(String password) {
        return password.equals(config.getProperty("password"));
    }

    public static boolean isUserPromptRequired() {
        return Config.needsConfiguring;
    }

    public static void setMySQLPassword(String mySQLPassword) {
        config.setProperty("mysqlpassword", mySQLPassword);
    }

    public static void setMySQLUser(String mySQLUser) {
        config.setProperty("mysqluser", mySQLUser);
    }

    public static void setPassword(String password) {
        config.setProperty("password", password);
    }

    public static String getMySQLUser() {
        return config.getProperty("mysqluser");
    }

    public static String getMySQLPassword() {
        return config.getProperty("mysqlpassword");
    }

    public static void save() {
        System.out.println(config);
        try {
            File file = new File(configPath);
            if(!file.exists()) {
                file.createNewFile();
            }
            config.setProperty("havesettingsbeenset", "true");
            config.store(new FileOutputStream(configPath), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
