package com.bcit.finalprojectandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


public class recyclerviewadapter extends RecyclerView.Adapter<recyclerviewadapter.ViewHolder> {

    public Data.Record[] localDataSet;

    /**
     * Provide a reference to the type of views that you are using
     * This template comes with a TextView
     */

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView deleteButton;
        private final TextView moreInfoButton;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView_favoritelist_locationname);
            deleteButton = view.findViewById(R.id.button_favoritelist_deletebutton);
            moreInfoButton = view.findViewById(R.id.moreInfo_fave);
        }

        public TextView getTextView() {
            return textView;
        }

        public TextView getDeleteButton() {
            return deleteButton;
        }

        public TextView getMoreInfoButton() {
            return moreInfoButton;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     *                by RecyclerView.
     */
    public recyclerviewadapter(Data.Record[] dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_favorite, viewGroup, false); //error here should be expected, this is a template

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(localDataSet[position].fields.buildingnamespecifics);
        viewHolder.getDeleteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (localDataSet.length == 1){
                    Data.Record emptyItem = new Data.Record(null, null, new Data.Fields("No Favourites Found"), null, null);
                    List<Data.Record> dataSetWithoutItem = new LinkedList<Data.Record>(Arrays.asList(localDataSet));
                    dataSetWithoutItem.add(emptyItem);
                    Data.Record[] arr = new Data.Record[dataSetWithoutItem.size()];
                    localDataSet = dataSetWithoutItem.toArray(arr);
                }
                String buildingname = localDataSet[position].fields.buildingnamespecifics;
                System.out.println(buildingname);
                System.out.println("The delete button was pressed. Also, who made these debug comments!?");
                List<Data.Record> dataSetWithoutItem = new LinkedList<Data.Record>(Arrays.asList(localDataSet));
                dataSetWithoutItem.remove(position);
                Data.Record[] arr = new Data.Record[dataSetWithoutItem.size()];
                localDataSet = dataSetWithoutItem.toArray(arr);
                FavoritesList.recyclerviewadapter.notifyItemRemoved(position);
                MainActivity.locationFragment.locationsAdded = dataSetWithoutItem;

            }
        });
        viewHolder.getMoreInfoButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("Record", localDataSet[position]);
                intent.putExtras(extras);
                view.getContext().startActivity(intent);
            }
        });
        if (localDataSet[position].fields.buildingnamespecifics == "No Favourites Found"){
            viewHolder.getMoreInfoButton().setVisibility(View.INVISIBLE);
            viewHolder.getDeleteButton().setVisibility(View.INVISIBLE);
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.length;
    }
}