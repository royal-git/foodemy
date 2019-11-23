package com.example.luvyourleftovers;
import com.example.luvyourleftovers.basic_classes.*;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Activity will show saved recipes and display them in List View (at this current version).
 * Each Recipe if clicked should send to a new Activity (Display Recipe Activity)
 **/
public class SavedRecipesDev extends AppCompatActivity {

    public static class savedIngredients implements Ingredient{
        private String name;
        private Types type;


        public savedIngredients(String name, int type){
            this.name = name;
            this.type = Types.values()[5];
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public void setName(String name) {
            this.name = name;
        }

        @Override
        public void setType(Types type) {
            this.type = Types.values()[5];
        }
    }


    public static class savedRecipe implements Recipe{

        private ArrayList<Ingredient> ingredients = new ArrayList<>();
        private ArrayList<String> instructions = new ArrayList<>();

        @Override
        public void addIngredient(Ingredient ingredient) {
            ingredients.add(ingredient);
        }

        @Override
        public void addInstruction(String instruction) {
            instructions.add(instruction);
        }

        @Override
        public ArrayList<Ingredient> getIngrediantList() {
            return ingredients;
        }

        @Override
        public ArrayList<String> getInstructions() {
            return instructions;
        }

        public String toString(){
            String ingredient_delimited = "";
            int i =0;
            for(Ingredient ingredient: ingredients){
                if(i-1 == ingredients.size())
                    ingredient_delimited+=ingredient.getName();
                else
                    ingredient_delimited+=ingredient.getName()+",";
                i++;
            }

            String instruction_delimited = "";
            i =0;
            for(String instruction: instructions){
                if(i-1 == ingredients.size())
                    instruction_delimited+=instruction;
                else
                    instruction_delimited+=instruction+",";
                i++;
            }
            return ingredient_delimited+"@@"+instruction_delimited+"\n";
        }
    }

    public void makeRecipes(Context context){


        try {

            InputStream inputStream = this.openFileInput("recipes.txt");
            //do nothing if recipes exists.

        }catch(FileNotFoundException e){

            String[] ingredient_names = {"Tomato", "Apple", "Steak", "Poultry", "Flour", "Water", "Orange"};
            ArrayList<Ingredient> ingredients = new ArrayList<>();

            for (String ingredient : ingredient_names) {
                savedIngredients ingr = new savedIngredients(ingredient, 0);
                ingredients.add(ingr);

            }

            ArrayList<savedRecipe> saved_recipes = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                int j = 0;
                savedRecipe newRecipe = new savedRecipe();
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

    public void writeRecipes(ArrayList<savedRecipe> recipes, Context context){
        String data = "";
        for(savedRecipe recipe: recipes)
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


        final ListView list = findViewById(R.id.list);
        ArrayList<String> arrayList = readFromFile(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, arrayList);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //send user to DisplayRecipe.
                String clickedItem=(String) list.getItemAtPosition(position);
                Toast.makeText(SavedRecipesDev.this,clickedItem,Toast.LENGTH_LONG).show();
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