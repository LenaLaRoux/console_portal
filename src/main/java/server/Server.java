package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    public static final int PORT = Config.PORT;
    private static LinkedHashMap<UUID, ServerFactory> serverMap = new LinkedHashMap<>();
    //public static Story story; // история переписки

    /**
     * @param args
     * @throws IOException
     */

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        Trace.LOG.inform(Server.class, "Server Started");

        try {
            while (true) {

                Socket socket = server.accept();
                addNewClient(socket);
            }
        } finally {
            server.close();
        }
    }
    public static synchronized void  addNewClient (Socket socket) throws IOException{
        try {
            UUID uuid = UUID.randomUUID();
            serverMap.put(uuid, new ServerFactory(socket, uuid)); // добавить новое соединенние в список
        } catch (IOException e) {
            socket.close();
        }
    }
    public static synchronized void  deleteClient (UUID uuid) {
            ServerFactory sf = Server.serverMap.get(uuid);
            sf.interrupt();
            Server.serverMap.remove(uuid);
    }
}
