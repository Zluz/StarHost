package jmr.home.rap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import jmr.home.comm.rmi.RMIMessageConsumer;

//import jmr.home.apps.StarApp;

public class Activator implements BundleActivator {

	static {
		System.out.println( Activator.class.toString() + " loaded." );
	}
	
	static BundleContext bundlecontext = null;
	
	@Override
	public void start( final BundleContext context ) throws Exception {
		bundlecontext = context;
		System.out.println( "BundleContext set: " + context );
		
//		new StarApp();
		
		new RMIMessageConsumer();
	}

	@Override
	public void stop( final BundleContext context ) throws Exception {
		// clear bundlecontext?
	}

}
