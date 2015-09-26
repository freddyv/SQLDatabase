package com.sqldatabase;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class Dao
{
    public SQLiteDatabase db;
    //private Connector connector;
    public Connector connector;


    public void open() throws SQLException
    {
        db = connector.getWritableDatabase();
    }

    public void close()
    {
        connector.close();
    }

    //get next key
    public int getNextKey(SQLiteDatabase db,String sql)
    {
        int nextKey=0;

        try
        {
            Cursor cursor = db.rawQuery(sql, null);
            if (cursor!=null)
            {

                if (cursor.moveToFirst()) {
                    do {
                        if (cursor.getString(0) != null)
                        {
                            nextKey = cursor.getInt(0);//Integer.parseInt(cursor.getString(0));
                        }
                        else
                        {
                            nextKey = 0;
                        }

                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }
        catch(SQLException e)
        {
            nextKey=1;
            writeLog(e, this.toString(), "count", sql);
        }

        return nextKey;
    }


    //this method exists to have only one try-catch for create, delete, update, insert queries
    public void executeUpdate(SQLiteDatabase db,String sql)
    {
        String error="";

        try
        {
            db.execSQL(sql);
        }
        catch(SQLException e)
        {
            writeLog(e, this.toString(), "executeUpdate", sql);
            error = e.getMessage();
        }
        if (error.trim().length()>0)
        {
            error = "here=" + error;
        }
    }
    // Gets number of records in the table
    public int getCount(SQLiteDatabase db,String sql)
    {
        int counter=0;

        try
        {
            Cursor cursor = db.rawQuery(sql, null);
            counter = cursor.getCount();
            cursor.close();
        }
        catch(SQLException e)
        {
            writeLog(e, this.toString(), "count", sql);
            counter=0;
        }

        return counter;
    }


    public void writeLog(Exception e, String className,
                         String methodName, String sql)
    {
        String message = "Class : "      + className +
                " Method : "    + methodName +
                " Query : "     + sql
              //  " Exception : " +e.getStackTrace().toString()
                ;

        Log.e(message, "");
    }
}
  