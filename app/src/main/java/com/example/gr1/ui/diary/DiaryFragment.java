package com.example.gr1.ui.diary;

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
import com.example.gr1.databinding.FragmentDiaryBinding;
import com.example.gr1.ui.helper.DatabaseHandler;

public class DiaryFragment extends Fragment {

    private FragmentDiaryBinding binding;
    private final MutableLiveData<String> mText = new MutableLiveData<>();
    private AppViewModel appViewModel;
    private DatabaseHandler dbHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onViewCreated(container,savedInstanceState);
        MainActivity activity = (MainActivity) getActivity();

        if (activity != null) {
            appViewModel= activity.getViewModel();
        }
        dbHelper = appViewModel.getDbHelper();

        DiaryViewModel diaryViewModel =
                new ViewModelProvider(this).get(DiaryViewModel.class);

        binding = FragmentDiaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textGallery;
//        diaryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}