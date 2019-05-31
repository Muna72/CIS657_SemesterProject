package com.example.cis657_semesterproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cis657_semesterproject.ResultsFragment.OnListFragmentInteractionListener;
import com.example.cis657_semesterproject.dummy.ResultsContent;
import com.example.cis657_semesterproject.dummy.ResultsContent.ResultsItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link ResultsItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

    private final List<ResultsContent.ResultsItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ResultsRecyclerViewAdapter(List<ResultsContent.ResultsItem> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_results, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mP1.setText("(" + holder.mItem.origLat + "," + holder.mItem.origLng + ")");

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mP1;
        public ResultsContent.ResultsItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mP1 = (TextView) view.findViewById(R.id.p1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mP1.getText() + "'";
        }
    }
}
