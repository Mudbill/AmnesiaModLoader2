package net.buttology.modloader.gui;

import net.buttology.modloader.Modloader;
import net.buttology.modloader.util.ResourceManager;
import net.buttology.modloader.util.Util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class DialogChangelog extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogChangelog(int style) {
		super(new Shell(), style);
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
		shell = new Shell(getStyle());
		shell.setMinimumSize(new Point(400, 200));
		shell.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		shell.setSize(437, 366);
		shell.setText("Modloader update");
		shell.setLayout(new FormLayout());
		shell.setImages(ResourceManager.loadInternalImages(
				"/res/icons/app_256.png", 
				"/res/icons/app_48.png", 
				"/res/icons/app_32.png", 
				"/res/icons/app_16.png"
			));
		Util.centerShell(shell);
		
		Label labelWelcome = new Label(shell, SWT.NONE);
		labelWelcome.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_labelWelcome = new FormData();
		fd_labelWelcome.top = new FormAttachment(0, 10);
		fd_labelWelcome.left = new FormAttachment(0, 10);
		labelWelcome.setLayoutData(fd_labelWelcome);
		labelWelcome.setText("Welcome to the new version");
		labelWelcome.setFont(ResourceManager.getSystemFontWithOptions(12, SWT.NORMAL));
		
		Label labelText = new Label(shell, SWT.NONE);
		labelText.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_labelText = new FormData();
		fd_labelText.top = new FormAttachment(labelWelcome, 6);
		fd_labelText.left = new FormAttachment(labelWelcome, 0, SWT.LEFT);
		labelText.setLayoutData(fd_labelText);
		labelText.setText("Check out what's new for " + Modloader.APP_VERSION);
		
		Label labelChangelog = new Label(shell, SWT.WRAP);
		labelChangelog.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_labelChangelog = new FormData();
		fd_labelChangelog.top = new FormAttachment(labelText, 21);
		labelChangelog.setText(ResourceManager.loadChangelog());
		fd_labelChangelog.bottom = new FormAttachment(100, -70);
		fd_labelChangelog.right = new FormAttachment(100, -20);
		fd_labelChangelog.left = new FormAttachment(0, 20);
		labelChangelog.setLayoutData(fd_labelChangelog);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100);
		fd_composite.left = new FormAttachment(0);
		fd_composite.top = new FormAttachment(100, -48);
		fd_composite.right = new FormAttachment(100);
		composite.setLayoutData(fd_composite);
		
		Button buttonOk = new Button(composite, SWT.NONE);
		FormData fd_buttonOk = new FormData();
		fd_buttonOk.left = new FormAttachment(100, -90);
		fd_buttonOk.bottom = new FormAttachment(100, -10);
		fd_buttonOk.right = new FormAttachment(100, -10);
		buttonOk.setLayoutData(fd_buttonOk);
		buttonOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				shell.close();
			}
		});
		buttonOk.setText("OK");
		
		Label separator = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_separator = new FormData();
		fd_separator.top = new FormAttachment(0);
		fd_separator.left = new FormAttachment(0);
		fd_separator.right = new FormAttachment(100);
		separator.setLayoutData(fd_separator);
	}
}
