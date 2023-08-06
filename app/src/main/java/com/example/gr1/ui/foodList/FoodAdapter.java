package com.example.gr1.ui.foodList;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gr1.databinding.FoodItemBinding;
import com.example.gr1.model.Food;
import com.example.gr1.ui.helper.DatabaseHandler;


import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<Food> foodList;
    private final List<Food> addList;
    private LayoutInflater layoutInflater;
    private DatabaseHandler databaseHandler;

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        private final FoodItemBinding binding;
        public FoodViewHolder(final FoodItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

    }
    public FoodAdapter(List<Food> foodList, DatabaseHandler databaseHandler) {
        this.foodList = foodList;
        this.databaseHandler = databaseHandler;
        this.addList = new ArrayList<>();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        FoodItemBinding binding = FoodItemBinding.inflate(layoutInflater, parent, false);
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.binding.serving.setText(Float.toString(food.getServing())+" gram");
        holder.binding.calories.setText(Float.toString(food.getCalories())+"KCal");
        holder.binding.itemName.setText(food.getName());
        holder.binding.addCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.binding.addCheckBox.isChecked()) {
                    addList.add(food);
                }
            }
        });
        holder.binding.reveal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.binding.foodDetail.getVisibility() == View.GONE){
                    holder.binding.foodDetail.setVisibility(View.VISIBLE);
                    String[] foodDetail = new String[]{};
                    foodDetail = databaseHandler.getFood(Integer.toString(food.getId()));
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            holder.binding.getRoot().getContext(),android.R.layout.simple_list_item_1
                            ,foodDetail);
                    holder.binding.foodDetail.setAdapter(arrayAdapter);
                }else{
                    holder.binding.foodDetail.setVisibility(View.GONE);
                }
            }
        });
        holder.binding.revealBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(holder.binding.foodDetail.getVisibility() == View.GONE){
                    holder.binding.foodDetail.setVisibility(View.VISIBLE);
                }else{
                    holder.binding.foodDetail.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
    public void setFoodList(List<Food> list){
        this.foodList = list;
    }

    public List<Food> getAddList(){
        return this.addList;
    }
}
