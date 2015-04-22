package com.icrane.quickmode.utils.common;

import com.icrane.quickmode.utils.Charset;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public final class Streams {
    /**
     * 获取BufferedReader
     *
     * @param is          输入流
     * @param charsetName 编码名称
     * @return 返回BufferedReader
     * @throws java.io.UnsupportedEncodingException {@link java.io.UnsupportedEncodingException}
     */
    public static BufferedReader obtainBufferedReader(InputStream is,
                                                      Charset charsetName) throws UnsupportedEncodingException {
        return new BufferedReader(new InputStreamReader(is,
                charsetName.obtain()));
    }

    /**
     * 获取BufferedReader
     *
     * @param reader      输入流
     * @param charsetName 字符编码
     * @return 返回BufferedReader
     * @throws java.io.UnsupportedEncodingException {@link java.io.UnsupportedEncodingException}
     */
    public static BufferedReader obtainBufferedReader(Reader reader,
                                                      Charset charsetName) throws UnsupportedEncodingException {
        return new BufferedReader(reader);
    }

    /**
     * 获取BufferedReader
     *
     * @param is 输入流
     * @return 返回BufferedReader
     * @throws java.io.UnsupportedEncodingException {@link java.io.UnsupportedEncodingException}
     */
    public static BufferedReader obtainBufferedReader(InputStream is)
            throws UnsupportedEncodingException {
        return obtainBufferedReader(is, Charset.UTF_8);
    }

    /**
     * 获取FileInputStream
     *
     * @param filePath 文件路径
     * @return 返回FileReader
     * @throws java.io.FileNotFoundException {@link java.io.FileNotFoundException}
     */
    public static FileInputStream obtainFileInputStream(String filePath)
            throws FileNotFoundException {
        return new FileInputStream(new File(filePath));
    }

    /**
     * 获取FileReader
     *
     * @param filePath 文件路径
     * @return 返回FileReader
     * @throws java.io.FileNotFoundException {@link java.io.FileNotFoundException}
     */
    public static FileReader obtainFileReader(String filePath)
            throws FileNotFoundException {
        return new FileReader(new File(filePath));
    }

    /**
     * 获取BufferWriter
     *
     * @param out 一个输出流
     * @return BufferedWriter 带缓存可写入的流
     */
    public static BufferedWriter obtainBufferedWriter(OutputStream out) {
        return obtainBufferedWriter(out, Charset.UTF_8);
    }

    /**
     * 获取BufferWrter
     *
     * @param writer 输出流
     * @return BufferedWriter 带缓存可写入的流
     */
    public static BufferedWriter obtainBufferedWriter(Writer writer) {
        return new BufferedWriter(writer);
    }

    /**
     * 获取BufferWriter
     *
     * @param out     一个输出流
     * @param charset 字符编码
     * @return BufferedWriter 带缓存可写入的流
     */
    public static BufferedWriter obtainBufferedWriter(OutputStream out,
                                                      Charset charset) {
        BufferedWriter bufferedWriter = null;
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(out,
                    charset.obtain()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return bufferedWriter;
    }

    /**
     * 获取FileOutputStream
     *
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @return FileOutputStream 文件输出流
     * @throws java.io.IOException {@link java.io.IOException}
     */
    public static FileOutputStream obtainFileOutputStream(String filePath, String fileName) throws IOException {
        File file = new File(filePath);
        File newFile = new File(filePath + fileName);
        if (!file.exists())
            file.mkdir();
        return new FileOutputStream(newFile);
    }

    /**
     * 获取FileWriter
     *
     * @param filePath 文件路径
     * @return FileWriter 文件写入流
     * @throws java.io.IOException {@link java.io.IOException}
     */
    public static FileWriter obtainFileWriter(String filePath)
            throws IOException {
        return new FileWriter(new File(filePath));
    }

    /**
     * 读取操作
     *
     * @param br 带缓存可读取的流
     * @return 字符串对象
     */
    public static String read(BufferedReader br) {
        String temp = "";
        StringBuffer buff = new StringBuffer();
        try {
            while ((temp = br.readLine()) != null) {
                buff.append(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buff.toString();
    }

    /**
     * 读取byte[]
     *
     * @param is       输入流
     * @param buffSize 缓冲大小
     * @param length   读取长度
     * @return 返回byte[]
     */
    public static byte[] read(InputStream is, int buffSize, long length) {
        if (buffSize <= 0)
            throw new IllegalArgumentException("buffSize cannot be '<=0'");
        if (length <= 0)
            throw new IllegalArgumentException("length cannot be '<=0'");
        byte[] buffer = new byte[buffSize];
        ByteArrayOutputStream out = new ByteArrayOutputStream((int) length);
        int size = 0;
        try {
            while ((size = is.read(buffer)) != -1) {
                out.write(buffer, 0, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return out.toByteArray();
    }

    /**
     * 写入操作
     *
     * @param writer  BufferedWriter
     * @param content 内容
     * @return true写入成功，反之为false
     */
    public static boolean writer(BufferedWriter writer, String content) {
        try {
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
