package com.example.luvyourleftovers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InputFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }


    public void onLaunch(View view) {
        Intent ingredientRecipes= new Intent(getActivity(), IngredientsRecipesActivity.class);
        startActivity(ingredientRecipes);
    }


}
