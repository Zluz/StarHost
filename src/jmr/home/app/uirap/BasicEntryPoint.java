package jmr.home.app.uirap;

import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.service.ServerPushSession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


@SuppressWarnings("serial")
public class BasicEntryPoint extends AbstractEntryPoint {

	static {
		System.out.println( BasicEntryPoint.class.toString() + " loaded." );
	}
	
    @Override
    protected void createContents( final Composite parent ) {
        parent.setLayout(new GridLayout(2, false));
        Button checkbox = new Button(parent, SWT.CHECK);
        checkbox.setText("Hello");
        final Button btn1 = new Button(parent, SWT.PUSH);
        btn1.setText( "Button 01" );
        final Button btn2 = new Button(parent, SWT.PUSH);
        btn2.setText( "Button 02" );
        final Button btn3 = new Button(parent, SWT.PUSH);
        btn3.setText( "Button 03" );
        
        final Label lbl = new Label( parent, SWT.NONE );
        lbl.setText( "time: xxxxx" );
        
        final Display display = parent.getDisplay();
        
//        final String[] strText = { "0" };
        
        
        ProgressBarTab pbar = new ProgressBarTab();
        pbar.createExampleControls( parent );
        pbar.createStateControl();
        
        UpdatingControls uc = new UpdatingControls();
        uc.createExampleControls( parent );
//        uc.createStateControl();
        
        
        final StringBuffer strbuf = new StringBuffer();
        
        final String CR = Text.DELIMITER;
        
        strbuf.append( "Display: " + display.toString() + CR );
        strbuf.append( "\twidth: " + display.getBounds().width + CR );
        strbuf.append( "\theight: " + display.getBounds().height + CR );
        
        final Text txtInfo = new Text( parent, SWT.MULTI | SWT.BORDER );
        txtInfo.setText( strbuf.toString() );
        
        
        
        
        for ( int i=0; i<10; i++ ) {
	        
	        
	        final ServerPushSession pushSession = new ServerPushSession();
	        
	        final Runnable bgRunnable = new Runnable() {
	          @Override
	          public void run() {
	            // do some background work ...
	        	  
				try {
					Thread.sleep( 100 );
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	  
	        	  
	            // schedule the UI update
	            display.asyncExec( new Runnable() {
	              @Override
	              public void run() {
	                if( !lbl.isDisposed() ) {
	                  // update the UI
	//                	lbl.setText( "Text: " + strText[0] );
						lbl.setText( "time: " + System.currentTimeMillis() );
	
	                  // close push session when finished
	                  pushSession.stop();
	//                  pushSession.start();
	                }
	              }
	            } );
	          }
	        };
	        pushSession.start();
	        Thread bgThread = new Thread( bgRunnable );
	        bgThread.setDaemon( true );
	        bgThread.start();
	        
	        
	        

			try {
				Thread.sleep( 1000 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        	  
        }
        
        
        
        
        
    }

}
