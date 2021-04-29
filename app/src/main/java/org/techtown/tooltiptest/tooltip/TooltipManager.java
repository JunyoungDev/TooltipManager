package org.techtown.tooltiptest.tooltip;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;


import org.techtown.tooltiptest.R;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class TooltipManager {
    private static  String prefName = "tooltip";
    private boolean isShow = false;
    static int LAST = 0;
    static int NEXT_BUTTON = 1;
    static int ALL = 2;
    private ArrayList<ViewGroup> layoutList;
    private int statusBarHeight;
    private Activity activity;
    private ArrayList<Target> targetList = new ArrayList<Target>();
    private String activityKey;

    public TooltipManager(Activity activity, String activityKey) {
        this.activity = activity;
        this.activityKey =activityKey;
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        statusBarHeight = rect.top;

        layoutList = new ArrayList<>();

    }

    public void addTarget(View view, String msg){

        int[] pos = new int[2];
        view.getLocationOnScreen(pos);

        int x = pos[0];
        int y = pos[1] - statusBarHeight;
        int width  = view.getWidth();
        int height = view.getHeight();


        targetList.add(new Target(x,y,width,height,msg));
    }
    //여러 컴포넌트를 한번에 설명 할 때
    public void addTarget(View[] views, String msg){
        if (views.length == 0) return;

        View firstView = views[0];

        int[] pos = new int[2];
        firstView.getLocationOnScreen(pos);

        int left = pos[0];
        int top = pos[1] - statusBarHeight;
        int right = left + firstView.getWidth();
        int bottom = top + firstView.getHeight();

        for(View view : views){
            int[] p = new int[2];
            view.getLocationOnScreen(p);
            left = Math.min(left,p[0]);
            top = Math.min(top,p[1]-statusBarHeight);
            right = Math.max(right,p[0]+view.getWidth());
            bottom = Math.max(bottom,p[1]-statusBarHeight + view.getHeight());
        }

        targetList.add(new Target(left,top,right-left,bottom-top,msg));
    }

    //툴팁 show
    public void show(){
        //해당 주석을 제거하면 초기 한번만 표기됩니다.

//        SharedPreferences pref = activity.getSharedPreferences(prefName,MODE_PRIVATE);
//        isShow = pref.getBoolean(activityKey, false) ? true : isShow;


        if(targetList.size() == 0 || isShow) return;
        isShow = true;

        for(int i = 0; i<targetList.size() ; i++){
            Target target = targetList.get(i);
            int buttonIdx = ALL;
            buttonIdx = i == 0 ? NEXT_BUTTON : buttonIdx;
            buttonIdx = i == targetList.size()-1 ? LAST : buttonIdx;
            final ViewGroup layout = TooltipPageService.createPage(activity,target, buttonIdx);
            final int finalI = i;

            //다음
            Button btnNext = layout.findViewById(R.id.nextYear);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ViewManager)layout.getParent()).removeView(layout);
                    showNext(finalI +1);
                    if(finalI == targetList.size()-1){
                        SharedPreferences pref = activity.getSharedPreferences(prefName,MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putBoolean(activityKey, true);
                        editor.commit();
                    }
                }
            });
            //이전
            if(buttonIdx != NEXT_BUTTON){
                Button button = layout.findViewById(R.id.prevYear);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((ViewManager)layout.getParent()).removeView(layout);
                        showNext(finalI -1);
                    }
                });
            }
            layoutList.add(layout);

        }

        activity.addContentView(layoutList.get(0),layoutList.get(0).getLayoutParams());


//        ViewGroup g = TooltipPageService.createPage(activity,targetList.get(0));
//        activity.addContentView(g,g.getLayoutParams());




//        WindowManager d = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
//        WindowManager.LayoutParams wParam = new WindowManager.LayoutParams(
//                WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
//                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH  | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, PixelFormat.TRANSLUCENT
//        );
//        d.addView(g,wParam);

//        AppCompatDialog dialog;
//        dialog = new AppCompatDialog(activity);
//        dialog.setCancelable(false);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//        dialog.setContentView(g);
//
//        dialog.show();
    }

    private void showNext(int idx) {
        if(idx <0 || targetList.size() <= idx) return;

        activity.addContentView(layoutList.get(idx),layoutList.get(idx).getLayoutParams());

    }
}
