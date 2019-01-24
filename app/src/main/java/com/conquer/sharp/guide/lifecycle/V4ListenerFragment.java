package com.conquer.sharp.guide.lifecycle;

import android.support.v4.app.Fragment;
import android.util.Log;

import com.conquer.sharp.guide.core.Guide;

public class V4ListenerFragment extends Fragment {

    FragmentLifecycle mFragmentLifecycle;

    public void setFragmentLifecycle(FragmentLifecycle lifecycle) {
        mFragmentLifecycle = lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Guide.TAG, "V4ListenerFragment onStart");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Guide.TAG, "V4ListenerFragment onStop");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(Guide.TAG, "V4ListenerFragment onDestroyView");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Guide.TAG, "V4ListenerFragment onDestroy");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onDestroy();
        }
    }
}
