package com.example.androideatit;

import android.content.Intent;
import android.os.Bundle;

import com.example.androideatit.Interface.ItemClickListener;
import com.example.androideatit.Model.Category;
import com.example.androideatit.Model.Order;
import com.example.androideatit.Viewfolder.MenuViewHolder;
import com.example.androideatit.common.Common;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar.Callback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    FirebaseDatabase database;
    DatabaseReference category;
    TextView txtFullName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);




        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent cartIntent = new Intent(Home.this,Cart.class);
               startActivity(cartIntent);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtFullName = (TextView)headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

       recycler_menu = (RecyclerView)findViewById(R.id.recycler_menu);
       recycler_menu.setHasFixedSize(true);
       layoutManager = new LinearLayoutManager(this);
       recycler_menu.setLayoutManager(layoutManager);

       loadMenu();

    }

    private void loadMenu() {

        FirebaseRecyclerOptions<Category> options =

                new FirebaseRecyclerOptions.Builder<Category>()

                        .setQuery(category, Category.class)

                        .build();



         adapter=new FirebaseRecyclerAdapter<Category,MenuViewHolder>(options) {


            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int i, @NonNull Category model) {

                holder.textMenuView.setText(model.getName());

                        Picasso .get().load(model.getImage()).into(holder.imageView);
                holder.textMenuView.setText(model.getName());

                final Category clickItem=model;

                holder.setItemClickListener(new ItemClickListener() {

                    @Override

                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent foodList =  new Intent(Home.this,FoodList.class);
                         foodList.putExtra("CategoryId", adapter.getRef(position).getKey());

                       Log.d("CategoryId", adapter.getRef(position).getKey());
                        startActivity(foodList);
                    }

                });

            }




            @NonNull

            @Override

            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item,viewGroup,false);

                return new MenuViewHolder(view);

            }

        };

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);

        recycler_menu.setLayoutManager(gridLayoutManager);

        adapter.startListening();

        recycler_menu.setAdapter(adapter);



    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_menu) {
            // Handle the camera action

        } else if (id == R.id.nav_cart) {
            Intent cartintent =  new Intent(Home.this,Cart.class);
            startActivity(cartintent);
        } else if (id == R.id.nav_orders) {
            Intent orderintent =  new Intent(Home.this, OrderStatus.class);
            startActivity(orderintent);

        } else if (id == R.id.nav_log_out) {
            Intent signIn =  new Intent(Home.this,SignIn.class);

           signIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(signIn);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
