package com.travelzen.framework.http.irsession;

import java.io.IOException;

import org.eclipse.jetty.http.HttpException;
import org.eclipse.jetty.http.Parser;
import org.eclipse.jetty.io.Buffer;
import org.eclipse.jetty.io.ByteArrayBuffer;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.EndPoint;
import org.eclipse.jetty.io.SimpleBuffers;
import org.eclipse.jetty.server.AbstractHttpConnection;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpConnection extends AbstractHttpConnection {

	private Logger logger = LoggerFactory.getLogger(HttpConnection.class);

	protected final Parser _parser;

	public HttpConnection(EndPoint endpoint) {
		super(new LocalConnector(), endpoint, new Server(80));

		SimpleBuffers buffers = new SimpleBuffers(new ByteArrayBuffer(1024), new ByteArrayBuffer(1024));
		
		_parser = new HttpParser(buffers, endpoint, new RequestHandler());
	}

	@Override
	public Connection handle() throws IOException {

		try {

			// do while the endpoint is open
			// AND the connection has not changed
			try {
				// If we are not ended then parse available
				_parser.parseAvailable();

			} catch (HttpException e) {
				logger.debug("uri=" + _uri);
				logger.debug("fields=" + _requestFields);
				logger.debug(e.getLocalizedMessage());
				_generator.sendError(e.getStatus(), e.getReason(), null, true);
				_parser.reset();
				_endp.shutdownOutput();
			} finally {

				_parser.reset();
				_endp.close();

			}

			return this;
		} finally {
			_parser.returnBuffers();
		}

	}

	/* ------------------------------------------------------------ */
	/* ------------------------------------------------------------ */
	/* ------------------------------------------------------------ */
	private class RequestHandler extends HttpParser.EventHandler {
		/*
		 * 
		 * @see
		 * org.eclipse.jetty.server.server.HttpParser.EventHandler#startRequest
		 * (org.eclipse.io.Buffer, org.eclipse.io.Buffer, org.eclipse.io.Buffer)
		 */
		@Override
		public void startRequest(Buffer method, Buffer uri, Buffer version)
				throws IOException {
			HttpConnection.this.startRequest(method, uri, version);
		}

		/*
		 * @see
		 * org.eclipse.jetty.server.server.HttpParser.EventHandler#parsedHeaderValue
		 * (org.eclipse.io.Buffer)
		 */
		@Override
		public void parsedHeader(Buffer name, Buffer value) throws IOException {
			HttpConnection.this.parsedHeader(name, value);
		}

		/*
		 * @see
		 * org.eclipse.jetty.server.server.HttpParser.EventHandler#headerComplete
		 * ()
		 */
		@Override
		public void headerComplete() throws IOException {
			HttpConnection.this.headerComplete();
		}

		/* ------------------------------------------------------------ */
		/*
		 * @see
		 * org.eclipse.jetty.server.server.HttpParser.EventHandler#content(int,
		 * org.eclipse.io.Buffer)
		 */
		@Override
		public void content(Buffer ref) throws IOException {
			HttpConnection.this.content(ref);
		}

		/* ------------------------------------------------------------ */
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jetty.server.server.HttpParser.EventHandler#messageComplete
		 * (int)
		 */
		@Override
		public void messageComplete(long contentLength) throws IOException {
			HttpConnection.this.messageComplete(contentLength);
		}

		/* ------------------------------------------------------------ */
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jetty.server.server.HttpParser.EventHandler#startResponse
		 * (org.eclipse.io.Buffer, int, org.eclipse.io.Buffer)
		 */
		@Override
		public void startResponse(Buffer version, int status, Buffer reason) {
			logger.debug("Bad request!: " + version + " " + status + " "
					+ reason);
		}
	}

}
