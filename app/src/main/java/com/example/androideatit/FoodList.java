package com.example.androideatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androideatit.Interface.ItemClickListener;
import com.example.androideatit.Model.Category;
import com.example.androideatit.Model.Foods;
import com.example.androideatit.Viewfolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    String categoryId="";
    FirebaseRecyclerAdapter<Foods, FoodViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");


        recyclerView = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if(getIntent()!=null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId !=null){
            loadListFood(categoryId);

        }

    }

    private void loadListFood(String categoryId) {
        Query query = FirebaseDatabase.getInstance().getReference("Foods")
                .orderByChild("MenuId").equalTo(categoryId);

        FirebaseRecyclerOptions<Foods> options = new FirebaseRecyclerOptions.Builder<Foods>().setQuery(query, Foods.class).build();
     //  Query query = FirebaseDatabase .getInstance() .getReference("Food") .orderByChild("MenuId").equalTo(categoryId);//select from food where Menuid is categoru
    //    foodList.orderByChild("MenuId").equalTo(categoryId);

        adapter = new FirebaseRecyclerAdapter<Foods, FoodViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int i, @NonNull Foods foods) {
            foodViewHolder.food_name.setText(foods.getName());
                Picasso.get().load(foods.getImage()).into(foodViewHolder.food_image);


                final Foods local=foods;

                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent fooddetail =  new Intent(FoodList.this,FoodDetail.class);
                        fooddetail.putExtra("FoodId", adapter.getRef(position).getKey());
                         startActivity(fooddetail);
                    }
                });

            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item,parent,false);

                return new FoodViewHolder(view);


            }
        };
        Log.d("TAG", "" + adapter.getItemCount());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);

        recyclerView.setLayoutManager(gridLayoutManager);

        adapter.startListening();

        recyclerView.setAdapter(adapter);

    }

}
