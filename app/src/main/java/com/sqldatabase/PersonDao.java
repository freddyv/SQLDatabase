package com.sqldatabase;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public class PersonDao extends Dao
{
  //  Connector connector;

    //***********************************************//
    //Designed by Jonathan Gama
    //Create, Read, Update, Delete, Count Operations
    //***********************************************

    public PersonDao(Context context)
    {
        connector = Connector.getInstance(context);
        open();
    }

    public void create()
    {

        String sql = "";
        sql = getQueryCreate();
        executeUpdate(db, sql);

    }

    // insert person
    public void insert(Person person)
    {

        if (person.getId() == -1)
        {
            Integer nextKey = -1;
            nextKey = nextKey();
            person.setId(nextKey);
        }

        String sql = getQueryInsert(person);
        executeUpdate(db, sql);

    }


    // Updating single person
    public void update(Person person)
    {

        String sql = getQueryUpdate(person);

        String where = " where id=" + person.getId();

        sql = sql + where;

        executeUpdate(db, sql);

    }

    // Deleting single person
    public void delete(Person person)
    {

        String sql = getQueryDelete();

        String where = " where id=" + person.getId();

        sql = sql + where;

        executeUpdate(db, sql);

    }

    public void deleteAll()
    {

        String sql = getQueryDelete();

        executeUpdate(db, sql);

    }


    //Getting one person
    public Person select(Person person)
    {

        List<Person> persons;

        String sql = getQuerySelect();

        String where = " where id=" + person.getId();

        //String orderby = " order by person.nameLast,person.nameFirst ";
        sql = sql + where; //+ orderby;

        persons = executeQuery(db, sql);

        if (persons.size() > 0) {
            person = persons.get(0);
        } else {
            person = null;
        }

        return person;
    }

    //Getting one person
    public ArrayList<Person> selectQueryString(String queryString)
    {

        ArrayList<Person> persons;

        String sql = getQuerySelect();

        queryString = formatQueryLike(queryString);

        String where = " where nameFirst || nameLast like '%" + queryString + "%'";

        String orderby = " order by person.nameLast, person.nameFirst";

        sql = sql + where + orderby;

        persons = executeQuery(db, sql);

        return persons;
    }

    private String formatQueryLike(String entry)
    {
        String formattedString = "";

        formattedString = entry.replace(' ', '%');

        return formattedString;
    }

    // Gets all persons
    public ArrayList<Person> selectAll()
    {

        ArrayList<Person> persons;

        String sql = getQuerySelect();

        String where = "";

        String orderby = " order by person.nameLast, person.nameFirst asc";

        sql = sql + where + orderby;

        persons = executeQuery(db, sql);

        return persons;
    }


    // count persons
    public int count()
    {

        int counter = 0;

        String sql = getQueryCount();

        counter = getCount(db, sql);

        return counter;
    }

    public Integer nextKey()
    {

        Integer nextKey = 0;

        String sql = getQueryNextKey();

        nextKey = getNextKey(db, sql);

        return nextKey;
    }

    public ArrayList<Person> getPersonList(Cursor cursor)
    {
        //int i =0;
        ArrayList<Person> persons;
        persons = new ArrayList<Person>();

        int id;
        String nameFirst;
        String nameLast;


        if (cursor.moveToFirst()) {
            do {

                id = Integer.parseInt(cursor.getString(0));
                nameFirst = cursor.getString(1);
                nameLast = cursor.getString(2);

                Person person = new Person();
                person.setId(id);
                person.setNameFirst(nameFirst);
                person.setNameLast(nameLast);

                persons.add(person);
            } while (cursor.moveToNext());
        }

        cursor.close();//faltaba esto

        return persons;

    }

    public ArrayList<Person> executeQuery(SQLiteDatabase db, String sql)
    {
        ArrayList<Person> persons;

        Cursor cursor;

        try {

            cursor = db.rawQuery(sql, null);

            if (cursor != null) {
                persons = getPersonList(cursor);
            } else {
                persons = new ArrayList<Person>();
            }

        } catch (SQLException e) {
            persons = new ArrayList<Person>();
            writeLog(e, this.toString(), "executeQuery", sql);
        }

        return persons;
    }

    public String getQuerySelect() {
        String sql = "select " +
                "person.id," +
                "person.nameFirst," +
                "person.nameLast " +
                "from person ";

        return sql;

    }

    public String getQueryUpdate(Person person) {

        String sql = "update person set " +
                "id=" + person.getId() + "," +
                "nameFirst='" + person.getNameFirst() + "'," +
                "nameLast='" + person.getNameLast() + "'";


        return sql;
    }

    public String getQueryInsert(Person person) {
        String sql = "insert into person (" +
                "id," +
                "nameFirst," +
                "nameLast) " +
                "values (" +
                person.getId() + ",'" +
                person.getNameFirst() + "','" +
                person.getNameLast() + "')";


        return sql;
    }

    public String getQueryDelete() {
        String sql = "delete from person";
        return sql;
    }

    public String getQueryDrop() {
        String sql = "drop table if exists person";
        return sql;
    }

    public String getQueryCount() {
        String sql = "select * from person";
        return sql;
    }

    public String getQueryNextKey() {
        String sql = "select max(id)+1 as id from person";
        return sql;
    }

    public String getQueryCreate() {
        String sql = "";

        sql = "CREATE TABLE " +
                "person" + " (" +
                "id" + " INTEGER," +
                "nameFirst" + " INTEGER PRIMARY KEY," +
                "nameLast" + " TEXT" +
                ")";

        return sql;
    }

}

