package com.ustc.bly.server.controller;

import com.ustc.bly.server.utils.BashUtil;
import com.ustc.bly.server.utils.upload;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/")
public class genController {
    static final String userName = "user";
    static final String path = "/opt/workdir/";

    //上传Java文件
    @RequestMapping(value = "/submitFile", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public void submitFile(@RequestParam String filename,
                           HttpServletRequest request,
                           HttpServletResponse response) throws IOException{

        String file = filename + ".java";


        new BashUtil("touch " + file).run();

        try(
                ServletInputStream is = request.getInputStream();
                FileOutputStream os = new FileOutputStream("./" + file)
                )
        {
            byte[] buf = new byte[1024];
            int len = 0;
            while((len = is.read(buf)) != -1){
                os.write(buf,0, len);
            }
        }
    }

    //上传AndroidManifest.xml
    @RequestMapping(value = "/submitXml", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public void submitXml(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String file = "AndroidManifest.xml";
        BashUtil bash = new BashUtil("touch " + file);
        bash.run();

        try(
                ServletInputStream is = request.getInputStream();
                FileOutputStream os = new FileOutputStream("./" + file)
                )
        {
            byte[] buf = new byte[1024];
            int len = 0;
            while((len = is.read(buf)) != -1){
                os.write(buf,0, len);
            }
        }
    }


    //download Apk
    @RequestMapping(value = "/postApk", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public void postApk(HttpServletRequest request, HttpServletResponse response) throws IOException {


//        //获取用户提交的request流，转换为StringBuilder
//        StringBuilder sb = new StringBuilder();
//        ServletInputStream is = request.getInputStream();
//        byte[] buf = new byte[1024];
//        int len = 0;
//        while ((len = is.read(buf)) != -1) {
//            sb.append(new String(buf, 0, len));
//        }
//        is.close();
//
//        //生成MainActivityX.java
//        List<String> classList = new ArrayList<>();
//        int num_javaFile = upload.genJavaFile(sb, classList);
//        for(int i = 0; i<num_javaFile; ++i)
//        {
//            BashUtil bash = new BashUtil("touch " + classList.get(i) + ".java");
//            System.out.println(bash.run());
//        }

        //导入环境变量
        BashUtil bash = new BashUtil("source ./env.sh");
        bash.run();

        //Xml文件归位
        bash = new BashUtil("sh ./mvXmlToWorkDir.sh");
        System.out.println(bash.run());

        //Java文件归位
        bash = new BashUtil("sh ./mvJavaToWorkDir.sh");
        System.out.println(bash.run());

        //执行initGen.sh初始化检查，自动调用工作目录下的autoGen.sh 打包apk
        bash = new BashUtil("sh ./autoGen.sh");
        System.out.println(bash.run());

        //apk文件下载
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");

        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("apk_signed_" + userName, "utf-8")+".apk");

        //PrintWriter os = response.getWriter();
        //Reader reader = new BufferedReader(new FileReader(new File(path,"apk_signed_" + userName + ".apk")));
        //Reader reader = new BufferedReader(new FileReader(new File(path,"test.apk")));

        try(
                FileInputStream fis = new FileInputStream(new File(path, "demo.apk"));
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
        ){
            int len;
            byte[] buffer = new byte[1024 * 100];
            while((len = bis.read(buffer)) != -1){
                os.write(buffer, 0, len);
                System.out.println("len = " + len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        System.out.println("[Successful to received Request]\n ");

    }


    @GetMapping("/getApk/{id}")
    public byte[] getApk(@PathVariable(name = "id")String id, @RequestParam(name = "code") String code) throws IOException {

//        System.out.println("code = " + code);
//        int seq = 1;
//        code = code.substring(7);
//        String[] dataSet = code.split("package");
//        for(String s : dataSet)
//        {
//            s = "package " + s;
//
//            String filePath="./java" + (seq++) + ".java";
//            FileOutputStream fos = new FileOutputStream(filePath);
//            fos.write(s.getBytes());
//            fos.close();
//        }

        return code.getBytes();
    }


    //Get Demo
    @RequestMapping(value = "/postDemo", method = RequestMethod.POST, produces = "text/plain;charset=UTF-8")
    public void getOne(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setCharacterEncoding("utf-8");
        response.setContentType("application/octet-stream");

        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode("apk_signed_" + userName, "utf-8")+".apk");

        try(
                FileInputStream fis = new FileInputStream(new File(path, "demo.apk"));
                BufferedInputStream bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream()
        ){
            int len;
            byte[] buffer = new byte[1024 * 100];
            while((len = bis.read(buffer)) != -1){
                os.write(buffer, 0, len);
                System.out.println("len = " + len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
