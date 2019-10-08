package com.conquer.sharp.keyboard.input;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.conquer.sharp.R;
import com.conquer.sharp.keyboard.emoji.EmojiFragment;
import com.conquer.sharp.keyboard.emoji.EmojiParentFragment;
import com.conquer.sharp.util.FragmentUtils;
import com.conquer.sharp.util.StringUtils;

public class InputFragment extends BaseInputFragment {

    private ImageView ivState;
    private EditText inputEditText;

    private int mInputMaxLength = Integer.MAX_VALUE;

    private InputListener mInputListener;

    private EmojiParentFragment mEmojiFragment; // 当前的表情fragment

    private EmojiInputFilter mEmojiFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public View onCreateExtraView() {
        View view = View.inflate(getContext(), R.layout.fragment_input, null);
        ivState = view.findViewById(R.id.iv_state);
        inputEditText = view.findViewById(R.id.et_msg);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initEditText();
        setListeners();
        pushEmojiFragment(EmojiParentFragment.TYPE.EMOJI);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEditText() {
        inputEditText.setText("");
        inputEditText.setFilters(new InputFilter[] {
            new NameLengthFilter(mInputMaxLength)
        });
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    notifyListener(inputEditText.getText().toString());
                }
                return true;
            }
        });
        inputEditText.setFocusable(true);
        inputEditText.setFocusableInTouchMode(true);
        if (Build.VERSION.SDK_INT >= 21) {
            inputEditText.setShowSoftInputOnFocus(false);
        }
        inputEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // 主动显示键盘
                showKeyBoard();
                if (!inputEditText.isCursorVisible()) {
                    resetInput();
                }
            }
            return false;
        });
        ivState.setImageResource(R.drawable.icon_emoji);
    }

    private void setListeners() {
        ivState.setOnClickListener(v -> {
            switch (getKeyboardStatus()) {
                case KEYBOARD:
                    if (!isShowEmojiFragment()) {
                        pushEmojiFragment(EmojiParentFragment.TYPE.EMOJI);
                    }
                    inputEditText.requestFocus();
                    showEmojiBoard();
                    break;
                case EMOTION_BOARD:
                    showKeyBoard();
                    break;
                case NONE:
                    if (!isShowEmojiFragment()) {
                        pushEmojiFragment(EmojiParentFragment.TYPE.EMOJI);
                    }
                    inputEditText.requestFocus();
                    showEmojiBoard();
                    break;
            }
        });
    }

    protected void pushEmojiFragment(EmojiParentFragment.TYPE type) {
        FragmentUtils.pushNested(getChildFragmentManager(), mEmojiFragment = getEmojiFragmentByType(type),
                getKeyboardContainerId());
    }

    @Override
    protected void onKeyboardStatusChanged(KeyboardStatus status) {
        super.onKeyboardStatusChanged(status);
        switch (status) {
            case KEYBOARD:
                ivState.setImageResource(R.drawable.icon_emoji);
                break;
            case EMOTION_BOARD:
                ivState.setImageResource(R.drawable.icon_input);
                break;
            case NONE:
                ivState.setImageResource(R.drawable.icon_emoji);
                break;
        }
    }

    @Override
    protected EditText onGetEditText() {
        return inputEditText;
    }

    private void notifyListener(String msg) {
        if (mInputListener != null) {
            mInputListener.onSend(msg);
            inputEditText.setText("");
        }
    }

    /**
     * 重置为手动输入状态
     */
    private void resetInput() {
        inputEditText.setCursorVisible(true);
        inputEditText.getEditableText().clear();
        inputEditText.setOnLongClickListener(null);
    }

    public void setInputMaxLength(int length) {
        mInputMaxLength = length;
    }

    public void setInputListener(InputListener mListener) {
        mInputListener = mListener;
    }

    public interface InputListener {
        void onSend(String msg);
    }

    public class NameLengthFilter implements InputFilter {
        int MAX_EN; // 最大英文/数字长度/一个汉字算两个字母

        NameLengthFilter(int mAX_EN) {
            super();
            MAX_EN = mAX_EN;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest,
                                   int dStart, int dEnd) {
            int destCount = dest.toString().length() + StringUtils.getChineseCount(dest.toString());
            int sourceCount = source.toString().length() + StringUtils.getChineseCount(source.toString());
            if (destCount + sourceCount > MAX_EN) {
                Toast.makeText(getActivity(), getResources().getString(R.string.input_max_length),
                        Toast.LENGTH_SHORT).show();
                return "";
            } else {
                return source;
            }
        }
    }

    protected boolean isShowEmojiFragment() {
        if (getShowingEmojiFragment() == null) {
            return false;
        }
        return getShowingEmojiFragment().isShowing(EmojiFragment.class);
    }

    public EmojiParentFragment getShowingEmojiFragment() {
        return mEmojiFragment;
    }

    @Override
    public EmojiParentFragment onCreateEmojiParentFragment(EmojiParentFragment.TYPE type) {

        if (type == EmojiParentFragment.TYPE.EMOJI) {
            EmojiParentFragment emojiParentFragment = new EmojiParentFragment().showEmoji();
            return emojiParentFragment.setInputListener(new EmojiParentFragment.OnEmojiListener() {
                @Override
                public void onDeleteClick() {
                    new Thread(() -> {
                        // 模拟按键
                        Instrumentation inst = new Instrumentation();
                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DEL);
                    }).start();
                }

                @Override
                public void onSendClick() {
                    notifyListener(inputEditText.getText().toString());
                }

                @Override
                public boolean onFilter(String emoji) {
                    if (mEmojiFilter != null) {
                        return mEmojiFilter.onFilter(emoji);
                    }
                    return super.onFilter(emoji);
                }

                @Override
                public void onInput(CharSequence sequence) {
                    if (inputEditText != null) {
                        inputEditText.getText().insert(inputEditText.getSelectionStart(), sequence);
                    }
                }
            });
        }
        return null;
    }

    /**
     * emoji输入过滤
     */
    public interface EmojiInputFilter {
        /**
         * @param emoji 输入的emoji unicode
         * @return 是否需要展示到输入框
         */
        boolean onFilter(String emoji);
    }
}
