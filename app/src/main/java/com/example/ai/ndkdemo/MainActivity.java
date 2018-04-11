package com.example.ai.ndkdemo;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("" + stringFromJava("I from Java哦"));

        imageView = (ImageView) findViewById(R.id.image);
        // 结果显示还是java更快...
        System.out.println("start jni gray bitmap");
        getJniBitmap();
        System.out.println("end jni gray bitmap");
        System.out.println("start java gray bitmap");
        convertGrayImg();
        System.out.println("end java gray bitmap");
        imageView.setImageBitmap(getJniBitmap());
    }

    // Java方法灰色话图片
    public Bitmap convertGrayImg() {
        Bitmap img1 = ((BitmapDrawable) getResources().getDrawable(R.mipmap.head)).getBitmap();

        int w = img1.getWidth(), h = img1.getHeight();
        int[] pix = new int[w * h];
        img1.getPixels(pix, 0, w, 0, 0, w, h);

        int alpha = 0xFF << 24;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                // 获得像素的颜色
                int color = pix[w * i + j];
                int red = ((color & 0x00FF0000) >> 16);
                int green = ((color & 0x0000FF00) >> 8);
                int blue = color & 0x000000FF;
                color = (red + green + blue) / 3;
                color = alpha | (color << 16) | (color << 8) | color;
                pix[w * i + j] = color;
            }
        }
        Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        result.setPixels(pix, 0, w, 0, 0, w, h);
        return result;
    }


    // JNI方法灰色话图片
    public Bitmap getJniBitmap() {
        Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.head)).getBitmap();
        int w = bitmap.getWidth(), h = bitmap.getHeight();
        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        int[] resultInt = getImgToGray(pix, w, h);
        Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        resultImg.setPixels(resultInt, 0, w, 0, 0, w, h);
        return resultImg;
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    // 测试int使用
    public native int getSum(int a, int b);

    // 测试String使用
    // java内部是使用16bit的unicode编码（UTF-16）来表示字符串的，无论中文英文都是2字节；
    //jni内部是使用UTF-8编码来表示字符串的，UTF-8是变长编码的unicode，一般ascii字符是1字节，中文是3字节；
    //c/c++使用的是原始数据，ascii就是一个字节了，中文一般是GB2312编码，用两个字节来表示一个汉字。
    public native String stringFromJava(String a);

    // 图片处理为灰色
    public native int[] getImgToGray(int[] data, int w, int h);
}
