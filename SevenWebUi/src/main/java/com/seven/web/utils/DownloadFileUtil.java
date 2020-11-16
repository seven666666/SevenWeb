package com.seven.web.utils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

public class DownloadFileUtil {
    private static Logger LOG = Logger.getLogger(DownloadFileUtil.class);

    /**
     * 以流的方式下载
     * @param response
     * @param filePath 文件路径
     * @param encode
     * @return response
     */
    public static HttpServletResponse downloadStream(HttpServletResponse response, String filePath, String encode) {
        response.setContentType("text/html;charset=" + encode);
        try {
            // path是指欲下载的文件的路径
            File file = new File(filePath);
            // 取得文件名
            String filename = file.getName();
            // 取得文件的后缀名
            // String ext = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
            // 以流的形式下载文件
            InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(encode), "ISO8859-1"));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            LOG.error(ex.getMessage());
        }
        return response;
    }
}
