package jmr.home.rap;
import javax.servlet.http.HttpServletRequest;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.application.AbstractEntryPoint;
import org.eclipse.rap.rwt.service.UISession;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import p110.ProgressBarTab;
import p110.UpdatingControls;

public class HomeEntryPoint extends AbstractEntryPoint {

	private static final long serialVersionUID = 1L;

	public HomeEntryPoint() {
		System.out.println( "--- homeEntryPoint.(ctor)" );
	}
	
	public static void buildUI(	final Composite parent,
								final boolean bRAP ) {
        parent.setLayout( new GridLayout( 3, false ) );

        final Display display = parent.getDisplay();

        final GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true );
        final GridData gdLeft = new GridData( SWT.FILL, SWT.FILL, true, true );

        final Composite compLeft = new Composite( parent, SWT.NONE );
        compLeft.setLayout( new GridLayout( 1, true ) );
        compLeft.setLayoutData( gdLeft );
        
        final Composite compCenter = new Composite( parent, SWT.NONE );
        compCenter.setLayout( new GridLayout( 1, true ) );
        compCenter.setLayoutData( gd );
        
        final Composite compRight = new Composite( parent, SWT.NONE );
        compRight.setLayout( new GridLayout( 1, true ) );
        compRight.setLayoutData( gd );
        
        final String strImageFile = "";
        final Image imgAlbum = new Image( display, strImageFile );
        gdLeft.widthHint = imgAlbum.getImageData().width;
        final Canvas canvas = new Canvas( compLeft, SWT.NONE );
        canvas.setBackgroundImage( imgAlbum );
        
        Button checkbox = new Button(compCenter, SWT.CHECK);
        checkbox.setText("Hello");
        final Button btn1 = new Button(compCenter, SWT.PUSH);
        btn1.setText( "Button 01" );
        final Button btn2 = new Button(compCenter, SWT.PUSH);
        btn2.setText( "Button 02" );
        final Button btn3 = new Button(compCenter, SWT.PUSH);
        btn3.setText( "Button 03" );
        
        final Label lbl = new Label( compCenter, SWT.NONE );
        lbl.setText( "time: xxxxx" );
        
        
//        final String[] strText = { "0" };
        
        if ( bRAP ) {
	        
	        ProgressBarTab pbar = new ProgressBarTab();
	        pbar.createExampleControls( compCenter );
	        pbar.createStateControl();
	        
	        UpdatingControls uc = new UpdatingControls();
	        uc.createExampleControls( compCenter );
	//        uc.createStateControl();
        }
        
        
        final StringBuffer strbuf = new StringBuffer();
        
        final String CR = Text.DELIMITER;
        
        strbuf.append( "Display: " + display.toString() + CR );
        strbuf.append( "\twidth: " + display.getBounds().width + CR );
        strbuf.append( "\theight: " + display.getBounds().height + CR );
        
        try {
	        
        	if ( bRAP ) {
		        final HttpServletRequest request = RWT.getRequest();
	
		        strbuf.append( "RWT.getRequest: " + request + CR );
		        strbuf.append( "\tgetRemoteAttr(): " + request.getRemoteAddr() + CR );
		        strbuf.append( "\tgetRemoteHost(): " + request.getRemoteHost() + CR );
		        strbuf.append( "\tgetRequestURI(): " + request.getRequestURI() + CR );
		        
		        final UISession session = RWT.getUISession();
		
		        strbuf.append( "RWT.getUISession: " + session + CR );
		        strbuf.append( "\tgetConnection: " + session.getConnection() + CR );
		        strbuf.append( "\tgetId(): " + session.getId() + CR );
        	}
	        
        } catch ( final Throwable t ) {
	        strbuf.append( CR + "ERROR: " + t.toString() + CR );
	        strbuf.append( "\tgetMessage: " + t.getMessage() + CR );
        }
        
        final Text txtInfo = new Text( compCenter, SWT.MULTI | SWT.BORDER );
        txtInfo.setText( strbuf.toString() );
		
	}
	
	@Override
	protected void createContents( final Composite parent ) {
		System.out.println( "--> homeEntryPoint.createContents(Composite)" );

//		buildUI( parent, true );
		final UserInterfaceRemote ui = new UserInterfaceRemote();
		ui.buildUI( parent );
        
		System.out.println( "<-- homeEntryPoint.createContents(Composite)" );
	}
	

	

}
