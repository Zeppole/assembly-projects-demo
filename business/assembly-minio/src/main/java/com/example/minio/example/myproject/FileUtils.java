package com.example.minio.example.myproject;

//import lombok.extern.log4j.Log4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.io.File.separator;

/**
 * desc: 文件操作.
 * date: 2018-09-13
 *
 * @author qianxm
 */
//@Log4j
public class FileUtils {

    private static String charset = "UTF-8";
    private final static String GBK = "GBK";
//    private static Logger logger = Logger.getLogger(FileUtils.class);

    /**
     * 获取可写的文件通道，使用此通道会覆盖原有文件 使用结束手工关闭
     *
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static FileChannel getWriterFileChannel(String filePath) throws IOException {
        return getWriterFileChannel(filePath, false);
    }

    /**
     * 创建可写的 文件通道 ,使用结束手工关闭
     *
     * @param filePath 文件路径
     * @param append   是否添加到文件尾
     * @return
     * @throws IOException
     */
    public static FileChannel getWriterFileChannel(String filePath, boolean append) throws IOException {
        //创建文件或者文件夹
        File file = fileCreator(filePath);
        return new FileOutputStream(file, append).getChannel();
    }

    /**
     * 将数据通过文件通道快速写入
     *
     * @param channel 文件通道
     * @param content 文件内容
     * @throws IOException
     */
    public static void writeFile(FileChannel channel, String content) throws IOException {
        writeFile(channel, content, false);
    }

    /**
     * 将数据通过文件通道快速写入
     *
     * @param channel 文件通道
     * @param content 文件内容
     * @param isGBK   是否将utf8转换为gbk
     * @throws IOException
     */
    public static void writeFile(FileChannel channel, String content, boolean isGBK) throws IOException {
        byte[] bytes = content.getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        if (isGBK) {
            CharsetDecoder de = Charset.forName("UTF-8").newDecoder();
            CharBuffer cb = de.decode(byteBuffer);
            CharsetEncoder en = Charset.forName("GBK").newEncoder();
            byteBuffer = en.encode(cb);
        }
        channel.write(byteBuffer);
    }

    /**
     * 将文件内容写入到指定文件路径
     *
     * @param filePath
     * @param content
     * @throws IOException
     */
    public static void writeFile(String filePath, String content) throws IOException {
        FileChannel fc = getWriterFileChannel(filePath);
        writeFile(fc, content);
        fc.close();
    }

