package jmr.home.comm.http;

import java.net.URLEncoder;

public class HttpSendEvent {
	
	public enum Type { 
		CLICK, TEXTEDIT, 
		SHUTDOWN, 
	};

	private static HttpSendEvent instance;

	private String strHost;

	
	
	public static HttpSendEvent get() {
		if ( null==instance ) {
			instance = new HttpSendEvent();
		}
		return instance;
	}
	
	
	public void setHost( final String strHost ) {
		if ( null==strHost ) return;
		if ( strHost.isEmpty() ) return;
		this.strHost = strHost;
	}
	
	
	public boolean send(	final Type type,
							final String strID,
							final String strData ) {
		if ( null==strHost ) return false;
		
		@SuppressWarnings("deprecation")
		final String strURL = 
				"http://" + strHost + "/atom?Type=" + type.name() 
						+ "&id=" + URLEncoder.encode( strID )
						+ "&data=" + URLEncoder.encode( strData );
		
		final URLReader reader = new URLReader( strURL );
		final String strResult = reader.getContent();
		return ( null!=strResult );
	}
	
}
