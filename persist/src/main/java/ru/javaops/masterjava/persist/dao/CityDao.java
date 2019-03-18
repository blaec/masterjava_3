package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class CityDao implements AbstractDao {

    public City insert(City city) {
        if (city.isNew()) {
            int id = insertGeneratedId(city);
            city.setId(id);
        } else {
            insertWithId(city);
        }
        return city;
    }

    @SqlUpdate("INSERT INTO cities (name, code) VALUES (:name, :code) ON CONFLICT DO NOTHING")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean City city);

    @SqlUpdate("INSERT INTO cities (id, name, code) VALUES (:id, :name, :code) ")
    abstract void insertWithId(@BindBean City city);

    @SqlQuery("SELECT * FROM cities ORDER BY name, code LIMIT :it")
    public abstract List<City> getWithLimit(@Bind int limit);

    @SqlQuery("SELECT c.id FROM cities c WHERE c.code = :it")
    public abstract int getCityId(@Bind String code);

    //   http://stackoverflow.com/questions/13223820/postgresql-delete-all-content
    @SqlUpdate("TRUNCATE users,cities")
    @Override
    public abstract void clean();
}
