package client;

import server.Config;

import java.util.UUID;

public class Client {

    public static String ipAddr = "localhost";
    public static int port =  Config.PORT;


    /**
     * создание клиент-соединения с узананными адресом и номером порта
     * @param args
     */

    public static void main(String[] args) {
        new ClientFactory(port);
    }
}
