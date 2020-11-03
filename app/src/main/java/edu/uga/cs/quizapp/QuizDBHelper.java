package edu.uga.cs.quizapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

/**
 * This is a SQLiteOpenHelper class, which Android uses to create, upgrade, delete an SQLite database
 * in an app.
 *
 * This class is a singleton, following the Singleton Design Pattern.
 * Only one instance of this class will exist.  To make sure, the
 * only constructor is private.
 * Access to the only instance is via the getInstance method.
 */
public class QuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "QuizDBHelper";

    private static final String DB_NAME = "quizapp.db";
    private static final int DB_VERSION = 2;

    private QuizActivity quizActivity = new QuizActivity(); // Call method to create initial data

    // Define all names for table quizQuestions and columns id, state, capital, city1, and city2
    public static final String TABLE_QUIZQUESTIONS = "quizQuestions";
    public static final String QUIZQUESTIONS_COLUMN_ID = "quizQuestionID";
    public static final String QUIZQUESTIONS_COLUMN_STATE = "state";
    public static final String QUIZQUESTIONS_COLUMN_CAPITAL = "capital";
    public static final String QUIZQUESTIONS_COLUMN_CITY1 = "city1";
    public static final String QUIZQUESTIONS_COLUMN_CITY2 = "city2";

    // Define all names for table quizResult and columns id, date, score, position (when user exits out for any reason), and questions 1-6
    public static final String TABLE_QUIZRESULTS = "quizResult";
    public static final String QUIZRESULTS_COLUMN_ID = "quizResultID";
    public static final String QUIZRESULTS_COLUMN_TIME = "time";
    public static final String QUIZRESULTS_COLUMN_SCORE = "score";
    public static final String QUIZRESULTS_COLUMN_POSITION = "position";
    public static final String QUIZRESULTS_COLUMN_QUESTION1 = "question1";
    public static final String QUIZRESULTS_COLUMN_QUESTION2 = "question2";
    public static final String QUIZRESULTS_COLUMN_QUESTION3 = "question3";
    public static final String QUIZRESULTS_COLUMN_QUESTION4 = "question4";
    public static final String QUIZRESULTS_COLUMN_QUESTION5 = "question5";
    public static final String QUIZRESULTS_COLUMN_QUESTION6 = "question6";

    // This is a reference to the only instance for the helper.
    private static QuizDBHelper helperInstance;

    // A Create table SQL statement to create the two tables
    private static final String CREATE_QUIZQUESTIONS =
            "create table " + TABLE_QUIZQUESTIONS + " ("
                    + QUIZQUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZQUESTIONS_COLUMN_STATE + " TEXT, "
                    + QUIZQUESTIONS_COLUMN_CAPITAL + " TEXT, "
                    + QUIZQUESTIONS_COLUMN_CITY1 + " TEXT, "
                    + QUIZQUESTIONS_COLUMN_CITY2 + " TEXT"
                    + ")";

    private static final String CREATE_QUIZRESULTS =
            "create table " + TABLE_QUIZRESULTS + " ("
                    + QUIZRESULTS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZRESULTS_COLUMN_TIME + " TEXT, "
                    + QUIZRESULTS_COLUMN_SCORE + " INTEGER, "
                    + QUIZRESULTS_COLUMN_POSITION + " INTEGER, "
                    + QUIZRESULTS_COLUMN_QUESTION1 + " TEXT " + " REFERENCES " + TABLE_QUIZQUESTIONS + " (" + QUIZQUESTIONS_COLUMN_ID + "), "
                    + QUIZRESULTS_COLUMN_QUESTION2 + " TEXT " + " REFERENCES " + TABLE_QUIZQUESTIONS + " (" + QUIZQUESTIONS_COLUMN_ID + "), "
                    + QUIZRESULTS_COLUMN_QUESTION3 + " TEXT " + " REFERENCES " + TABLE_QUIZQUESTIONS + " (" + QUIZQUESTIONS_COLUMN_ID + "), "
                    + QUIZRESULTS_COLUMN_QUESTION4 + " TEXT " + " REFERENCES " + TABLE_QUIZQUESTIONS + " (" + QUIZQUESTIONS_COLUMN_ID + "), "
                    + QUIZRESULTS_COLUMN_QUESTION5 + " TEXT " + " REFERENCES " + TABLE_QUIZQUESTIONS + " (" + QUIZQUESTIONS_COLUMN_ID + "), "
                    + QUIZRESULTS_COLUMN_QUESTION6 + " TEXT " + " REFERENCES " + TABLE_QUIZQUESTIONS + " (" + QUIZQUESTIONS_COLUMN_ID + ") "
                    + ")";


    // select state, city1, city2 from quiz join quizQuestions on quizQuestionId=question6;

    // Note that the constructor is private!
    // So, it can be called only from
    // this class, in the getInstance method.
    private QuizDBHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    // Access method to the single instance of the class.
    // It is synchronized, so that only one thread executes this method.
    public static synchronized QuizDBHelper getInstance( Context context ) {
        // check if the instance already exists and if not, create the instance
        if( helperInstance == null ) {
            helperInstance = new QuizDBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    // Create database if it doesn't exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( CREATE_QUIZQUESTIONS );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZQUESTIONS + " created" );
        db.execSQL( CREATE_QUIZRESULTS );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZRESULTS + " created" );
        quizActivity.setInitialData();
    }

    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL( "drop table if exists " + TABLE_QUIZQUESTIONS );
        db.execSQL( "drop table if exists " + TABLE_QUIZRESULTS );
        onCreate( db );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZQUESTIONS + " upgraded" );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUIZRESULTS + " upgraded" );
    }

}
