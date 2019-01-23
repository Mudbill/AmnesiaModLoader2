package net.buttology.modloader.gui;

import net.buttology.modloader.util.ResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class DialogNewUser extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogNewUser(Shell parent, int style) {
		super(parent, style);
		createContents();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
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
		shell.setSize(493, 205);
		shell.setText("Add new user");
		shell.setLayout(new FormLayout());
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		composite.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		fd_composite.right = new FormAttachment(100);
		composite.setLayoutData(fd_composite);
		
		Label label = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_OUT);
		FormData fd_label = new FormData();
		fd_label.bottom = new FormAttachment(100);
		fd_label.left = new FormAttachment(0);
		fd_label.right = new FormAttachment(100);
		label.setLayoutData(fd_label);
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		fd_composite.bottom = new FormAttachment(btnCancel, -11);
		
		Label lblSetCustomResolution = new Label(composite, SWT.NONE);
		lblSetCustomResolution.setForeground(ResourceManager.getSystemColor(SWT.COLOR_DARK_BLUE));
		lblSetCustomResolution.setFont(ResourceManager.getSystemFontWithOptions(12, SWT.NORMAL));
		FormData fd_lblSetCustomResolution = new FormData();
		fd_lblSetCustomResolution.top = new FormAttachment(0, 10);
		fd_lblSetCustomResolution.left = new FormAttachment(0, 10);
		lblSetCustomResolution.setLayoutData(fd_lblSetCustomResolution);
		lblSetCustomResolution.setText("New user");
		
		Label lblTypeTheWidth = new Label(composite, SWT.WRAP);
		FormData fd_lblTypeTheWidth = new FormData();
		fd_lblTypeTheWidth.top = new FormAttachment(lblSetCustomResolution, 6);
		fd_lblTypeTheWidth.left = new FormAttachment(0, 10);
		fd_lblTypeTheWidth.right = new FormAttachment(100, -10);
		lblTypeTheWidth.setLayoutData(fd_lblTypeTheWidth);
		lblTypeTheWidth.setText("Type the name of the new user you want to create.");
		
		text = new Text(composite, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(lblTypeTheWidth, 0, SWT.RIGHT);
		fd_text.top = new FormAttachment(lblTypeTheWidth, 6);
		fd_text.left = new FormAttachment(0, 10);
		text.setLayoutData(fd_text);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.bottom = new FormAttachment(100, -13);
		fd_btnCancel.right = new FormAttachment(100, -10);
		fd_btnCancel.top = new FormAttachment(100, -36);
		fd_btnCancel.left = new FormAttachment(100, -85);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		
		Button btnOk = new Button(shell, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = text.getText();
				shell.close();
			}
		});
		btnOk.setText("OK");
		FormData fd_btnOk = new FormData();
		fd_btnOk.left = new FormAttachment(btnCancel, -79, SWT.LEFT);
		fd_btnOk.right = new FormAttachment(btnCancel, -6);
		fd_btnOk.top = new FormAttachment(btnCancel, 0, SWT.TOP);
		fd_btnOk.bottom = new FormAttachment(btnCancel, 0, SWT.BOTTOM);
		btnOk.setLayoutData(fd_btnOk);
		
		text.setFocus();
		shell.setDefaultButton(btnOk);
	}
}
