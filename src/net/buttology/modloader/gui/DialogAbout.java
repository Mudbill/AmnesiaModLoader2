package net.buttology.modloader.gui;

import java.util.Arrays;

import net.buttology.modloader.Modloader;
import net.buttology.modloader.UpdateCallback;
import net.buttology.modloader.file.AppSettings;
import net.buttology.modloader.util.Log;
import net.buttology.modloader.util.ResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class DialogAbout extends Dialog {

	protected Object result;
	protected Shell shell;
	
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogAbout(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		Log.info("Opening about GUI.");
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		Log.info("Closing about GUI.");
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 330);
		shell.setText("About " + Modloader.APP_NAME);
		shell.setLayout(new FormLayout());
		
		Label lblTitle = new Label(shell, SWT.NONE);
		lblTitle.setFont(ResourceManager.getSystemFontWithOptions(11, SWT.NORMAL));
		FormData fd_lblTitle = new FormData();
		lblTitle.setLayoutData(fd_lblTitle);
		lblTitle.setText(Modloader.APP_NAME);
		
		Label lblVersion = new Label(shell, SWT.NONE);
		lblVersion.setFont(ResourceManager.getSystemFontWithOptions(8, SWT.NORMAL));
		FormData fd_lblVersion = new FormData();
		fd_lblVersion.top = new FormAttachment(lblTitle, 4);
		fd_lblVersion.left = new FormAttachment(lblTitle, 0, SWT.LEFT);
		lblVersion.setLayoutData(fd_lblVersion);
		lblVersion.setText("Version " + Modloader.APP_VERSION);
		
		Button buttonClose = new Button(shell, SWT.NONE);
		buttonClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		FormData fd_buttonClose = new FormData();
		fd_buttonClose.bottom = new FormAttachment(100, -10);
		fd_buttonClose.left = new FormAttachment(100, -90);
		fd_buttonClose.right = new FormAttachment(100, -10);
		buttonClose.setLayoutData(fd_buttonClose);
		buttonClose.setText("Close");
		
		Label lblMadeByMudbill = new Label(shell, SWT.NONE);
		lblMadeByMudbill.setForeground(ResourceManager.getSystemColor(SWT.COLOR_TITLE_FOREGROUND));
		FormData fd_lblMadeByMudbill = new FormData();
		fd_lblMadeByMudbill.top = new FormAttachment(lblTitle, 4, SWT.TOP);
		fd_lblMadeByMudbill.right = new FormAttachment(buttonClose, 0, SWT.RIGHT);
		lblMadeByMudbill.setLayoutData(fd_lblMadeByMudbill);
		lblMadeByMudbill.setText("Made by Mudbill");
		
		Label lblThankYouFor = new Label(shell, SWT.WRAP | SWT.CENTER);
		FormData fd_lblThankYouFor = new FormData();
		fd_lblThankYouFor.right = new FormAttachment(100, -10);
		lblThankYouFor.setLayoutData(fd_lblThankYouFor);
		lblThankYouFor.setText("Thank you for using this application. I've put a lot of work into it and I'm quite happy with the results. If you have any feedback, do contact me.\r\n\r\nSpecial thanks to Traggey for making the artwork (background and icon).\r\nAlso thanks to the following people for helping this project come to fruition:\r\nDaemian, Kreekakon, MrBehemoth, Romulator, Slanderous, Muffin");
		
		Label icon = new Label(shell, SWT.NONE);
		fd_lblThankYouFor.left = new FormAttachment(icon, 0, SWT.LEFT);
		fd_lblTitle.left = new FormAttachment(icon, 16);
		fd_lblTitle.top = new FormAttachment(icon, 0, SWT.TOP);
		icon.setImage(ResourceManager.loadInternalImage("/res/icons/app_48.png"));
		FormData fd_icon = new FormData();
		fd_icon.top = new FormAttachment(0, 10);
		fd_icon.left = new FormAttachment(0, 10);
		icon.setLayoutData(fd_icon);
		
		Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		fd_lblThankYouFor.top = new FormAttachment(separator, 16);
		FormData fd_separator = new FormData();
		fd_separator.top = new FormAttachment(icon, 10);
		fd_separator.left = new FormAttachment(0);
		fd_separator.right = new FormAttachment(100);
		separator.setLayoutData(fd_separator);
		
		Label label = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.left = new FormAttachment(separator, 0, SWT.LEFT);
		fd_label.right = new FormAttachment(100);
		label.setLayoutData(fd_label);
		
		Button buttonYoutube = new Button(shell, SWT.NONE);
		fd_label.bottom = new FormAttachment(buttonYoutube, -65);
		buttonYoutube.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Log.info("Opening url: '%s'", AppSettings.URL_YOUTUBE);
				Program.launch(AppSettings.URL_YOUTUBE);
//				Util.openWebsiteInBrowser(urlYoutube);
			}
		});
		buttonYoutube.setImage(ResourceManager.loadInternalImage("/res/icons/youtube.png"));
		FormData fd_buttonYoutube = new FormData();
		fd_buttonYoutube.bottom = new FormAttachment(buttonClose, 0, SWT.BOTTOM);
		fd_buttonYoutube.left = new FormAttachment(lblThankYouFor, 0, SWT.LEFT);
		buttonYoutube.setLayoutData(fd_buttonYoutube);
		buttonYoutube.setToolTipText(AppSettings.URL_YOUTUBE);
		
		Button buttonTwitter = new Button(shell, SWT.NONE);
		buttonTwitter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Log.info("Opening url: '%s'", AppSettings.URL_TWITTER);
				Program.launch(AppSettings.URL_TWITTER);
