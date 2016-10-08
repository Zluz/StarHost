package jmr.home.app.uirap;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;

import jmr.home.api.comm.IConstants;
import jmr.home.app.uirap.BasicEntryPoint;
import jmr.home.comm.http.HttpAtomConsumer;
import jmr.home.rap.HomeEntryPoint;


public class BasicApplication implements ApplicationConfiguration {

	static {
		System.out.println( BasicApplication.class.toString() + " loaded." );
	}

    public void configure( final Application application ) {
    	
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(WebClient.PAGE_TITLE, "Hello RAP");
        application.addEntryPoint("/hello", BasicEntryPoint.class, properties);
        application.addEntryPoint("/home", HomeEntryPoint.class, properties);
        
        System.out.println( "Configured host: " + IConstants.HOST__UI_RAP );

        this.initializeComms();
    }
    
    
    private void initializeComms() {
    	
    	// RMI
//      ConfigureRMI.initialize();
    	
    	// HTTP
//      final HttpAtomConsumer httpServer = 
      		new HttpAtomConsumer( IConstants.PORT__UI_RAP, 
      						IConstants.HTTP_SERVICE__UI_RAP__SEND );
    }

}
