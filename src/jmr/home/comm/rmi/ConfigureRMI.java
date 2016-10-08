package jmr.home.comm.rmi;

public class ConfigureRMI {

	public static final String PATH_RMI_CLASSES = 
//			"C:\\Development\\workspaces\\20160807_Eclipse_4.6\\Star_API\\bin\\";
//			"C:\\Development\\workspaces\\20160807_Eclipse_4.6\\Star_API\\bin\\jmr\\home\\api\\com\\rmi\\";
//			"C:\\Development\\workspaces\\20160807_Eclipse_4.6\\Star_API\\";
//			"C:\\Development\\Git\\StarHost__20161005_005\\lib\\";
			"file:///C:/Development/Git/StarHost__20161005_005/lib/";
//			"file:///C:/Development/Git/StarHost__20161005_005/lib";
//			"file:///C:/Development/workspaces/20160807_Eclipse_4.6/Star_API/bin/jmr/home/api/com/rmi/";

	public static void initialize() {
		
	    try {
	    	System.setProperty( "java.rmi.server.useCodebaseOnly", "false" );
			System.setProperty( "java.rmi.server.codebase", PATH_RMI_CLASSES );
	    	
	    	RMIMessageConsumer.initialize();
	    } catch ( final NoClassDefFoundError error ) {
	    	
	    	System.err.println( "Unable to start RMI services." );
	    	System.err.println( "RMI codebase: " 
	    				+ System.getProperty( "java.rmi.server.codebase" ) );
	    	
	//    	error.printStackTrace();
	    	
	    	System.exit( 1 );
	    }
	}
}
