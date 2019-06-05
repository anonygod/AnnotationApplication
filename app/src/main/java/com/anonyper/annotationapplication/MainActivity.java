package com.anonyper.annotationapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.anonyper.annotation.TestAnnotation;
//import com.anonyper.autocreate.AutoCreateClass;

/**
 * 被注解标注的类
 */
@TestAnnotation
public class MainActivity extends AppCompatActivity {

    /**
     * 被注解标注的变量
     */
    private final @TestAnnotation String name = "MainActivity >> 变量";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AutoCreateClass autoCreateClass = new AutoCreateClass();
        TextView textView = (TextView) this.findViewById(R.id.show_text);
//        textView.setText(autoCreateClass.getMessage());
    }

    /**
     * 被注解标注的方法
     * ElementType.TYPE_USE 的注解不能标注返回void的方法，我们返回一个string
     *
     * @param name
     * @return
     */
    @TestAnnotation
    private String mainMethon(String name) {
        return "mainMethon";
    }
}
