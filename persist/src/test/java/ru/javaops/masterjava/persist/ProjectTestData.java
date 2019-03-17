package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;

import java.util.List;

public class ProjectTestData {
    public static Project MASTERJAVA;
    public static Project TOPJAVA;
    public static List<Project> FIRST_PROJECT;

    public static void init() {
        MASTERJAVA = new Project("masterjava", "Masterjava");
        TOPJAVA = new Project("topjava", "Topjava");
        FIRST_PROJECT = ImmutableList.of(MASTERJAVA);
    }

    public static void setUp() {
        ProjectDao dao = DBIProvider.getDao(ProjectDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST_PROJECT.forEach(dao::insert);
            dao.insert(TOPJAVA);
        });
    }
}
