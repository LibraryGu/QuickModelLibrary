package com.icrane.quickmode.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.icrane.quickmode.R;
import com.icrane.quickmode.utils.common.CommonUtils;

public class LimitedEditText extends LinearLayout implements TextWatcher {

    public static final String ATTR_NAME_TEXT_NUM = "textNum";
    public static final String ATTR_NAME_TEXT_APPEARENCE = "textAppearance";
    public static final String ATTR_NAME_HINT = "hint";

    private static final int DEFAULT_TEXT_NUM = 140;
    private static final String DEFAULT_TEXT_NUM_FORMAT = "%1$s/"
            + DEFAULT_TEXT_NUM;

    private String textNumFormat = DEFAULT_TEXT_NUM_FORMAT;

    private EditText editText;
    private TextView textNumText;

    private TextWatcher watcher;

    private int textNum;

    public LimitedEditText(Context context) {
        this(context, null);
    }

    public LimitedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setOrientation(LinearLayout.VERTICAL);
        Resources resources = getResources();

        LayoutParams params;
        editText = new EditText(context);
        editText.setBackgroundResource(R.mipmap.bg_text_bar_n_9);
        editText.setGravity(Gravity.TOP);
        int padding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 5, resources.getDisplayMetrics());
        editText.setPadding(padding, padding, padding, padding);
        editText.addTextChangedListener(this);
        textNum = attrs.getAttributeIntValue(null, ATTR_NAME_TEXT_NUM,
                DEFAULT_TEXT_NUM);
        textNumFormat = "%1$s/" + textNum;
        editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                textNum)});

        int textAppearence = attrs.getAttributeResourceValue(null,
                ATTR_NAME_TEXT_APPEARENCE, R.style.TextStyle_LimitedEditText);
        editText.setTextAppearance(context, textAppearence);

        int hint = attrs.getAttributeResourceValue(null, ATTR_NAME_HINT, 0);
        if (0 != hint) {
            editText.setHint(hint);
        }

        params = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                resources
                        .getDimensionPixelSize(R.dimen.limited_edit_text_height));
        addView(editText, params);

        textNumText = new TextView(context);
        textNumText.setText(String.format(textNumFormat, 0));
        textNumText.setTextColor(resources.getColor(R.color.limited_text_num));
        textNumText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        params = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.RIGHT;
        addView(textNumText, params);

    }

    public void setText(String text) {
        editText.setText(text);
    }

    public String getText() {
        return editText.getText().toString();
    }

    /**
     * 设置TextChangedListener监听器
     *
     * @param watcher TextWatcher对象
     */
    public void setTextChangedListener(TextWatcher watcher) {
        this.watcher = watcher;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        if (watcher != null) {
            watcher.beforeTextChanged(s, start, count, after);
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (watcher != null) {
            watcher.onTextChanged(s, start, count, count);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (watcher != null) {
            watcher.afterTextChanged(s);
        }

        int num;
        if (CommonUtils.matchesChinese(s.toString())) {
            num = textNum / 2;
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    num)});
            if (s.length() > num) {
                s.delete(0, s.length() - num);
            }
        } else {
            num = textNum;
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                    num)});
        }

        textNumFormat = "%1$s/" + num;
        textNumText.setText(String.format(textNumFormat, s.length()));
    }

}
