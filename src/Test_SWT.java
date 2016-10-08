import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Test_SWT {

	public static void main( final String[] args ) {

//		final Display display = Display.getDefault();
//		final Display display = Display.getCurrent();
		final Display display = new Display();
		
		final Shell shell = new Shell( display );
//		HomeEntryPoint.buildUI( shell, false );
		
		shell.open();
		while ( !shell.isDisposed() ) {
	    	if ( !display.readAndDispatch() ) {
	    		display.sleep();
	    	}
		}
		System.exit( 0 );
	}
	
}
