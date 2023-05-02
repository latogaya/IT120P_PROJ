package com.chatfunction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.products.MainActivity;
import com.example.products.ProfilePageActivity;
import com.example.products.R;
import com.example.products.SellerHomeActivity;
import com.orders.OrderMAIN;
import com.sendbird.uikit.fragments.ChannelListFragment;
import com.tapadoo.alerter.Alerter;


public class ChatMainActivity extends AppCompatActivity {

    Button home, chat, orders, profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);
        //==========================================================================================
        //NAV BAR
        home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GotoHome();
            }
        });
        chat = findViewById(R.id.chat);
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlert("You're already at chat");
            }
        });

        orders = findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMainActivity.this, OrderMAIN.class);
                startActivity(intent);
                finish();
            }
        });
        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMainActivity.this, ProfilePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //==========================================================================================

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment chatFragment = getChatFragment();

        fragmentTransaction.add(R.id.frameLayout, chatFragment);
        fragmentTransaction.commit();
    }
    private Fragment getChatFragment(){
        ChannelListFragment.Builder fragment = new ChannelListFragment.Builder().setUseHeader(true);
        return fragment.build();
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

    public void GotoHome(){
        Intent newIntent = new Intent(this, MainActivity.class);
        startActivity(newIntent);
        finish();
    }

    public void GotoSellerHome(){
        Intent newIntent = new Intent(this, SellerHomeActivity.class);
        startActivity(newIntent);
        finish();
    }

}