package server;

import Utils.JsonProtocolObject;
import com.google.gson.Gson;
import entities.*;

import java.io.*;
import java.net.*;
import java.util.UUID;

/**
 * проект реализует консольный многопользовательский чат.
 * вход в программу запуска сервера - в классе Server.
 *
 * @author izotopraspadov, the tech
 * @version 2.0
 */

class ServerFactory extends Thread {

    private Socket socket;
    private UUID uuid;
    private BufferedReader in;
    private BufferedWriter out;
    private Gson gson = new Gson();

    /**
     * для общения с клиентом необходим сокет (адресные данные)
     *
     * @param socket
     * @param uuid
     * @throws IOException
     */

    public ServerFactory(Socket socket, UUID uuid) throws IOException {
        this.socket = socket;
        this.uuid = uuid;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start(); // вызываем run()
    }

    @Override
    public void run() {
        String word;

        try {
                while (!socket.isClosed()) {
                    word = in.readLine();
                    Trace.LOG.inform(this.getClass(), "Get message from client");

                    if (word.equals("stop")) {
                        this.downService(); // харакири
                        break; // если пришла пустая строка - выходим из цикла прослушки
                    }
                    this.send(action(word));
                }
            }catch(NullPointerException ignored){
            } catch (IOException e) {
                this.downService();
            }
    }

    /**
     * отсылка одного сообщения клиенту по указанному потоку
     *
     * @param msg
     */
    private void send(String msg) {
        try {
            out.write(msg + "\n");
            Trace.CustomLogger.inform( this.getClass(), "RESPONSE: " +msg);
            out.flush();
        } catch (IOException ignored) {
        }

    }

    /**
     * закрытие сервера
     * прерывание себя как нити и удаление из списка нитей
     */
    private void downService() {
        try {
            if (!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();

                Server.deleteClient(this.uuid);
            }
        } catch (IOException ignored) {
        }
    }


    private String action(String jsonRq) {
        Trace.CustomLogger.inform(this.getClass(), jsonRq);

        JsonProtocolObject jsonObject = JsonProtocolObject.jsonRs(jsonRq);

        final String login = jsonObject.getLogIn();
        if (login == null) {
            Trace.CustomLogger.warning(this.getClass(), "Login is not valid");
            return "Login is not valid";
        }

        Trace.CustomLogger.inform(this.getClass(), "Search user by login: "+login);
        UserEntity user = Service.findById(UserEntity.class, login);
        if (user == null) {
            Trace.CustomLogger.inform(this.getClass(), "User doesn't exist by login: "+ login);
            user = new UserEntity();
            user. setLogin(login);
            Trace.CustomLogger.inform(this.getClass(), "Attempt to create user by login: "+ login);
            user.save();
        }

        Trace.CustomLogger.inform(this.getClass(), "User: " + jsonObject.getLogIn() );

        if (jsonObject.rq == null) {
            Trace.CustomLogger.warning(this.getClass(), "No request was sent");
            return "No request was sent";
        }

        if (jsonObject.rq.isSetAddContent()) {
            Trace.CustomLogger.inform(this.getClass(), "Add content");

            Trace.CustomLogger.inform(this.getClass(), "Search link by login+contentId: "+login+", "+jsonObject.rq.getAddContent());
            LinkEntityPK linkPk = new LinkEntityPK(login, jsonObject.rq.getAddContent());
            LinkEntity link = Service.findById(LinkEntity.class, linkPk);

            if (link ==null){
                Trace.CustomLogger.inform(this.getClass(), "Link doesn't exist by login+contentId: "+login+", "+jsonObject.rq.getAddContent());
                link = new LinkEntity(linkPk);
                Trace.CustomLogger.inform(this.getClass(), "Attempt to create link by login+contentId: "+login+", "+jsonObject.rq.getAddContent());
                link.save();

            }else {
                Trace.CustomLogger.inform(this.getClass(), "Content has been added earlier");
            }
            //need reset
            jsonObject.rq.setAddContent(null);
        }

        if (jsonObject.rq.isSetDeleteContent()) {
            Trace.CustomLogger.inform(this.getClass(), "Delete content");

            LinkEntityPK linkPk = new LinkEntityPK(login, jsonObject.rq.getDeleteContent());
            LinkEntity link = Service.findById(LinkEntity.class, linkPk);

            if (link !=null){
                link.delete();
            }else{
                Trace.CustomLogger.inform(this.getClass(), "Content doesn't exist");
            }

            //need reset
            jsonObject.rq.setDeleteContent(null);
        }

        if (jsonObject.rq.isSetContentViewing()) {
            Trace.CustomLogger.inform(this.getClass(), "View common content");
            jsonObject.rs.setContentViewing(ContentEntity.getCommonContentIds());
            //need reset
            jsonObject.rq.setContentViewing(null);
        }

        if (jsonObject.rq.isSetContentViewingByUser()) {
            Trace.CustomLogger.inform(this.getClass(), "View user content");
            jsonObject.rs.setContentViewing(user.getContentList());
            //need reset
            jsonObject.rq.setContentViewingByUser(null);
        }

        return jsonObject.jsonString();
    }
}

/**
 * класс хранящий в ссылочном приватном
 * списке информацию о последних 10 (или меньше) сообщениях
 */

/*class Story {

    private LinkedList<String> story = new LinkedList<>();

//    *
//     * добавить новый элемент в список
//     * @param el

    public void addStoryEl(String el) {
        // если сообщений больше 10, удаляем первое и добавляем новое
        // иначе просто добавить
        if (story.size() >= 10) {
            story.removeFirst();
            story.add(el);
        } else {
            story.add(el);
        }
    }

//    *
//     * отсылаем последовательно каждое сообщение из списка
//     * в поток вывода данному клиенту (новому подключению)
//     * @param writer
    public void printStory(BufferedWriter writer) {
        if(story.size() > 0) {
            try {
                writer.write("History messages" + "\n");
                for (String vr : story) {
                    writer.write(vr + "\n");
                }
                writer.write("/...." + "\n");
                writer.flush();
            } catch (IOException ignored) {}

        }

    }
}
*/

