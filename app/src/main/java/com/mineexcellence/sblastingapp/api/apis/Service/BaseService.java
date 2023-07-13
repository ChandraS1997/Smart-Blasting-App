package com.mineexcellence.sblastingapp.api.apis.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mineexcellence.sblastingapp.api.HostSelectionInterceptor;
import com.mineexcellence.sblastingapp.api.RestUtils;
import com.mineexcellence.sblastingapp.api.SslUtils;
import com.mineexcellence.sblastingapp.app.BaseApplication;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.BuildConfig;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.TlsVersion;
import okhttp3.internal.Util;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class BaseService {
    private static Retrofit retrofit;

    public static Retrofit getAPIClient(String baseUrl) {
//        if (retrofit == null) {
        Builder okHttpClient = null;
        TrustManagerFactory trustManagerFactory;
        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, null);

            okHttpClient = new OkHttpClient()
                    .newBuilder()
                    .addInterceptor(chain -> {
                        Request request;
                        if (BaseApplication.getPreferenceManger().getAuthToken().equalsIgnoreCase("")){
                            request = chain.request().newBuilder()
                                    .build();
                        }else{
                            request = chain.request().newBuilder().addHeader("Accept","application/json")
                                    .build();
                        }


                        return chain.proceed(request);
                    })
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .protocols(Util.immutableListOf(Protocol.HTTP_1_1))
                    .writeTimeout(3, TimeUnit.MINUTES);

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
            okHttpClient.addInterceptor(HostSelectionInterceptor.getSelectionInterceptor());
            okHttpClient.addInterceptor(loggingInterceptor);

            if (SslUtils.INSTANCE.getTrustAllHostsSSLSocketFactory() != null) {
                okHttpClient.sslSocketFactory(SslUtils.INSTANCE.getTrustAllHostsSSLSocketFactory(), trustManager);
            }

            try {
                okHttpClient.sslSocketFactory(SslUtils.INSTANCE.getSslContextForCertificateFile(BaseApplication.getInstance().getApplicationContext()).getSocketFactory(), trustManager);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }

        OkHttpClient mOkHttpClient = getUnsafeOkHttpClient();
        if (baseUrl.equalsIgnoreCase(Constants.API_BLADES_URL) || baseUrl.equalsIgnoreCase(Constants.API_IMAGE_VIDEO_BASE_URL) || baseUrl.equalsIgnoreCase(Constants.TEST_BLAST_S_BLAST_BASE_URL) || baseUrl.equalsIgnoreCase(Constants.BLAST_S_BLAST_BASE_URL)) {
            mOkHttpClient = okHttpClient.build();
        } else {
            mOkHttpClient = getUnsafeOkHttpClient();
        }

        //  .addConverterFactory(new NullOnEmptyConverterFactory())
        //                    .client(getUnsafeOkHttpClient())
        retrofit = new Retrofit.Builder().baseUrl(RestUtils.getEndPoint(baseUrl))
                // .addConverterFactory(new NullOnEmptyConverterFactory())
                .client(mOkHttpClient)
                .addConverterFactory(GsonConverterFactory.create(createGSON()))
                .build();
        return retrofit;
    }

    private static Gson createGSON(){
        return new GsonBuilder().setLenient().create();
    }

    private static OkHttpClient getUnsafeOkHttpClient() {

        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                .tlsVersions(TlsVersion.TLS_1_2, TlsVersion.TLS_1_1, TlsVersion.TLS_1_0)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA)
                .build();

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            Builder builder = new Builder();
//
//            if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
//
//            }


            builder.connectionSpecs(Collections.singletonList(spec));
            builder.readTimeout(5, TimeUnit.MINUTES);
            builder.connectTimeout(5,TimeUnit.MINUTES);
            builder.setProtocols$okhttp(Arrays.asList(Protocol.HTTP_1_1));
            if(BuildConfig.DEBUG){
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(Level.BODY);
                builder.addInterceptor(interceptor);
                builder.interceptors().add(interceptor);
            }

            // builder.addInterceptor(headerAuthorizationInterceptor);

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
