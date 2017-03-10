package com.sudaotech.chatlibrary.db;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.sudao.basemodule.basicapi.UserBean;
import com.sudao.basemodule.common.util.GsonHelper;
import com.sudao.basemodule.common.util.SPHelper;
import com.sudaotech.chatlibrary.ChatConstant;
import com.sudaotech.chatlibrary.model.UserInfo;
import com.sudaotech.chatlibrary.utils.ChatUtil;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Samuel on 2016/12/10 11:59
 * Email:xuzhou40@gmail.com
 * desc:用户信息操作
 */

public class UserDao {

    private final Realm mRealm;
    protected Context mContext;
    private long mUserId;


    public UserDao(Context context) {
        mContext = context;
        String userInfoJson = SPHelper.getString(ChatConstant.USER_INFO, "");//获取当前用户资料
        if (userInfoJson.length() > 0) {
            UserBean userBean = GsonHelper.fromJson(userInfoJson, UserBean.class);
            mUserId = userBean.getUserId();
        }
        mRealm = RealmUtil.getInstance(context).getRealm("user_info" + String.valueOf(mUserId) + ".realm");

    }

    /**
     * 查找用户是否存在
     *
     * @param contactId
     * @return
     */
    public boolean queryUser(String contactId) {
        mRealm.beginTransaction();
        UserInfo userInfo = mRealm.where(UserInfo.class)
                .equalTo("contactId", contactId)
                .findFirst();
        mRealm.commitTransaction();

        if (userInfo != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据id取出用户信息
     */
    public UserInfo findUser(String contactId) {
        mRealm.beginTransaction();
        UserInfo userInfo = mRealm.where(UserInfo.class)
                .equalTo("contactId", contactId)
                .findFirst();
        mRealm.commitTransaction();
        return userInfo;
    }

    /**
     * 插入用户资料(先查询判断是否存在)
     *
     * @param userInfo
     */
    public void insertUser(final UserInfo userInfo) {
        if (ChatUtil.isInMainThread()) {
            mRealm.beginTransaction();

            UserInfo queryUserInfo = mRealm.where(UserInfo.class)
                    .equalTo("contactId", userInfo.getContactId())
                    .findFirst();
            if (queryUserInfo == null) {
                mRealm.insertOrUpdate(userInfo);
            }
            mRealm.commitTransaction();
        } else {
            ((AppCompatActivity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRealm.beginTransaction();

                    UserInfo queryUserInfo = mRealm.where(UserInfo.class)
                            .equalTo("contactId", userInfo.getContactId())
                            .findFirst();
                    if (queryUserInfo == null) {
                        mRealm.insertOrUpdate(userInfo);
                    }
                    mRealm.commitTransaction();
                }
            });
        }


    }

    /**
     * 更新用户信息，先删除之前的数据，再重新插入
     *
     * @param userInfo
     */
    public void updateUser(final UserInfo userInfo) {
        if (!queryUser(userInfo.getContactId())) {
            final RealmResults<UserInfo> userInfos = mRealm.where(UserInfo.class)
                    .equalTo("contactId", userInfo.getContactId())
                    .findAll();
            mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    userInfos.deleteAllFromRealm();
                    mRealm.insertOrUpdate(userInfo);
                }
            });
        }


    }

}
