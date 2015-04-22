package com.icrane.quickmode.utils.common;

import android.annotation.TargetApi;
import android.os.Build;

import com.icrane.quickmode.cache.BasicLruCache;
import com.icrane.quickmode.cache.CacheType;
import com.icrane.quickmode.cache.LruCache;
import com.icrane.quickmode.cache.LruCache2;
import com.icrane.quickmode.http.RequestMethod;
import com.icrane.quickmode.http.exec.data.HttpRequestForm;
import com.icrane.quickmode.utils.Charset;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpVersion;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.UnsupportedEncodingException;
import java.net.CookiePolicy;
import java.util.List;
import java.util.Locale;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public final class HttpUtils {

    /* HttpProtocolParams */
    public static final HttpVersion DEFAULT_HTTP_VERSION = HttpVersion.HTTP_1_1;
    public static final boolean DEFAULT_USE_EXPECT_CONTINUE = false;

    public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Linux;U;Android 4.4x;en-us;Nexus One Build.FRG83;Macintosh) "
            + "AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Mobile Chrome/39.0.2171.99 Safari/537.36";

    /* HttpConnectionParams */
    public static final int DEFAULT_CONNECTION_TIMEOUT = 8 * 1000;
    public static final int DEFAULT_SOCKET_BUFFER_SIZE = 8 * 1024;

    public static final int DEFAULT_LINGER = 0;
    public static final int DEFAULT_SO_TIMEOUT = DEFAULT_CONNECTION_TIMEOUT;

    public static final boolean DEFAULT_STALE_CHECKING = false;
    public static final boolean DEFAULT_TCP_NO_DELAY = false;

    /* ConnManagerParams */
    public static final int DEFAULT_SOCKET_TIMEOUT = 1000;
    public static final int DEFAULT_HOST_CONNECTIONS = 20;
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS = 20;

    /* HttpClientParams */
    public static final boolean AUTHENTICATING = false;
    public static final CookiePolicy COOKIE_POLICY = CookiePolicy.ACCEPT_ALL;

    /* SchemeRegistry */
    public static final String SCHEME_HTTP = "http";
    public static final String SCHEME_HTTPS = "https";

    /**
     * port *
     */
    public static final int SCHEME_HTTP_PORT = 80;
    public static final int SCHEME_HTTPS_PORT = 443;

    /**
     * 创建一个Scheme对象
     *
     * @param name    协议名
     * @param factory 套接字工厂
     * @param port    端口号
     * @return 返回Scheme对象
     */
    public static Scheme createScheme(String name, SocketFactory factory, int port) {
        return new Scheme(name, factory, port);
    }

    /**
     * 添加要注册的端口集
     *
     * @param schemes Scheme对象可变数组
     * @return SchemeRegistry对象
     */
    public static SchemeRegistry createSchemeRegistry(Scheme... schemes) {

        SchemeRegistry schemeRegistry = new SchemeRegistry();

        for (Scheme scheme : schemes) {
            schemeRegistry.register(scheme);
        }

        return schemeRegistry;
    }

    /**
     * 创建请求头
     *
     * @param name  请求头名称
     * @param value 请求头值
     * @return 请求头
     */
    public static Header createHeader(String name, String value) {
        return new BasicHeader(name, value);
    }

    /**
     * 实例化一个HttpGet对象
     *
     * @param uri  请求地址
     * @param form 请求表单
     * @return HttpGet对象
     */
    public static HttpGet newHttpGet(String uri, HttpRequestForm form) {

        // 获取请求表单参数
        uri = uri + form.getParametersForm();

        // 生成HttpGet对象
        return new HttpGet(uri);
    }

    /**
     * 实例化一个HttpPost对象
     *
     * @param uri  请求地址
     * @param form 请求表单
     * @return HttpPost对象
     */
    @SuppressWarnings("unchecked")
    public static HttpPost newHttpPost(String uri, HttpRequestForm form) {

        HttpPost httpPost = null;

        try {

            List<BasicNameValuePair> nameValuePair = (List<BasicNameValuePair>) form.getParametersForm();
            HttpEntity requestEntity = new UrlEncodedFormEntity(nameValuePair);

            // 生成HttpPost对象
            httpPost = new HttpPost(uri);

            // 作为实体添加进HttpPost对象中
            httpPost.setEntity(requestEntity);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return httpPost;
    }

    /**
     * 获取Charset
     *
     * @param charsetName 字符编码名
     * @return 字符编码枚举对象
     */
    public static Charset getEncodingCharset(String charsetName) {

        String upperCharSetName = charsetName.toUpperCase(Locale.ENGLISH);

        if (upperCharSetName.equals("UTF-8")) {

            return Charset.UTF_8;

        } else if (upperCharSetName.equals("GB2312")) {

            return Charset.GB_2312;

        } else if (upperCharSetName.equals("GBK")) {

            return Charset.GBK;

        } else if (upperCharSetName.equals("BIG-5")) {

            return Charset.BIG_5;

        } else if (upperCharSetName.equals("ISO-8859-1")) {

            return Charset.ISO_8859_1;

        } else if (upperCharSetName.equals("GB-18030")) {

            return Charset.GB_18030;

        }

        return Charset.UTF_8;
    }

    /**
     * 添加请求头
     *
     * @param requestBase 请求对象
     * @param headers     可变数组，请求头数组
     */
    public static void addHeaders(HttpRequestBase requestBase, Header... headers) {

        if (CommonUtils.isEmpty(headers))
            return;

        for (Header header : headers) {
            requestBase.addHeader(header);
        }

    }

    /**
     * 根据请求方式不同请求对象
     *
     * @param uri    请求地址
     * @param method 请求方法
     * @param form   请求表单
     * @return 请求对象
     * @throws java.io.UnsupportedEncodingException 不支持编码异常
     */
    public static HttpRequestBase switchRequestBase(String uri, RequestMethod method, HttpRequestForm form) throws UnsupportedEncodingException {
        return (method == RequestMethod.GET) ? newHttpGet(uri, form) : newHttpPost(uri, form);
    }

    /**
     * 根据缓存类型选择缓存对象
     *
     * @param cacheType 缓存类型
     * @return 缓存对象
     */
    public static BasicLruCache switchCache(CacheType cacheType) {

        switch (cacheType) {
            case LRU:
                return new LruCache(0);
            case LRU2:
                return new LruCache2(0, 0);
            default:
                return new LruCache(0);
        }

    }

    /**
     * http协议属性
     *
     * @param userAgent         用户代理
     * @param contentCharset    内容字符编码
     * @param elementCharset    标签字符编码
     * @param useExpectContinue 使用期望连续握手
     * @param version           http协议版本
     * @param httpParams        http属性对象
     */
    public static void setHttpProtocolParams(String userAgent, Charset contentCharset, Charset elementCharset,
                                             boolean useExpectContinue, HttpVersion version, HttpParams httpParams) {

        if (CommonUtils.isEmpty(httpParams)) throw new NullPointerException("HttpParams is null!");

        String contentCharsetStr = contentCharset.obtain();
        if (CommonUtils.isEmpty(contentCharsetStr))
            throw new NullPointerException("Content Charset is not null or \"\"!");
        // 内容字符编码
        HttpProtocolParams.setContentCharset(httpParams, contentCharsetStr);

        String elementCharsetStr = elementCharset.obtain();
        if (CommonUtils.isEmpty(elementCharsetStr))
            throw new NullPointerException("Element Charset is not null or \"\"!");
        // 标签字符编码
        HttpProtocolParams.setHttpElementCharset(httpParams, elementCharsetStr);

        HttpProtocolParams.setUseExpectContinue(httpParams, useExpectContinue);

        if (CommonUtils.isEmpty(userAgent))
            throw new NullPointerException("User Agent is null!");
        // 用户代理
        HttpProtocolParams.setUserAgent(httpParams, userAgent);

        if (CommonUtils.isEmpty(version))
            throw new NullPointerException("version is null!");
        // Http协议版本
        HttpProtocolParams.setVersion(httpParams, version);
    }

    /**
     * http链接属性
     *
     * @param connTimeout   连接到服务器超时时间
     * @param linger        值为0意味着该选项被禁用。值1意味着使用默认的
     * @param buffSize      缓冲大小
     * @param soTime        从服务器传输数据超时时间
     * @param staleChecking 过期检查
     * @param tcpNoDelay    Nagle算法
     * @param httpParams    http属性对象
     */
    public static void setHttpConnectionParams(int connTimeout, int linger, int buffSize, int soTime, boolean staleChecking,
                                               boolean tcpNoDelay, HttpParams httpParams) {

        HttpConnectionParams.setConnectionTimeout(httpParams, connTimeout);
        HttpConnectionParams.setLinger(httpParams, linger);
        HttpConnectionParams.setSocketBufferSize(httpParams, buffSize);
        HttpConnectionParams.setSoTimeout(httpParams, soTime);
        HttpConnectionParams.setStaleCheckingEnabled(httpParams, staleChecking);
        HttpConnectionParams.setTcpNoDelay(httpParams, tcpNoDelay);

    }

    /**
     * 设置连接管理器属性
     *
     * @param connPerRoute        每个路由的最大连接主机
     * @param maxTotalConnections 设置最大总连接数
     * @param timeout             从连接池连接超时时间
     * @param httpParams          http属性对象
     */
    public static void setConnManagerParams(ConnPerRoute connPerRoute, int maxTotalConnections, long timeout, HttpParams httpParams) {

        ConnManagerParams.setMaxConnectionsPerRoute(httpParams, connPerRoute);
        ConnManagerParams.setMaxTotalConnections(httpParams,
                maxTotalConnections);
        ConnManagerParams.setTimeout(httpParams, timeout);

    }

    /**
     * 设置客户端属性
     *
     * @param authenticating 是否需要认证
     * @param cookiePolicy   Cookie策略
     * @param Redirecting    是否需要重定向
     * @param httpParams     http属性对象
     */
    public static void setHttpClientParams(boolean authenticating, String cookiePolicy, boolean Redirecting, HttpParams httpParams) {

        HttpClientParams.setAuthenticating(httpParams, authenticating);
        if (!CommonUtils.isEmpty(cookiePolicy))
            HttpClientParams.setCookiePolicy(httpParams, cookiePolicy);
        HttpClientParams.setRedirecting(httpParams, Redirecting);

    }

}
