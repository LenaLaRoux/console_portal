package entities;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class LinkEntityPK implements Serializable {
    private String userLogin;
    private int contentId;

    public LinkEntityPK(){

    }
    public LinkEntityPK(String login, Integer contentId){
        this.userLogin = login;
        this.contentId = contentId;
    }
    @Column(name = "userLogin", nullable = false, length = 10)
    @Id
    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    @Column(name = "contentId", nullable = false)
    @Id
    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LinkEntityPK that = (LinkEntityPK) o;
        return contentId == that.contentId &&
                Objects.equals(userLogin, that.userLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLogin, contentId);
    }
}
