package org.example;

import javax.annotation.processing.Messager;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class Main {

    public static final String RESET_COLOR = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface Print {
        String style() default "";
        String color() default "";
    }

    @Print(style = "*", color = RED)
    public static class Messages {

        @Print(style = "@", color = GREEN)
        public String happyBirthday(){
            return "С днем рождения";
        }

        @Print(style = "!", color = YELLOW)
        public String warning(){
            return "Предупреждение";
        }

        @Print
        public String error(){
            return "Ошибка";
        }

        public String grac(){
            return "Поздравляю";
        }

    }



    public static void main(String[] args) {

        //System.out.println(RED + "This text is red!" + RESET_COLOR);
        Messages msgs = new Messages();
        print(msgs);
    }

    private static void print(Messages msgs) {
        Class<?> clazz = msgs.getClass();
        Annotation classAnnotation = clazz.getAnnotation(Print.class);
        String classStyle = "";
        String classColor = "";

        if (classAnnotation != null) {
            Print printAnnotation = (Print) classAnnotation;
            classStyle = printAnnotation.style();
            classColor = printAnnotation.color();
        }

        for (Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Print.class)) {
                Print methodAnnotation = method.getAnnotation(Print.class);
                String style = methodAnnotation.style().isEmpty() ? classStyle : methodAnnotation.style();
                String color = methodAnnotation.color().isEmpty() ? classColor : methodAnnotation.color();

                try {
                    Object result = method.invoke(msgs);
                    System.out.println(color + style+style+style + result + style + style + style+ RESET_COLOR);
                } catch (Exception e) {
                    System.out.println("Error");
                }

            }
        }
    }


}