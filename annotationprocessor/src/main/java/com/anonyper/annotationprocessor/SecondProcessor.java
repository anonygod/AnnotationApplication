package com.anonyper.annotationprocessor;

import com.anonyper.annotation.TestAnnotation;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@SupportedAnnotationTypes("com.anonyper.annotation.TestAnnotation")//注解解释器支持的注解类型
@SupportedSourceVersion(SourceVersion.RELEASE_8)//注解解释器支持的版本
@AutoService(Processor.class)//自动生成注解配置信息
public class SecondProcessor extends AbstractProcessor {
    private static final String TAG = "MyProcessor >>> ";
    private Filer filerUtils; // 文件写入
    private Elements elementUtils; // 操作Element 的工具类
    private Messager messagerUtils; //可以在编译时输出信息 用于调试

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filerUtils = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
        messagerUtils = processingEnv.getMessager();
        messagerUtils.printMessage(Diagnostic.Kind.WARNING, TAG + "Myprocessor init ");
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        messagerUtils.printMessage(Diagnostic.Kind.WARNING, TAG + "Myprocessor process ");
        //信息收集器
        StringBuilder builder = new StringBuilder();

        //获取带有TestAnnotation注解信息的元素
        Set<? extends Element> elementsAnnotatedWith = roundEnvironment
                .getElementsAnnotatedWith(TestAnnotation.class);

        //遍历
        for (Element element : elementsAnnotatedWith) {

            //获取元素方法
            builder.append("元素名称>>>：" + element.getSimpleName().toString());

            switch (element.getKind()) {
                case CLASS://类
                    builder.append(" 是一个类 >> ");
                    TypeElement typeElement = (TypeElement) element;
                    //包名信息
                    builder.append("包名信息是>>>：" + typeElement.getQualifiedName() + "\n");
                    appendClassInfo(typeElement, builder);
                    break;
                case FIELD://变量
                    builder.append(" 是一个变量 >> ");
                    VariableElement variableElement = (VariableElement) element;
                    builder.append(" 值为 " + variableElement.getConstantValue() + "\n");

                    break;
                case METHOD://方法
                    builder.append(" 是一个方法 >> ");
                    ExecutableElement executableElement = (ExecutableElement) element;
                    appendMethodInfo(executableElement, builder);
                    break;
                default:
                    break;
            }

        }
        if (builder.toString().length() > 0) {
            //创建方法
            MethodSpec getMessage = MethodSpec.methodBuilder("getMessage")
                    .addModifiers(Modifier.PUBLIC)
                    .returns(String.class)
                    .addStatement("return $S", builder.toString())
                    .build();
            //创建类
            TypeSpec AutoCreateClass = TypeSpec.classBuilder("AutoCreateClass")
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addMethod(getMessage)
                    .build();
            //创建包名
            JavaFile javaFile = JavaFile.builder("com.anonyper.autocreate", AutoCreateClass)
                    .build();
            try {
                //开始生成代码
                javaFile.writeTo(filerUtils);
            } catch (IOException e) {
                messagerUtils.printMessage(Diagnostic.Kind.WARNING, TAG + "异常报错：" + e.toString());
//            e.printStackTrace();
            }
        }

        messagerUtils.printMessage(Diagnostic.Kind.WARNING, TAG + builder.toString());

        return true;
    }

    /**
     * 遍历类元素 获取下面信息
     *
     * @param typeElement
     * @param builder
     */
    public void appendClassInfo(TypeElement typeElement, StringBuilder builder) {
        if (typeElement == null || builder == null)
            return;
        List<? extends Element> enclosedElements = typeElement
                .getEnclosedElements();
        builder.append("该类下面有如下元素：");
        for (Element element : enclosedElements) {
            builder.append(element.getKind().name() + ">>>" + element.getSimpleName() + "    ");
        }
        builder.append("\n");
    }

    /**
     * 遍历类元素 获取下面信息
     *
     * @param executableElement
     * @param builder
     */
    public void appendMethodInfo(ExecutableElement executableElement, StringBuilder builder) {
        if (executableElement == null || builder == null)
            return;
        builder.append("该方法下面有如下参数：\n");
        //获取参数
        List<? extends VariableElement> parameters = executableElement
                .getParameters();
        //获取参数类型
        List<? extends TypeParameterElement> typeParameters = executableElement
                .getTypeParameters();
        for (int i = 0; i < parameters.size(); i++) {
            builder.append(parameters.get(i).getSimpleName() + " 类型 >> " + parameters.get(i).asType().toString() + "\n");
        }
        //获取返回值
        TypeMirror returnType = executableElement.getReturnType();

        builder.append("该方法返回值：>>" + returnType.toString());

    }
}
