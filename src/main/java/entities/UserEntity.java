package entities;

import server.Service;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user", schema = "portal")
public class UserEntity extends EntityModel{

    @Id
    @Column(name = "login", nullable = false, length = 10, unique = true)
    private String login;
    @ManyToMany (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable (name="link",
            joinColumns=@JoinColumn (name="userLogin"),
            inverseJoinColumns=@JoinColumn(name="contentId"))
    private List<ContentEntity> contentList = new ArrayList<ContentEntity>();

    @OneToMany(mappedBy = "userByUserLogin")
    private Collection<LinkEntity> linksByLogin;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<ContentEntity> getContentList() {
        return contentList;
    }

    public void setContentList(List<ContentEntity> contentList) {
        this.contentList = contentList;
    }

    public void addContent(ContentEntity content){
        contentList.add(content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (login != null ? !login.equals(that.login) : that.login != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }


    public Collection<LinkEntity> getLinksByLogin() {
        return linksByLogin;
    }

    public void setLinksByLogin(Collection<LinkEntity> linksByLogin) {
        this.linksByLogin = linksByLogin;
    }
}
