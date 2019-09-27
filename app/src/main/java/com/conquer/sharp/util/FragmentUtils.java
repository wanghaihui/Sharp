package com.conquer.sharp.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {

    public static void replace(FragmentManager manager, Fragment fragment, int rootResId) {
        if (fragment == null) {
            throw new RuntimeException("add a null fragment");
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(rootResId, fragment);
        ft.commitAllowingStateLoss();
        if (fragment.getFragmentManager() != null) {
            // 立即执行
            fragment.getFragmentManager().executePendingTransactions();
        }
    }

    public static void add(FragmentManager manager, Fragment fragment, int rootResId) {
        if (fragment == null) {
            throw new RuntimeException("add a null fragment");
        }
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(rootResId, fragment);
        ft.commitAllowingStateLoss();
        if (fragment.getFragmentManager() != null) {
            // 立即执行
            fragment.getFragmentManager().executePendingTransactions();
        }
    }

    public static void hide(FragmentManager manager, Fragment fragment) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.hide(fragment);
        ft.commitAllowingStateLoss();
        if (fragment.getFragmentManager() != null) {
            // 立即执行
            fragment.getFragmentManager().executePendingTransactions();
        }
    }

}
