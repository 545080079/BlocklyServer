package com.ustc.bly.server.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.PriorityQueue;


public class upload {


    //代码转化为.java
    public static int genJavaFile(StringBuilder sb, List<String> classList) throws IOException {

        int seq = 1;
        String code = sb.toString();
Math.pow(1.0,4);
PriorityQueue<Integer> p = new PriorityQueue<>((x1,x2)->x2-x1);

        //以package作为文件分割符
        code = code.substring(7, code.length());
        String[] dataSet = code.split("package");
        for(String s : dataSet)
        {
            StringBuilder tsb = new StringBuilder("package");
            tsb.append(s);

            String filePath="./MainActivity" + (seq++) + ".java";
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(tsb.toString().getBytes());
            fos.close();


            //找到当前java文件的Class Name
            String preClassName = "public class ";
            int begin = s.indexOf(preClassName) + preClassName.length();
            int end = begin + 1;
            while(s.charAt(end) != ' ')
                ++end;
            classList.add(s.substring(begin, end));

        }
        return seq - 1;//生成java文件数
    }
}
