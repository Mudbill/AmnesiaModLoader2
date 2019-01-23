package net.buttology.modloader.gui;

import net.buttology.modloader.gui.widgets.BalloonMessage;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.wb.swt.SWTResourceManager;

public class Test extends Dialog {

	protected Object result;
	protected Shell shell;

	public static void main(String[] args) {
		Test t = new Test(new Shell(), SWT.TITLE | SWT.RESIZE | SWT.CLOSE | SWT.MIN | SWT.MAX);
		t.open();
	}
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public Test(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		shell.setSize(450, 300);
		shell.setText(getText());
		shell.setLayout(new FormLayout());
		
		BalloonMessage balloonMessage = new BalloonMessage(shell, SWT.ICON_INFORMATION);
		balloonMessage.setText("testing message asd dwa wda nfekljnrvs lkerdn lnsbtd knjftlkjnts dkljn ftlkjn t kl n btdxflkjn");
		FormData fd_balloonMessage = new FormData();
		fd_balloonMessage.bottom = new FormAttachment(0, 68);
		fd_balloonMessage.right = new FormAttachment(100, -10);
		fd_balloonMessage.top = new FormAttachment(0, 10);
		fd_balloonMessage.left = new FormAttachment(0, 10);
		balloonMessage.setLayoutData(fd_balloonMessage);

	}
}
