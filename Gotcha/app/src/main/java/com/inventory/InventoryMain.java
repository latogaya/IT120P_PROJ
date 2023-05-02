package com.inventory;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import com.chatfunction.ChatMainActivity;
import com.example.products.ProductDetailActivity;
import com.example.products.ProfilePageActivity;
import com.example.products.R;
import com.example.products.SellerHomeActivity;
import com.example.products.SellerProductDetailActivity;
import com.seller.inventory.Product;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InventoryMain extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_PRODUCT = 1;

    private ListView mProductsListView;
    private List<Product> mProducts;
    private ProductAdapter mProductAdapter;

    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        mProductsListView = findViewById(R.id.productsListView);
        mProducts = new ArrayList<>();
        mProductAdapter = new ProductAdapter(this, R.layout.inventory_recycler_view, mProducts);
        mProductsListView.setAdapter(mProductAdapter);
        Product newProduct = new Product("of", "example", 593, "new", "adding");
        mProducts.add(newProduct);
        Product newProduct6 = new Product("DBTK", "DBTK OG", 25.0, "Shirt", "Large");
        mProducts.add(newProduct6);
        Product newProduct1 = new Product("DBTK", "DBTK MOON", 12.0, "Shirt", "Small");
        mProducts.add(newProduct1);
        Product newProduct2 = new Product("DBTK", "DBTK SHORTS", 10.0, "Shorts", "Medium");
        mProducts.add(newProduct2);
        Product newProduct3 = new Product("Zalu", "Zalu OG", 40.0, "Shirt", "Large");
        mProducts.add(newProduct3);
        Product newProduct4 = new Product("Ambher", "Ambher OG", 35.0, "Shorts", "Small");
        mProducts.add(newProduct4);
        Product newProduct5 = new Product("Bastion", "Bastion OG", 25.0, "Shorts", "Medium");
        mProducts.add(newProduct5);


        // Initialize the SearchView variable
        mSearchView = findViewById(R.id.search_view);

        ImageButton notifbutton = findViewById(R.id.notif);
        notifbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view){
                Alerter alerter = Alerter.create(InventoryMain.this)
                        .setText("No New Notifications")
                        .setBackgroundColorRes(R.color.black)
                        .setIcon(R.drawable.notif_yellowlogo)
                        .setIconColorFilter(0)
                        .setDuration(5000)
                        .setEnterAnimation(com.tapadoo.alerter.R.anim.alerter_slide_in_from_left)
                        .setExitAnimation(com.tapadoo.alerter.R.anim.alerter_slide_out_to_right)
                        .enableSwipeToDismiss()
                        .setTextAppearance(R.style.CustomAlerterTextAppearance)
                        .setTitleAppearance(R.style.CustomAlerterTextAppearance)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                View alerterView = alerter.show();
            }});

        // Set a query listener on the SearchView to handle search queries
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // This method is called when the user submits the search query
                // Here you can handle the search query and show the search results
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // This method is called when the user types or changes the search query
                // Here you can filter the list based on the search query
                filterProducts(newText);
                return true;
            }
        });
        Button sortButton = findViewById(R.id.sortButton);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortProducts();
            }
        });

        Button chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryMain.this, ChatMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryMain.this, SellerHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryMain.this, ProfilePageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button inventory = findViewById(R.id.inventory);
        inventory.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                showAlert("You're already at Inventory");
            }
        });

        // Find the remove button in the layout
        Button removeButton = findViewById(R.id.deleteButton);

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected item position in the ListView
                int position = mProductsListView.getCheckedItemPosition();
                if (position != ListView.INVALID_POSITION) {
                    // Remove the item from the List and update the adapter
                    if (TextUtils.isEmpty(mSearchView.getQuery())) {
                        // If the query is empty, remove the item from the original list
                        mProducts.remove(position);
                        showAlert("Product Successfully Removed");
                    } else {
                        // If the query is not empty, remove the item from the filtered list
                        Product selectedProduct = (Product) mProductAdapter.getItem(position);
                        mProducts.remove(selectedProduct);

                    }
                    mProductAdapter.notifyDataSetChanged();
                }
            }
        });

        // Set an item click listener on the ListView to handle item selection
        mProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Set the item as checked in the ListView
                mProductsListView.setItemChecked(position, true);
            }
        });

        // Find the transaction button in the layout
        Button transactionButton = findViewById(R.id.transactionButton);

        // Set a click listener on the transaction button
        transactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the selected item position in the ListView
                int position = mProductsListView.getCheckedItemPosition();
                if (position != ListView.INVALID_POSITION) {
                    // Get the selected product from the List
                    Product selectedProduct = mProducts.get(position);
                    // Start the TransactionDetails activity with the selected product
                    Intent intent = new Intent(InventoryMain.this, SellerProductDetailActivity.class);
                    intent.putExtra("product", selectedProduct);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_PRODUCT && resultCode == RESULT_OK) {
            // Get the new product from the Intent
            Product newProduct = data.getParcelableExtra("product");

            // Add the new product to the list and update the adapter
            mProducts.add(newProduct);
            mProductAdapter.notifyDataSetChanged();
        }
    }

    private void sortProducts() {
        // Sort the products list by name using a Comparator
        showAlert("Products Now Sorted Alphabetically");
        Collections.sort(mProducts, new Comparator<Product>() {
            @Override
            public int compare(Product product1, Product product2) {
                return product1.getName().compareToIgnoreCase(product2.getName());
            }
        });

        // Notify the adapter that the data set has changed
        mProductAdapter.notifyDataSetChanged();
    }

    private void showAlert(String text) {
        Alerter alerter = Alerter.create(this)
                .setTextAppearance(R.style.CustomAlerterTextAppearance)
                .setTitleAppearance(R.style.CustomAlerterTextAppearance)
                .setText(text)
                .setBackgroundColorRes(R.color.black)
                .setIcon(R.drawable.notif_yellowlogo)
                .setIconColorFilter(0)
                .setDuration(5000)
                .setEnterAnimation(com.tapadoo.alerter.R.anim.alerter_slide_in_from_left)
                .setExitAnimation(com.tapadoo.alerter.R.anim.alerter_slide_out_to_right)
                .enableSwipeToDismiss()
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
        alerter.show();
    }

    private void filterProducts(String query) {
        List<Product> filteredProducts = new ArrayList<>();

        for (Product product : mProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase()) ||
                    product.getmBrand().toLowerCase().contains(query.toLowerCase()) ||
                    product.getSize().toLowerCase().contains(query.toLowerCase()) ||
                    product.getType().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        // Sort the filtered products list by name using a lambda expression
        //filteredProducts.sort((product1, product2) -> product1.getName().compareToIgnoreCase(product2.getName()));
        Collections.sort(filteredProducts, (product1, product2) -> product1.getName().compareToIgnoreCase(product2.getName()));
        // Update the adapter with the filtered products list, or show all products if the query is empty
        if (TextUtils.isEmpty(query)) {
            mProductAdapter = new ProductAdapter(this, R.layout.inventory_recycler_view, mProducts);
        } else {
            mProductAdapter = new ProductAdapter(this, R.layout.inventory_recycler_view, filteredProducts);
        }
        mProductsListView.setAdapter(mProductAdapter);
    }

}