//				Util.openWebsiteInBrowser(urlTwitter);
			}
		});
		buttonTwitter.setImage(ResourceManager.loadInternalImage("/res/icons/twitter.png"));
		FormData fd_buttonTwitter = new FormData();
		fd_buttonTwitter.bottom = new FormAttachment(buttonClose, 0, SWT.BOTTOM);
		fd_buttonTwitter.left = new FormAttachment(buttonYoutube, 6);
		buttonTwitter.setLayoutData(fd_buttonTwitter);
		buttonTwitter.setToolTipText(AppSettings.URL_TWITTER);
		
		Link link = new Link(shell, SWT.NONE);
		FormData fd_link = new FormData();
		fd_link.bottom = new FormAttachment(buttonClose, -6, SWT.BOTTOM);
		link.setLayoutData(fd_link);
		link.setText("<a>mudbill@buttology.net</a>");
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Program.launch("mailto:mudbill@buttology.net");
			}
		});
		
		Button buttonForum = new Button(shell, SWT.NONE);
		fd_link.left = new FormAttachment(buttonForum, 6);
		buttonForum.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Log.info("Opening url: '%s'", AppSettings.URL_FORUM);
				Program.launch(AppSettings.URL_FORUM);
			}
		});
		buttonForum.setToolTipText(AppSettings.URL_FORUM);
		buttonForum.setImage(ResourceManager.loadInternalImage("/res/icons/fg.png"));
		FormData fd_buttonForum = new FormData();
		fd_buttonForum.top = new FormAttachment(buttonClose, -1, SWT.TOP);
		fd_buttonForum.left = new FormAttachment(buttonTwitter, 6);
		buttonForum.setLayoutData(fd_buttonForum);
		
		Button buttonUpdate = new Button(shell, SWT.NONE);
		buttonUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonUpdate.setEnabled(false);
				Modloader.checkForUpdates(new UpdateCallback() {
					@Override
					public void updated() {
						MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
						m.setText("Update");
						m.setMessage("Already up-to-date with the latest version: " + Arrays.toString(Modloader.APP_VERSION_ARRAY));
						m.open();
						buttonUpdate.setEnabled(true);
					}

					@Override
					public void outdated() {
						MessageBox m = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
						m.setText("Update available");
						m.setMessage("There's an update available for Amnesia Modloader.\n"
								+ "Current version: " + Arrays.toString(Modloader.APP_VERSION_ARRAY) + "\n"
								+ "Latest version: " + Arrays.toString(Modloader.tempOnlineVersion) + "\n\n"
								+ "Do you want to open the website where you can download the latest version?");
						if(m.open() == SWT.YES)
						{
							Log.info("Opening URL: " + AppSettings.URL_FORUM);
							Program.launch(AppSettings.URL_FORUM);
						}
						buttonUpdate.setEnabled(true);
					}

					@Override
					public void error() {
						MessageBox m = new MessageBox(shell, SWT.ICON_ERROR);
						m.setText("Update");
						m.setMessage("Failed to check for updates.");
						m.open();
						buttonUpdate.setEnabled(true);
					}
				});
			}
		});
		FormData fd_buttonUpdate = new FormData();
		fd_buttonUpdate.top = new FormAttachment(label, 6);
		fd_buttonUpdate.left = new FormAttachment(lblThankYouFor, 0, SWT.LEFT);
		buttonUpdate.setLayoutData(fd_buttonUpdate);
		buttonUpdate.setText("Check for updates");

	}
}
