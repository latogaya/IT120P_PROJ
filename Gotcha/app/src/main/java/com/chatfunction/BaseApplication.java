package com.chatfunction;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sendbird.android.exception.SendbirdException;
import com.sendbird.android.handler.InitResultHandler;
import com.sendbird.uikit.SendbirdUIKit;
import com.sendbird.uikit.adapter.SendbirdUIKitAdapter;
import com.sendbird.uikit.interfaces.UserInfo;

public class BaseApplication extends Application {

    String userId, userNickname;
    final String APP_ID = "E891A124-3A8C-42BE-AE2F-9ECD41C4AB0E";

    @Override
    public void onCreate() {
        super.onCreate();

        SendbirdUIKit.init(new SendbirdUIKitAdapter() {
            @Override
            public String getAppId() { return APP_ID; }

            @Override
            public String getAccessToken() { return "null"; }

            @Override
            public UserInfo getUserInfo() {
                return new UserInfo() {
                    @Override
                    public String getUserId() { return userId; }

                    @Nullable
                    @Override
                    public String getNickname() { return userNickname; }

                    @Nullable
                    @Override
                    public String getProfileUrl() { return ""; }
                };
            }

            @NonNull
            @Override
            public InitResultHandler getInitResultHandler() {
                return new InitResultHandler() {
                    @Override
                    public void onMigrationStarted() {
                        // DB migration has started.
                    }

                    @Override
                    public void onInitFailed(SendbirdException e) {
                        // If DB migration fails, this method is called.
                    }

                    @Override
                    public void onInitSucceed() {
                        // If DB migration is successful, this method is called and you can proceed to the next step.
                        // In the sample app, the `LiveData` class notifies you on the initialization progress
                        // And observes the `MutableLiveData<InitState> initState` value in `SplashActivity()`.
                        // If successful, the `LoginActivity` screen
                        // Or the `HomeActivity` screen will show.
                    }
                };
            }
        }, this);
    }
    public void setUserId(String userId){ this.userId = userId; }
}
