package Utils;

import client.PortalGui;
import com.google.gson.Gson;
import entities.ContentEntity;

import java.util.List;
import java.util.UUID;

public class JsonProtocolObject {
    //Common attributes
    private String logIn;
    public Request rq = new Request();
    public Response rs = new Response();

    public String getLogIn() {
        return logIn;
    }

    public void setLogIn(String logIn) {
        this.logIn = logIn;
    }
    public  boolean isSetLogIn (){
        return logIn != null;
    }

    public static JsonProtocolObject jsonRs (String jsonStr){
        Gson gson = new Gson();
        return  gson.fromJson(jsonStr, JsonProtocolObject.class);
    }

    public String jsonString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static class Response {
        private String[] contentViewing;
        //private String[] contentViewingByUser;
        private String personalAccount;
        private String addContent;
        private String deleteContent;


        public String getAddContent() {
            return addContent;
        }

        public void setAddContent(String addContent) {
            this.addContent = addContent;
        }

        public String[] getContentViewing() {
            return contentViewing;
        }

        private String[] convertListToArr (List<ContentEntity> l){
            Gson gson = new Gson();
            String[] contentArr = new String[l.size()];

            int i=0;
            ContentViewing viewContent = null;

            for (ContentEntity c : l){
                viewContent = new ContentViewing(c.getId(), c.getContent());
                contentArr[i] = gson.toJson(viewContent);
                i++;
            }

            return contentArr;
        }

        public void setContentViewing(List<ContentEntity> contents) {
            this.contentViewing = convertListToArr(contents);
        }
        /*public String[] getContentViewingByUser() {
            return contentViewingByUser;
        }

        public void setContentViewingByUser(List<ContentEntity> contents) {
            this.contentViewingByUser = convertListToArr(contents);
        }*/

        public String getDeleteContent() {
            return deleteContent;
        }

        public void setDeleteContent(String deleteContent) {
            this.deleteContent = deleteContent;
        }

        public String getPersonalAccount() {
            return personalAccount;
        }

        public void setPersonalAccount(String personalAccount) {
            this.personalAccount = personalAccount;
        }
    }

    public static class ContentViewing {
        public Integer contentId;
        public String text;
        public String userLogin;
        public ContentViewing (Integer contentId, String text){
            this.contentId = contentId;
            this.text = text;
        }
        public ContentViewing (Integer contentId, String text, String login){
            this(contentId, text);
            userLogin = login;
        }
    }

    public static class Request {
        private Boolean contentViewing;
        private Boolean contentViewingByUser;
        private Integer addContent;
        private Integer deleteContent;

        public Integer getDeleteContent() {
            return deleteContent;
        }

        public void setDeleteContent(Integer deleteContent) {
            this.deleteContent = deleteContent;
        }

        public boolean isSetDeleteContent (){
            return deleteContent != null;        }

        public Integer getAddContent() {
            return addContent;
        }

        public void setAddContent(Integer addContent) {
            this.addContent = addContent;
        }

        public boolean isSetAddContent (){
            return addContent != null ;
        }
        public Boolean getContentViewing() {
            return contentViewing;
        }

        public void setContentViewing(Boolean isSet) {
            this.contentViewing = isSet;
        }
        public boolean isSetContentViewing(){
            return  contentViewing != null && contentViewing == true;
        }

        public Boolean getContentViewingByUser() {
            return contentViewingByUser;
        }
        public void setContentViewingByUser(Boolean isSet) {
            this.contentViewingByUser = isSet;
        }
        public boolean isSetContentViewingByUser(){
            return  contentViewingByUser != null && contentViewingByUser == true;
        }

    }
}
