package com.flyoutsgroup.flyouts;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Flyer.class);
        ParseObject.registerSubclass(Item.class);
        ParseObject.registerSubclass(Post.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("HxhqeNgKWrYANxApmYDohGN27PBdhcAILo1Qoxxt")
                .clientKey("dIUdVwEVqXDS7kO1p11ktGT6LNNaEpWRsRtU97rM")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
