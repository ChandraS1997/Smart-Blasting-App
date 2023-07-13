package com.mineexcellence.sblastingapp.api;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import kotlin.jvm.internal.Intrinsics;


public class SslUtils {
    public static final SslUtils INSTANCE;

    @NotNull
    public SSLContext getSslContextForCertificateFile(@NotNull Context context) throws Throwable {
        Intrinsics.checkNotNullParameter(context, "context");

        try {
            KeyStore keyStore = this.getKeyStore(context);
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            Intrinsics.checkNotNullExpressionValue(trustManagerFactory, "trustManagerFactory");
            sslContext.init((KeyManager[])null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            Intrinsics.checkNotNullExpressionValue(sslContext, "sslContext");
            return sslContext;
        } catch (Exception var5) {
            String msg = "Error during creating SslContext for certificate from assets";
            var5.printStackTrace();
            throw (Throwable)(new RuntimeException(msg));
        }
    }

    public KeyStore getKeyStore(Context context) {
        KeyStore keyStore = (KeyStore)null;

        try {
            AssetManager assetManager = context.getAssets();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            String[] certs = new String[]{"Certificates.cer"};
            String keyStoreType = KeyStore.getDefaultType();
            keyStore = KeyStore.getInstance(keyStoreType);
            Intrinsics.checkNotNull(keyStore);
            keyStore.load((InputStream)null, (char[])null);
            int i = 1;

            for(int var8 = certs.length + 1; i < var8; ++i) {
                InputStream var10000 = assetManager.open(certs[i - 1]);
                Intrinsics.checkNotNullExpressionValue(var10000, "assetManager.open(certs[i - 1])");
                Certificate ca ;

                try {
                    Certificate var16 = cf.generateCertificate(var10000);
                    Intrinsics.checkNotNullExpressionValue(var16, "cf.generateCertificate(caInput)");
                    ca = var16;
                    StringBuilder var10001 = (new StringBuilder()).append("ca=");

                    Log.d("SslUtilsAndroid", var10001.append(((X509Certificate)ca).getSubjectDN()).toString());
                } finally {
                    var10000.close();
                }

                keyStore.setCertificateEntry("ca" + i, ca);
            }
        } catch (Exception var15) {
            var15.printStackTrace();
            Log.d("SSL", var15.getLocalizedMessage());
        }

        return keyStore;
    }

    @Nullable
    public final SSLSocketFactory getTrustAllHostsSSLSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{(TrustManager)(new X509TrustManager() {
                @NotNull
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(@NotNull X509Certificate[] chain, @NotNull String authType) {
                    Intrinsics.checkNotNullParameter(chain, "chain");
                    Intrinsics.checkNotNullParameter(authType, "authType");
                }

                public void checkServerTrusted(@NotNull X509Certificate[] chain, @NotNull String authType) {
                    Intrinsics.checkNotNullParameter(chain, "chain");
                    Intrinsics.checkNotNullParameter(authType, "authType");
                }
            })};
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init((KeyManager[])null, trustAllCerts, new SecureRandom());
            Intrinsics.checkNotNullExpressionValue(sslContext, "sslContext");
            return sslContext.getSocketFactory();
        } catch (KeyManagementException | NoSuchAlgorithmException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public SslUtils() {
    }

    static {
        INSTANCE = new SslUtils();
    }
}
