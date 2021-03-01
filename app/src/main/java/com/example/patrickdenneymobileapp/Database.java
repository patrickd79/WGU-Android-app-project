package com.example.patrickdenneymobileapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    public static final String dbName = "Scheduler Database";
    public static final int version = 1;
    //Term table col names
    public static final String terms = "TERMS";
    public static final String term_id = "ID";
    public static final String term_title = "TITLE";
    public static final String term_start = "TERM_START";
    public static final String term_end = "TERM_END";
    public static final String term_got_courses = "TERM_COURSE";
    //Courses table col names
    public static final String courses = "COURSES";
    public static final String course_id = "COURSE_ID";
    public static final String course_title = "COURSE_TITLE";
    public static final String course_start = "COURSE_START";
    public static final String course_end = "COURSE_END";
    public static final String course_status = "COURSE_STATUS";
    public static final String course_instructor_id = "COURSE_INSTRUCTOR_ID";
    public static final String course_notes = "COURSE_NOTES";
    //Instructor table col names
    public static final String instructors = "INSTRUCTORS";
    public static final String ins_id = "INSTRUCTOR_ID";
    public static final String ins_name = "INSTRUCTOR_NAME";
    public static final String ins_ph_number = "INSTRUCTOR_PHONE";
    public static final String ins_email = "INSTRUCTOR_EMAIL";
    //Assessment table col names
    public static final String assessments = "ASSESSMENTS";
    public static final String assess_id = "ASSESSMENT_ID";
    public static final String perf_or_obj = "PERF_OR_OBJ";
    public static final String assess_title = "TITLE";
    public static final String assess_end = "ASSESSMENT_END_DATE";
    //List to hold the objects
    public static List<Term> termList = new ArrayList<>();
    public static List<Term> courseList = new ArrayList<>();
    public static List<Term> instructorList = new ArrayList<>();
    public static List<Term> assessmentList = new ArrayList<>();


    public Database(Context context){
        super(context, dbName, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+terms+"("+term_id+" INTEGER PRIMARY KEY, "+term_title+" TEXT, " +  term_start + " TEXT, " +  term_end + " TEXT, " + term_got_courses+ " BOOL)");
        db.execSQL("CREATE TABLE "+courses+"("+course_id+" INTEGER PRIMARY KEY, "+course_title+" TEXT, " +  course_start + " TEXT, " +  course_end + " TEXT, " + course_status+ " TEXT, "+course_instructor_id+" INTEGER, "+course_notes+" TEXT)");
        db.execSQL("CREATE TABLE "+instructors+"("+ins_id+" INTEGER PRIMARY KEY, "+ins_name+" TEXT, " +  ins_ph_number + " TEXT, " +  ins_email + " TEXT)");
        db.execSQL("CREATE TABLE "+assessments+"("+assess_id+" PRIMARY KEY, "+perf_or_obj+" TEXT, " + assess_title + " TEXT, " +assess_end + " TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+terms);
        db.execSQL("drop table if exists "+courses);
        db.execSQL("drop table if exists "+instructors);
        db.execSQL("drop table if exists "+assessments);
        onCreate(db);
    }
    //method to insert a new term to the database
    public boolean addTermToDB(Term term){
        //get a writeable database
        SQLiteDatabase db = this.getWritableDatabase();
        //create a key value pair list of the values of the term
        ContentValues values = new ContentValues();
        //add the term values to the list
        values.put(term_title, term.getTitle());
        values.put(term_start, term.getStart());
        values.put(term_end, term.getEnd());
        values.put(term_got_courses, term.getCourses());
        //insert the value list into the term table and returns -1 if unsuccessful or 0 if successful
        long insert = db.insert(terms, null, values);
        //returns true for 0, false for -1
        return insert != -1;
    }

    public List<Term> getAllTermsFromDB(){
       String query = "SELECT * FROM "+terms;
       SQLiteDatabase db = this.getReadableDatabase();
       Cursor cursor = db.rawQuery(query, null);
       if(cursor.moveToFirst()){
           int i = 0;
           do{
               int termId = cursor.getInt(0);
               String title = cursor.getString(1);
               String start = cursor.getString(2);
               String end = cursor.getString(3);
               boolean courses = cursor.getInt(4) == 1 ? true: false;
               Term term = new Term(termId, title, start, end, courses);
               termList.add(term);
               Log.d("obj", "getAllTermsFromDB: " + termList.get(i).getTermId());
               Log.d("ID", "getAllTermsFromDB:" +term_id);
               i++;
           }while(cursor.moveToNext());
       }else{
           //nothing to return
       }
       cursor.close();
       db.close();
       return termList;
    }

    public void updateTermInformation(long termId){
        //first have the edit term activity update the term in the termList in this class with the new information

    }

    public boolean deleteOneTerm(Term term) {
        //get a writeable database
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM TERMS WHERE "+term_id+"=" + term.getTermId();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            return true;
        }else{
            return false;
        }
    }





}
