package ru.javaops.masterjava.persist.dao;

import com.bertoncelj.jdbi.entitymapper.EntityMapperFactory;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapperFactory;
import ru.javaops.masterjava.persist.model.Message;

@RegisterMapperFactory(EntityMapperFactory.class)
public abstract class MessageDao implements AbstractDao {

    public Message insert(Message email) {
        if (email.isNew()) {
            int id = insertGeneratedId(email);
            email.setId(id);
        } else {
            insertWithId(email);
        }
        return email;
    }

    @SqlUpdate("INSERT INTO emails (send_to, send_cc, subject, body, sent) VALUES (:to, :cc, :subject, :body, :sent) ")
    @GetGeneratedKeys
    abstract int insertGeneratedId(@BindBean Message email);

    @SqlUpdate("INSERT INTO emails (id, full_name, email, flag, city_ref) VALUES (:id, :fullName, :email, CAST(:flag AS USER_FLAG), :cityRef) ")
    abstract void insertWithId(@BindBean Message email);
}
