package net.buttology.modloader.gui;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import net.buttology.modloader.Mod;
import net.buttology.modloader.Modloader;
import net.buttology.modloader.file.FileHandler;
import net.buttology.modloader.file.UserSettings;
import net.buttology.modloader.util.Log;
import net.buttology.modloader.util.ResourceManager;
import net.buttology.modloader.util.Util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import de.ikoffice.widgets.SplitButton;

public class ShellPrimary implements IShellPrimary {

	private Shell shell;
	private Display display;
	private Text textModDescription;
	private Button buttonOpenFolder;
	private CompositeModList compositeModList;
	private boolean welcome = false;
	private Label labelModName;
	private Label labelModAuthor;
	private SplitButton buttonStart;
	private Label labelVersion;
	private ProgressBar progressBar;
	private Button buttonScanForMods;
	private Link link;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void init() {
		display = Display.getDefault();
		createContents();
	}
	
	/**
	 * Open the window.
	 */
	@Override
	public void open() {
		shell.open();
		if(welcome)
		{
			MessageBox m = new MessageBox(shell, SWT.ICON_INFORMATION);
			m.setText("Welcome");
			m.setMessage("Is this your... first time?");
//			m.open();
		}
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell(display, SWT.CLOSE | SWT.MIN | SWT.TITLE);
		shell.setImages(ResourceManager.loadInternalImages(
				"/res/icons/app_256.png", 
				"/res/icons/app_48.png", 
				"/res/icons/app_32.png", 
				"/res/icons/app_16.png"
			));
		shell.setText(Modloader.APP_NAME + " v" + Modloader.APP_VERSION);
		shell.setSize(700, 576);
		shell.setBackgroundImage(ResourceManager.loadInternalImage("/res/modloader_bg.png"));
//		shell.setBackgroundImage(ResourceManager.scaleImage(ResourceManager.loadImage("/res/modloader_bg.png"), shell.getClientArea().width, shell.getClientArea().height));
		shell.setLayout(new FormLayout());
		shell.setBackgroundMode(SWT.INHERIT_FORCE);
		Util.centerShell(shell);
		
		buttonScanForMods = new Button(shell, SWT.NONE);
		buttonScanForMods.setToolTipText("Scans the selected directory for matching mod start files while skipping excluded folders.");
		buttonScanForMods.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				buttonScanForMods.setEnabled(false);
				buttonStart.setEnabled(false);
				updateModList();
			}
		});
		buttonScanForMods.setImage(ResourceManager.loadInternalImage("/res/icons/scan.png"));
		FormData fd_buttonScanForMods = new FormData();
		fd_buttonScanForMods.left = new FormAttachment(0, 10);
		fd_buttonScanForMods.top = new FormAttachment(0, 10);
		fd_buttonScanForMods.right = new FormAttachment(0, 110);
		buttonScanForMods.setLayoutData(fd_buttonScanForMods);
		buttonScanForMods.setText("Find mods");
		
		progressBar = new ProgressBar(shell, SWT.NONE);
		FormData fd_progressBar = new FormData();
		fd_progressBar.top = new FormAttachment(0, 12);
		fd_progressBar.left = new FormAttachment(buttonScanForMods, 10, SWT.RIGHT);
		fd_progressBar.bottom = new FormAttachment(buttonScanForMods, -2, SWT.BOTTOM);
		progressBar.setLayoutData(fd_progressBar);
		progressBar.setVisible(false);
		
		Button buttonAbout = new Button(shell, SWT.NONE);
		buttonAbout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DialogAbout d = new DialogAbout(shell, SWT.SHEET);
				d.open();
			}
		});
		FormData fd_buttonAbout = new FormData();
		fd_buttonAbout.right = new FormAttachment(100, -10);
		fd_buttonAbout.left = new FormAttachment(100, -110);
		fd_buttonAbout.top = new FormAttachment(buttonScanForMods, 0, SWT.TOP);
		buttonAbout.setLayoutData(fd_buttonAbout);
		buttonAbout.setText("About");
		fd_progressBar.right = new FormAttachment(buttonAbout, -6);
		
		Composite panelLeft = new Composite(shell, SWT.BORDER);
		panelLeft.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		panelLeft.setLayout(new FormLayout());
		FormData fd_panelLeft = new FormData();
		fd_panelLeft.top = new FormAttachment(40);
		fd_panelLeft.right = new FormAttachment(50);
		panelLeft.setLayoutData(fd_panelLeft);
		
		Composite panelRight = new Composite(shell, SWT.BORDER);
		panelRight.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		panelRight.setLayout(new FormLayout());
		FormData fd_panelRight = new FormData();
		fd_panelRight.top = new FormAttachment(panelLeft, 0, SWT.TOP);
		fd_panelRight.left = new FormAttachment(50);
		fd_panelRight.bottom = new FormAttachment(panelLeft, 0, SWT.BOTTOM);

		compositeModList = new CompositeModList(panelLeft, SWT.NONE);
		compositeModList.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				compositeModList.setTitleWidth(compositeModList.getClientArea().width);
			}
		});
		FormData fd_compositeModList = new FormData();
		fd_compositeModList.left = new FormAttachment(0);
		fd_compositeModList.right = new FormAttachment(100);
		fd_compositeModList.top = new FormAttachment(0);
		fd_compositeModList.bottom = new FormAttachment(100);
		compositeModList.setLayoutData(fd_compositeModList);
		
		if(welcome)
		{
			CompositeWelcome compositeNoMods = new CompositeWelcome(panelLeft, SWT.NONE);
			FormData fd_compositeNoMods = new FormData();
			fd_compositeNoMods.top = new FormAttachment(0);
			fd_compositeNoMods.left = new FormAttachment(0);
			fd_compositeNoMods.bottom = new FormAttachment(100);
			fd_compositeNoMods.right = new FormAttachment(100);
			compositeNoMods.setLayoutData(fd_compositeNoMods);
			compositeNoMods.setVisible(true);
			compositeModList.setVisible(false);
		}
		
		panelRight.setLayoutData(fd_panelRight);
		
		labelModName = new Label(panelRight, SWT.NONE);
		labelModName.setText("<mod title>");
		labelModName.setFont(ResourceManager.getSystemFontWithOptions(11, SWT.BOLD));
		FormData fd_labelModName = new FormData();
		fd_labelModName.top = new FormAttachment(0, 6);
		fd_labelModName.left = new FormAttachment(0, 10);
		fd_labelModName.right = new FormAttachment(100, -10);
		labelModName.setLayoutData(fd_labelModName);
		
		labelModAuthor = new Label(panelRight, SWT.NONE);
		labelModAuthor.setText("<mod author>");
		labelModAuthor.setFont(ResourceManager.getSystemFontWithOptions(9, SWT.ITALIC));
		FormData fd_labelModAuthor = new FormData();
		fd_labelModAuthor.top = new FormAttachment(labelModName, 4);
		fd_labelModAuthor.left = new FormAttachment(labelModName, 0, SWT.LEFT);
		fd_labelModAuthor.right = new FormAttachment(labelModName, 0, SWT.RIGHT);
		labelModAuthor.setLayoutData(fd_labelModAuthor);
		
		textModDescription = new Text(panelRight, SWT.READ_ONLY | SWT.WRAP | SWT.MULTI);
		textModDescription.setText("<mod description>");
		textModDescription.setEditable(false);
		FormData fd_textModDescription = new FormData();
		fd_textModDescription.top = new FormAttachment(labelModAuthor, 10);
		fd_textModDescription.left = new FormAttachment(0, 10);
		fd_textModDescription.right = new FormAttachment(100, -10);
		textModDescription.setLayoutData(fd_textModDescription);
		Util.attachDefaultTextContextMenu(textModDescription);
		
		Label sep = new Label(panelRight, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		FormData fd_sep = new FormData();
		fd_sep.top = new FormAttachment(labelModAuthor, 6);
		fd_sep.left = new FormAttachment(labelModName, 0, SWT.LEFT);
		fd_sep.right = new FormAttachment(labelModName, 0, SWT.RIGHT);
		sep.setLayoutData(fd_sep);
		
		Label label = new Label(panelRight, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		fd_textModDescription.bottom = new FormAttachment(label, -6);
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(labelModName, 0, SWT.RIGHT);
		fd_label.left = new FormAttachment(labelModName, 0, SWT.LEFT);
		label.setLayoutData(fd_label);
		
		labelVersion = new Label(panelRight, SWT.NONE);
		fd_label.bottom = new FormAttachment(labelVersion, -6);
		labelVersion.setAlignment(SWT.RIGHT);
		FormData fd_labelVersion = new FormData();
		fd_labelVersion.right = new FormAttachment(labelModName, 0, SWT.RIGHT);
		labelVersion.setLayoutData(fd_labelVersion);
		labelVersion.setText("<mod version>");
		labelVersion.setFont(ResourceManager.getSystemFontWithOptions(8, SWT.BOLD));
		
		Label labelVersionText = new Label(panelRight, SWT.NONE);
		fd_labelVersion.top = new FormAttachment(labelVersionText, 0, SWT.TOP);
		FormData fd_labelVersionText = new FormData();
		fd_labelVersionText.bottom = new FormAttachment(100, -31);
		fd_labelVersionText.left = new FormAttachment(labelModName, 0, SWT.LEFT);
		labelVersionText.setLayoutData(fd_labelVersionText);
		labelVersionText.setText("Required game version:");
		labelVersionText.setFont(ResourceManager.getSystemFontWithOptions(8, SWT.NORMAL));
		
		link = new Link(panelRight, SWT.NONE);
		link.setEnabled(false);
		FormData fd_link = new FormData();
		fd_link.top = new FormAttachment(labelVersion, 6);
		fd_link.right = new FormAttachment(labelModName, 0, SWT.RIGHT);
		link.setLayoutData(fd_link);
		link.setText("<a>More info</a>");
		
		buttonOpenFolder = new Button(shell, SWT.NONE);
		buttonOpenFolder.setToolTipText("Open the game directory in your file manager.");
		buttonOpenFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String filepath = UserSettings.getVar("AmnesiaDir");
				if(!filepath.isEmpty()) {
					File f = new File(filepath);
					if(f.isDirectory()) {
						try {
							Desktop.getDesktop().open(f);
							Log.info("Opening folder '%s'", filepath);
						} catch (IOException e1) {
							e1.printStackTrace();
							Modloader.promptErrorMessage("Failed to open folder at '" + filepath + "'");
						}
					}
				}
			}
		});
		buttonOpenFolder.setText("Open folder");
		FormData fd_buttonOpenFolder = new FormData();
		fd_buttonOpenFolder.right = new FormAttachment(0, 110);
		fd_buttonOpenFolder.left = new FormAttachment(0, 10);
		fd_buttonOpenFolder.bottom = new FormAttachment(100, -10);
		buttonOpenFolder.setLayoutData(fd_buttonOpenFolder);
		fd_panelLeft.left = new FormAttachment(buttonOpenFolder, 0, SWT.LEFT);
		fd_panelLeft.bottom = new FormAttachment(buttonOpenFolder, -6, SWT.TOP);
		
		buttonStart = new SplitButton(shell, SWT.NONE);
		buttonStart.setEnabled(false);
		buttonStart.setText("Launch");
		FormData fd_buttonStart = new FormData();
		fd_buttonStart.right = new FormAttachment(50, -2);
		fd_buttonStart.left = new FormAttachment(50, -102);
		fd_buttonStart.bottom = new FormAttachment(100, -10);
		buttonStart.setLayoutData(fd_buttonStart);
		buttonStart.setMenu(compositeModList.getMenu());
		
		Button buttonExit = new Button(shell, SWT.NONE);
		buttonExit.setText("Exit");
		FormData fd_buttonExit = new FormData();
		fd_buttonExit.right = new FormAttachment(50, 102);
		fd_buttonExit.left = new FormAttachment(50, 2);
		fd_buttonExit.bottom = new FormAttachment(100, -10);
		buttonExit.setLayoutData(fd_buttonExit);
		
		Button buttonOptions = new Button(shell, SWT.NONE);
		buttonOptions.setText("Settings");
		FormData fd_buttonOptions = new FormData();
		fd_buttonOptions.left = new FormAttachment(100, -110);
		fd_buttonOptions.bottom = new FormAttachment(100, -10);
		fd_buttonOptions.right = new FormAttachment(100, -10);
		buttonOptions.setLayoutData(fd_buttonOptions);
		fd_panelRight.right = new FormAttachment(buttonOptions, 0, SWT.RIGHT);
		
		buttonOptions.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openOptionsForm();
			}
		});
		buttonExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.dispose();
			}
		});
		buttonStart.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				launch(compositeModList.getSelectedMod());
			}
		});
		link.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				DialogModInfo d = new DialogModInfo(shell, SWT.PRIMARY_MODAL | SWT.DIALOG_TRIM);
				d.open();
			}
		});
		
		loadUserSettings();
	}

	public void addLoadedModToTable(Mod mod) {
//		File iconFile = mod.getIcon();
//		Image icon = ResourceManager.loadInternalImage("/res/icons/default.png");
//		try {
//			icon = ResourceManager.loadAbsoluteImage(iconFile.getAbsolutePath());
//		} catch (Exception e) {
//			Log.error("Error loading image '%s'", iconFile);
////			e.printStackTrace();
//		}
//		compositeModList.addTableItem(icon, mod.getName());
		compositeModList.addModToList(mod);
	}
	
	private void updateModList() {
		if(UserSettings.getModDirectory() == null) return;
		compositeModList.clearMods();
		String startingDir = UserSettings.getModDirectory();
		String configName = Modloader.getAppSettings().getMainInitConfigName();
		String[] filters = UserSettings.getVar("ExcludedFolders").split(";");
		FileHandler.loadAll(startingDir, configName, filters);
	}
	
	public void openOptionsForm() {
		DialogSettings f = new DialogSettings(shell, SWT.RESIZE | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		f.open();
	}
	
	@Override
	public void loadUserSettings() {
		buttonOpenFolder.setEnabled(UserSettings.isSet("AmnesiaDir") && !UserSettings.getVar("AmnesiaDir").isEmpty());
		buttonScanForMods.setEnabled(UserSettings.getModDirectory() != null && !UserSettings.getModDirectory().isEmpty());
	}

	@Override
	public void close() {
		shell.close();
	}

	@Override
	public boolean isDisposed() {
		return shell.isDisposed();
	}

	@Override
	public void setWelcomeEnabled(boolean b) {
		welcome = b;
	}

	@Override
	public void setModInfo(Mod mod) {
		labelModName.setText(mod.getName());
		labelModAuthor.setText("By " + mod.getAuthor());
		textModDescription.setText(mod.getDescription());
		labelVersion.setText(mod.getRequiredVersion());
	}
	
	@Override
	public void setStartButtonEnabled(boolean x) {
		buttonStart.setEnabled(x);
		link.setEnabled(x);
	}

	@Override
	public void incrementProgressBarStage() {
		if(!progressBar.isDisposed()) progressBar.setSelection(progressBar.getSelection() + 10);
	}

	@Override
	public void setProgressBarMax(int x) {
		if(!progressBar.isDisposed()) progressBar.setMaximum(x * 10);
	}
	
	@Override
	public void setProgressBarVisible(boolean x) {
		if(!progressBar.isDisposed()) progressBar.setVisible(x);
	}
	
	@Override
	public CompositeModList getModList() {
		return compositeModList;
	}

	@Override
	public void resetProgressBar() {
		if(!progressBar.isDisposed()) {
			progressBar.setSelection(0);
			progressBar.setVisible(false);
			buttonScanForMods.setEnabled(true);
		}
	}

	@Override
	public void launch(Mod mod) {
		if(mod != null)	Modloader.prepareLaunchMod(mod);
	}

	@Override
	public Shell getShell() {
		return shell;
	}
	
	@Override
	public void search() {
		buttonScanForMods.setEnabled(false);
		buttonStart.setEnabled(false);
		updateModList();
	}
}
