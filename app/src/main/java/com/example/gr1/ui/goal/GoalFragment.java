package com.example.gr1.ui.goal;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.gr1.AppViewModel;
import com.example.gr1.MainActivity;
import com.example.gr1.databinding.FragmentGoalBinding;
import com.example.gr1.model.Goal;
import com.example.gr1.ui.helper.DatabaseHandler;

public class GoalFragment extends Fragment {
    private static final String[] levels = {
            "Little/no exercise"
            , "Light exercise (2-3 days/wk)"
            , "Moderate exercise(3-5 days/wk)"
            , "Very active(6-7 days/wk)"
            , "Extra active (athletes/physical job)"};
    private static final String[] purposes = {
            "Losing 0.5 lb per week"
            , "Losing 1 lb per week"
            , "Gaining 0.5 lb per week"
            , "Gaining 1 lb per week"};
    private FragmentGoalBinding binding;

    private Goal goal = new Goal();
    private int purpose;
    private long id_sprinner;
    private final MutableLiveData<String> mText = new MutableLiveData<>();
    private AppViewModel appViewModel;

    @SuppressLint("SetTextI18n")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container, savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();
        if (activity != null) {
            appViewModel = activity.getViewModel();
        }
        DatabaseHandler dbHelper = appViewModel.getDbHelper();

        binding = FragmentGoalBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //***  Initialize dropdown lists
        //  - activity level
        Spinner spinner = binding.spinner;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Goal.Level a = Goal.Level.valueOfLabel(levels[(int) id]);
                id_sprinner = id;
                goal.setActivityLevel(a);
                System.out.println(goal.getActivityLevel());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //  - purpose
        Spinner spinner2 = binding.spinner2;
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, purposes);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch ((int) id) {
                    case 0:
                        purpose = -250;
                        break;
                    case 1:
                        purpose = -500;
                        break;
                    case 2:
                        purpose = 250;
                        break;
                    case 3:
                        purpose = 500;
                }
                goal.setPurpose(purpose);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //Handle Gender Radio buttons
        RadioGroup radioGroup = binding.radioGroup;
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    RadioButton male = binding.male;
                    if (male.isChecked()) {
                        goal.setGender("Male");
                    } else {
                        goal.setGender("Female");
                    }
                }
            }
        });

        //Handle calculate button when clicked
        Button cal = binding.Cal;
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.age.getText().toString().equals("") && appViewModel.getGoal().getAge() != 0) {
                    goal = appViewModel.getGoal();
                }
                if (!binding.age.getText().toString().equals("")) {
                    goal.setAge(Integer.parseInt(binding.age.getText().toString()));
                }
                if (!binding.height.getText().toString().equals("")) {
                    goal.setHeight(Float.parseFloat(binding.height.getText().toString()));
                }
                if (!binding.weight.getText().toString().equals("")) {
                    goal.setWeight(Float.parseFloat(binding.weight.getText().toString()));
                }
                if (!binding.male.getText().toString().equals("")) {
                    goal.setGender(binding.male.getText().toString());
                } else {
                    goal.setGender(binding.female.getText().toString());
                }
                double BMR;
                if (binding.male.isChecked()) {
                    BMR = 66.47 + (13.75 * goal.getWeight()) + (5.003 * goal.getHeight()) - (6.755 * goal.getAge());
                } else {
                    BMR = 655.1 + (9.563 * goal.getWeight()) + (1.85 * goal.getHeight()) - (4.676 * goal.getAge());
                }
                System.out.println(goal.getActivityLevel());
                Double a = Goal.Level.valueOfLevel(Goal.Level.valueOf(goal.getActivityLevel()));

                BMR = Math.ceil(BMR * a + purpose);
                System.out.println(BMR);
                goal.setBMR((float) BMR);
                appViewModel.setGoal(goal);
                mText.setValue("Daily Goal :  " + (appViewModel.getGoal().getBMR()));
            }
        });
        //*********
        // Fill in the info if a goal's already existed.
        final TextView textView = binding.dailyGoal;
        appViewModel.setGoal(dbHelper.getGoal());
        if (appViewModel.getGoal().getWeight() != 0) {
            goal = appViewModel.getGoal();
            binding.weight.setText("" + appViewModel.getGoal().getWeight());
            binding.height.setText("" + appViewModel.getGoal().getHeight());
            binding.age.setText("" + appViewModel.getGoal().getAge());
            for (int i = 0; i < levels.length; i++) {
                String level = levels[i];
                if (level.equals(Goal.Level.valueOfValue(
                                Goal.Level.valueOf(appViewModel.getGoal().getActivityLevel()
                                )
                        )
                )
                ) {
                    binding.spinner.setSelection(i);
                    break;
                }
            }
            switch (appViewModel.getGoal().getPurpose()) {
                case -250:
                    binding.spinner2.setSelection(0);
                    break;
                case -500:
                    binding.spinner2.setSelection(1);
                    break;
                case 250:
                    binding.spinner2.setSelection(2);
                    break;
                case 500:
                    binding.spinner2.setSelection(3);
            }
        }
        // Set textView("Daily Goal") in goal fragment
        mText.setValue("Daily Goal :  " + (appViewModel.getGoal().getBMR()));
        mText.observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}