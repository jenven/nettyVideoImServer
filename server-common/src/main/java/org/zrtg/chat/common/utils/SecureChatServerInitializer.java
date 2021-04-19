package org.zrtg.chat.common.utils;

import org.springframework.beans.factory.annotation.Autowired;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * @author wangq
 * @create_at 2021-4-19 9:58
 */
public class SecureChatServerInitializer
{

    @Autowired
    private SSLContext sslContext;

    public static SSLContext createSSLContext(String type ,InputStream stream ,String password) throws Exception {
        KeyStore ks = KeyStore.getInstance(type); /// "JKS"
        ks.load(stream, password.toCharArray());
        //KeyManagerFactory充当基于密钥内容源的密钥管理器的工厂。
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());//getDefaultAlgorithm:获取默认的 KeyManagerFactory 算法名称。
        kmf.init(ks, password.toCharArray());
        //SSLContext的实例表示安全套接字协议的实现，它充当用于安全套接字工厂或 SSLEngine 的工厂。
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), null, null);
        return sslContext;
    }
}
