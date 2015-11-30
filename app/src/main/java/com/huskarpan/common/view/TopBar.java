package com.huskarpan.common.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.huskarpan.common.util.DisplayUtil;
import com.huskarpan.timecardpunching.R;

/**
 * Created by Huskar on 2015/7/29.
 *
 * @description 自定义顶部导航栏
 */
public class TopBar extends FrameLayout {
    private ImageButton ibLeft;
    private ImageButton ibRight;
    private TextView tvMiddle;
    private TextView tvLeft;
    private TextView tvRight;
    private int textColor;
    private int background;
    private String leftText;
    private String middleText;
    private String rightText;
    private float textSize;
    private int leftIcon;
    private int rightIcon;
    private ILeftButtonOnClickListener leftButtonOnClickListener;
    private IRightButtonOnClickListener rightButtonOnClickListener;
    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar);
        textColor=ta.getColor(R.styleable.TopBar_textColor, 0);
        background=ta.getColor(R.styleable.TopBar_barBackground, Color.WHITE);
        leftIcon=ta.getResourceId(R.styleable.TopBar_leftButtonIcon, 0);
        rightIcon=ta.getResourceId(R.styleable.TopBar_rightButtonIcon, 0);
        textSize = DisplayUtil.px2sp(context,
                ta.getDimensionPixelOffset(R.styleable.TopBar_textSize, DisplayUtil.sp2px(context, 20))) ;
        leftText = ta.getString(R.styleable.TopBar_leftText);
        middleText = ta.getString(R.styleable.TopBar_middleText);
        rightText = ta.getString(R.styleable.TopBar_rightText);
        ta.recycle();
        View topBar = LayoutInflater.from(context).inflate(R.layout.view_top_bar, this, true);
        //RelativeLayout topBar = (RelativeLayout) View.inflate(context, R.layout.layout_top_bar, null);
        if(background!=0)
            topBar.setBackgroundColor(background);

        ibLeft = (ImageButton) findViewById(R.id.ib_left);
        if (leftIcon != 0) {
            ibLeft.setBackgroundResource(leftIcon);
            ibLeft.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (leftButtonOnClickListener != null)
                        leftButtonOnClickListener.onClick(v);
                }
            });
        }else{
            ibLeft.setVisibility(GONE);
        }

        tvLeft = (TextView) findViewById(R.id.tv_left);
        if (!TextUtils.isEmpty(leftText)) {
            tvLeft.setText(leftText);
            tvLeft.setTextSize(textSize);
            tvLeft.setTextColor(textColor);
        }else{
            tvLeft.setVisibility(GONE);
        }


        ibRight = (ImageButton) findViewById(R.id.ib_right);
        if (rightIcon != 0) {
            ibRight.setBackgroundResource(rightIcon);
            ibRight.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (rightButtonOnClickListener != null)
                        rightButtonOnClickListener.onClick(v);
                }
            });
        }else{
            ibRight.setVisibility(GONE);
        }

        tvRight = (TextView) findViewById(R.id.tv_right);
        if (!TextUtils.isEmpty(rightText)) {
            tvRight.setText(rightText);
            tvRight.setTextSize(textSize);
            tvRight.setTextColor(textColor);
        }else{
            tvRight.setVisibility(GONE);
        }

        tvMiddle = (TextView) findViewById(R.id.tv_middle);
        if(!TextUtils.isEmpty(middleText)){
            tvMiddle.setText(middleText);
            tvMiddle.setTextSize(textSize);
            tvMiddle.setTextColor(textColor);
        }else{
            tvMiddle.setVisibility(GONE);
        }
    }

    /**
     * 回调左边按钮点击事件
     * @param listener 左边按钮点击监听器
     */
    public void setOnLeftButtonOnClickListener(ILeftButtonOnClickListener listener) {
        if (listener != null) {
            this.leftButtonOnClickListener=listener;
        }
    }

    /**
     * 回调右边按钮点击事件
     * @param listener 右边按钮点击监听器
     */
    public void setOnRightButtonOnClickListener(IRightButtonOnClickListener listener) {
        if (listener != null) {
            this.rightButtonOnClickListener=listener;
        }
    }


    /**
     * 左边按钮点击监听器
     */
    public interface ILeftButtonOnClickListener{
        void onClick(View view);
    }

    /**
     * 右边按钮点击监听器
     */
    public interface IRightButtonOnClickListener{
        void onClick(View view);
    }
}
