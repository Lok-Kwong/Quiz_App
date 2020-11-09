package edu.uga.cs.quizapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * FinalResultScreen is the final screen on swipe right on question 6. Displays and stores the result and time
 */
public class FinalResultScreen extends AppCompatActivity {
    private static final String DEBUG_TAG = "Final result screen";

    private TextView textView;
    private Button button;
    int score;
    int position;
    int question1_id;
    int question2_id;
    int question3_id;
    int question4_id;
    int question5_id;
    int question6_id;
    private QuizData quizData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result_screen);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Your final score was " + score);

        Result result = new Result();
        result.setScore(score);
        result.setPosition(intent.getIntExtra("position", 0));
        result.setTime(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));
        result.setQuestion1(intent.getLongExtra("question1", 0));
        result.setQuestion2(intent.getLongExtra("question2", 0));
        result.setQuestion3(intent.getLongExtra("question3", 0));
        result.setQuestion4(intent.getLongExtra("question4", 0));
        result.setQuestion5(intent.getLongExtra("question5", 0));
        result.setQuestion6(intent.getLongExtra("question6", 0));

        quizData = new QuizData( this );
        new FinalResultScreen.storeResult().execute(result);

        button = (Button) findViewById( R.id.button );
        button.setOnClickListener( new FinalResultScreen.mainMenuButtonClickListener() );

    }

    private class mainMenuButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity( intent );

        }
    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB writing of a job lead, asynchronously.
    private class storeResult extends AsyncTask<Result, Void, Void> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreateMethod
        @Override
        protected Void doInBackground(Result ... result) {
            quizData.open();
            quizData.storeResult(result[0]);

            Log.d(DEBUG_TAG, "storeResult: quiz result stored ");
            return null;
        }
    }
}