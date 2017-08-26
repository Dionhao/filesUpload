package com.file.servlet;  
  
import java.io.IOException;  
  
import javax.servlet.ServletException;  
import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import com.file.upload.FileUpload;  
  
  
@SuppressWarnings("serial")  
public class FileUploadServlet extends HttpServlet {  
      
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        this.doPost(request, response);  
    }  
  
      
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws ServletException, IOException {  
        FileUpload.upload(request);  
    }  
  
}  