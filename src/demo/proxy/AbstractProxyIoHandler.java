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

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class of {@link org.apache.mina.core.service.IoHandler} classes which handle
 * proxied connections.
 *
 */
public abstract class AbstractProxyIoHandler extends IoHandlerAdapter {
    private static final Charset CHARSET = Charset.forName("UTF-8");
    public static final String OTHER_IO_SESSION = AbstractProxyIoHandler.class.getName() + ".OtherIoSession";

    private final static Logger LOGGER = LoggerFactory.getLogger(AbstractProxyIoHandler.class);
    
    @Override
    public void sessionCreated(IoSession session) throws Exception {
    	// suspend read/write operation for future session
    	// these will be resume when session open is complete
        session.suspendRead();
        session.suspendWrite();
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        if (session.getAttribute( OTHER_IO_SESSION ) != null) {
            IoSession sess = (IoSession) session.getAttribute(OTHER_IO_SESSION);
            sess.setAttribute(OTHER_IO_SESSION, null);
            sess.close(false);
            session.setAttribute(OTHER_IO_SESSION, null);
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        IoBuffer rb = (IoBuffer) message;
        IoBuffer wb = IoBuffer.allocate(rb.remaining());
        rb.mark();
        wb.put(rb);
        wb.flip();
        ((IoSession) session.getAttribute(OTHER_IO_SESSION)).write(wb);
        rb.reset();
        LOGGER.info(rb.getString(CHARSET.newDecoder()));
    }
}
