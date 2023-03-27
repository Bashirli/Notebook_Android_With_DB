package com.bashirli.notebook.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bashirli.notebook.R;
import com.bashirli.notebook.adapter.DataAdapter;
import com.bashirli.notebook.model.DataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class FirstFragment extends Fragment {
FirebaseAuth auth;
RecyclerView recyclerView;
FirebaseFirestore firestore;
ArrayList<DataModel> mainData;
DataAdapter dataAdapter;

public FirstFragment(){

}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
firestore=FirebaseFirestore.getInstance();
auth=FirebaseAuth.getInstance();

FirebaseUser user=auth.getCurrentUser();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_first,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
recyclerView=view.findViewById(R.id.recycler);
recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

mainData=new ArrayList<>();
        dataAdapter=new DataAdapter(mainData);
        recyclerView.setAdapter(dataAdapter);
        getData();
    }

    public void getData(){
        CollectionReference reference=firestore.collection("Note");
        reference.orderBy("date", Query.Direction.DESCENDING).whereEqualTo("email",auth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {

            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null){
                    Toast.makeText(getActivity().getApplicationContext(), error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                }
                if(value!=null){

                    for(DocumentSnapshot snapshot:value.getDocuments()){
                        Map<String,Object> data=snapshot.getData();
                   String baslig=(String) data.get("main");
                    String email=(String) data.get("email");
                    String date= String.valueOf(data.get("date"));
                    DataModel dataModel=new DataModel(email,baslig,date);
                    mainData.add(dataModel);

                    }
dataAdapter.notifyDataSetChanged();

                }

            }

        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }
}