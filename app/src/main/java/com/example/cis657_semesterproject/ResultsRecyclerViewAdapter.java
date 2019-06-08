package com.example.cis657_semesterproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cis657_semesterproject.ResultsFragment.OnListFragmentInteractionListener;
import java.util.List;

public class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

    private final List<ResultsActivity.ResultsItem> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ResultsRecyclerViewAdapter(List items, OnListFragmentInteractionListener listener) {
        System.out.println("ADAPTER CREATED");
        mValues = items;
        mListener = listener;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("ONCREATEVIEWHOLDER HIT");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_results, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        System.out.println("DATA GETTING SET");

        holder.mP1.setText(mValues.get(position).breedName);
        holder.breedImage = mValues.get(position).breedImage;

        holder.mView.setOnClickListener(v -> {
                System.out.println("HI");
                if (null != mListener) {
                    System.out.println("OH BOY YAY");
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public ImageView breedImage;
        public final TextView mP1;
        public ResultsActivity.ResultsItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mP1 = (TextView) view.findViewById(R.id.p1);
            breedImage = (ImageView) view.findViewById(R.id.breedImage);
        }

    }
}
