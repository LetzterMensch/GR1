package com.example.gr1.ui.home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.gr1.AppViewModel;
import com.example.gr1.MainActivity;
import com.example.gr1.databinding.FragmentHomeBinding;
import com.example.gr1.model.Diary;
import com.example.gr1.ui.helper.DatabaseHandler;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AppViewModel appViewModel;
    private HomeViewModel homeViewModel;
    private DatabaseHandler dbHelper;
    private final MutableLiveData<String> mText = new MutableLiveData<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Get activity (context) and data from AppViewModel
        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        if (activity.getViewModel() != null) {
            appViewModel = activity.getViewModel();
            dbHelper = appViewModel.getDbHelper();
        } else {
            homeViewModel=
                    new ViewModelProvider(this).get(HomeViewModel.class);
            appViewModel =
                    new ViewModelProvider(this).get(AppViewModel.class);
        }
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Display daily goal if existed, else display 0.0
        final TextView textView = binding.goal;
        if (dbHelper == null || dbHelper.getGoal() == null) {
            homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        } else {
            appViewModel.setGoal(dbHelper.getGoal());
            mText.setValue(Float.toString((appViewModel.getGoal().getBMR())));
            mText.observe(getViewLifecycleOwner(), textView::setText);
        }
        Diary todayDiary = new Diary((appViewModel.getGoal().getBMR()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            todayDiary = dbHelper.getTodayDiary(LocalDate.now());
        }
        final TextView used = binding.used;
        used.setText(Float.toString(Math.round(todayDiary.getCaloriesIn())));
        final TextView remaining = binding.remaining;
        remaining.setText(Float.toString(appViewModel.getGoal().getBMR() - todayDiary.getCaloriesIn()));

        // Display bar chart
        BarChart barChart = binding.barChart;
        // Initialize array list
        ArrayList<BarEntry> barEntriesIn = new ArrayList<>();
        ArrayList<BarEntry> barEntriesOut = new ArrayList<>();

        todayDiary = appViewModel.getDiary();
        BarEntry barEntry = new BarEntry(1,2450.5f);
        barEntriesIn.add(barEntry);
        barEntry = new BarEntry(2,2250.5f);
        barEntriesIn.add(barEntry);
        barEntry = new BarEntry(3,todayDiary.getCaloriesIn());
        barEntriesIn.add(barEntry);
        BarDataSet barDataSetIn = new BarDataSet(barEntriesIn,"Calories In");
        barDataSetIn.setColor(Color.RED);
//        barChart.setData(new BarData(barDataSetIn));
//        barChart.getData().setValueTextSize(40.0f);
        barEntry = new BarEntry(1,225.5f);
        barEntriesOut.add(barEntry);
        barEntry = new BarEntry(2,245.5f);
        barEntriesOut.add(barEntry);
        barEntry = new BarEntry(3,todayDiary.getCaloriesOut());
        barEntriesOut.add(barEntry);
        BarDataSet barDataSetOut = new BarDataSet(barEntriesOut,"Calories Out");
        barDataSetOut.setColor(Color.GREEN);

        barChart.setData(new BarData(barDataSetIn, barDataSetOut));
        barChart.getData().setValueTextSize(15.0f);

        String[] dates = new String[]{"2023/8/4","2023/8/5","2023/8/6"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(dates));
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setGranularityEnabled(true);

        barChart.setDragEnabled(true);
        barChart.setVisibleXRangeMaximum(4);

        float barSpace = 0.28f;
        float groupSpace = 0.14f;
        barChart.getData().setBarWidth(0.15f);

        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setAxisMaximum(0+barChart.getBarData().getGroupWidth(groupSpace,barSpace)*3);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.groupBars(0,groupSpace,barSpace);
//        barChart.animateY(3000);
        barChart.getDescription().setText("Statistics Chart");
        barChart.invalidate();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}