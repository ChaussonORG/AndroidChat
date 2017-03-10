package com.sudaotech.chatlibrary.db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Samuel on 2016/12/9 15:58
 * Email:xuzhou40@gmail.com
 * desc:
 */

public class RealmUtil {
    private static RealmUtil sInstance;
    public final Context mContext;
    private String realmName = "chat.realm";

    private RealmUtil(Context context) {
        mContext = context;
    }

    public static RealmUtil getInstance(Context context) {
        if (sInstance == null) {
            synchronized ((RealmUtil.class)) {
                if (sInstance == null) {
                    sInstance = new RealmUtil(context);
                }
            }
        }
        return sInstance;
    }

    public Realm getRealm(String realmName) {
        Realm realm = Realm.getInstance(new RealmConfiguration.Builder()
                .name(realmName)
                .deleteRealmIfMigrationNeeded()
                .build());
        return realm;
    }
}
