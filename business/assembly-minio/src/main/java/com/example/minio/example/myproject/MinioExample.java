package com.example.minio.example.myproject;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author qianxm
 * @date 2020/6/24 14:47
 */
public class MinioExample {

    public static void fileUpload(String endpoint, String accessKey,
                                  String secrtKey, String bucketName,
                                  String filename, String filepath) {
        try {

            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);
            }

            // 使用putObject上传一个文件到存储桶中。
            minioClient.putObject(bucketName, filename, filepath, null);
            System.out.println("/home/user/Photos/asiaphotos.zip is successfully uploaded as asiaphotos.zip to `asiatrip` bucket.");
        } catch (MinioException | NoSuchAlgorithmException | IOException | InvalidKeyException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public static void fileDownload(String endpoint, String accessKey,
                                    String secrtKey, String bucketName,
                                    String filename, String result) {
        try {

            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);

            }
            minioClient.getObject(bucketName, filename, result);


        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

    /**
     * 下载文件，通过读取流下载到本地.
     *
     * @param endpoint   minio访问地址url
     * @param accessKey  用户名
     * @param secrtKey   密码
     * @param bucketName 桶名称
     * @param filename   Minio文件 包括路径
     * @param result     本地文件（如果有父级目录请自行创建）
     */
    public static void download(String endpoint, String accessKey,
                                String secrtKey, String bucketName,
                                String filename, String result) {
        OutputStream out = null;
        InputStream in = null;

        try {
            // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);

            }

            // 使用putObject上传一个文件到存储桶中。
            in = minioClient.getObject(bucketName, filename);
            int len = 0;
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(result);

            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 根据文件字节其实位置读取指定长度的字符
    public static List<String> read(String endpoint, String accessKey,
                                    String secrtKey, String bucketName,
                                    String filename, long offset, Long length) {
        InputStream in = null;
        List<String> result = new ArrayList<>();
        try {
            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            } else {
                // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
                minioClient.makeBucket(bucketName);

            }

            // 使用putObject上传一个文件到存储桶中。
            in = minioClient.getObject(bucketName, filename, offset, length);
            // 读取输入流直到EOF并打印到控制台。
            byte[] buf = new byte[65535];
            int bytesRead;
            while ((bytesRead = in.read(buf)) >= 0) {
                System.out.println(new String(buf, 0, bytesRead));
                result.add(new String(buf, 0, bytesRead));
            }


            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 从offset行读到length行
     *
     * @param endpoint   访问地址
     * @param accessKey  用户名
     * @param secrtKey   密码
     * @param bucketName 桶名称
     * @param filename   文件名
     * @param offset     开始行
     * @param length     结束行
     * @return
     */
    public static List<String> readByLine(String endpoint, String accessKey,
                                          String secrtKey, String bucketName,
                                          String filename, int offset, int length) {
        InputStream in = null;
        List<String> result = new ArrayList<>();
        List<String> list = new ArrayList<>();
        try {
            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (!isExist) {
                System.out.println("Bucket not exists.");
            }
            minioClient.statObject(bucketName, filename);

            // 使用putObject上传一个文件到存储桶中。
            in = minioClient.getObject(bucketName, filename);
            // 读取输入流直到EOF并打印到控制台。
            byte[] buf = new byte[1024];
            int bytesRead;

            long i = 1;


            while ((bytesRead = in.read(buf)) >= 0) {
                result.add(new String(buf, 0, bytesRead));
            }


            for (int i1 = 0; i1 < result.size(); i1++) {
                String line = result.get(i1);
                String[] lines = line.split("\r\n", -1);
                list.addAll(new ArrayList<String>(Arrays.asList(lines)));
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        System.out.println(list.size());
        return list.subList(offset - 1, length);
    }

    /**
     * 列出桶里所有对象，即桶下所有文件.
     *
     * @param endpoint
     * @param accessKey
     * @param secrtKey
     * @param bucketName
     * @param filename
     */
    public static void listBucketNameObject(String endpoint, String accessKey,
                                            String secrtKey, String bucketName,
                                            String filename) {
        try {

            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            }
            // 检查'mybucket'是否存在。
            boolean found = minioClient.bucketExists(bucketName);
            if (found) {
                // 列出'my-bucketname'里的对象
                Iterable<Result<Item>> myObjects = minioClient.listObjects(bucketName);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());
                }
            } else {
                System.out.println("mybucket does not exist");
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> listObject(String endpoint, String accessKey,
                                          String secrtKey, String bucketName,
                                          String prefix, boolean recursive) {
        List<String> file = new ArrayList<>();
        try {

            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (isExist) {
                System.out.println("Bucket already exists.");
            }
            // 检查'mybucket'是否存在。
            boolean found = minioClient.bucketExists(bucketName);
            if (found) {
                // 列出'my-bucketname'里的对象
                Iterable<Result<Item>> myObjects = minioClient.listObjects(bucketName, prefix, recursive, false);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName() + "," + item.size());
                    file.add(item.objectName());
                }
            } else {
                System.out.println("mybucket does not exist");
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 将前缀中的所有对象都下载到本地目录

    /**
     * 批量下载文件.
     *
     * @param endpoint   访问地址
     * @param accessKey  账号
     * @param secrtKey   密码
     * @param bucketName 同名称
     * @param prefix     前缀名称 即桶中对象文件的前缀路径
     * @param resultPath 本地路径
     */
    public static void downFiles(String endpoint, String accessKey,
                                 String secrtKey, String bucketName,
                                 String prefix, String resultPath) {
        try {

            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);

            // 检查存储桶是否已经存在
            boolean isExist = minioClient.bucketExists(bucketName);
            if (!isExist) {
                System.out.println("Bucket not exists.");
                return;
            }
            // 检查'mybucket'是否存在。
            boolean found = minioClient.bucketExists(bucketName);
            // 如果目标文件不存在创建目录
            if (found) {
                // 列出'my-bucketname'里的对象
                Iterable<Result<Item>> myObjects = minioClient.listObjects(bucketName, prefix, true, false);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    String objectName = item.objectName();
                    String localfile = resultPath + File.separator + objectName.replaceAll(prefix, "");
                    File file = new File(localfile);
                    String parent = file.getParent();
                    if (!FileUtils.isExists(parent)) {
                        FileUtils.createDirs(parent);
                    }

                    minioClient.getObject(bucketName, objectName, localfile);
                }
            } else {
                System.out.println("mybucket does not exist");
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 拷贝文件，将桶1中的文件拷贝到另一个桶中，修改名称。
     *
     * @param endpoint
     * @param accessKey
     * @param secrtKey
     * @param bucketName
     * @param object
     * @param destBucketName
     */
    public static void copy(String endpoint, String accessKey,
                            String secrtKey, String bucketName,
                            String object, String destBucketName,
                            String destBucketObjectName) {
        try {

            MinioClient minioClient = new MinioClient(endpoint, accessKey, secrtKey);


            if (!minioClient.bucketExists(destBucketName)) {
                minioClient.makeBucket(destBucketName);
            }
            // 检查'mybucket'是否存在。
            boolean found = minioClient.bucketExists(bucketName);
            // 如果目标文件不存在创建目录
            if (found) {
//                minioClient.copyObject(destBucketName,destBucketObjectName,null,null,bucketName,object,null,null);

                minioClient.copyObject(destBucketName, destBucketObjectName, null, null,
                        bucketName, object, null, null);
                System.out.println("拷贝成功");

            } else {
                System.out.println("mybucket does not exist");
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
