package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.GroupFlag;

import java.util.List;

import static ru.javaops.masterjava.persist.ProjectTestData.MASTERJAVA;
import static ru.javaops.masterjava.persist.ProjectTestData.TOPJAVA;

public class GroupTestData {
    public static Group TOPJAVA06;
    public static Group TOPJAVA07;
    public static Group TOPJAVA08;
    public static Group MASTERJAVA01;
    public static List<Group> FIRST3_GROUPS;

    public static void init() {
        ProjectTestData.init();
        ProjectTestData.setUp();
        TOPJAVA06 = new Group(TOPJAVA.getId(), "topjava06", GroupFlag.FINISHED);
        TOPJAVA07 = new Group(TOPJAVA.getId(), "topjava07", GroupFlag.FINISHED);
        TOPJAVA08 = new Group(TOPJAVA.getId(), "topjava08", GroupFlag.CURRENT);
        MASTERJAVA01 = new Group(MASTERJAVA.getId(), "masterjava01", GroupFlag.CURRENT);
        FIRST3_GROUPS = ImmutableList.of(MASTERJAVA01, TOPJAVA06, TOPJAVA07);
    }

    public static void setUp() {
        GroupDao dao = DBIProvider.getDao(GroupDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST3_GROUPS.forEach(dao::insert);
            dao.insert(TOPJAVA08);
        });
    }
}
