package jmr.home.rap;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	static BundleContext bundlecontext = null;
	
	@Override
	public void start( final BundleContext context ) throws Exception {
		bundlecontext = context;
		System.out.println( "BundleContext set: " + context );
	}

	@Override
	public void stop( final BundleContext context ) throws Exception {
		// clear bundlecontext?
	}

}
