package edu.uga.cs.quizapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for facilitating storing and restoring Quiz results, as well as storing all the
 * questions by creating a POJO object of Questions
 */
public class QuizData {
    public static final String DEBUG_TAG = "QuizData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase   db;
    private SQLiteOpenHelper quizDBHelper;
    private static final String[] QUIZQUESTIONS_allColumns = {
            QuizDBHelper.QUIZQUESTIONS_COLUMN_ID,
            QuizDBHelper.QUIZQUESTIONS_COLUMN_STATE,
            QuizDBHelper.QUIZQUESTIONS_COLUMN_CAPITAL,
            QuizDBHelper.QUIZQUESTIONS_COLUMN_CITY1,
            QuizDBHelper.QUIZQUESTIONS_COLUMN_CITY2
    };

    private static final String[] QUIZRESULTS_allColumns = {
            QuizDBHelper.QUIZRESULTS_COLUMN_ID,
            QuizDBHelper.QUIZRESULTS_COLUMN_TIME,
            QuizDBHelper.QUIZRESULTS_COLUMN_SCORE,
            QuizDBHelper.QUIZRESULTS_COLUMN_POSITION,
            QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION1,
            QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION2,
            QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION3,
            QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION4,
            QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION5,
            QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION6,
    };

    public QuizData( Context context ) {
        this.quizDBHelper = QuizDBHelper.getInstance(context);
    }

    // Open the database
    public void open() {
        db = quizDBHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "QuizData: db open" );
    }

    // Close the database
    public void close() {
        if( quizDBHelper != null ) {
            quizDBHelper.close();
            Log.d(DEBUG_TAG, "QuizData: db closed");
        }
    }

    // Retrieve all the questions and return them as a List.
    // This is how we restore persistent objects stored as rows in the quizQuestions table in the database.
    // For each retrieved row, we create a new question (Java POJO object) instance and add it to the list.
    public List<Question> retrieveAllQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( QuizDBHelper.TABLE_QUIZQUESTIONS, QUIZQUESTIONS_allColumns,
                    null, null, null, null, null );

            // collect all questions into a List
            if( cursor.getCount() > 0 ) {
                while( cursor.moveToNext() ) {
                    // get all attribute values of this question
                    long id = cursor.getLong( cursor.getColumnIndex( QuizDBHelper.QUIZQUESTIONS_COLUMN_ID ) );
                    String state = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZQUESTIONS_COLUMN_STATE ) );
                    String capital = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZQUESTIONS_COLUMN_CAPITAL ) );
                    String city1 = cursor.getString( cursor.getColumnIndex(  QuizDBHelper.QUIZQUESTIONS_COLUMN_CITY1 ) );
                    String city2 = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZQUESTIONS_COLUMN_CITY2 ) );

                    // create a new question object and set its state to the retrieved values
                    Question question = new Question( state, capital, city1, city2 );
                    question.setId( id ); // set the id (the primary key) of this object
                    // add it to the list
                    questions.add( question );
                    Log.d( DEBUG_TAG, "Retrieved question: " + question );
                }
            }
            Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved questions
        return questions;
    }

    // Store results of the quiz into the database.
    public Result storeResult( Result result ) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the Result argument.
        // This is how we are providing persistence to a Result (Java object) instance
        // by storing it as a new row in the database table representing results of the quiz.
        ContentValues values = new ContentValues();
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_TIME, result.getTime() );
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_SCORE, result.getScore() );
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_POSITION, result.getPosition() );
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION1, result.getQuestion1() );
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION2, result.getQuestion2() );
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION3, result.getQuestion3() );
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION4, result.getQuestion4() );
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION5, result.getQuestion5() );
        values.put( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION6, result.getQuestion6() );


        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert( QuizDBHelper.TABLE_QUIZRESULTS, null, values );

        // store the id (the primary key) in the JobLead instance, as it is now persistent
        result.setId( id );

        Log.d( DEBUG_TAG, "Stored new result with id: " + String.valueOf( result.getId() ) );

        return result;
    }

    // Retrieve all the quiz results and return them as a List.
    // This is how we restore persistent objects stored as rows in the quizresults table in the database.
    // For each retrieved row, we create a new result (Java POJO object) instance and add it to the list.
    public List<Result> retrieveAllResults() {
        ArrayList<Result> results = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( QuizDBHelper.TABLE_QUIZRESULTS, QUIZRESULTS_allColumns,
                    null, null, null, null, null );

            // collect all questions into a List
            if( cursor.getCount() > 0 ) {
                while( cursor.moveToNext() ) {
                    // get all attribute values of this question
                    long id = cursor.getLong( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_ID ) );
                    String time = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_TIME ) );
                    int score = cursor.getInt( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_SCORE ) );
                    int position = cursor.getInt( cursor.getColumnIndex(  QuizDBHelper.QUIZRESULTS_COLUMN_POSITION ) );
                    String question1 = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION1 ) );
                    String question2 = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION2 ) );
                    String question3 = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION3 ) );
                    String question4 = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION4 ) );
                    String question5 = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION5 ) );
                    String question6 = cursor.getString( cursor.getColumnIndex( QuizDBHelper.QUIZRESULTS_COLUMN_QUESTION6 ) );

                    // create a new question object and set its state to the retrieved values
                    Result result = new Result( time, score, position, question1, question2,question3, question4, question5, question6 );
                    result.setId( id ); // set the id (the primary key) of this object
                    // add it to the list
                    results.add( result );
                    Log.d( DEBUG_TAG, "Retrieved result: " + result );
                }
            }
            Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved questions
        return results;
    }


}
