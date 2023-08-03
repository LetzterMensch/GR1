package com.example.gr1.ui.home;

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
import com.example.gr1.ui.helper.DatabaseHandler;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private AppViewModel appViewModel;
    private HomeViewModel homeViewModel;
    private DatabaseHandler dbHelper;
    private final MutableLiveData<String> mText = new MutableLiveData<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
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

        final TextView textView = binding.goal;
        if (dbHelper == null || dbHelper.getGoal() == null) {
            homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        } else {
            appViewModel.setGoal(dbHelper.getGoal());
            mText.setValue(Float.toString((appViewModel.getGoal().getBMR())));
            mText.observe(getViewLifecycleOwner(), textView::setText);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}