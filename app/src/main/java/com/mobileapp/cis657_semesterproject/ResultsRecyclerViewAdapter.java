package com.mobileapp.cis657_semesterproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobileapp.cis657_semesterproject.ResultsFragment.OnListFragmentInteractionListener;
import java.util.List;

public class ResultsRecyclerViewAdapter extends RecyclerView.Adapter<ResultsRecyclerViewAdapter.ViewHolder> {

    private final List<ResultsActivity.ResultsItem> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;

    public ResultsRecyclerViewAdapter(List items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
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
        String text = "<a href=\"http://www.google.com/search?q=" + formattedName + "\"> " + breedName + " </a>";
        String url = "http://www.google.com/search?q=" + formattedName;
        holder.mP1.setClickable(true);
        holder.mP1.setText(Html.fromHtml(text));
        holder.mP1.setTextSize(15);
        Linkify.addLinks(holder.mP1, Linkify.ALL);
        //TODO links not working

        holder.breedImage.setImageDrawable(mValues.get(position).breedImage.getDrawable());

        holder.mP1.setOnClickListener(v -> {
            if(mContext instanceof ResultsActivity){
                ((ResultsActivity)mContext).processGoogleSearch(url);
            }
        });

        holder.mView.setOnClickListener(v -> {
                if (null != mListener) {
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
