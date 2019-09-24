package entities;

import server.Service;

import java.io.Serializable;
import java.util.List;

public abstract class EntityModel implements Serializable, IEntity {
    //IService - CRUD operations
    protected Service service = new Service(this);

    //service delegates
    public void save() {
        service.save();
    }

    public void update() {
        service.update();
    }

    public void delete() {
        service.delete();
    }

    public <T> List<T> executeStatement (String sql){
        return Service.executeStatementSelect(sql);
    }

}