package com.example.mygroceryapps.ui.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mygroceryapps.R;
import com.example.mygroceryapps.adapters.PopularAdapters;
import com.example.mygroceryapps.models.PopularModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;



public class HomeFragment extends Fragment {
    RecyclerView recyclerview;
    FirebaseFirestore db;
    List<PopularModel> list;
    PopularAdapters adapters;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home,container,false);
        db = FirebaseFirestore.getInstance();

        recyclerview = root.findViewById(R.id.pop_rec);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        recyclerview.setLayoutManager(llm);
        recyclerview.setAdapter(adapters);
        list = new ArrayList<>();
        adapters = new PopularAdapters(getActivity(),list);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                PopularModel popularModel = document.toObject(PopularModel.class);
                                list.add(popularModel);
                                adapters.notifyDataSetChanged();
                                // Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                });

        return root ;
    }
}

