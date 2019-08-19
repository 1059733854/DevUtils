package testview;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MySurfaceView extends GLSurfaceView {
    //角度缩放比例
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    //场景渲染器
    private SceneRenderer mRenderer;
    //投影标志位
    public boolean isPerspective = false;
    //上次的触控位置Y坐标
    private float mPreviousY;
    //整体绕x轴旋转的角度
    public float xAngle = 0;
    private Context context;

    public MySurfaceView(Context context) {

        super(context);

        this.context = context;
        //创建场景渲染器
        mRenderer = new SceneRenderer();
        //设置渲染器
        setRenderer(mRenderer);
        //主动渲染
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    //触摸事件回调方法
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        //获取动作
        switch (e.getAction()) {
            //判断是否是滑动
            case MotionEvent.ACTION_MOVE:
                //计算触控笔Y位移
                float dy = y - mPreviousY;
                //设置沿x轴旋转角度
                xAngle += dy * TOUCH_SCALE_FACTOR;
                //重绘画面
                requestRender();
        }
        //作为上一次触点的Y坐标
        mPreviousY = y;
        return true;
    }

    private class SceneRenderer implements GLSurfaceView.Renderer {
        private GLBitmap glBitmap;

        FloatBuffer vertextBuffer;

        //顶点
        float[] vertex = new float[]{
                -2.0f, -10.8f, 0f,
                -2.0f, 10.8f, 0f,

                -1.8f, -10.8f, 0f,
                -1.8f, 10.8f, 0f,

                -1.6f, -10.8f, 0f,
                -1.6f, 10.8f, 0f,

                -1.4f, -10.8f, 0f,
                -1.4f, 10.8f, 0f,

                -1.2f, -10.8f, 0f,
                -1.2f, 10.8f, 0f,

                -1.0f, -10.8f, 0f,
                -1.0f, 10.8f, 0f,

                -0.8f, -10.8f, 0f,
                -0.8f, 10.8f, 0f,

                -0.6f, -10.8f, 0f,
                -0.6f, 10.8f, 0f,

                -0.4f, -10.8f, 0f,
                -0.4f, 10.8f, 0f,

                -0.2f, -10.8f, 0f,
                -0.2f, 10.8f, 0f,

                -0.0f, -10.8f, 0f,
                -0.0f, 10.8f, 0f,

                0.2f, -10.8f, 0f,
                0.2f, 10.8f, 0f,

                0.4f, -10.8f, 0f,
                0.4f, 10.8f, 0f,

                0.6f, -10.8f, 0f,
                0.6f, 10.8f, 0f,

                0.8f, -10.8f, 0f,
                0.8f, 10.8f, 0f,

                1.0f, -10.8f, 0f,
                1.0f, 10.8f, 0f,

                1.2f, -10.8f, 0f,
                1.2f, 10.8f, 0f,

                1.4f, -10.8f, 0f,
                1.4f, 10.8f, 0f,

                1.6f, -10.8f, 0f,
                1.6f, 10.8f, 0f,

                1.8f, -10.8f, 0f,
                1.8f, 10.8f, 0f,

                2.0f, -10.8f, 0f,
                2.0f, 10.8f, 0f,
        };

        public SceneRenderer() {
            vertextBuffer = floatArray2Buffer(vertex);
            glBitmap = new GLBitmap();
        }                  //渲染器构造类

        @Override
        public void onDrawFrame(GL10 gl) {
            gl.glMatrixMode(GL10.GL_PROJECTION);      //设置当前矩阵为投影矩阵
            gl.glLoadIdentity();                  //设置当前矩阵为单位矩阵
            float ratio = (float) 320 / 480;          //计算透视投影的比例

            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);

            gl.glFrustumf(-ratio, ratio, -1, 1, 1f, 10);//调用此方法计算产生透视投影矩阵

            gl.glEnable(GL10.GL_CULL_FACE);             //设置为打开背面剪裁
            gl.glShadeModel(GL10.GL_SMOOTH);          //设置着色模型为平滑着色
            gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);//清除缓存
            gl.glMatrixMode(GL10.GL_MODELVIEW);          //设置当前矩阵为模式矩阵
            gl.glLoadIdentity();                   //设置当前矩阵为单位矩阵

            gl.glTranslatef(0, -xAngle / 100, -1.8f + xAngle / 100);              //沿z轴向远处推
            gl.glRotatef(-30, 1, 0, 0);          //绕x轴旋转制定角度


            //设置顶点位置数据
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertextBuffer);
            gl.glDrawArrays(GL10.GL_LINES, 0, 42);

            glBitmap.draw(gl);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            gl.glViewport(0, 0, width, height); //设置视窗大小及位置
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            glBitmap.loadGLTexture(gl, context);
            gl.glDisable(GL10.GL_DITHER);                       //关闭抗抖动
            gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);//设置Hint模式
            gl.glClearColor(0, 0, 0, 0);                          //设置屏幕背景色黑色
            gl.glEnable(GL10.GL_DEPTH_TEST);                     //启用深度测试
            //设置阴影平滑模式
            gl.glShadeModel(GL10.GL_SMOOTH);
            //启用深度测试 记录Z轴深度
            gl.glEnable(GL10.GL_DEPTH_TEST);
            //设置深度测试的类型
            gl.glDepthFunc(GL10.GL_LEQUAL);

        }
    }

    //将顶点位置数组转换为FloatBuffer,是OpenGl ES所需要的
    private FloatBuffer floatArray2Buffer(float[] rect1) {
        FloatBuffer floatBuffer;
        //不用该方法得到FloatBuffer,因为Android平台限制，Buffer必须为native Buffer,所以要通过allocateDirect()创建
        //并且该Buffer必须是排序的，所以要order()方法进行排序
        //floatBuffer = FloatBuffer.wrap(rect1);
        //因为一个int=4字节
        ByteBuffer bb = ByteBuffer.allocateDirect(rect1.length * 4);
        bb.order(ByteOrder.nativeOrder());
        floatBuffer = bb.asFloatBuffer();
        floatBuffer.put(rect1);
        floatBuffer.position(0); //移到第一个点的数据
        return floatBuffer;
    }
}
