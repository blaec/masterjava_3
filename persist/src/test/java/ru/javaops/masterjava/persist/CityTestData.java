package ru.javaops.masterjava.persist;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static City KYEV;
    public static City SEINT_PETERSBURG;
    public static City MINSK;
    public static City MOSCOW;
    public static List<City> FIRST3_CITIES;

    public static void init() {
        KYEV = new City("Киев", "kiv");
        MINSK = new City("Минск", "mnsk");
        MOSCOW = new City("Москва", "mos");
        SEINT_PETERSBURG = new City("Санкт-Питербург", "spb");
        FIRST3_CITIES = ImmutableList.of(KYEV, MINSK, MOSCOW);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            FIRST3_CITIES.forEach(dao::insert);
            dao.insert(SEINT_PETERSBURG);
        });
    }
}
