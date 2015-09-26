package com.sqldatabase;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Connector extends SQLiteOpenHelper
{
    SQLiteDatabase db;
    // All Static variables
    //Singleton design pattern
    private static Connector connector = null;
    // Database Version
    private static final int DATABASE_VERSION = 3;
    // Database Name with db extension as best practice
    private static final String DATABASE_NAME = "database.db";

    private Connector(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public static Connector getInstance(Context context)
    {

        if (connector == null)
        {
            connector = new Connector(context.getApplicationContext());
        }

        return connector;
    }

    public SQLiteDatabase open()
    {
        this.db = this.getWritableDatabase();
        return this.db;
    }

    public void close()
    {
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql="";

        List<String> createQuerys;

        createQuerys = getCreateQuerys();

        for(String createQuery : createQuerys)
        {
            executeUpdate(db,createQuery);
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        String sql="";
        // Drop older table if existed
        List<String> tables =getTables();

        for(String table : tables)
        {
            sql= getQueryDrop(table);
            executeUpdate(db,sql);
        }
        // Create tables again
        onCreate(db);
    }


    public String getQueryDrop(String table)
    {
        String sql="drop table if exists " + table;
        return sql;
    }

    //this method exists to have only one try-catch for create, delete, update, insert queries
    public void executeUpdate(SQLiteDatabase db,String sql)
    {
        try
        {
            db.execSQL(sql);
        }
        catch(SQLException e)
        {
            Log.d("SQLException query =" + sql + " " + e.getStackTrace().toString(), "");
        }
    }

    //here is the list of all tables in the database
    public List<String> getTables()
    {
        List<String> tables;
        tables = new ArrayList<String>();

        tables.add("person");

        return tables;
    }

    public List<String> getCreateQuerys()
    {
        List<String> createQuerys;
        createQuerys = new ArrayList<String>();

        String sql="";

        /**Starts Catalogs Definition**/

        //Person Catalog
        sql = "CREATE TABLE "        +
                "person"              + " ("                      +
                "id"                  + " INTEGER PRIMARY KEY,"  +  //Person Id
                "nameFirst"          + " TEXT,"                 +  //Fist name
                "nameLast"           + " TEXT"                 +  //last Name
                ")";

        createQuerys.add(sql);

        return createQuerys;
    }


}