package edu.uga.cs.quizapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.opencsv.CSVReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class QuizViewPager extends AppCompatActivity {
    private static final String DEBUG_TAG = "Quiz View Pager";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ActionBar mActionBar;
    final int numberQuestions = 6;
    int currentPos = 0;
    int score = 0;
    static List<Question> questions;
    static List<Boolean> numCorrect = Arrays.asList(false, false, false, false, false, false);

    private QuizData quizData;

    private boolean isLastPageSwiped;
    private int counterPageScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_view_pager);

        mActionBar = getSupportActionBar();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), numberQuestions);
        mActionBar.setTitle(mSectionsPagerAdapter.getPageTitle(0));
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        quizData = new QuizData( this );
        new retrieveAllQuestionsTask().execute();


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Here 5 is last position
                if (position == 5 && positionOffset == 0 && !isLastPageSwiped){
                    if(counterPageScroll != 0){
                        isLastPageSwiped = true;
                        //Next Activity here
                        Intent intent = new Intent(QuizViewPager.this, FinalResultScreen.class);
                        intent.putExtra("score", score);
                        intent.putExtra("position", currentPos);
                        intent.putExtra("question1", questions.get(0).getId());
                        intent.putExtra("question2", questions.get(1).getId());
                        intent.putExtra("question3", questions.get(2).getId());
                        intent.putExtra("question4", questions.get(3).getId());
                        intent.putExtra("question5", questions.get(4).getId());
                        intent.putExtra("question6", questions.get(5).getId());
                        QuizViewPager.this.startActivity( intent );
                    }
                    counterPageScroll++;
                }else{
                    counterPageScroll = 0;
                }
            }

            @Override
            public void onPageSelected(final int position) {
                mActionBar.setTitle(mSectionsPagerAdapter.getPageTitle(position));
                score = countCorrect();
                currentPos = position;
                Log.d(DEBUG_TAG, "Correct answers " + countCorrect());
                Log.d(DEBUG_TAG, "Page changed " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    // Count the number of correct answers after each page selected
    private Integer countCorrect() {
        int count = 0;
        for (boolean b : numCorrect) {
            if (b) {
                count++;
            }
        }
        return count;
    }

//    // Execute the retrieval of the CSV file in an asynchronous way, without blocking the UI thread.
//    // Called by DBHelper in the onCreate method bc of csv file getting needing an activity
//    public void setInitialData() {
//        new getIntialCSVDataTask().execute();
//    }
//
//    // This is an AsyncTask class (it extends AsyncTask) to perform grab csv file data and calls
//    // quizData to insert into db.
//    private class getIntialCSVDataTask extends AsyncTask<Void, Void, Void> {
//
//        // This method will run as a background process to write into db.
//        // It will be automatically invoked by Android, when we call the execute method
//        // in the onCreateMethod
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                // read the CSV data
//                CSVReader reader = new CSVReader(new InputStreamReader(in_s));
//                String[] nextLine;
//
//                while ((nextLine = reader.readNext()) != null) {
//                    System.out.println("State : " + nextLine[0]);
//                    System.out.println("Capital : " + nextLine[1]);
//                    System.out.println("Second city : " + nextLine[2]);
//                    System.out.println("Third city : " + nextLine[3]);
//                    System.out.println("==========================");
//                    ContentValues values = new ContentValues();
//                    values.put(QuizDBHelper.QUIZQUESTIONS_COLUMN_STATE, nextLine[0]);
//                    values.put(QuizDBHelper.QUIZQUESTIONS_COLUMN_CAPITAL, nextLine[1]);
//                    values.put(QuizDBHelper.QUIZQUESTIONS_COLUMN_CITY1, nextLine[2]);
//                    values.put(QuizDBHelper.QUIZQUESTIONS_COLUMN_CITY2, nextLine[3]);
//
//                    // Insert the new row into the database table;
//                    // The id (primary key) is automatically generated by the database system
//                    // and returned as from the insert method call.
//                    quizData.insertQuestion(values);
//                }
//
//                reader.close();
//
//            } catch (Exception e) {
//                Log.e(DEBUG_TAG, e.toString());
//            }
//            Log.d(DEBUG_TAG, "getIntialCSVDataTask: quiz questions retrieved: ");
//            return null;
//        }
//    }

    // This is an AsyncTask class (it extends AsyncTask) to perform DB writing of a job lead, asynchronously.
    private class retrieveAllQuestionsTask extends AsyncTask<Void, Void, Void> {

        // This method will run as a background process to write into db.
        // It will be automatically invoked by Android, when we call the execute method
        // in the onCreateMethod
        @Override
        protected Void doInBackground(Void... voids) {
            quizData.open();
            questions = quizData.retrieveAllQuestions();

            // Shuffle questions for future use (Grab first 7 to be questions)
            Collections.shuffle(questions);

            Log.d(DEBUG_TAG, "QUIZ VIEW PAGER: quiz questions retrieved: ");
            return null;
        }
    }

    public void loadView(TextView textView, String state, RadioButton rb1, RadioButton rb2, RadioButton rb3, List<String> choices) {
        textView.setText("What is the capital of " + state + "?");

        rb1.setText(choices.get(0));
        rb2.setText(choices.get(1));
        rb3.setText(choices.get(2));
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final int mSize;

        public SectionsPagerAdapter(FragmentManager fm, int size) {
            super(fm);
            this.mSize = size;
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int questionNum = position + 1;
            return String.valueOf("Question " + questionNum);
        }
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int questionNum; // Store 0-6 based on section number and pull from randomized questions[questionNumber]

        private TextView questionText;
        private RadioGroup radioGroup;
        private RadioButton rb1;
        private RadioButton rb2;
        private RadioButton rb3;

        // GET CAPITALS
        // GET USER ANSWERS
        // RADIO BUTTON GET ANSWER
        private Question question;
        List<String> choices = Arrays.asList("capital", "city1", "city2"); // construct list from questions.


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            if (getArguments() != null) {
                questionNum = getArguments().getInt(ARG_SECTION_NUMBER); // QuestionNum grabs from question array (0-6) depending on current page
                // give random order to list
                Collections.shuffle(choices);
                Log.d(DEBUG_TAG, "Question Number is: " + questionNum);
            } else {
                questionNum = -1;
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_quiz, container, false);
            questionText = (TextView) rootView.findViewById(R.id.questionText);
            rb1 = (RadioButton) rootView.findViewById(R.id.option1);
            rb2 = (RadioButton) rootView.findViewById(R.id.option2);
            rb3 = (RadioButton) rootView.findViewById(R.id.option3);
            radioGroup = (RadioGroup) rootView.findViewById( R.id.radioGroup );

            // Check the correct answer and modify the list of booleans at that position
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId){
                        case R.id.option1:
                            if (rb1.getText() == question.getCapital()){
                                numCorrect.set(questionNum, true);
                                Log.d(DEBUG_TAG, "Correct! ");
                            }
                            else {
                                numCorrect.set(questionNum, false);
                                Log.d(DEBUG_TAG, "Incorrect! ");
                            }
                            break;
                        case R.id.option2:
                            if (rb2.getText() == question.getCapital()){
                                numCorrect.set(questionNum, true);
                                Log.d(DEBUG_TAG, "Correct! ");
                            }
                            else {
                                numCorrect.set(questionNum, false);
                                Log.d(DEBUG_TAG, "Incorrect! ");
                            }
                            break;
                        case R.id.option3:
                            if (rb3.getText() == question.getCapital()){
                                numCorrect.set(questionNum, true);
                                Log.d(DEBUG_TAG, "Correct! ");
                            }
                            else {
                                numCorrect.set(questionNum, false);
                                Log.d(DEBUG_TAG, "Incorrect! ");
                            }
                            break;
                    }
                }
            });
            return rootView;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            if (QuizViewPager.class.isInstance(getActivity())) {
                question = questions.get(questionNum);
                randomizeChoices();

                ((QuizViewPager) getActivity()).loadView(questionText, question.getState(), rb1, rb2, rb3, choices);
            }
        }

        public void randomizeChoices() {
            int capitalIndex = choices.indexOf("capital"); // Find where capital, city1, and city2 is and put the respective answers into that index
            int city1Index = choices.indexOf("city1"); // 1
            int city2Index = choices.indexOf("city2"); // 2

            if (capitalIndex != -1) { // In case of user scrolling back
                choices.set(capitalIndex, question.getCapital());
                choices.set(city1Index, question.getCity1());
                choices.set(city2Index, question.getCity2());
            }
        }
    }

//    // Save the list index selection
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putInt( "Position", currentPos);
//        Log.d( DEBUG_TAG, "onSaveInstanceState(): saved Position: " + currentPos );
//        outState.putParcelableArray("Questions", );
//    }

    @Override
    protected void onResume() {
        Log.d( DEBUG_TAG, "QuizActivity.onResume()" );

        // RETRIEVE QUIZ PROGRESS HERE onRestoreInstance

        // open the database in onResume
        if( quizData != null )
            quizData.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d( DEBUG_TAG, "QuizActivity.onPause()" );

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