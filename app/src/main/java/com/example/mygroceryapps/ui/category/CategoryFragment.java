package com.example.mygroceryapps.ui.category;

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
import com.example.mygroceryapps.adapters.NavCategoryAdapter;
import com.example.mygroceryapps.databinding.FragmentHomeBinding;
import com.example.mygroceryapps.models.NavCategoryModel;
import com.example.mygroceryapps.models.PopularModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {
    RecyclerView recyclerView;
    List<NavCategoryModel> categoryModelList;
    NavCategoryAdapter adapter;
    FirebaseFirestore db;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category,container,false);
        db = FirebaseFirestore.getInstance();

        recyclerView = root.findViewById(R.id.cat_rec);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(llm);
        categoryModelList = new ArrayList<>();
        adapter = new NavCategoryAdapter(getActivity(),categoryModelList);
        recyclerView.setAdapter(adapter);

        db.collection("categoryProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                NavCategoryModel model = document.toObject(NavCategoryModel.class);
                                categoryModelList.add(model);
                                adapter.notifyDataSetChanged();
                                // Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                });


        return root;
    }
}
