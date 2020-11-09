package edu.uga.cs.quizapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * This is an adapter class for the RecyclerView to show all results
 */
public class ResultsRecyclerAdapter extends RecyclerView.Adapter<ResultsRecyclerAdapter.ResultHolder> {

    public static final String DEBUG_TAG = "ResultsRecyclerAdapter";

    private List<Result> resultsList;

    public ResultsRecyclerAdapter(List<Result> resultsList ) {
        this.resultsList = resultsList;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class ResultHolder extends RecyclerView.ViewHolder {

        TextView quizId;
        TextView time;
        TextView score;

        public ResultHolder(View itemView ) {
            super(itemView);

            quizId = (TextView) itemView.findViewById( R.id.quizId);
            time = (TextView) itemView.findViewById( R.id.time);
            score = (TextView) itemView.findViewById( R.id.score);
        }
    }

    @Override
    public ResultHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
        // We need to make sure that all CardViews have the same, full width, allowed by the parent view.
        // This is a bit tricky, and we must provide the parent reference (the second param of inflate)
        // and false as the third parameter (don't attach to root).
        // Consequently, the parent view's (the RecyclerView) width will be used (match_parent).
        View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.results_list, parent, false );
        return new ResultHolder( view );
    }

    // This method fills in the values of a holder to show a result.
    // The position parameter indicates the position on the list of results list.
    @Override
    public void onBindViewHolder( ResultHolder holder, int position ) {
        Result result = resultsList.get( position );

        Log.d( DEBUG_TAG, "onBindViewHolder: " + result );

        holder.quizId.setText( "Quiz " + Long.toString(result.getId()) );
        holder.time.setText("Time: " + result.getTime() );
        holder.score.setText("Score: " + result.getScore() );
    }

    @Override
    public int getItemCount() {
        return resultsList.size();
    }
}
