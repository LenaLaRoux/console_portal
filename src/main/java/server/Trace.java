package server;

import java.io.FileInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Trace {

    public static CustomLogger LOG = new CustomLogger();

    public static class CustomLogger {
        public static Logger LOGGER;
        static {
            try(FileInputStream ins = new FileInputStream("./log.config")){ //полный путь до файла с конфигами
                LogManager.getLogManager().readConfiguration(ins);
            }catch (Exception ignore){
                ignore.printStackTrace();
            }
        }

        private static void customLog (Class clazz, Level level, String msg, Exception e){
            LOGGER = Logger.getLogger(clazz.getName());
            LOGGER.log(level, msg, e);
        }
        private static void customLog (Class clazz, Level level, String msg){
            CustomLogger.customLog(clazz, level, msg, null);
        }
        public static void warning (Class clazz, String msg){
            CustomLogger.customLog(clazz, Level.WARNING, msg);
        }

        public static void inform (Class clazz,  String msg){
            CustomLogger.customLog(clazz, Level.INFO, msg);
        }
        public static void error (Class clazz,  String msg, Exception e){
            CustomLogger.customLog(clazz, Level.WARNING, msg, e);
        }
    }
}
