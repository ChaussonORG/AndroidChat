package com.sudao.basemodule.common.util;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.sudao.basemodule.R;

/**
 * Created by Samuel on 5/15/16 19:09
 * Email:xuzhou40@gmail.com
 * desc:toolbar 工具类
 */
public class ToolbarUtil {
    //没有返回箭头
    public static void setupHomeToolbar(Context context, Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
    }

    //有返回箭头
    public static void setupToolbar(Context context, Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static void setupToolbar(Context context, Toolbar toolbar, int resId) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(resId);
        }
    }

    public static void setupToolbarNoTitle(Context context, Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        appCompatActivity.setSupportActionBar(toolbar);
    }

    //e商会专用-白色返回箭头
    public static void setupToolbarWhite(Context context, Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_white);

        }
    }

    //e商会专用-红色返回箭头
    public static void setupToolbarRed(Context context, Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_arrow_red);

        }
    }

    //e商会专用-红色叉返回箭头(主要用于发布)
    public static void setupToolbarBackIcon(Context context, Toolbar toolbar) {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context;
        appCompatActivity.setSupportActionBar(toolbar);
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back_cancel);

        }
    }
}
