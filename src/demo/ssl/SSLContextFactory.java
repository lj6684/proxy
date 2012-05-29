package demo.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;

import java.util.Properties;
import java.util.prefs.Preferences;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class SSLContextFactory {
	private static SSLContext instance = null;

	public static SSLContext newInstance(SSLConfig sslConfig) throws Exception {
		return createSSLContext(sslConfig);
	}

	/**
	 * ´´½¨ServerÄ£Ê½SSLContext
	 * 
	 * @return SSLContextÊµÀý
	 * @throws Exception
	 */
	private static SSLContext createSSLContext(SSLConfig sslConfig) throws Exception {
		SSLContext ctx = null;
		KeyManagerFactory kmf = null;
		TrustManagerFactory tmf = null;

		ctx = SSLContext.getInstance(sslConfig.getProtocol());

		KeyStore keyStore = KeyStore.getInstance("JKS");
		String keyStorePath = sslConfig.getKeyStorePath();
		String keyStorePassword = sslConfig.getKeyStorePassword();
		FileInputStream keyStoreFileIns = new FileInputStream(keyStorePath);
		keyStore.load(keyStoreFileIns, keyStorePassword.toCharArray());
		keyStoreFileIns.close();
		kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(keyStore, keyStorePassword.toCharArray());

		KeyStore trustStore = KeyStore.getInstance("JKS");
		String trustStorePath = sslConfig.getTrustStorePath();
		String trustStorePassword = sslConfig.getTrustStorePassword();
		FileInputStream trustStoreFileIns = new FileInputStream(trustStorePath);
		trustStore.load(trustStoreFileIns, trustStorePassword.toCharArray());
		trustStoreFileIns.close();

		tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(trustStore);

		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		return ctx;
	}
}
