package com.hwj.dell.a20190709defineview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import testview.MySurfaceView;

public class MainActivity extends AppCompatActivity {
    private MySurfaceView mSurfaceView;                 //声明MySurfaceView对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        GLRender render = new GLRender(this);
//        GLSurfaceView glSurfaceView = new GLSurfaceView(this);
//        glSurfaceView.setRenderer(render);
        setContentView(R.layout.activity_main);
        //创建
        mSurfaceView = new MySurfaceView(this);
        //获取焦点  //MySurfaceView对象
        mSurfaceView.requestFocus();
        //设置为可触控
        mSurfaceView.setFocusableInTouchMode(true);
        //获得布局引用
        LinearLayout ll = (LinearLayout) findViewById(R.id.main_liner);
        //在布局中添加MySurfaceView对象
        ll.addView(mSurfaceView);
        //控制是否打开背面剪裁的ToggleButton
        ToggleButton tb = (ToggleButton) this.findViewById(R.id.ToggleButton01);//获得按钮引用
        tb.setOnCheckedChangeListener(new MyListener()); //为按钮设置监听器
    }

    class MyListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            // TODO Auto-generated method stub
            mSurfaceView.isPerspective = !mSurfaceView.isPerspective;//在正交投影与透视投影之间切换
            mSurfaceView.requestRender();//重新绘制
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
    }
}
