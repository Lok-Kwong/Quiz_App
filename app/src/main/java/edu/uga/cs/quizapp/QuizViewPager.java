package edu.uga.cs.quizapp;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * QuizViewPager is the acutal quiz. It creates 6 quiz question fragments with randomized
 * state questions, randomized answer choices, and checks the answer on swipe right.
 */
public class QuizViewPager extends AppCompatActivity {
    private static final String DEBUG_TAG = "Quiz View Pager";

    SectionsPagerAdapter mSectionsPagerAdapter;
    ViewPager mViewPager;
    ActionBar mActionBar;
    final int numberQuestions = 6;
    int currentPos = 0;
    int score = 0;
    static ArrayList<Question> questions;
    static ArrayList<Boolean> numCorrect = new ArrayList<>(Arrays.asList(false, false, false, false, false, false));

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
        // savedInstanceState check
        if (savedInstanceState == null) {
            new retrieveAllQuestionsTask().execute();
        }
        else {
            questions = savedInstanceState.getParcelableArrayList("Questions");
            currentPos = savedInstanceState.getInt("Position");
            score = savedInstanceState.getInt("Score");
        }


        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            // Checks the last swipe to submit and go to final result activity
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
                } else{
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

    // Creates the actual quiz question screen
    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private int questionNum; // Store 0-6 based on section number and pull from randomized questions[questionNumber]

        private TextView questionText;
        private RadioGroup radioGroup;
        private RadioButton rb1;
        private RadioButton rb2;
        private RadioButton rb3;

        private Question question;
        ArrayList<String> choices = new ArrayList<>(Arrays.asList("capital", "city1", "city2")); // construct list from questions.


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
            setRetainInstance(true);
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

        // Randomizes the answers set into the radio btns
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

    // Save the current question selections, current position, and score
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt( "Position", currentPos);
        Log.d( DEBUG_TAG, "onSaveInstanceState(): saved Position: " + currentPos );
        outState.putParcelableArrayList("Questions", questions);
        outState.putInt( "Score", score);
        Log.d( DEBUG_TAG, "onSaveInstanceState(): saved score: " + score );
//        outState.putBooleanArray("Choices", numCorrect); // SAVE CHOICES???
    }

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