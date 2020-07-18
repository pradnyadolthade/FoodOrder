package com.example.androideatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.androideatit.Database.Database;
import com.example.androideatit.Model.Foods;
import com.example.androideatit.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {


    TextView food_description,food_price,food_name;
    ImageView img_food;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String foodId="";

    FirebaseDatabase database;
    DatabaseReference foods;
    Foods currentfoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);


        database = FirebaseDatabase.getInstance();
        foods = database.getReference("Foods");

        numberButton = (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart = (FloatingActionButton)findViewById(R.id.btnCart);
         btnCart.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 new Database(getBaseContext()).addToCart(new Order(
                     foodId,
                      currentfoods.getName(),
                         numberButton.getNumber(),
                         currentfoods.getPrice(),
                         currentfoods.getDiscount()


                 ));
                 Toast.makeText(FoodDetail.this,"Added to cart",Toast.LENGTH_SHORT).show();
             }
         });

        food_description =(TextView)findViewById(R.id.food_description);
        food_name =(TextView)findViewById(R.id.food_name);
        food_price =(TextView)findViewById(R.id.food_price);
        img_food=(ImageView)findViewById(R.id.img_food);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);



        if(getIntent()!=null)
            foodId = getIntent().getStringExtra("FoodId");
        if(!foodId.isEmpty()){
            getDetailsFood(foodId);

        }

    }

    private void getDetailsFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               currentfoods = dataSnapshot.getValue(Foods.class);

                Picasso.get().load(currentfoods.getImage()).into(img_food);
                collapsingToolbarLayout.setTitle(currentfoods.getName());
                food_price.setText(currentfoods.getPrice());
                food_name.setText(currentfoods.getName());
                food_description.setText(currentfoods.getDescription());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
