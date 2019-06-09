package com.example.cis657_semesterproject;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
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
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_results, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(final ViewHolder holder, int position) {

        String breedName = mValues.get(position).breedName;
        String formattedName = breedName.replaceAll(", ","-");
        formattedName = formattedName.replaceAll(" ", "-");
        String text = "<a href=\"https://www.google.com/search?q=" + formattedName + "\"> " + breedName + " </a>";

        holder.mP1.setClickable(true);
        holder.mP1.setText(Html.fromHtml(text));
        System.out.println("WEB ADDRESS: " + text);
        Linkify.addLinks(holder.mP1, Linkify.ALL);

        holder.breedImage.setImageDrawable(mValues.get(position).breedImage.getDrawable());

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
        public TextView mP1;
        public ResultsActivity.ResultsItem mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mP1 = (TextView) view.findViewById(R.id.p1);
            breedImage = (ImageView) view.findViewById(R.id.breedImage);
        }

    }
}
