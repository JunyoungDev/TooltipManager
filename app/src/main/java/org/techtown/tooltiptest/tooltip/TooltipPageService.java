package org.techtown.tooltiptest.tooltip;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import org.techtown.tooltiptest.R;

public class TooltipPageService {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static LinearLayout createPage(Activity activity, Target target, int buttonIdx){
        LinearLayout layout = new LinearLayout(activity);

//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height =WindowManager.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams lp  = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setClickable(true);
//        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
//        lp.height =WindowManager.LayoutParams.MATCH_PARENT;

        layout.setBackgroundColor(Color.argb(0,0,0,0));
        layout.setLayoutParams(lp);
        layout.setOrientation(LinearLayout.VERTICAL);


        LinearLayout topLayout = new LinearLayout(activity);
        LinearLayout mainLayout = new LinearLayout(activity);
        LinearLayout bottomLayout = new LinearLayout(activity);

        topLayout.setOrientation(LinearLayout.VERTICAL);
        bottomLayout.setOrientation(LinearLayout.VERTICAL);

        mainLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams paramTop = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramMain =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams paramBottom =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        paramTop.height = target.y;
        paramBottom.height = 0;
        paramBottom.weight = 1;
        paramMain.height = target.height;



        topLayout.setLayoutParams(paramTop);
        mainLayout.setLayoutParams(paramMain);
        bottomLayout.setLayoutParams(paramBottom);

        topLayout.setBackgroundColor(Color.argb(200,0,0,0));
        bottomLayout.setBackgroundColor(Color.argb(200,0,0,0));

        LinearLayout mainLeft = new LinearLayout(activity);
        LinearLayout mainCenter = new LinearLayout(activity);
        LinearLayout mainRight = new LinearLayout(activity);

        LinearLayout.LayoutParams paramLeft = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams paramCenter =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams paramRight =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        paramLeft.width = target.x;
        paramCenter.width = target.width;
        paramRight.width = 0;
        paramRight.weight = 1;

        mainLeft.setLayoutParams(paramLeft);
        mainCenter.setLayoutParams(paramCenter);
        mainRight.setLayoutParams(paramRight);

        mainLeft.setBackgroundColor(Color.argb(200,0,0,0));
        mainRight.setBackgroundColor(Color.argb(200,0,0,0));
        mainCenter.setBackgroundColor(Color.argb(0,0,0,0));

        mainLayout.addView(mainLeft);
        mainLayout.addView(mainCenter);
        mainLayout.addView(mainRight);


        layout.addView(topLayout);
        layout.addView(mainLayout);
        layout.addView(bottomLayout);

        topLayout.setGravity(Gravity.CENTER);
        bottomLayout.setGravity(Gravity.CENTER);
        //텍스트 추가
        TextView textView = new TextView(activity);
        textView.setGravity(Gravity.CENTER);
        textView.setText(target.msg);
        textView.setTextSize(20);
        textView.setTextColor(Color.WHITE);

        //다음 버튼 추가
        LinearLayout buttonArea = new LinearLayout(activity);
        LinearLayout.LayoutParams btnAreaParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        btnAreaParam.topMargin = 10;
        buttonArea.setLayoutParams(btnAreaParam);
        buttonArea.setOrientation(LinearLayout.HORIZONTAL);
        Button btnPrev = new Button(activity);
        Button btnNext = new Button(activity);
        View spaceView = new View(activity);
        spaceView.setLayoutParams(new LinearLayout.LayoutParams(10, 0));
        btnPrev.setBackground(activity.getDrawable(R.drawable.main_foot_box));
        btnNext.setBackground(activity.getDrawable(R.drawable.main_foot_box));


        btnPrev.setId(R.id.prevYear);
        btnNext.setId(R.id.nextYear);
        spaceView.setBackgroundColor(Color.BLUE);

        btnPrev.setText("이전");
        btnNext.setText("다음");

        if(buttonIdx != TooltipManager.NEXT_BUTTON) {
            buttonArea.addView(btnPrev);
            buttonArea.addView(spaceView);
        }
        if(buttonIdx == TooltipManager.LAST) {
            btnNext.setText("확인");
            btnNext.setBackground(activity.getDrawable(R.drawable.distress_box));
        }
        buttonArea.addView(btnNext);




        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int middle = (rect.bottom + rect.top) / 2;

        if(middle < (target.y +target.height/2)) {
            topLayout.addView(textView);
            topLayout.addView(buttonArea);

        }
        else {
            bottomLayout.addView(textView);
            bottomLayout.addView(buttonArea);
        }

        return layout;
    }
}
