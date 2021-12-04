package com.km.utils;

import org.apache.catalina.connector.ClientAbortException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class VideoStream {

    public static void getVideo(/*HttpServletRequest request,*/ HttpServletResponse response) {
        String file = "D:\\用户数据\\视频\\终结者2018.1080p.国英粤三语.BD中英双字.mp4";
        try {
            FileInputStream inputStream = new FileInputStream(file);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String diskfilename = "final.mp4";
            response.setContentType("video/mp4");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + diskfilename + "\"");

            System.out.println("data.length " + data.length);

            response.setContentLength(data.length);
            response.setHeader("Content-Range", String.valueOf(data.length - 1));
            response.setHeader("Accept-Ranges", "bytes");
            response.setHeader("Etag", "W/\"9767057-1323779115364\"");
            OutputStream os = response.getOutputStream();

            os.write(data);
            //先声明的流后关掉！
            os.flush();
            os.close();
            inputStream.close();

        } catch (Exception e) {

        }
    }


    /**
     * 文件支持分块下载和断点续传
     *
     * @param filePath 文件完整路径
     * @param request  请求
     * @param response 响应
     */
    private static void fileChunkDownload(String filePath, HttpServletRequest request, HttpServletResponse response,Logger logger) {
        String range = request.getHeader("Range");

        logger.log(Level.INFO,"current request rang:" + range);
        File file = new File(filePath);
        //开始下载位置
        long startByte = 0;
        //结束下载位置
        long endByte = file.length() - 1;
        logger.log(Level.INFO,String.format("文件开始位置：{%d}，文件结束位置：{%d}，文件总长度：{%d}", startByte, endByte, file.length()));

        //有range的话
        if (range != null && range.contains("bytes=") && range.contains("-")) {
            range = range.substring(range.lastIndexOf("=") + 1).trim();
            String[] ranges = range.split("-");
            try {
                //判断range的类型
                if (ranges.length == 1) {
                    //类型一：bytes=-2343
                    if (range.startsWith("-")) {
                        endByte = Long.parseLong(ranges[0]);
                    }
                    //类型二：bytes=2343-
                    else if (range.endsWith("-")) {
                        startByte = Long.parseLong(ranges[0]);
                    }
                }
                //类型三：bytes=22-2343
                else if (ranges.length == 2) {
                    startByte = Long.parseLong(ranges[0]);
                    endByte = Long.parseLong(ranges[1]);
                }

            } catch (NumberFormatException e) {
                startByte = 0;
                endByte = file.length() - 1;
                logger.log(Level.INFO,"Range Occur Error,Message:{}", e.getLocalizedMessage());
            }
        }

        //要下载的长度
        long contentLength = endByte - startByte + 1;
        //文件名
        String fileName = file.getName();
        //文件类型
        String contentType = request.getServletContext().getMimeType(fileName);

        ////解决下载文件时文件名乱码问题
        byte[] fileNameBytes = fileName.getBytes(StandardCharsets.UTF_8);
        fileName = new String(fileNameBytes, 0, fileNameBytes.length, StandardCharsets.ISO_8859_1);

        //各种响应头设置
        //支持断点续传，获取部分字节内容：
        response.setHeader("Accept-Ranges", "bytes");
        //http状态码要为206：表示获取部分内容
        response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
        response.setContentType(contentType);
        response.setHeader("Content-Type", contentType);
        //inline表示浏览器直接使用，attachment表示下载，fileName表示下载的文件名
        response.setHeader("Content-Disposition", "inline;filename=" + fileName);
        response.setHeader("Content-Length", String.valueOf(contentLength));
        // Content-Range，格式为：[要下载的开始位置]-[结束位置]/[文件总大小]
        response.setHeader("Content-Range", "bytes " + startByte + "-" + endByte + "/" + file.length());

        BufferedOutputStream outputStream = null;
        RandomAccessFile randomAccessFile = null;
        //已传送数据大小
        long transmitted = 0;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[4096];
            int len = 0;
            randomAccessFile.seek(startByte);
            //坑爹地方四：判断是否到了最后不足4096（buff的length）个byte这个逻辑（(transmitted + len) <= contentLength）要放前面！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
            //不然会会先读取randomAccessFile，造成后面读取位置出错，找了一天才发现问题所在
            while ((transmitted + len) <= contentLength && (len = randomAccessFile.read(buff)) != -1) {
                outputStream.write(buff, 0, len);
                transmitted += len;
            }
            //处理不足buff.length部分
            if (transmitted < contentLength) {
                len = randomAccessFile.read(buff, 0, (int) (contentLength - transmitted));
                outputStream.write(buff, 0, len);
                transmitted += len;
            }

            outputStream.flush();
            response.flushBuffer();
            randomAccessFile.close();
            logger.log(Level.INFO,"下载完毕：" + startByte + "-" + endByte + "：" + transmitted);
        } catch (ClientAbortException e) {
            logger.log(Level.INFO,"用户停止下载：" + startByte + "-" + endByte + "：" + transmitted);
            //捕获此异常表示拥护停止下载
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.INFO,"用户下载IO异常，Message：{}", e.getLocalizedMessage());
        } finally {
            try {
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public static void player(HttpServletRequest request, HttpServletResponse response,Logger logger) {
        String path = "D:\\用户数据\\视频\\终结者2018.1080p.国英粤三语.BD中英双字.mp4";
        File file = new File(path);
        BufferedInputStream bis;
        try {
            long p = 0L;
            long toLength;
            long contentLength;
            int rangeSwitch = 0; // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
            long fileLength;
            String rangBytes;
            fileLength = file.length();

            // get file content
            bis = new BufferedInputStream(new FileInputStream(file));

            // tell the client to allow accept-ranges
            response.reset();
            response.setHeader("Accept-Ranges", "bytes");

            // client requests a file block download start byte
            String range = request.getHeader("Range");
            if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                rangBytes = range.replaceAll("bytes=", "");
                if (rangBytes.endsWith("-")) { // bytes=270000-
                    rangeSwitch = 1;
                    p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                    contentLength = fileLength - p; // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                } else { // bytes=270000-320000
                    rangeSwitch = 2;
                    String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                    String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1);
                    p = Long.parseLong(temp1);
                    toLength = Long.parseLong(temp2);
                    contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
                }
            } else {
                contentLength = fileLength;
            }

            // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
            // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
            response.setHeader("Content-Length", Long.toString(contentLength));

            // 断点开始
            // 响应的格式是:
            // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
            if (rangeSwitch == 1) {
                String contentRange = "bytes " + p + "-" + (fileLength - 1) + "/" + fileLength;
                response.setHeader("Content-Range", contentRange);
                bis.skip(p);
            } else if (rangeSwitch == 2) {
                String contentRange = range.replace("=", " ") + "/" + fileLength;
                response.setHeader("Content-Range", contentRange);
                bis.skip(p);
            } else {
                String contentRange = "bytes " + "0-" + (fileLength - 1) + "/" + fileLength;
                response.setHeader("Content-Range", contentRange);
            }

            String fileName = file.getName();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

            OutputStream out = response.getOutputStream();
            int n;
            long readLength = 0;
            int bsize = 1024;
            byte[] bytes = new byte[bsize];
            if (rangeSwitch == 2) {
                // 针对 bytes=27000-39000 的请求，从27000开始写数据
                while (readLength <= contentLength - bsize) {
                    n = bis.read(bytes);
                    readLength += n;
                    out.write(bytes, 0, n);
                }
                if (readLength <= contentLength) {
                    n = bis.read(bytes, 0, (int) (contentLength - readLength));
                    out.write(bytes, 0, n);
                }
            } else {
                while ((n = bis.read(bytes)) != -1) {
                    out.write(bytes, 0, n);
                }
            }
            out.flush();
            out.close();
            bis.close();

        } catch (IOException ie) {
            // 忽略 ClientAbortException 之类的异常
            logger.log(Level.WARNING,ie::getMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
