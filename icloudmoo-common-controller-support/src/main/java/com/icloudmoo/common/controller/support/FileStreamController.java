package com.icloudmoo.common.controller.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.icloudmoo.common.exception.BusinessServiceException;
import com.icloudmoo.common.redis.suport.RedisUtil;
import com.icloudmoo.common.util.ConfigureUtil;
import com.icloudmoo.common.vo.FileInfo;
import com.icloudmoo.common.vo.SCT;

/**
 * @ClassName: DownloadController
 * @Description: TODO（用一句话描述这个类的用途）
 * @author 信息管理部-gengchong
 * @date 2015年10月13日 下午2:49:31
 *
 */
@Controller
@RequestMapping("/file")
public class FileStreamController extends AbstractManagerController {

    @RequestMapping(value = "/download")
    public String downloadFile(HttpServletResponse response) {
        InputStream is = null;
        OutputStream os = null;
        try {
            String sessionId = servletRequest.getParameter("sid");
            FileInfo fileInfo = RedisUtil.redisQueryObject(sessionId);
            if (null == fileInfo) {
                throw new BusinessServiceException("未找到下载文件！！");
            }

            // 文件流
            is = new FileInputStream(fileInfo.getPath());
            // 请求的响应输出流
            os = response.getOutputStream();
            response.setHeader("content-disposition",
                    "attachment;filename=" + URLEncoder.encode(fileInfo.getFileName(), SCT.CHAR_ENCODING));
            response.setContentType(fileInfo.getContentType());
            response.setCharacterEncoding(SCT.CHAR_ENCODING);
            // 将文件输入流转发给响应输出流，实现文件下载
            IOUtils.copy(is, os);
            os.flush();
            RedisUtil.redisDeleteKey(sessionId);
            return null;
        }
        catch (Exception e) {
            logger.error("下载文件失败！！", e);
        }
        finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(os);
        }

        return "DOWNLOAD ERROR!!";
    }

    @RequestMapping(value = "/upload")
    public String uploadFile(HttpServletRequest request) {
        try {
            DiskFileItemFactory dff = new DiskFileItemFactory();
            ServletFileUpload fileUpload = new ServletFileUpload(dff);
            String savePath = ConfigureUtil.getProperty("upload.path", "/upload/");
            List<FileItem> fileItems = fileUpload.parseRequest(request);
            for (FileItem fileItem : fileItems) {
                FileUtils.writeByteArrayToFile(new File(savePath + fileItem.getName()),
                        IOUtils.toByteArray(fileItem.getInputStream()));
            }
        }
        catch (Exception e) {
            logger.error("上传文件失败！！", e);
        }

        return "UPLOAD ERROR!!";
    }
}
