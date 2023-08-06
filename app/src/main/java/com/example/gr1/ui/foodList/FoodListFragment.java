package com.example.gr1.ui.foodList;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.lang.Object;
import com.example.gr1.AppViewModel;
import com.example.gr1.MainActivity;
import com.example.gr1.R;
import com.example.gr1.model.Diary;
import com.example.gr1.model.Food;
import com.example.gr1.ui.diary.DiaryFragment;
import com.example.gr1.ui.helper.DatabaseHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.gr1.databinding.FragmentFoodlistBinding;

import org.json.JSONArray;
import org.json.JSONObject;

public class FoodListFragment extends Fragment {
    private AppViewModel appViewModel;
    private FragmentFoodlistBinding binding;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private DatabaseHandler dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*
         * Get access to database
         */
        super.onViewCreated(container, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            appViewModel = activity.getViewModel();
        }
        dbHelper = appViewModel.getDbHelper();
        binding = FragmentFoodlistBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        /*
         * Handling Search operation
         */
        final SearchView searchView = binding.searchView;
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            //Search for new food
            @Override
            public boolean onQueryTextSubmit(String query) {
                /*
                 * To avoid NetworkOnMainThreadException :
                 * This exception is thrown when application attempts to perform a networking
                 * operation in the main thread.
                 * Use below code in your onViewCreated to avoid this error else
                 * Call your networking operations (getting data from web server) request in thread or Async class.
                 * */
                int SDK_INT = android.os.Build.VERSION.SDK_INT;
                if (SDK_INT > 8) {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                            .permitAll().build();
                    StrictMode.setThreadPolicy(policy);
                    findFood(query);
                    updateList();
                }
                return true;
            }

            //Search within already known food list
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });
        /*
         * Displaying RecyclerList View of existing Food.
         */
        updateList();
        /*
         * Handling Add button when clicked
         */
        Button addBtn = binding.addBtn;
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder foodArr = new StringBuilder();
                for (Food food: foodAdapter.getAddList()) {
                    if(foodArr.toString().isEmpty()){
                        foodArr.append(food.getName());
                    }else {
                        foodArr.append("#").append(food.getName());
                    }
                }
                Diary diary = appViewModel.getDiary();
                diary.setFoodList(foodArr.toString());
                dbHelper.insertDiary(diary);
                Navigation.findNavController(v).navigate(R.id.action_nav_foodList_to_nav_diary);
            }
        });
        return root;
    }
    public void filterList(String text){
            List<Food> filteredList = new ArrayList<>();
            for(Food food : foodList){
                if(food.getName().toLowerCase().contains(text.toLowerCase())){
                    filteredList.add(food);
                }
            }
            if(filteredList.isEmpty() || text.isEmpty()){
                updateList();
            }else {
                updateList(filteredList);
            }
    }
    public void updateList(){
        foodList = new ArrayList<>();
        foodList = dbHelper.getAllFood();
        foodAdapter = new FoodAdapter(foodList,dbHelper);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity().getApplicationContext());
        binding.filteredList.setLayoutManager(llm);
        binding.filteredList.setAdapter(foodAdapter);
    }
    public void updateList(List<Food> filteredList){
        foodAdapter.setFoodList(filteredList);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity().getApplicationContext());
        binding.filteredList.setLayoutManager(llm);
        binding.filteredList.setAdapter(foodAdapter);
    }
    public void findFood(String query) {
        URL url = null;
        String apiKey = "L7IQCkHNY0CvpffBDeC2yA==xfG66rCzHNKVPEFi";
        try {
            url = new URL("https://api.api-ninjas.com/v1/nutrition?query=" + query);
            HttpURLConnection connection = null;
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            connection.addRequestProperty("x-api-key", apiKey);

            int status = connection.getResponseCode();
            System.out.println(status);

            InputStream responseStream = null;

            responseStream = connection.getInputStream();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNodeRoot = null;
            jsonNodeRoot = mapper.readTree(responseStream);
            Food food = new Food(jsonNodeRoot.elements().next().get("name").asText(),
                    Float.parseFloat(jsonNodeRoot.elements().next().get("calories").asText())
                    , Float.parseFloat(jsonNodeRoot.elements().next().get("serving_size_g").asText())
                    , Float.parseFloat(jsonNodeRoot.elements().next().get("protein_g").asText())
                    , Float.parseFloat(jsonNodeRoot.elements().next().get("fat_total_g").asText())
                    , Float.parseFloat(jsonNodeRoot.elements().next().get("carbohydrates_total_g").asText())
            );
            System.out.println(food);
            if(dbHelper.insertFood(food) == 1){
                Toast.makeText(this.getActivity().getApplicationContext(),"No data found",Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}