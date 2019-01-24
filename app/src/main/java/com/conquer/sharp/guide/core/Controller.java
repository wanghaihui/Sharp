package com.conquer.sharp.guide.core;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.conquer.sharp.guide.lifecycle.FragmentLifecycleAdapter;
import com.conquer.sharp.guide.lifecycle.ListenerFragment;
import com.conquer.sharp.guide.lifecycle.V4ListenerFragment;
import com.conquer.sharp.guide.listener.OnGuideChangedListener;
import com.conquer.sharp.guide.listener.OnPageChangedListener;

import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.List;

/**
 * 实际控制器
 */
public class Controller {
    private static final String LISTENER_FRAGMENT = "listener_fragment";

    private Activity activity;
    private Fragment fragment;
    private android.support.v4.app.Fragment v4Fragment;
    private String label;
    private int showCounts;
    private boolean alwaysShow;
    private OnGuideChangedListener onGuideChangedListener;
    private OnPageChangedListener onPageChangedListener;
    private List<GuidePage> guidePages;

    private FrameLayout mParentView;
    private int indexOfChild = -1; // 使用anchor时记录的在父布局的位置
    private SharedPreferences sp;
    private boolean isShowing;
    private int current; // 当前页
    private GuideLayout currentLayout;

    public Controller(Guide.Builder builder) {
        activity = builder.activity;
        fragment = builder.fragment;
        v4Fragment = builder.v4Fragment;
        label = builder.label;
        showCounts = builder.showCounts;
        alwaysShow = builder.alwaysShow;
        onGuideChangedListener = builder.onGuideChangedListener;
        onPageChangedListener = builder.onPageChangedListener;
        guidePages = builder.guidePages;

        View anchor = builder.anchor;
        if (anchor == null) {
            anchor = activity.findViewById(android.R.id.content);
        }

        if (anchor instanceof FrameLayout) {
            mParentView = (FrameLayout) anchor;
        } else {
            FrameLayout frameLayout = new FrameLayout(activity);

            ViewGroup parent = (ViewGroup) anchor.getParent();
            indexOfChild = parent.indexOfChild(anchor);
            parent.removeView(anchor);
            if (indexOfChild >= 0) {
                parent.addView(frameLayout, indexOfChild, anchor.getLayoutParams());
            } else {
                parent.addView(frameLayout, anchor.getLayoutParams());
            }

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            frameLayout.addView(anchor, layoutParams);

            mParentView = frameLayout;
        }

        sp = activity.getSharedPreferences(Guide.TAG, Activity.MODE_PRIVATE);
    }

    public void show() {
        final int showedCount = sp.getInt(label, 0);
        if (!alwaysShow) {
            if (showedCount >= showCounts) {
                return;
            }
        }

        if (isShowing) {
            return;
        }

        isShowing = true;

        mParentView.post(new Runnable() {
            @Override
            public void run() {
                if (guidePages == null || guidePages.size() == 0) {
                    throw new IllegalStateException("there is no guide to show!! Please add at least one Page.");
                }

                current = 0;
                showGuidePage();
                if (onGuideChangedListener != null) {
                    onGuideChangedListener.onShow(Controller.this);
                }
                addListenerFragment();
                sp.edit().putInt(label, showedCount + 1).apply();
            }
        });
    }

    public void showPage(int position) {
        if (position < 0 || position > guidePages.size() - 1) {
            throw new InvalidParameterException("The Guide page position is out of range. current:"
                    + position + ", range: [ 0, " + guidePages.size() + " )");
        }
        if (current == position) {
            return;
        }
        current = position;
        //fix #59 GuideLayout.setOnGuideLayoutDismissListener() on a null object reference
        if (currentLayout != null) {
            currentLayout.setOnGuideLayoutDismissListener(new GuideLayout.OnGuideLayoutDismissListener() {
                @Override
                public void onGuideLayoutDismiss(GuideLayout guideLayout) {
                    showGuidePage();
                }
            });
            currentLayout.remove();
        } else {
            showGuidePage();
        }
    }

    public void showPreviewPage() {
        showPage(--current);
    }

