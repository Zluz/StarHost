package jmr.home.comm.http;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import jmr.home.rap.UserInterfaceRemote;

public class HttpAtomConsumer {

	private HttpServer server;
    
	
	public HttpAtomConsumer(	final int iPort,
								final String strEndpoint ) {
		try {
			System.out.println( "Hosting port " + iPort );
			final InetSocketAddress port = new InetSocketAddress( iPort );
			server = HttpServer.create( port, 0 );
			
	        server.createContext( strEndpoint, new MessageHandler() );
	        server.setExecutor(null); // creates a default executor
	        server.start();
			
		} catch ( final IOException e ) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

    static class MessageHandler implements HttpHandler {
        @Override
        public void handle( final HttpExchange exchange ) throws IOException {
			if ( null==exchange ) return;
			
//			final InetAddress addrRemote = exchange.getRemoteAddress().getAddress();
			final URI uri = exchange.getRequestURI();
			final String strURI = exchange.getRequestURI().toString();

			final String strMessage = uri.getQuery();
			
			UserInterfaceRemote.processMessage( strMessage );
			
			
			final String strResponse = 
					Integer.toString( strURI.getBytes().length ) 
					+ " byte(s) received.";


        	try ( final OutputStream os = exchange.getResponseBody() ) {

                final byte[] bytes = strResponse.getBytes();
				exchange.sendResponseHeaders( 200, bytes.length );

                os.write( bytes );
                os.close();
                
        	} catch ( final IOException e ) {
        		//TODO handle this
        		e.printStackTrace();
        	}
        }
    }
    
    
    public static void main( final String[] args ) {
		final HttpAtomConsumer server = new HttpAtomConsumer( 8090, "/test" );
		for (;;);
	}
}
