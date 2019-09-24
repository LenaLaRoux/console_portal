package entities;

import server.Service;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "content", schema = "portal", catalog = "")
public class ContentEntity extends EntityModel{

    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "content", nullable = false, length = -1)
    private String content;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (name="link",
            joinColumns=@JoinColumn (name="contentId"),
            inverseJoinColumns=@JoinColumn(name="userLogin"))

    private List<UserEntity> userList = new ArrayList<>();
    @OneToMany(mappedBy = "contentByContentId")
    private Collection<LinkEntity> linksById;

    @Transient
    public static List<ContentEntity> getCommonContentIds (){
        String sql = "from "+ ContentEntity.class.getName();

        return Service.executeStatementSelect(sql);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<UserEntity> getUserList() {
        return userList;
    }

    public void setUserList(List<UserEntity> userList) {
        this.userList = userList;
    }

    public void addContent(UserEntity user){
        userList.add(user);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContentEntity that = (ContentEntity) o;

        if (id != that.id) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        return result;
    }


    public Collection<LinkEntity> getLinksById() {
        return linksById;
    }

    public void setLinksById(Collection<LinkEntity> linksById) {
        this.linksById = linksById;
    }
}
