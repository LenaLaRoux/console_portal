package client;

import Utils.JsonProtocolObject;
import com.google.gson.Gson;
import server.Config;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PortalGui {
    private Section menu;
    List<JsonProtocolObject.ContentViewing>  contents;

    public void start(){
        menu = new PortalGui.Login();
    }

    public void print(){
        menu.print();
    }

    public boolean hasNext(String userCommand){
        return menu.hasNext(userCommand);
    }

    public void prepareRq(JsonProtocolObject rq){
        menu.prepareRq(rq);
    }
    public boolean shouldSendToServer(){
        return menu.shouldSendToServer();
    }

    public void proceedRs (JsonProtocolObject rs){
        if (rs != null && rs.rs!=null )
            contents = arrStrContentToListContent(rs.rs.getContentViewing());
    }

    public void update (){
        if (menu instanceof ContentSection){
            ((ContentSection) menu).update();
        }
    }

    public void next (){
        if (menu instanceof PortalGui.MainMenu){
            menu = ((PortalGui.MainMenu) menu).next(contents);
        }else{
            menu = menu.next();
        }
    }

    protected List<JsonProtocolObject.ContentViewing> arrStrContentToListContent (String[] contentsStr){
        if (contentsStr == null)
            return null;

        List<JsonProtocolObject.ContentViewing> result = new ArrayList<>();
        Gson gson = new Gson();
        JsonProtocolObject.ContentViewing cv = null;

        for (int i = 0; i<contentsStr.length; i++) {
            cv = gson.fromJson(contentsStr[i], JsonProtocolObject.ContentViewing.class);
            if (cv != null)
                result.add(cv);
        }
        return result;
    }


    public abstract static class Section {
        protected String selectedCommand;

        public abstract void print ();
        public abstract void prepareRq (JsonProtocolObject jsonObj);
        public abstract Section next ();
        protected abstract String[] getCommands();

        public boolean hasNext (String inputCommand){

                for (String s : getCommands()) {
                    if (s.equals(inputCommand)) {
                        selectedCommand = s;
                        return true;
                    }
                }
                return false;
        }

        public boolean shouldSendToServer() {
            return false;
        }
    }

    public static class Login extends  Section{
        private String login = null;

        @Override
        public void print (){
            System.out.println(Config.ENTER_LOGIN);
        }

        @Override
        public void prepareRq(JsonProtocolObject jsonObj) {
            if (login != null)
                jsonObj.setLogIn(login);
        }

        @Override
        public boolean hasNext (String inputCommand){
               if (inputCommand != null && inputCommand.trim()!="") {
                   login = inputCommand;
               }
            return true;
        }
        @Override
        public Section next (){
            if (login == null) {
                return this;
            }else {
                return new MainMenu();
            }
        }

        @Override
        protected String[] getCommands() {
            return null;
        }

    }

    public static class MainMenu extends  Section{
        final private String VIEW_Command = "1";
        final private String PERSONAL_ACCOUNT_Command = "2";
        final private String MAIN_MENU = "*";

        @Override
        public void print (){
            System.out.println(Config.SECTION_CHOICE_MESSAGE);
        }

        @Override
        public void prepareRq(JsonProtocolObject jsonObj) {
            switch (selectedCommand){
                case PERSONAL_ACCOUNT_Command:
                    jsonObj.rq.setContentViewingByUser(true);
                    break;
                case VIEW_Command:
                    jsonObj.rq.setContentViewing(true);
                    break;
                default:;
            }
        }

        @Override
        public boolean shouldSendToServer() {
            if (selectedCommand.equals(MAIN_MENU))
                return false;
            else
                return true;
        }

        @Override
        public Section next() {
            switch (selectedCommand){
                case PERSONAL_ACCOUNT_Command:
                    return new PersonalAccount();
                case VIEW_Command:
                    return new ViewContent();
                default: return null;
            }
        }

        public ContentSection next(List<JsonProtocolObject.ContentViewing> contents) {
            ContentSection next = (ContentSection) this.next();
            next.setContents(contents);
            return next;
        }

        @Override
        protected String[] getCommands() {
            return new String[]{VIEW_Command, PERSONAL_ACCOUNT_Command};
        }
    }

    public static abstract class ContentSection extends Section{

        private List<JsonProtocolObject.ContentViewing> contents = null;
        protected JsonProtocolObject.ContentViewing currentContent = null;
        protected Iterator<JsonProtocolObject.ContentViewing> contentIt = null;

        public List<JsonProtocolObject.ContentViewing> getContents() {
            return contents;
        }

        public void setContents(List<JsonProtocolObject.ContentViewing> contents) {
            this.contents = contents;
        }

        private JsonProtocolObject.ContentViewing nextContent (){
            if (contents !=null && contents.size() > 0){
                if (contentIt == null || !contentIt.hasNext())
                    contentIt = contents.iterator();
                return contentIt.next();
            }else
                return null;
        }
        @Override
        public void print (){
            printMessage ();
            currentContent = nextContent();

            if (currentContent != null){
                System.out.println(currentContent.text);
            }
        }

        protected  abstract  void printMessage ();
        protected  abstract  void update ();
    }

    public static class PersonalAccount extends  ContentSection{
        final public String NEXT_CONTENT_Command = "1";
        final private String DELETE_CONTENT_Command = "2";
        final private String EXIT_TO_MAIN_MENU_Command = "*";

        @Override
        public void printMessage() {
            System.out.println(Config.PERSONAL_ACCOUNT_MESSAGE);
        }

        @Override
        protected void update() {
            if (selectedCommand.equals(DELETE_CONTENT_Command))
                contentIt.remove();
        }

        @Override
        public boolean shouldSendToServer() {
            if (selectedCommand.equals(DELETE_CONTENT_Command))
                return true;
            else
                return false;
        }

        @Override
        public void prepareRq(JsonProtocolObject jsonObj) {
            switch (selectedCommand){
                case DELETE_CONTENT_Command:
                    if (currentContent != null) {
                        jsonObj.rq.setDeleteContent(currentContent.contentId);
                    }
                default: ;
            }
        }

        @Override
        public Section next (){
            switch (selectedCommand){
                case NEXT_CONTENT_Command:
                case DELETE_CONTENT_Command:
                    return this;
                case EXIT_TO_MAIN_MENU_Command:
                    return new MainMenu();
                default: return null;
            }

        }

//        private boolean deleteContent (JsonProtocolObject.ContentViewing content){
//            contentIt.remove();
//            /*JsonProtocolObject.ContentViewing[] arr = new JsonProtocolObject.ContentViewing[contents.length-1];
//
//            for (int i =0; i<contents.length; i++){
//                if (contents[i].equals(content)) {
//                    System.arraycopy(contents, 0, arr, 0, i - 1);
//                    System.arraycopy(contents, i+1, arr, i, contents.length-1 - i);
//
//                    contents = arr;
//                    currentContent = contents[contents.length > i? i : 0];
//
//                    return true;
//                }
//            }*/
//            return false;
//        }

        @Override
        protected String[] getCommands() {
            return new String[]{NEXT_CONTENT_Command, DELETE_CONTENT_Command, EXIT_TO_MAIN_MENU_Command};
        }

    }

    public static class ViewContent extends  ContentSection{
        final private String NEXT_CONTENT_Command = "1";
        final private String ADD_CONTENT_Command = "2";
        final private String EXIT_TO_MAIN_MENU_Command = "*";

        @Override
        protected void printMessage (){
            System.out.println(Config.CONTENT_VIEWING_MESSAGE);
        }

        @Override
        protected void update() {

        }

        @Override
        public void prepareRq(JsonProtocolObject jsonObj) {
            switch (selectedCommand) {
                case ADD_CONTENT_Command:
                    if (currentContent != null) {
                        jsonObj.rq.setAddContent(currentContent.contentId);
                    }

                default:
                    ;
            }
        }

        @Override
        public boolean shouldSendToServer() {
            if (selectedCommand.equals(ADD_CONTENT_Command))
                return true;
            else
                return false;
        }

        @Override
        public Section next (){
            switch (selectedCommand){

                case NEXT_CONTENT_Command:
                case ADD_CONTENT_Command:
                    return this;
                case EXIT_TO_MAIN_MENU_Command:
                    return new MainMenu();
                default: return null;
            }
        }

        @Override
        protected String[] getCommands() {
            return new String[]{NEXT_CONTENT_Command, ADD_CONTENT_Command, EXIT_TO_MAIN_MENU_Command};
        }

    }
}
