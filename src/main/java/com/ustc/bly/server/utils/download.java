package com.ustc.bly.server.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class download {



    public static void download(HttpServletResponse response) throws UnsupportedEncodingException {
        String fileName = "/home/springboot/apk_signed.apk";
        File file = new File(fileName);
        if(file.exists())
        {
            response.setHeader("content-type","application/octet-stream");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition","attachment;filename="
                    + URLEncoder.encode(fileName,"UTF-8"));
            byte[] buffer = new byte[1024];
            try(
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis);
            ) {
              OutputStream os =response.getOutputStream();
              int i = bis.read(buffer);
              while(i!=-1)
              {
                  os.write(buffer,0,i);
                  i = bis.read(buffer);
              }
              System.out.println("Download apk successfully!");

            } catch (IOException e) {
                e.printStackTrace();
            }


        }
        else{//文件不存在

            System.out.println("fail ! File not exist");
        }


    }

}
