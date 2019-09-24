package server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Config {
    private static final String PROPERTIES_FILE = "./server.properties";

    public static int PORT;
    public static String HELLO_MESSAGE;
    public static String CONTENT_VIEWING_MESSAGE;
    public static String CONTENT_VIEWING;
    public static String PERSONAL_ACCOUNT;
    public static String PERSONAL_ACCOUNT_MESSAGE;
    public static String ADDING_CONTENT;
    public static String DELETING_CONTENT;
    public static String SECTION_CHOICE;
    public static String SECTION_CHOICE_MESSAGE;
    public static String ENTER_LOGIN;
    //Prefix
    public static String _HELLO_MESSAGE="HELLO_MESSAGE";
    public static String _CONTENT_VIEWING_MESSAGE ="CONTENT_VIEWING_MESSAGE";
    public static String _CONTENT_VIEWING = "CONTENT_VIEWING";
    public static String _PERSONAL_ACCOUNT = "PERSONAL_ACCOUNT";
    public static String _PERSONAL_ACCOUNT_MESSAGE = "PERSONAL_ACCOUNT_MESSAGE";
    public static String _ADDING_CONTENT = "ADDING_CONTENT";
    public static String _DELETING_CONTENT = "DELETING_CONTENT";
    public static String _SECTION_CHOICE = "SECTION_CHOICE";
    public static String _SECTION_CHOICE_MESSAGE = "SECTION_CHOICE_MESSAGE";
    public static String _ENTER_LOGIN = "ENTER_LOGIN";
    public static String _CLIENT_ID = "CLIENT_ID";
    public static String SEPARATOR = ",";

    static {
        try {
            System.setOut(new java.io.PrintStream(System.out, true, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Properties properties = new Properties();
        try(FileInputStream propertiesFile = new FileInputStream(PROPERTIES_FILE)){

            properties.load(propertiesFile);

            PORT             = Integer.parseInt(properties.getProperty("PORT"));
            HELLO_MESSAGE    = properties.getProperty(_HELLO_MESSAGE);
            CONTENT_VIEWING_MESSAGE = properties.getProperty(_CONTENT_VIEWING_MESSAGE);
            CONTENT_VIEWING  = properties.getProperty(_CONTENT_VIEWING);
            PERSONAL_ACCOUNT = properties.getProperty(_PERSONAL_ACCOUNT);
            PERSONAL_ACCOUNT_MESSAGE = properties.getProperty(_PERSONAL_ACCOUNT_MESSAGE);
            ADDING_CONTENT   = properties.getProperty(_ADDING_CONTENT);
            DELETING_CONTENT = properties.getProperty(_DELETING_CONTENT);
            SECTION_CHOICE   = properties.getProperty(_SECTION_CHOICE);
            SECTION_CHOICE_MESSAGE = properties.getProperty(_SECTION_CHOICE_MESSAGE);
            ENTER_LOGIN = properties.getProperty(_ENTER_LOGIN);

        } catch (FileNotFoundException ex) {
            Trace.LOG.error(Config.class, "Properties config file not found", ex);
        } catch (IOException ex) {
            Trace.LOG.error(Config.class, "Error while reading file", ex);
        }
    }
}