package com.conquer.sharp.guide.lifecycle;

import android.app.Fragment;
import android.util.Log;

import com.conquer.sharp.guide.core.Guide;

public class ListenerFragment extends Fragment {

    FragmentLifecycle mFragmentLifecycle;

    public void setFragmentLifecycle(FragmentLifecycle lifecycle) {
        mFragmentLifecycle = lifecycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(Guide.TAG, "ListenerFragment onStart");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onStart();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(Guide.TAG, "ListenerFragment onStop");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onStop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(Guide.TAG, "ListenerFragment onDestroyView");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onDestroyView();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Guide.TAG, "ListenerFragment onDestroy");
        if (mFragmentLifecycle != null) {
            mFragmentLifecycle.onDestroy();
        }
    }
}
