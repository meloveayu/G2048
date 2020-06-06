package com.example.g2048;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment {
    private LinearLayout mRootLayout;
    private TextView mTextViewScore;
    private TextView[][] mTextViews= new TextView[4][4];
    private Game mGame = new Game();
    private Button mButtonGoBack;
    private Button mButtonRestart;
    private float x = 0;
    private float y = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game,container,false);
        //绑定16个TextView
        mTextViews[0][0] = view.findViewById(R.id.bt1);
        mTextViews[0][1] = view.findViewById(R.id.bt2);
        mTextViews[0][2] = view.findViewById(R.id.bt3);
        mTextViews[0][3] = view.findViewById(R.id.bt4);
        mTextViews[1][0] = view.findViewById(R.id.bt5);
        mTextViews[1][1] = view.findViewById(R.id.bt6);
        mTextViews[1][2] = view.findViewById(R.id.bt7);
        mTextViews[1][3] = view.findViewById(R.id.bt8);
        mTextViews[2][0] = view.findViewById(R.id.bt9);
        mTextViews[2][1] = view.findViewById(R.id.bt10);
        mTextViews[2][2] = view.findViewById(R.id.bt11);
        mTextViews[2][3] = view.findViewById(R.id.bt12);
        mTextViews[3][0] = view.findViewById(R.id.bt13);
        mTextViews[3][1] = view.findViewById(R.id.bt14);
        mTextViews[3][2] = view.findViewById(R.id.bt15);
        mTextViews[3][3] = view.findViewById(R.id.bt16);
        //绑定两个Button
        mButtonGoBack = view.findViewById(R.id.button_goback);
        mButtonRestart = view.findViewById(R.id.button_restart);
        //绑定显示分数的TextView
        mTextViewScore = view.findViewById(R.id.textview_score);
        //绑定整个布局，因为滑动操作是针对整个界面进行的，没有特定的按键
        mRootLayout = view.findViewById(R.id.root_view);
        //返回上一步的监听器函数
        mButtonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //读取上一步的数字分布状态
                mGame.goback();
                numMapSync(mGame.getNumMap());
            }
        });
        //重开的监听器函数
        mButtonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGame.restart();
                numMapSync(mGame.getNumMap());
            }
        });
        //滑动的监听器函数
        mRootLayout.setFocusable(true);//允许获得焦点
        mRootLayout.requestFocus();//立即获得焦点
        mRootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float slop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (mGame.getIsGameOver()){
                    Toast.makeText(getContext(),R.string.gameover,Toast.LENGTH_SHORT).show();
                    numMapSync(mGame.getNumMap());
                    return true;
                }
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        x = motionEvent.getX();
                        y = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        float hor = motionEvent.getX() - x;
                        float ver = motionEvent.getY() - y;
                        try{
                            if (Math.abs(hor) > Math.abs(ver)){
                                if (hor > slop){
                                    mGame.pressRight();
                                    numMapSync(mGame.getNumMap());
                                }else if (hor < -slop){
                                    mGame.pressLeft();
                                    numMapSync(mGame.getNumMap());
                                }else {
                                    //这里是不满足最小滑动距离的情况，无效滑动
                                    //Toast.makeText(getContext(),R.string.uselessmove,Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                if (ver > slop){
                                    mGame.pressDown();
                                    numMapSync(mGame.getNumMap());
                                } else if (ver < -slop) {
                                    mGame.pressUp();
                                    numMapSync(mGame.getNumMap());
                                }else {
                                    //Toast.makeText(getContext(),R.string.uselessmove,Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (GameOverException ger){
                            Toast.makeText(getContext(),R.string.gameover,Toast.LENGTH_SHORT).show();
                        }catch (Exception er){
                            Toast.makeText(getContext(),er.getMessage()+"nmdwsm",Toast.LENGTH_SHORT).show();
                        }
                        x = 0;
                        y = 0;
                        break;
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        numMapSync(mGame.getNumMap());
    }

    //执行numMap填入textView的函数，稍后可以添加颜色
    private void numMapSync(int[][] numMap){
        for (int i = 0;i < 4;i++){
            for (int j = 0;j < 4;j++){
               switch (numMap[i][j]){
                   case 0:
                       mTextViews[i][j].setText(R.string.num_empty);
                       break;
                   case 2:
                       mTextViews[i][j].setText(R.string.num_2);
                       break;
                   case 4:
                       mTextViews[i][j].setText(R.string.num_4);
                       break;
                   case 8:
                       mTextViews[i][j].setText(R.string.num_8);
                       break;
                   case 16:
                       mTextViews[i][j].setText(R.string.num_16);
                       break;
                   case 32:
                       mTextViews[i][j].setText(R.string.num_32);
                       break;
                   case 64:
                       mTextViews[i][j].setText(R.string.num_64);
                       break;
                   case 128:
                       mTextViews[i][j].setText(R.string.num_128);
                       break;
                   case 256:
                       mTextViews[i][j].setText(R.string.num_256);
                       break;
                   case 512:
                       mTextViews[i][j].setText(R.string.num_512);
                       break;
                   case 1024:
                       mTextViews[i][j].setText(R.string.num_1024);
                       break;
                   case 2048:
                       mTextViews[i][j].setText(R.string.num_2048);
                       break;
                       default:
                           break;
               }
               //这里设置对应的颜色
            }
        }
        //在视图上刷新分数
        mTextViewScore.setText(String.format(getResources().getString(R.string.score),mGame.getScore()));
    }
}
