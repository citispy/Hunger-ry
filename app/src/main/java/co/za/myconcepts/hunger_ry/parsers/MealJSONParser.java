package co.za.myconcepts.hunger_ry.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import co.za.myconcepts.hunger_ry.model.Meal;

public class MealJSONParser {
    public static List<Meal> parseFeed(String content){

        try {
            JSONArray ar = new JSONArray(content);
            List<Meal> mealList = new ArrayList<>();

            for (int i = 0; i < ar.length(); i++) {
                JSONObject obj = ar.getJSONObject(i);
                Meal meal = new Meal();

                //Parsing ID's
                meal.setTitle(obj.getString("title"));
                meal.setDescription(obj.getString("description"));

                mealList.add(meal);

            }

            return mealList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
