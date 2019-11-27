package com.example.luvyourleftovers;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.luvyourleftovers.basic_classes.Ingredient;
import com.example.luvyourleftovers.basic_classes.IngredientObject;
import com.example.luvyourleftovers.basic_classes.Recipe;
import com.example.luvyourleftovers.basic_classes.RecipeObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static com.example.luvyourleftovers.basic_classes.Ingredient.*;

/**
 * Activity will show saved recipes and display them in List View (at this current version).
 * Each Recipe if clicked should send to a new Activity (Display Recipe Activity)
 **/
public class SavedRecipesDev extends AppCompatActivity {

    public void makeRecipes(Context context){


        try {

            InputStream inputStream = this.openFileInput("recipes.txt");
            //do nothing if recipes exists.

        }catch(FileNotFoundException e){

            String[] ingredient_names = {"Tomato", "Apple", "Steak", "Poultry", "Flour", "Water", "Orange"};
            ArrayList<Ingredient> ingredients = new ArrayList<>();

            for (String ingredient : ingredient_names) {
                IngredientObject ingr = new IngredientObject(ingredient, 0);
                ingredients.add(ingr);

            }

            ArrayList<RecipeObject> saved_recipes = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                int j = 0;
                RecipeObject newRecipe = new RecipeObject();
                // add 3 random ingredients to the recipe list.
                while (j < 3) {
                    double randomDouble = Math.random();
                    randomDouble = randomDouble * 7;
                    int randomInt = (int) randomDouble;
//                    Log.d("Ingredient", "chosen ingredient "+ingredients.get(randomInt).getName() +" vs "+ingredients.get(0).getName());
                    newRecipe.addIngredient(ingredients.get(randomInt));
                    j++;
                }
                newRecipe.addInstruction("Stop being dumb");
                newRecipe.addInstruction("Fool!");
                saved_recipes.add(newRecipe);
            }

            writeRecipes(saved_recipes, context);
        }
    }

    public void writeRecipes(ArrayList<RecipeObject> recipes, Context context){
        String data = "";
        for(RecipeObject recipe: recipes)
            data += recipe.toString();

        try{
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("recipes.txt", Context.MODE_APPEND));
            outputStreamWriter.append(data+"\n");
            outputStreamWriter.close();
        }
        catch(IOException  e){
            Log.e("Exception", "File write failed: "+e.toString());
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_recipes_dev);
        makeRecipes(this);
        Context context = this;

        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = readFromFile(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //send user     to DisplayRecipe.
                String clickedItem=(String) list.getItemAtPosition(position);
                Toast.makeText(SavedRecipesDev.this,clickedItem,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context, DisplayRecipe.class);
                startActivity(intent);
            }
        });
    }




    private ArrayList<String> readFromFile(Context context){
        /**
         * @author: Mohamed Jama
         * @description: reads from a file that contains list of Recipes.
         * @changes: needs to go from plain txt file to some JSON or other mark-down document type
         * for easy retrieval of values.
         **/
        ArrayList<String> fileContent = new ArrayList<>();

        try{
            InputStream inputStream = context.openFileInput("recipes.txt");

            if( inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String recieveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while( (recieveString = bufferedReader.readLine()) != null)
                    //stringBuilder.append(recieveString);
                    fileContent.add(recieveString);

                inputStream.close();
            }
        }
        catch (FileNotFoundException e){
            Log.e("Reading Exception", "file not found: "+e.toString());
        }
        catch(IOException e){
            Log.e("Exception", "can not read file: "+e.toString());
        }

        Log.d("File Reading", "number of items found: "+fileContent.size());
        return fileContent;
    }

}