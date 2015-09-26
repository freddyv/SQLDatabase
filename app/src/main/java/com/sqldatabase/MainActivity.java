package com.sqldatabase;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sqldatabase.Person;
import com.sqldatabase.PersonDao;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener
{

    File file;
    TextView lblPersons;
    EditText txtId;
    EditText txtNameFirst;
    EditText txtNameLast;
    Button btnSave;
    Button btnDelete;
    Button btnSelect;
    Button btnNew;
    Person person;
    PersonDao personDao;

    ArrayList<Person> persons;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        initControls();
        initializeDao();
        person = getPersonNew();
        setTxts(person);


    }

    private void initControls()
    {
        lblPersons = (TextView) findViewById(R.id.lblPersons);
        btnSelect = (Button) findViewById(R.id.btnSelect);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnNew = (Button) findViewById(R.id.btnNew);
        txtId = (EditText) findViewById(R.id.txtId);
        txtNameLast = (EditText) findViewById(R.id.txtNameLast);
        txtNameFirst = (EditText) findViewById(R.id.txtNameFirst);
        lblPersons = (TextView) findViewById(R.id.lblPersons);

        btnSelect.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnNew.setOnClickListener(this);
    }

    public void initializeDao()
    {


        personDao = new PersonDao(context);

        persons = personDao.selectAll();
        listPersons(persons);


    }

    private Person getPersonNew()
    {
        //fake person to get all contact and group data
        Person person;
        person = new Person();
        person.setId(-1);
        person.setNameFirst("");
        person.setNameLast("");


        return person;
    }


    @Override
    public void onClick(View v)
    {
        // TODO Auto-generated method stub
        person = getTxts();

        switch (v.getId()) {
            case R.id.btnNew:
                person = getPersonNew();
                setTxts(person);
                break;
            case R.id.btnSelect:
                person = getTxts();
                person = select(person);
                setTxts(person);
                break;
            case R.id.btnSave:
                save(person);
                persons = personDao.selectAll();
                listPersons(persons);
                person = getPersonNew();
                setTxts(person);
                break;
            case R.id.btnDelete:
                delete(person);
                persons = personDao.selectAll();
                listPersons(persons);
                person = getPersonNew();
                setTxts(person);
                break;
        }
    }//end on click

    private Person select(Person person)
    {
        person = personDao.select(person);
        return person;
    }

    private void save(Person person)
    {
        if (person.getId()>-1)
        {
            update(person);
        }
        else
        {
            insert(person);
        }
        person = getPersonNew();
        setTxts(person);
    }

    private void update(Person person)
    {
        personDao.update(person);
        setTxts(person);
    }

    private void insert(Person person)
    {
        personDao.insert(person);
        setTxts(person);
    }

    private void delete(Person person)
    {
        personDao.delete(person);
        setTxts(person);
    }


    public void setTxts(Person person)
    {
        Integer id;
        String nameFirst;
        String nameLast;

        id          = person.getId();
        nameFirst   = person.getNameFirst();
        nameLast    = person.getNameLast();

        txtId.setText(id.toString());
        txtNameFirst.setText(nameFirst);
        txtNameLast.setText(nameLast);
    }

    public Person getTxts()
    {
        Integer id;
        String nameFirst;
        String nameLast;

        id          = Integer.parseInt(txtId.getText().toString());
        nameFirst   = txtNameFirst.getText().toString();;
        nameLast    = txtNameLast.getText().toString();;

        person.setId(id);
        person.setNameFirst(nameFirst);
        person.setNameLast(nameLast);

        return person;
    }

    public void listPersons(ArrayList<Person> persons)
    {
        String string="";
        for(Person person : persons)
        {
            string = string + person.getId() + "\t" + person.getNameFirst() + "\t" + person.getNameLast() + "\n";
        }
        lblPersons.setText(string);
    }

}//end class