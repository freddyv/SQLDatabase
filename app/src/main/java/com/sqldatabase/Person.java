package com.sqldatabase;

public class Person
{
    private Integer id;
    private String nameFirst;
    private String nameLast;

    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getNameFirst()
    {
        return nameFirst;
    }
    public void setNameFirst(String nameFirst)
    {
        this.nameFirst = nameFirst;
    }
    public String getNameLast()
    {
        return nameLast;
    }

    public void setNameLast(String nameLast)
    {
        this.nameLast = nameLast;
    }

}