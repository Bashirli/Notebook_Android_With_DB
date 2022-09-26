package com.bashirli.notebook.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bashirli.notebook.databinding.RecyclerxmlBinding;
import com.bashirli.notebook.model.DataModel;
import com.bashirli.notebook.view.FirstFragmentDirections;
import com.bashirli.notebook.view.PublishFragment;
import com.bashirli.notebook.view.PublishFragmentDirections;
import com.google.android.gms.common.data.DataHolder;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.AdapterHolder> {
    ArrayList<DataModel> arrayList;
    String [] colors={"#FCE7E7","#96C3B9","#EAAC08"};

    public DataAdapter(ArrayList<DataModel> arrayList) {
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerxmlBinding recyclerxmlBinding=RecyclerxmlBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new AdapterHolder(recyclerxmlBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder,  int position) {
holder.recyclerxmlBinding.text.setText(arrayList.get(position).getMain());
holder.recyclerxmlBinding.text.setTextColor(Color.BLACK);
holder.recyclerxmlBinding.text.setBackgroundColor(Color.parseColor(colors[position%3]));
holder.recyclerxmlBinding.text.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        FirstFragmentDirections.ActionFirstFragmentToPublishFragment direction=FirstFragmentDirections.actionFirstFragmentToPublishFragment("old");
    direction.setInfo("old");
direction.setEmail(arrayList.get(position).getEmail());
direction.setDate(arrayList.get(position).getDate());
        Navigation.findNavController(view).navigate(direction);
    }
});
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class AdapterHolder extends RecyclerView.ViewHolder{
public RecyclerxmlBinding recyclerxmlBinding;

        public AdapterHolder(@NonNull RecyclerxmlBinding recyclerxmlBinding) {
            super(recyclerxmlBinding.getRoot());
          this.recyclerxmlBinding=recyclerxmlBinding;
        }
    }
}
