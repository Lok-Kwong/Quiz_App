package edu.uga.cs.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "MainActivity";
    private Button newQuiz;
    private Button results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Log.d( DEBUG_TAG, "In Main" );

            newQuiz = findViewById( R.id.startBtn );
            results = findViewById( R.id.ViewBtn );

            newQuiz.setOnClickListener( new NewQuizButtonClickListener() );
            results.setOnClickListener( new ResultsButtonClickListener() );
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