    private void showGuidePage() {
        GuidePage page = guidePages.get(current);
        GuideLayout guideLayout = new GuideLayout(activity, page, this);
        guideLayout.setOnGuideLayoutDismissListener(new GuideLayout.OnGuideLayoutDismissListener() {
            @Override
            public void onGuideLayoutDismiss(GuideLayout guideLayout) {
                showNextOrRemove();
            }
        });

        mParentView.addView(guideLayout, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        currentLayout = guideLayout;
        if (onPageChangedListener != null) {
            onPageChangedListener.onPageChanged(current);
        }
        isShowing = true;
    }

    private void showNextOrRemove() {
        if (current < guidePages.size() - 1) {
            current++;
            showGuidePage();
        } else {
            if (onGuideChangedListener != null) {
                onGuideChangedListener.onRemoved(Controller.this);
            }
            removeListenerFragment();
            isShowing = false;
        }
    }

    public void resetLabel() {
        resetLabel(label);
    }

    public void resetLabel(String label) {
        sp.edit().putInt(label, 0).apply();
    }

    /**
     * 中断引导层的显示，后续未显示的page将不再显示
     */
    public void remove() {
        if (currentLayout != null && currentLayout.getParent() != null) {
            ViewGroup parent = (ViewGroup) currentLayout.getParent();
            parent.removeView(currentLayout);
            // 移除anchor添加的frameLayout
            if (!(parent instanceof FrameLayout)) {
                ViewGroup original = (ViewGroup) parent.getParent();
                View anchor = parent.getChildAt(0);
                parent.removeAllViews();
                if (anchor != null) {
                    if (indexOfChild > 0) {
                        original.addView(anchor, indexOfChild, parent.getLayoutParams());
                    } else {
                        original.addView(anchor, parent.getLayoutParams());
                    }
                }
            }
            if (onGuideChangedListener != null) {
                onGuideChangedListener.onRemoved(this);
            }
            currentLayout = null;
        }
        isShowing = false;
    }

    public boolean isShowing() {
        return isShowing;
    }

    private void addListenerFragment() {
        // fragment监听销毁界面关闭引导层
        if (fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            compatibleFragment(fragment);

            FragmentManager fm = fragment.getChildFragmentManager();
            ListenerFragment listenerFragment = (ListenerFragment) fm.findFragmentByTag(LISTENER_FRAGMENT);
            if (listenerFragment == null) {
                listenerFragment = new ListenerFragment();
                fm.beginTransaction().add(listenerFragment, LISTENER_FRAGMENT).commitAllowingStateLoss();
            }
            listenerFragment.setFragmentLifecycle(new FragmentLifecycleAdapter() {
                @Override
                public void onDestroyView() {
                    remove();
                }
            });
        }

        if (v4Fragment != null) {
            android.support.v4.app.FragmentManager v4Fm = v4Fragment.getChildFragmentManager();
            V4ListenerFragment v4ListenerFragment = (V4ListenerFragment) v4Fm.findFragmentByTag(LISTENER_FRAGMENT);
            if (v4ListenerFragment == null) {
                v4ListenerFragment = new V4ListenerFragment();
                v4Fm.beginTransaction().add(v4ListenerFragment, LISTENER_FRAGMENT).commitAllowingStateLoss();
            }
            v4ListenerFragment.setFragmentLifecycle(new FragmentLifecycleAdapter() {
                @Override
                public void onDestroyView() {
                    remove();
                }
            });
        }
    }

    private void removeListenerFragment() {
        // 隐藏引导层时移除监听fragment
        if (fragment != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
            FragmentManager fm = fragment.getChildFragmentManager();
            ListenerFragment listenerFragment = (ListenerFragment) fm.findFragmentByTag(LISTENER_FRAGMENT);
            if (listenerFragment != null) {
                fm.beginTransaction().remove(listenerFragment).commitAllowingStateLoss();
            }
        }
        if (v4Fragment != null) {
            android.support.v4.app.FragmentManager v4Fm = v4Fragment.getChildFragmentManager();
            V4ListenerFragment v4ListenerFragment = (V4ListenerFragment) v4Fm.findFragmentByTag(LISTENER_FRAGMENT);
            if (v4ListenerFragment != null) {
                v4Fm.beginTransaction().remove(v4ListenerFragment).commitAllowingStateLoss();
            }
        }
    }

    /**
     * For bug of Fragment in Android
     * https://issuetracker.google.com/issues/36963722
     *
     * @param fragment
     */
    private void compatibleFragment(Fragment fragment) {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(fragment, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