    /**
     * 指定字符集
     *
     * @param filePath 路径
     * @param content  内容
     * @param append   追加
     * @param charset  字符集
     * @throws IOException
     */
    public static void writeFile(String filePath, String content, boolean append, String charset) throws IOException {
        File file = fileCreator(filePath);//创建文件或者文件夹
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file, append), charset);
            osw.write(content);
            osw.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            if (osw != null) {
                osw.close();
            }
        }

    }


    /**
     * 获取文件格式：UTF-8、gbk
     *
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getFileType(String filePath) throws IOException {
        File file = new File(filePath);
        InputStream in = null;
        byte[] b = new byte[3];
        try {
            in = new FileInputStream(file);
            if (in.read(b) == -1) {
                throw new IOException(filePath + " is empty!");
            }
        } catch (FileNotFoundException fe) {
            throw fe;
        } finally {
            if (in != null) {
                in.close();
            }
        }
        //常见文件编码为GBK和UTF-8，对于UTF-8编码格式的文本文件，其前3个字节的值就是-17、-69、-65
        //中文在Windows下默认的编码是GBK
        if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
            return charset;
        } else {
            return GBK;
        }
    }

    /**
     * 获取文件总行数
     *
     * @param filePath 文件路径
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static int getFileLines(String filePath) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        int count = 0;
        while (br.readLine() != null) {
            count++;
        }
        br.close();
        return count;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return
     */
    public static boolean delete(String filePath) {
        File file = new File(filePath);
        boolean isDel = true;//文件不存在则已删除
        if (file.exists()) {
            isDel = file.delete();
        }
        return isDel;
    }

    /**
     * 删除多个文件
     *
     * @param filePath
     */
    public static void delete(String... filePath) {
        for (String file : filePath) {
            delete(file);
        }
    }

    /**
     * 删除目录
     *
     * @param dir 目录
     */
    public static void deleteDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            if (!file.delete()) {
//                log.error(" delete file fail! path: " + dir);
            }
        }

        if (file.isDirectory()) {
            String[] child = file.list();
            if (child != null && child.length > 0) {
                for (String childFile : child) {
                    deleteDir(dir + separator + childFile);
                }
            }
            if (!file.delete()) {
//                log.error("  delete file fail! path: " + file.getName());
            }
        }

    }

    /**
     * 如果不存在，则创建文件或者文件夹
     *
     * @param filePath 文件路径
     */
    public static File fileCreator(String filePath) throws IOException {
        File file = new File(filePath);
        if (file.exists()) {
            return file;
        }
        if (filePath.endsWith(separator)) {//如果以路径分隔符结尾 则为目录
            if (!file.mkdirs()) {
                throw new IOException("create file fail! filePath: " + filePath);
            }
        } else {
            File pf = file.getParentFile();
            if (!pf.exists()) {
                if (!pf.mkdirs()) {
                    throw new IOException(" create dirs fail! dir: " + pf.getPath());
                }
            }
            if (!file.createNewFile()) {
                throw new IOException(" create file fail! path: " + file.getPath());
            }
        }
        return file;
    }

    /**
     * 判断文件或文件夹是否存在
     */
    public static boolean isExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 创建多级目录
     * 注意：只创建文件夹,不包括文件
     *
     * @param path 文件目录,例如["c:/gzone/liang/"]
     * @return
     */
    public static boolean createDirs(String path) {
        File dir = new File(path);
        boolean flag = false;
        if (!dir.exists()) {
            flag = dir.mkdirs();
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 文件拷贝
     *
     * @param srcPath  源文件路径
     * @param destPath 目标文件路径
     * @throws IOException 异常
     */
    public static void fileChannelCopy(String srcPath, String destPath) throws IOException {
        fileChannelCopy(new File(srcPath), new File(destPath));
    }

    /**
     * 使用文件通道的方式复制文件
     *
     * @param s 源文件
     * @param t 复制到的新文件
     */
    public static void fileChannelCopy(File s, File t) throws IOException {
        if (!s.exists()) {
            return;
        }
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            if (!t.exists()) {
                if (!t.createNewFile()) {
                    throw new IOException("创建文件出错，" + t.getAbsolutePath());
                }
            }
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();// 得到对应的文件通道
            out = fo.getChannel();// 得到对应的文件通道
            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                if (fi != null) {
                    fi.close();
                }
                if (in != null) {
                    in.close();
                }
                if (fo != null) {
                    fo.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 读取文件行数，支持跳过开头的行数
     *
     * @param file     文件
     * @param charset  字符集
     * @param skipLine 跳过开头的行数
     * @return list
     */
    public static List<String> readLines(File file, String charset, int skipLine) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        String line;
        int i = -1;
        List<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            i++;
            if (i < skipLine) {
                continue;
            }
            list.add(line);
        }
        br.close();
        return list;
    }

    /**
     * 读取文件行数， 支持分页读取
     *
     * @param file    文件
     * @param charset 字符集
     * @param start   开始行数
     * @param size    容量
     * @return list
     */
    public static List<String> readLines(File file, String charset, int start, int size) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), charset));
        String line;
        int i = 0;
        List<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            i++;
            if (i < start) {
                continue;
            } else if (i >= (start + size)) {
                break;
            } else if (i >= start && i < (start + size)) {
                list.add(line);
            }
        }
        br.close();
        return list;
    }


    /**
     * 读取文件行数， 支持分页读取
     *
     * @param start 开始行数
     * @param size  容量
     * @return list
     */
    public static List<String> readLines(BufferedReader br, int start, int size) throws Exception {
        String line;
        int i = 0;
        List<String> list = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            System.out.println(i);
            i++;
            if (i < start) {
                continue;
            } else if (i >= (start + size)) {
                break;
            } else if (i >= start && i < (start + size)) {
                list.add(line);
            }
        }
        br.close();
        return list;
    }

    /**
     * 获取文件大小.
     *
     * @param fileName 文件名称
     * @return 返回文件大小
     */
    public static String getFileSize(String fileName) {
        File file = new File(fileName);
        String fileSize;
        long fileLength = file.length();
        DecimalFormat df = new DecimalFormat("#.00");
        if (fileLength < 1024) {
            fileSize = fileLength + "B";
        } else if (fileLength >= 1024 && fileLength < 1048576) {
            fileSize = df.format(new Double(fileLength) / 1024) + "KB";
        } else if (fileLength >= 1048576 && fileLength < 1073741824) {
            fileSize = df.format(new Double(fileLength) / (1048576)) + "MB";
        } else {
            fileSize = df.format(new Double(fileLength) / (1073741824)) + "GB";
        }
        return fileSize;
    }


}
