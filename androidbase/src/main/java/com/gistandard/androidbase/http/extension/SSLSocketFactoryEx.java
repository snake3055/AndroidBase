package com.gistandard.androidbase.http.extension;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLSocketFactoryEx extends SSLSocketFactory {

    private SSLContext sslContext = SSLContext.getInstance("TLS");

    public SSLSocketFactoryEx() throws NoSuchAlgorithmException,
            KeyManagementException, KeyStoreException, UnrecoverableKeyException {

        super();
        TrustManager tm = new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {
                // TODO Auto-generated method stub
                return null;
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // TODO Auto-generated method stub

            }

            public void checkClientTrusted(X509Certificate[] chain, String authType)
                    throws CertificateException {
                // TODO Auto-generated method stub

            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);

    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
            throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    @Override
    public String[] getDefaultCipherSuites() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String[] getSupportedCipherSuites() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        // TODO Auto-generated method stub
        return sslContext.getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        // TODO Auto-generated method stub
        return sslContext.getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort)
            throws IOException, UnknownHostException {
        // TODO Auto-generated method stub
        return sslContext.getSocketFactory().createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress,
            int localPort) throws IOException {
        // TODO Auto-generated method stub
        return sslContext.getSocketFactory().createSocket(address, port, localAddress, localPort);
    }

}
