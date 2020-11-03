package edu.uga.cs.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.ContentValues;

import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.opencsv.CSVReader;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Quiz Activity";

    Context context = this;
    boolean initialData = true;
    private TextView textViewQuestion;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private QuizData quizData = null;
    List<Question> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        textViewQuestion = findViewById( R.id.questionText );
        rbGroup = findViewById( R.id.radio_group );
        rb1 = findViewById( R.id.option1 );
        rb2 = findViewById( R.id.option2 );
        rb3 = findViewById( R.id.option3 );


        Log.d( DEBUG_TAG, "Database: " + doesDatabaseExist(context, "quizapp.db"));

        quizData = new QuizData( this );

        // GET INITIAL DATA FROM state_capital.csv -> Make initialData = false if not
        if (!initialData) {
            // Execute the retrieval of the CSV file in an asynchronous way, without blocking the UI thread.
            new getIntialCSVDataTask().execute();
        }
        new retrieveAllQuestionsTask().execute();
    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB writing of a job lead, asynchronously.
    private class retrieveAllQuestionsTask extends AsyncTask<Void, Void, Void> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreateMethod
        @Override
        protected Void doInBackground(Void... voids) {
            quizData.open();
            results = quizData.retrieveAllQuestions();

            textViewQuestion.setText("What is the capital of " + String.valueOf(results.get(49).getState()) + "?");
            rb1.setText(results.get(49).getCity1());
            rb2.setText(results.get(49).getCity2());
            rb3.setText(results.get(49).getCapital());
            Log.d( DEBUG_TAG, "getIntialCSVDataTask: quiz questions retrieved: ");
            return null;
        }
    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB writing of a job lead, asynchronously.
    private class getIntialCSVDataTask extends AsyncTask<Void, Void, Void> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreateMethod
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d( DEBUG_TAG, "Here in create: " );
            try {
                Resources res = getResources();
                InputStream in_s = res.openRawResource( R.raw.state_capitals );
//                    String file = "src/main/res/raw/state_capitals.csv";
//                    InputStream in_s = this.getClass().getClassLoader().getResourceAsStream(file);

                // read the CSV data
                CSVReader reader = new CSVReader(new InputStreamReader(in_s));
                String[] nextLine;


                while ((nextLine = reader.readNext()) != null) {
                    System.out.println("State : " + nextLine[0]);
                    System.out.println("Capital : " + nextLine[1]);
                    System.out.println("Second city : " + nextLine[2]);
                    System.out.println("Third city : " + nextLine[3]);
                    System.out.println("==========================");
                    //                Question question = new Question();
                    //                question.setState(nextLine[0]);
                    //                question.setCapital(nextLine[1]);
                    //                question.setCity1(nextLine[2]);
                    //                question.setCity2(nextLine[3]);
                    //                questions.add(question);
                    ContentValues values = new ContentValues();
                    values.put(QuizDBHelper.QUIZQUESTIONS_COLUMN_STATE, nextLine[0]);
                    values.put(QuizDBHelper.QUIZQUESTIONS_COLUMN_CAPITAL, nextLine[1]);
                    values.put(QuizDBHelper.QUIZQUESTIONS_COLUMN_CITY1, nextLine[2]);
                    values.put(QuizDBHelper.QUIZQUESTIONS_COLUMN_CITY2, nextLine[3]);

                    // Insert the new row into the database table;
                    // The id (primary key) is automatically generated by the database system
                    // and returned as from the insert method call.
                    quizData.insertQuestion(values);
                }

                reader.close();

            } catch(Exception e) {
                Log.e(DEBUG_TAG, e.toString());
            }
            Log.d( DEBUG_TAG, "getIntialCSVDataTask: quiz questions retrieved: ");
            return null;
        }
    }

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "QuizActivity.onResume()" );
        // open the database in onResume
        if( quizData != null )
            quizData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "QuizActivity.onPause()" );
        // close the database in onPause
        if( quizData != null )
            quizData.close();
        super.onPause();
    }

    // The following activity callback methods are not needed and are for
    // educational purposes only.
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "QuizActivity.onStart()" );
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "QuizActivity.onStop()" );
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "QuizActivity.onDestroy()" );
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "QuizActivity.onRestart()" );
        super.onRestart();
    }
}
