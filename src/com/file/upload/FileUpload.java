package com.file.upload;  
  
import java.io.File;  
import java.util.Iterator;  
import java.util.List;  
  
import javax.servlet.http.HttpServletRequest;  
  
import org.apache.commons.fileupload.FileItem;  
import org.apache.commons.fileupload.FileUploadException;  
import org.apache.commons.fileupload.disk.DiskFileItemFactory;  
import org.apache.commons.fileupload.servlet.ServletFileUpload;  
  
import com.file.helper.FileConst;  
import com.file.helper.ReadUploadFileType;  
  
public class FileUpload {  
     private static String uploadPath = null;// 上传文件的目录  
     private static String tempPath = null; // 临时文件目录  
     private static File uploadFile = null;  
     private static File tempPathFile = null;  
     private static int sizeThreshold = 1024;  
     private static int sizeMax = 4194304;  
     //初始化  
     static{  
           sizeMax  = Integer.parseInt(FileConst.getValue("sizeMax"));  
           sizeThreshold = Integer.parseInt(FileConst.getValue("sizeThreshold"));  
           uploadPath = FileConst.getValue("uploadPath");  
           uploadFile = new File(uploadPath);  
           if (!uploadFile.exists()) {  
               uploadFile.mkdirs();  
           }  
           tempPath = FileConst.getValue("tempPath");  
           tempPathFile = new File(tempPath);  
           if (!tempPathFile.exists()) {  
               tempPathFile.mkdirs();  
           }  
     }  
    /** 
     * 上传文件  
     * @param request 
     * @return true 上传成功 false上传失败 
     */  
    @SuppressWarnings("unchecked")  
    public static boolean upload(HttpServletRequest request){  
        boolean flag = true;  
        //检查输入请求是否为multipart表单数据。  
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);  
        //若果是的话  
        if(isMultipart){  
            /**为该请求创建一个DiskFileItemFactory对象，通过它来解析请求。执行解析后，所有的表单项目都保存在一个List中。**/  
            try {  
                DiskFileItemFactory factory = new DiskFileItemFactory();  
                factory.setSizeThreshold(sizeThreshold); // 设置缓冲区大小，这里是4kb  
                factory.setRepository(tempPathFile);// 设置缓冲区目录  
                ServletFileUpload upload = new ServletFileUpload(factory);  
                upload.setHeaderEncoding("UTF-8");//解决文件乱码问题  
                upload.setSizeMax(sizeMax);// 设置最大文件尺寸  
                List<FileItem> items = upload.parseRequest(request);  
                // 检查是否符合上传的类型  
                if(!checkFileType(items)) return false;  
                Iterator<FileItem> itr = items.iterator();//所有的表单项  
                //保存文件  
                while (itr.hasNext()){  
                     FileItem item = (FileItem) itr.next();//循环获得每个表单项  
                     if (!item.isFormField()){//如果是文件类型  
                             String name = item.getName();//获得文件名 包括路径啊  
                             if(name!=null){  
                                 File fullFile=new File(name);  
                                 File savedFile=new File(uploadPath,fullFile.getName());  
                                 item.write(savedFile);  
                             }  
                     }  
                }  
            } catch (FileUploadException e) {  
                flag = false;  
                e.printStackTrace();  
            }catch (Exception e) {  
                flag = false;  
                e.printStackTrace();  
            }  
        }else{  
            flag = false;  
            System.out.println("the enctype must be multipart/form-data");  
        }  
        return flag;  
    }  
      
    /** 
     * 删除一组文件 
     * @param filePath 文件路径数组 
     */  
    public static void deleteFile(String [] filePath){  
        if(filePath!=null && filePath.length>0){  
            for(String path:filePath){  
                String realPath = uploadPath+path;  
                File delfile = new File(realPath);  
                if(delfile.exists()){  
                    delfile.delete();  
                }  
            }  
              
        }  
    }  
      
    /** 
     * 删除单个文件 
     * @param filePath 单个文件路径 
     */  
    public static void deleteFile(String filePath){  
        if(filePath!=null && !"".equals(filePath)){  
            String [] path = new String []{filePath};  
            deleteFile(path);  
        }  
    }  
      
    /** 
     * 判断上传文件类型 
     * @param items 
     * @return 
     */  
    private static Boolean checkFileType(List<FileItem> items){  
        Iterator<FileItem> itr = items.iterator();//所有的表单项  
        while (itr.hasNext()){  
             FileItem item = (FileItem) itr.next();//循环获得每个表单项  
             if (!item.isFormField()){//如果是文件类型  
                     String name = item.getName();//获得文件名 包括路径啊  
                     if(name!=null){  
                         File fullFile=new File(item.getName());  
                         boolean isType = ReadUploadFileType.readUploadFileType(fullFile);  
                         if(!isType) return false;  
                         break;  
                     }  
             }  
        }  
          
        return true;  
    }  
}  