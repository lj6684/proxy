/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package demo.proxy;

import java.io.FileInputStream;
import java.net.InetSocketAddress;
import java.util.Properties;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.ssl.SslFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import demo.ssl.SSLConfig;
import demo.ssl.SSLContextFactory;

/**
 * (<b>Entry point</b>) Demonstrates how to write a very simple tunneling proxy
 * using MINA. The proxy only logs all data passing through it. This is only
 * suitable for text based protocols since received data will be converted into
 * strings before being logged.
 * <p>
 * Start a proxy like this:<br/>
 * <code>org.apache.mina.example.proxy.Main 12345 www.google.com 80</code><br/>
 * and open <a href="http://localhost:12345">http://localhost:12345</a> in a
 * browser window.
 * </p>
 * 
 * @author <a href="http://mina.apache.org">Apache MINA Project</a>
 */
public class Proxy {

	public static void main(String[] args) throws Exception {
		
		SSLConfig sslConfig = new SSLConfig();
		Properties props = new Properties();
		FileInputStream fins = new FileInputStream("./config.properties");
		props.load(fins);
		fins.close();
		sslConfig.setConnectMode(props.getProperty("connectMode"));
		sslConfig.setKeyStorePath(props.getProperty("keyStorePath"));
		sslConfig.setKeyStorePassword(props.getProperty("keyStorePassword"));
		sslConfig.setTrustStorePath(props.getProperty("trustStorePath"));
		sslConfig.setTrustStorePassword(props.getProperty("trustStorePassword"));
		sslConfig.setProtocol(props.getProperty("protocol"));
		sslConfig.setSessionTimeout(props.getProperty("sessionTimeout"));
		sslConfig.setNeedClientAuth(props.getProperty("needClientAuth"));
		
		String connectMode = props.getProperty("connectMode");
		String listenerPort = props.getProperty("listenerPort");
		String remoteIP = props.getProperty("remoteIP");
		String remotePort = props.getProperty("remotePort");
		
		// Create TCP/IP acceptor.
		NioSocketAcceptor acceptor = new NioSocketAcceptor();

		// Create TCP/IP connector.
		IoConnector connector = new NioSocketConnector();

		// Set connect timeout.
		connector.setConnectTimeoutMillis(30 * 1000L);
		if(connectMode.equalsIgnoreCase("client")) {
			// force to connect remote target use SSL mode
			DefaultIoFilterChainBuilder filterChain = connector.getFilterChain();
			SslFilter sslFilter = new SslFilter(SSLContextFactory.newInstance(sslConfig));
			sslFilter.setUseClientMode(true);
			filterChain.addFirst("ssl_client_filter", sslFilter);
		}

		ClientToProxyIoHandler handler = new ClientToProxyIoHandler(
													connector, 
													new InetSocketAddress(remoteIP, Integer.parseInt(remotePort)));

		// Start proxy.
		acceptor.setHandler(handler);
		if(connectMode.equalsIgnoreCase("server")) {
			// accept connection use SSL mode
			DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();
			SslFilter sslFilter = new SslFilter(SSLContextFactory.newInstance(sslConfig));
			sslFilter.setNeedClientAuth(Boolean.parseBoolean(sslConfig.getNeedClientAuth()));
			filterChain.addFirst("ssl_server_filter", sslFilter);
		}
		acceptor.bind(new InetSocketAddress(Integer.parseInt(listenerPort)));

		System.out.println("Listening on port " + Integer.parseInt(listenerPort));
	}

}
