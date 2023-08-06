package com.example.gr1.ui.diary;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.gr1.AppViewModel;
import com.example.gr1.MainActivity;
import com.example.gr1.R;
import com.example.gr1.databinding.FragmentDiaryBinding;
import com.example.gr1.model.Diary;
import com.example.gr1.model.Food;
import com.example.gr1.ui.foodList.FoodAdapter;
import com.example.gr1.ui.helper.DatabaseHandler;
import com.example.gr1.ui.home.HomeViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiaryFragment extends Fragment {

    private FragmentDiaryBinding binding;
    private final MutableLiveData<String> mText = new MutableLiveData<>();
    private Diary todayDiary;
    private AppViewModel appViewModel;
    private DatabaseHandler dbHelper;
    private DiaryViewModel diaryViewModel;
    private List<Food> foodList;
    private FoodAdapter foodAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        /*
        * Displaying Daily goal's calories.
        *
        */

        //Get activity (context) and data from AppViewModel
        super.onViewCreated(container,savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        if (activity.getViewModel() != null) {
            appViewModel = activity.getViewModel();
            dbHelper = appViewModel.getDbHelper();
        } else {
            diaryViewModel =
                    new ViewModelProvider(this).get(DiaryViewModel.class);
            appViewModel =
                    new ViewModelProvider(this).get(AppViewModel.class);
        }

        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        //Display daily goal if existed, else display 0.0

        View root = binding.getRoot();
        final TextView textView = binding.goal;
        if (dbHelper == null || dbHelper.getGoal() == null) {
            diaryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        } else {
            appViewModel.setGoal(dbHelper.getGoal());
            mText.setValue(Float.toString((appViewModel.getGoal().getBMR())));
            mText.observe(getViewLifecycleOwner(), textView::setText);
        }
        /*
        * Connect to Database and read used (eaten) calories.
        */
        todayDiary = new Diary((appViewModel.getGoal().getBMR()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            todayDiary = dbHelper.getTodayDiary(LocalDate.now());
        }
        final TextView used = binding.used;
        used.setText(Float.toString(todayDiary.getCaloriesIn()));
        final TextView remaining = binding.remaining;
        remaining.setText(Float.toString(appViewModel.getGoal().getBMR() - todayDiary.getCaloriesIn()));

        /*
         * Add new food into today's Diary
         */
        updateList();

        final Button addFoodBtn = binding.addFoodBtn;
        addFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_nav_diary_to_nav_foodList);
            }
        });
        /*
         * Display Pie chart
         */
        float totalNutrients = todayDiary.getTotalFat() + todayDiary.getTotalProtein() + todayDiary.getTotalCarb();
        PieChart pieChart = binding.pieChart;
        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        PieEntry pieEntry = new PieEntry(todayDiary.getTotalCarb()*100/totalNutrients,todayDiary.getTotalCarb());
        pieEntries.add(pieEntry);
        pieEntry = new PieEntry(todayDiary.getTotalProtein()*100/totalNutrients,todayDiary.getTotalProtein());
        pieEntries.add(pieEntry);
        pieEntry = new PieEntry(todayDiary.getTotalFat()*100/totalNutrients,todayDiary.getTotalFat());
        pieEntries.add(pieEntry);
        // Initialize pie data set
        PieDataSet pieDataSet = new PieDataSet(pieEntries,"Carb, Protein, Fat");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setData(new PieData(pieDataSet));
        pieChart.getData().setValueTextSize(20f);
        pieChart.animateXY(3000,3000);
        pieChart.getDescription().setEnabled(false);
        return root;
    }
    @SuppressLint("SetTextI18n")
    public void updateList(){
        foodList = new ArrayList<>();
        if(!appViewModel.getDiary().getFoodList().isEmpty()){
            foodList = dbHelper.getDiaryFood(appViewModel.getDiary().getFoodList());
        }
        foodAdapter = new FoodAdapter(foodList,dbHelper);
        LinearLayoutManager llm = new LinearLayoutManager(this.getActivity().getApplicationContext());
        binding.foodList.setLayoutManager(llm);
        binding.foodList.setAdapter(foodAdapter);
        float caloIn = 0.0F;
        float totalProtein = 0.0F;
        float totalFat = 0.0F;
        float totalCarb = 0.0F;
        for (Food food: foodList
        ) {
            caloIn += food.getCalories();
            totalProtein += food.getProtein();
            totalFat += food.getFat();
            totalCarb += food.getCarb();
        }
        todayDiary = appViewModel.getDiary();
        todayDiary.setCaloriesIn(caloIn);
        todayDiary.setTotalProtein(totalProtein);
        todayDiary.setTotalFat(totalFat);
        todayDiary.setTotalCarb(totalCarb);
        appViewModel.setDiary(todayDiary);
        final TextView used = binding.used;
        used.setText(Float.toString(Math.round(todayDiary.getCaloriesIn())));
        final TextView burntCalo = binding.caloriesBurnt;
        todayDiary.setCaloriesOut(255.4f);
        burntCalo.setText("Calories Burnt : "+Float.toString(todayDiary.getCaloriesOut()));
        final TextView remaining = binding.remaining;
        remaining.setText(Float.toString(appViewModel.getGoal().getBMR() - todayDiary.getCaloriesIn()
        + todayDiary.getCaloriesOut()));
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}