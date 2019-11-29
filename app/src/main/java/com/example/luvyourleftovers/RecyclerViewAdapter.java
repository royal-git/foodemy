package com.example.luvyourleftovers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.luvyourleftovers.basic_classes.RecipeObject;
import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * This class populates the RecyclerView with data, as well as converts pure Java objects into list item
 * Views to be inserted and displayed to the user in the Android UI.
 *
 * The variable mData (List<String>) will contain the data elements that are in the list.
 * //TODO Possibly change this as required
 *
 * The layout inflater mInflater instantiates the XML Layout object to a Java object so it can be used.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<RecipeObject> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    RecyclerViewAdapter(Context context, List<RecipeObject> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    //Inflate each row from the recyclerview_row.xml file
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RecipeObject recipe = mData.get(position);
        holder.recipeName.setText(recipe.getName());
        Picasso.get().load(recipe.getImageLink()).into(holder.recipeImage);
        holder.missingIngredients
            .setText("Ingredients match: (" + recipe.getUsedIngredients().size() + "/" + (recipe.getMissedIngredients().size() + recipe.getUsedIngredients().size()) + ")");
    }

    //get size of list (number of items)
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // dynamically loads only a subset of the mData container to keep resource usage low.
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeName;
        TextView remainingTime;
        TextView missingIngredients;
        ImageView recipeImage;

        ViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipe_name);
            missingIngredients = itemView.findViewById(R.id.missing_ingredients);
            recipeImage = itemView.findViewById(R.id.recipe_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }


    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}