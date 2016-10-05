package p110;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.ApplicationConfiguration;
import org.eclipse.rap.rwt.client.WebClient;

import jmr.home.rap.HomeEntryPoint;


public class BasicApplication implements ApplicationConfiguration {

    public void configure( final Application application ) {
        Map<String, String> properties = new HashMap<String, String>();
        properties.put(WebClient.PAGE_TITLE, "Hello RAP");
        application.addEntryPoint("/hello", BasicEntryPoint.class, properties);
        application.addEntryPoint("/home", HomeEntryPoint.class, properties);
    }

}
