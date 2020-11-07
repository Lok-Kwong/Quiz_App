package edu.uga.cs.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


/**
 * MainaActivity is the start screen/splash screen for the app. The user can go to results or start a quiz
 */
public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";
    private Button newQuiz;
    private Button results;

    private static Context context;
    private QuizData quizData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        Log.d( DEBUG_TAG, "In Main" );

        newQuiz = findViewById( R.id.startBtn );
        results = findViewById( R.id.ViewBtn );

        newQuiz.setOnClickListener( new NewQuizButtonClickListener() );
        results.setOnClickListener( new ResultsButtonClickListener() );

        // Hacky solution for first time opening DB -> STARTS onCreate in QuizDBHelper
        quizData = new QuizData( this );
        quizData.open();
        quizData.close(); // Prevents retrieving all questions before inserting
    }

    public Context getContext() {
        return context;
    }

    private class NewQuizButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), QuizViewPager.class);
            view.getContext().startActivity( intent );
        }
    }

    private class ResultsButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), ReviewResults.class);
            view.getContext().startActivity(intent);
        }
    }
}