package edu.uga.cs.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class ReviewResults extends AppCompatActivity {

    public static final String DEBUG_TAG = "ReviewResults";

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter recyclerAdapter;

    private QuizData quizData = null;
    private List<Result> resultList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_results);

        recyclerView = (RecyclerView) findViewById( R.id.recyclerView );

        // use a linear layout manager for the recycler view
        layoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager( layoutManager );

        // Create a quizData instance, since we will need to get results from the db.
        quizData = new QuizData( this );

        // Execute the retrieval of the job leads in an asynchronous way
        new JobLeadDBReaderTask().execute();
    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB reading of job leads, asynchronously.
    private class JobLeadDBReaderTask extends AsyncTask<Void, Void, List<Result>> {

        // This method will run as a background process to read from db.
        // It returns a list of retrieved JobLead objects.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreate callback (the job leads review activity is started).
        @Override
        protected List<Result> doInBackground( Void... params ) {
            quizData.open();
            resultList = quizData.retrieveAllResults();

            Log.d( DEBUG_TAG, "JobLeadDBReaderTask: Job leads retrieved: " + resultList.size() );

            return resultList;
        }

        // This method will be automatically called by Android once the db reading
        // background process is finished.  It will then create and set an adapter to provide
        // values for the RecyclerView.
        // onPostExecute is like the notify method in an asynchronous method call discussed in class.
        @Override
        protected void onPostExecute( List<Result> jobLeadsList ) {
            super.onPostExecute(resultList);
            recyclerAdapter = new ResultsRecyclerAdapter( resultList );
            recyclerView.setAdapter( recyclerAdapter );
        }
    }

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "ReviewResults.onResume()" );

        // RETRIEVE QUIZ PROGRESS HERE onRestoreInstance

        // open the database in onResume
        if( quizData != null )
            quizData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "ReviewResults.onPause()" );

        // STORE QUIZ PROGRESS HERE onSaveInstance

        // close the database in onPause
        if( quizData != null )
            quizData.close();
        super.onPause();
    }

    // The following activity callback methods are not needed and are for
    // educational purposes only.
    @Override
    protected void onStart() {
        Log.d( DEBUG_TAG, "ReviewResults.onStart()" );
        super.onStart();
    }

    @Override
    protected void onStop() {
        Log.d( DEBUG_TAG, "ReviewResults.onStop()" );
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d( DEBUG_TAG, "ReviewResults.onDestroy()" );
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        Log.d( DEBUG_TAG, "ReviewResults.onRestart()" );
        super.onRestart();
    }
}

