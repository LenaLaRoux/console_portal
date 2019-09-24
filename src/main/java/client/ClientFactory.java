package client;

import Utils.JsonProtocolObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import server.Config;
import server.Trace;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

/**
 * создание клиента со всеми необходимыми утилитами, точка входа в программу в классе Client
 */

class ClientFactory {

    private Socket socket;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток чтения в сокет
    private BufferedReader inputUser; // поток чтения с консоли
    private int port; // порт соединения
    final private JsonProtocolObject jsonProtocolObject = new JsonProtocolObject();
    private CountDownLatch waitForRs;

    /**
     * для создания необходимо принять адрес и номер порта
     *
     * @param port
    // * @param uuid
     */

    public ClientFactory(int port) {
        this.port = port;

        try {
            this.socket = new Socket("", this.port);
        } catch (IOException e) {
            e.printStackTrace();
            Trace.LOG.error(ClientFactory.class, "Socket failed", e);
        }
        try {
            inputUser = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            new ReadMsg().start(); // нить читающая сообщения из сокета в бесконечном цикле
            new WriteMsg().start(); // нить пишущая сообщения в сокет приходящие с консоли в бесконечном цикле
        } catch (IOException e) {
            // Сокет должен быть закрыт при любой
            // ошибке, кроме ошибки конструктора сокета:
            ClientFactory.this.downService();
        }
        // В противном случае сокет будет закрыт
        // в методе run() нити.
    }

    /**
     * закрытие сокета
     */
    public void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
            }
        } catch (IOException ignored) {
        }
    }


    // нить чтения сообщений с сервера
    protected class ReadMsg extends Thread {
        final Gson gson = new Gson();
        @Override
        public void run() {
            String str;

            try {
                while (true) {
                    str = in.readLine(); // ждем сообщения с сервера

                    if (str.equals("stop")) {
                        ClientFactory.this.downService();
                        break;
                    }

                    JsonProtocolObject jsonProtocolObject = null;
                    try {
                        jsonProtocolObject = gson.fromJson(str, JsonProtocolObject.class);
                    }catch (JsonSyntaxException ingnore){ }

                    if (jsonProtocolObject == null)
                        System.out.println("CLIENT RS: " + str); // пишем сообщение с сервера на консоль
                    else {
                        ClientFactory.this.jsonProtocolObject.rq = jsonProtocolObject.rq;
                        ClientFactory.this.jsonProtocolObject.rs = jsonProtocolObject.rs;
                    }
                    waitForRs.countDown();
                }
            } catch (IOException e) {
                ClientFactory.this.downService();
            }
        }
    }

    // нить отправляющая сообщения приходящие с консоли на сервер
    public class WriteMsg extends Thread {

        @Override
        public void run() {
            PortalGui portal = new PortalGui();
            portal.start();

            while (true) {
                waitForRs = new CountDownLatch(1);

                portal.print();
                String userCommand;
                try {
                    userCommand = inputUser.readLine(); // сообщения с консоли

                    if (!portal.hasNext(userCommand)) {
                        out.write("stop" + "\n");
                        ClientFactory.this.downService();
                        break;

                    } else {
                        portal.prepareRq(jsonProtocolObject);

                        if (portal.shouldSendToServer()) {
                            out.write(jsonProtocolObject.jsonString() + "\n"); // отправляем на сервер
                            out.flush();

                            waitForRs.await();
                        }

                        portal.proceedRs(jsonProtocolObject);

                        portal.update();

                        portal.next();
                    }

                } catch (IOException | InterruptedException e) {
                    ClientFactory.this.downService();
                }
            }
        }
    }
}

