package demo.ssl;

public class SSLConfig {
	
    private String keyStorePath;
    private String keyStorePassword;

    private String trustStorePath;
    private String trustStorePassword;

    private String needClientAuth;
    private String protocol;
    private String sessionTimeout;
    private String connectMode;
    
    
	public String getConnectMode() {
		return connectMode;
	}
	public void setConnectMode(String connectMode) {
		this.connectMode = connectMode;
	}
	public String getKeyStorePath() {
		return keyStorePath;
	}
	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}
	public String getKeyStorePassword() {
		return keyStorePassword;
	}
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	public String getTrustStorePath() {
		return trustStorePath;
	}
	public void setTrustStorePath(String trustStorePath) {
		this.trustStorePath = trustStorePath;
	}
	public String getTrustStorePassword() {
		return trustStorePassword;
	}
	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}
	public String getNeedClientAuth() {
		return needClientAuth;
	}
	public void setNeedClientAuth(String needClientAuth) {
		this.needClientAuth = needClientAuth;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getSessionTimeout() {
		return sessionTimeout;
	}
	public void setSessionTimeout(String sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}
	

}
