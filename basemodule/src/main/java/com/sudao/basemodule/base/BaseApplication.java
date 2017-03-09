package com.sudao.basemodule.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.lzy.ninegrid.NineGridView;
import com.sudao.basemodule.common.util.ImageHelper;
import com.sudao.basemodule.common.util.LogUtil;
import com.umeng.socialize.PlatformConfig;

import java.util.Iterator;
import java.util.List;

import io.realm.Realm;

import static com.hyphenate.chat.EMGCMListenerService.TAG;


/**
 * Created by Samuel on 5/25/16 17:44
 * Email:xuzhou40@gmail.com
 * desc:
 */
public class BaseApplication extends Application {
    public static BaseApplication instance;

    public static Context getContext() {
        return instance;
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化友盟
        initUmeng();
        initEase();
        initRealm();
        initNineGridView();
    }

    private void initNineGridView() {
        NineGridView.setImageLoader(new NineGridView.ImageLoader() {
            @Override
            public void onDisplayImage(Context context, ImageView imageView, String url) {
                ImageHelper.load(context, url, imageView);
            }

            @Override
            public Bitmap getCacheImage(String url) {
                return null;
            }
        });
    }

    private void initRealm() {
        Realm.init(this);
    }

    private void initEase() {

        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
// 如果APP启用了远程的service，此application:onCreate会被调用2次
// 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
// 默认的app会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null || !processAppName.equalsIgnoreCase("com.sudaotech.ecoc")) {
            LogUtil.e(TAG, "enter the service process!");
            //"com.easemob.chatuidemo"为demo的包名，换到自己项目中要改成自己包名

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        options.setAutoLogin(true);
        options.setMipushConfig(BundleConst.XIAOMI_APP_ID, BundleConst.XIAOMI_APP_KEY);
        options.setHuaweiPushAppId(BundleConst.HUAWEI_APP_ID);
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(false);


    }

    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    // Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
                    // info.processName +"  Label: "+c.toString());
                    // processName = c.toString();
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }

    private void initUmeng() {
        //微信 appid appsecret
        PlatformConfig.setWeixin(BundleConst.WECHAT_APP_ID, BundleConst.WECHAT_APP_SECRET);
        //新浪微博 appkey appsecret
//        PlatformConfig.setSinaWeibo(BundleConst.WEIBO_APP_KEY, BundleConst.WEIBO_APP_SECRET);
        // QQ和Qzone appid appkey
//        PlatformConfig.setQQZone(BundleConst.QQ_APP_ID, BundleConst.QQ_APP_KEY);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
