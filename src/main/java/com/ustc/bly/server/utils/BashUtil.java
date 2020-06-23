package com.ustc.bly.server.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * @command为bash脚本的地址,初始化的时候接受bash脚本的地址，run方法返回结果对应的String
 * @throws
 * @author
 * @data 2020/3/24 5:10 下午
 */
public class BashUtil {
    String command;
    public BashUtil(String command){
        this.command = command;
    }
    public String run(){
        String result = null;
        try{
            System.out.println(command);
            Runtime runtime = Runtime.getRuntime();
            Process pro = runtime.exec(command);
            int status = pro.waitFor();
            if (status != 0)
            {
                return "Failed to call shell's command ";
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            StringBuilder strbr = new StringBuilder();
            String line;
            while ((line = br.readLine())!= null)
            {
                strbr.append(line).append("\n");
            }
            result = strbr.toString();
        }
        catch (IOException | InterruptedException ec)
        {
            ec.printStackTrace();
        }
        return result;
    }

    public static void main(String[] args) {
        BashUtil bashUtil = new BashUtil("/Users/zhangyixiao/IdeaProjects/test2nd/autoGen.sh");
        String result = bashUtil.run();
        System.out.println(result);
    }

}
