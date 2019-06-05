package com.anonyper.annotationapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.anonyper.annotation.TestAnnotation;

/**
 * 被注解标注的类
 */
@TestAnnotation
public class SecondActivity extends AppCompatActivity {

    /**
     * 被注解标注的变量
     */
    private final @TestAnnotation String name = "SecondActivity >> name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 被注解标注的方法
     * ElementType.TYPE_USE 的注解不能标注返回void的方法，我们返回一个string
     *
     * @param name
     * @return
     */
    @TestAnnotation
    private String secondMethon(String name) {
        return "secondMethon";
    }
}
