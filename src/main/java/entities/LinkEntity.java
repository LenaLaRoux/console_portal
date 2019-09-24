package entities;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "link", schema = "portal" )
@IdClass(LinkEntityPK.class)
public class LinkEntity extends EntityModel{
    @Id
    @Column(name = "userLogin", nullable = false, length = 10)
    private String userLogin;
    @Id
    @Column(name = "contentId", nullable = false)
    private int contentId;
    @ManyToOne
    @JoinColumn(name = "userLogin", referencedColumnName = "login", insertable = false, updatable = false, nullable = false)
    private UserEntity userByUserLogin;
    @ManyToOne
    @JoinColumn(name = "contentId", referencedColumnName = "id", insertable = false, updatable = false, nullable = false)
    private ContentEntity contentByContentId;

    public LinkEntity(LinkEntityPK pk){
        userLogin = pk.getUserLogin();
        contentId = pk.getContentId();
    }

    public LinkEntity() {

    }


    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

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
        LinkEntity that = (LinkEntity) o;
        return contentId == that.contentId &&
                Objects.equals(userLogin, that.userLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userLogin, contentId);
    }


    public UserEntity getUserByUserLogin() {
        return userByUserLogin;
    }

    public void setUserByUserLogin(UserEntity userByUserLogin) {
        this.userByUserLogin = userByUserLogin;
    }


    public ContentEntity getContentByContentId() {
        return contentByContentId;
    }

    public void setContentByContentId(ContentEntity contentByContentId) {
        this.contentByContentId = contentByContentId;
    }
}
