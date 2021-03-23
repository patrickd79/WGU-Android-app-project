package com.example.patrickdenneymobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cursoradapter.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class CourseAdd extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText addCourseTitle;
    private EditText addCourseStart;
    private EditText addCourseEnd;
    private Spinner addCourseStatus;
    private Spinner addCourseInstructor;
    private Spinner courseAddAssociatedTermSpinner;
    private EditText addCourseNotes;
    private Button addCourseBtn;
    private Database db;
    private List<Term> termList;
    private List<String> termStrings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_add);
        this.setTitle("Add a Course");
        loadFields();


    }

    protected void onResume(){
        super.onResume();
        //clear the old term spinner
        termList.clear();
        termStrings.clear();
        loadFields();

    }
    protected void onDestroy(){
        super.onDestroy();
        termList.clear();
        termStrings.clear();
    }

    public void loadFields(){
        addCourseTitle = findViewById(R.id.addCourseTitleTV);
        addCourseStart = findViewById(R.id.editTextDateAddCourseStart);
        addCourseEnd = findViewById(R.id.addCourseEndTV);
        addCourseNotes = findViewById(R.id.addCourseNotesTV);
        addCourseBtn = findViewById(R.id.addCourseToDBBtn);

        //create the course status spinner functionality
        addCourseStatus = findViewById(R.id.addCourseStatusSpinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(this, R.array.course_status_spinner_options, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addCourseStatus.setAdapter(statusAdapter);
        addCourseStatus.setOnItemSelectedListener(this);

        //add Course Instructor spinner functionality
        addCourseInstructor = findViewById(R.id.addCourseIntructorSpinner);
        ArrayAdapter<CharSequence> instructorAdapter = ArrayAdapter.createFromResource(this, R.array.course_instructor_spinner_options, android.R.layout.simple_spinner_item);
        instructorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addCourseInstructor.setAdapter(instructorAdapter);
        addCourseInstructor.setOnItemSelectedListener(this);

        // add course terms spinner functionality
        //NEED TO BUILD A RESOURCE TO GET THE ASSOCIATED TERMS FROM THE DB
        fillTermStringsArray();
        courseAddAssociatedTermSpinner = findViewById(R.id.courseAddAssociatedTermSpinner);
        ArrayAdapter<String> termAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, termStrings);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseAddAssociatedTermSpinner.setAdapter(termAdapter);
        courseAddAssociatedTermSpinner.setOnItemSelectedListener(this);
    }

    public void fillTermStringsArray(){
        //create a db object
        db = new Database(CourseAdd.this);
        //get a list of the terms from the DB object
        termList = db.getAllTermsFromDB();

        termStrings = new ArrayList<>();
        for( Term term : termList) {
            termStrings.add( term.getTitle());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedItem = parent.getItemAtPosition(position).toString();
        Toast.makeText(getApplicationContext(), selectedItem, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    //method to add a new course when the add course btn is clicked
    public void addCourse(View v) {
        db = new Database(CourseAdd.this);
        Course course;
        int instructorID = db.getInstructorID(addCourseInstructor.getSelectedItem().toString());
        try {
            //String status = addCourseStatus.getSelectedItem().toString();
            //create a Course object instance
            course = new Course(addCourseTitle.getText().toString(), addCourseStart.getText().toString(), addCourseEnd.getText().toString(), addCourseStatus.getSelectedItem().toString(),
                    instructorID,
                    addCourseNotes.getText().toString(), courseAddAssociatedTermSpinner.getSelectedItem().toString()
            );
            //create a db object
            Database db = new Database(CourseAdd.this);
            //add the course object to the DB
            boolean added = db.addCourseToDB(course);
            //if addCourseToDB returns true then this Toast displays that
            Toast.makeText(CourseAdd.this, "Course added: " + added, Toast.LENGTH_LONG).show();
            Intent refresh = new Intent(this, CourseList.class);
            startActivity(refresh);
        } catch (Exception e) {
            Toast.makeText(CourseAdd.this, "Error adding course.", Toast.LENGTH_SHORT).show();
        }
    }
}
