package net.buttology.modloader.gui;

import net.buttology.modloader.file.UserSettings;
import net.buttology.modloader.util.Log;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class DialogSettingsAdvanced extends Dialog {

	protected Object result;
	protected Shell shell;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogSettingsAdvanced(Shell parent, int style) {
		super(parent, style);
		createContents(parent, style);
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		Log.info("Opening advanced options GUI.");
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		Log.info("Closing advanced options GUI.");
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents(Shell parent, int style) {
		shell = new Shell(parent, SWT.DIALOG_TRIM | style);
		shell.setSize(450, 455);
		shell.setText("Advanced settings");
		shell.setLayout(new FormLayout());
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(shell, SWT.H_SCROLL | SWT.V_SCROLL);
		FormData fd_scrolledComposite = new FormData();
		fd_scrolledComposite.left = new FormAttachment(0);
		fd_scrolledComposite.top = new FormAttachment(0);
		fd_scrolledComposite.right = new FormAttachment(100);
		scrolledComposite.setLayoutData(fd_scrolledComposite);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		CompositeAdvanced compositeAdvanced = new CompositeAdvanced(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(compositeAdvanced);
		scrolledComposite.setMinSize(compositeAdvanced.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		
		Button btnCancel = new Button(shell, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		fd_scrolledComposite.bottom = new FormAttachment(btnCancel, -6);
		FormData fd_btnCancel = new FormData();
		fd_btnCancel.bottom = new FormAttachment(100, -10);
		fd_btnCancel.right = new FormAttachment(100, -10);
		fd_btnCancel.left = new FormAttachment(100, -90);
		btnCancel.setLayoutData(fd_btnCancel);
		btnCancel.setText("Cancel");
		
		Button btnOk = new Button(shell, SWT.NONE);
		FormData fd_btnOk = new FormData();
		fd_btnOk.left = new FormAttachment(btnCancel, -86, SWT.LEFT);
		fd_btnOk.bottom = new FormAttachment(100, -10);
		fd_btnOk.right = new FormAttachment(btnCancel, -6);
		btnOk.setLayoutData(fd_btnOk);
		btnOk.setText("OK");
		btnOk.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				UserSettings.setVar("DisableShadows", 		compositeAdvanced.getShadowsDisabled());
				UserSettings.setVar("LimitFPS", 			compositeAdvanced.getLimitFps());
				UserSettings.setVar("Gamma", 				compositeAdvanced.getGamma());
				UserSettings.setVar("MaxChannels", 			compositeAdvanced.getMaxChannels());
				UserSettings.setVar("StreamBuffers", 		compositeAdvanced.getStreamBuffers());
				UserSettings.setVar("StreamBufferSize", 	compositeAdvanced.getStreamBufferSize());
				UserSettings.setVar("SkipPreMenu", 			compositeAdvanced.getSkipPremenu());
				UserSettings.setVar("DoNotSleep", 			compositeAdvanced.getDoNotSleep());
				UserSettings.setVar("LoadFastPhysics", 		compositeAdvanced.getLoadFastPhysics());
				UserSettings.setVar("LoadFastStaticObjects",compositeAdvanced.getLoadFastStatic());
				UserSettings.setVar("LoadFastEntities", 	compositeAdvanced.getLoadFastEntities());
				shell.close();
			}
		});
		
		shell.setDefaultButton(btnOk);
	}
}
