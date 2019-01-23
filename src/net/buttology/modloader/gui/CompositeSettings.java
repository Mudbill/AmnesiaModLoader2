package net.buttology.modloader.gui;

import java.io.File;

import net.buttology.modloader.file.UserSettings;
import net.buttology.modloader.util.ResourceManager;
import net.buttology.modloader.util.Util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CompositeSettings extends Composite {

	private Text textGameDir;
	private Text textModDir;
	private Text textExclusion;
	private Button radioCustom;
	private List listExcluded;
	private Button radioGameDir;
	private Button buttonOpenMod;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeSettings(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		Shell shell = getShell();

		Group groupMainSetup = new Group(this, SWT.NONE);
		groupMainSetup.setText("Main setup");
		groupMainSetup.setLayout(new FormLayout());
		FormData fd_groupMainSetup = new FormData();
		fd_groupMainSetup.top = new FormAttachment(0, 10);
		fd_groupMainSetup.left = new FormAttachment(0, 10);
		fd_groupMainSetup.bottom = new FormAttachment(100, -10);
		fd_groupMainSetup.right = new FormAttachment(100, -10);
		groupMainSetup.setLayoutData(fd_groupMainSetup);
		
		Label labelGameDirectory = new Label(groupMainSetup, SWT.NONE);
		FormData fd_labelGameDirectory = new FormData();
		fd_labelGameDirectory.top = new FormAttachment(0, 10);
		fd_labelGameDirectory.left = new FormAttachment(0, 10);
		labelGameDirectory.setLayoutData(fd_labelGameDirectory);
		labelGameDirectory.setText("Amnesia directory");
		
		textGameDir = new Text(groupMainSetup, SWT.BORDER);
		FormData fd_textGameDir = new FormData();
		fd_textGameDir.top = new FormAttachment(labelGameDirectory, 4);
		fd_textGameDir.left = new FormAttachment(labelGameDirectory, 0, SWT.LEFT);
		textGameDir.setLayoutData(fd_textGameDir);
		Util.attachDefaultTextContextMenu(textGameDir);
		
		Button buttonOpenGame = new Button(groupMainSetup, SWT.NONE);
		buttonOpenGame.setToolTipText("Locate folder");
		buttonOpenGame.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dd = new DirectoryDialog(shell, SWT.SHEET);
				dd.setText("Open Amnesia directory");
				dd.setMessage("Point to your Amnesia - The Dark Descent install directory, where the executable lies.");
				dd.setFilterPath(textGameDir.getText());
				String path = dd.open();
				if(path != null)
				{
					textGameDir.setText(path);
					//UserSettings.setVar("AmnesiaDir", path);
				}
			}
		});
		fd_textGameDir.right = new FormAttachment(buttonOpenGame, -6);
		buttonOpenGame.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		buttonOpenGame.setImage(ResourceManager.loadInternalImage("/res/icons/folder.png"));
		FormData fd_buttonOpenGame = new FormData();
		fd_buttonOpenGame.top = new FormAttachment(0, 26);
		fd_buttonOpenGame.right = new FormAttachment(100, -10);
		buttonOpenGame.setLayoutData(fd_buttonOpenGame);
		
		Label sep_1 = new Label(groupMainSetup, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		FormData fd_sep_1 = new FormData();
		fd_sep_1.top = new FormAttachment(textGameDir, 14);
		fd_sep_1.left = new FormAttachment(0);
		fd_sep_1.right = new FormAttachment(100);
		sep_1.setLayoutData(fd_sep_1);
		
		Label labelSearchForMods = new Label(groupMainSetup, SWT.NONE);
		FormData fd_labelSearchForMods = new FormData();
		fd_labelSearchForMods.top = new FormAttachment(sep_1, 10);
		fd_labelSearchForMods.left = new FormAttachment(labelGameDirectory, 0, SWT.LEFT);
		labelSearchForMods.setLayoutData(fd_labelSearchForMods);
		labelSearchForMods.setText("Search for mods in");
		
		radioGameDir = new Button(groupMainSetup, SWT.RADIO);
		radioGameDir.setSelection(true);
		FormData fd_radioGameDir = new FormData();
		fd_radioGameDir.top = new FormAttachment(labelSearchForMods, 6);
		fd_radioGameDir.left = new FormAttachment(labelSearchForMods, 10, SWT.LEFT);
		radioGameDir.setLayoutData(fd_radioGameDir);
		radioGameDir.setText("Amnesia directory");
		
		radioCustom = new Button(groupMainSetup, SWT.RADIO);
		buttonOpenMod = new Button(groupMainSetup, SWT.NONE);
		buttonOpenMod.setToolTipText("Locate folder");
		buttonOpenMod.setEnabled(false);
		radioCustom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean b = radioCustom.getSelection();
				textModDir.setEnabled(b);
				buttonOpenMod.setEnabled(b);
				//UserSettings.setVar("ModsUseAmnesia", b);
			}
		});
		FormData fd_radioCustom = new FormData();
		fd_radioCustom.left = new FormAttachment(radioGameDir, 0, SWT.LEFT);
		fd_radioCustom.top = new FormAttachment(radioGameDir, 6);
		radioCustom.setLayoutData(fd_radioCustom);
		radioCustom.setText("Custom:");
		
		textModDir = new Text(groupMainSetup, SWT.BORDER);
		textModDir.setEnabled(false);
		FormData fd_textModDir = new FormData();
		fd_textModDir.left = new FormAttachment(0, 10);
		fd_textModDir.top = new FormAttachment(radioCustom, 4);
		textModDir.setLayoutData(fd_textModDir);
		Util.attachDefaultTextContextMenu(textModDir);

		buttonOpenMod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				DirectoryDialog dd = new DirectoryDialog(shell, SWT.SHEET);
				dd.setText("Open mod directory");
				dd.setMessage("Point to where you want Amnesia Modloader to search for mods.");
				dd.setFilterPath(textModDir.getText());
				String path = dd.open();
				if(path != null)
				{
					textModDir.setText(path);
					//UserSettings.setVar("ModDir", path);
				}
			}
		});
		fd_textModDir.right = new FormAttachment(buttonOpenMod, -6);
		buttonOpenMod.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		buttonOpenMod.setImage(ResourceManager.loadInternalImage("/res/icons/folder.png"));
		FormData fd_buttonOpenMod = new FormData();
		fd_buttonOpenMod.top = new FormAttachment(textModDir, -3, SWT.TOP);
		fd_buttonOpenMod.left = new FormAttachment(buttonOpenGame, 0, SWT.LEFT);
		buttonOpenMod.setLayoutData(fd_buttonOpenMod);
		
		Label labelExcludedFolders = new Label(groupMainSetup, SWT.NONE);
		FormData fd_labelExcludedFolders = new FormData();
		fd_labelExcludedFolders.top = new FormAttachment(textModDir, 6);
		fd_labelExcludedFolders.left = new FormAttachment(0, 10);
		labelExcludedFolders.setLayoutData(fd_labelExcludedFolders);
		labelExcludedFolders.setText("Excluded folders");
		
		listExcluded = new List(groupMainSetup, SWT.BORDER | SWT.V_SCROLL);
		listExcluded.setItems(new String[] {"/config", "/custom_stories", "/music", "/maps", "*entities*", "*static_objects*", "*sounds*", "*textures*"});
		FormData fd_listExcluded = new FormData();
		fd_listExcluded.right = new FormAttachment(100, -10);
		fd_listExcluded.top = new FormAttachment(labelExcludedFolders, 6);
		fd_listExcluded.bottom = new FormAttachment(labelExcludedFolders, 118);
		fd_listExcluded.left = new FormAttachment(labelGameDirectory, 0, SWT.LEFT);
		listExcluded.setLayoutData(fd_listExcluded);
		Button buttonAddExclusion = new Button(groupMainSetup, SWT.NONE);
		buttonAddExclusion.setToolTipText("Add the folder filter to the exclusion list.");
		buttonAddExclusion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = textExclusion.getText();
				if(!text.isEmpty())
				{
					listExcluded.add(text);
					textExclusion.setText("");
					buttonAddExclusion.setEnabled(false);
				}
			}
		});
		Button buttonRemoveExclusion = new Button(groupMainSetup, SWT.NONE);
		buttonRemoveExclusion.setToolTipText("Remove the selected folder filter from the exclusion list.");
		buttonRemoveExclusion.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = listExcluded.getSelectionIndex();
				if(index != -1)
				{
					listExcluded.remove(index);
					buttonRemoveExclusion.setEnabled(false);
				}
			}
		});
		listExcluded.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = listExcluded.getSelectionIndex();
				buttonRemoveExclusion.setEnabled(index != -1);
			}
		});
		
		textExclusion = new Text(groupMainSetup, SWT.BORDER);
		textExclusion.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String text = textExclusion.getText();
				if(text.contains(";") || text.contains("%")) {					
					text = text.replace(";", "").replace("%", "");
					textExclusion.setText(text);
					textExclusion.setSelection(text.length());
				}
				buttonAddExclusion.setEnabled(!text.isEmpty());
				shell.setDefaultButton(buttonAddExclusion);
			}
		});
		FormData fd_textExclusion = new FormData();
		fd_textExclusion.top = new FormAttachment(listExcluded, 8);
		fd_textExclusion.left = new FormAttachment(labelGameDirectory, 0, SWT.LEFT);
		textExclusion.setLayoutData(fd_textExclusion);
		Util.attachDefaultTextContextMenu(textExclusion);

		buttonAddExclusion.setEnabled(false);
		buttonAddExclusion.setImage(ResourceManager.loadInternalImage("/res/icons/plus.png"));
		fd_textExclusion.right = new FormAttachment(buttonAddExclusion, -6, SWT.LEFT);
		buttonAddExclusion.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_buttonAddExclusion = new FormData();
		fd_buttonAddExclusion.top = new FormAttachment(listExcluded, 6);
		buttonAddExclusion.setLayoutData(fd_buttonAddExclusion);
		
		buttonRemoveExclusion.setEnabled(false);
		buttonRemoveExclusion.setImage(ResourceManager.loadInternalImage("/res/icons/minus.png"));
		fd_buttonAddExclusion.right = new FormAttachment(buttonRemoveExclusion, -6);
		buttonRemoveExclusion.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		FormData fd_buttonRemoveExclusion = new FormData();
		fd_buttonRemoveExclusion.top = new FormAttachment(listExcluded, 6);
		fd_buttonRemoveExclusion.right = new FormAttachment(buttonOpenGame, 0, SWT.RIGHT);
		buttonRemoveExclusion.setLayoutData(fd_buttonRemoveExclusion);
		
		Link link = new Link(groupMainSetup, SWT.NONE);
		link.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				MessageBox m = new MessageBox(shell, SWT.SHEET | SWT.ICON_INFORMATION);
				m.setText("Information about exclusion list");
				m.setMessage("The exclusion list can contain a list of folder filters which are applied when the Modloader searches for existing mods."
						+ " Excluding large asset folders can drastically reduce the time it takes to find mods."
						+ "\n\nThese filters can be used:\n"
						+ "- A regular word matches all folders of that name.\n"
						+ "- Asterisk before the word matches all folders that end in the word.\n"
						+ "- Asterisk after the word matches all folders that start with the word.\n"
						+ "- Asterisks on either side match all folders that contain the word.\n"
						+ "- Starting with a forward slash matches only top-level folders.");
				m.open();
			}
		});
		FormData fd_link = new FormData();
		fd_link.bottom = new FormAttachment(labelExcludedFolders, 0, SWT.BOTTOM);
		fd_link.right = new FormAttachment(buttonOpenGame, 0, SWT.RIGHT);
		link.setLayoutData(fd_link);
		link.setText("<a>Info</a>");

		loadUserSettings();
	}
	
	private void loadUserSettings()
	{
		if(toBool("UseCustomModDir")) {			
			radioCustom.setSelection(true);
			radioGameDir.setSelection(false);
			textModDir.setEnabled(true);
			buttonOpenMod.setEnabled(true);
		}
		textModDir.setText(UserSettings.getVar("ModDir"));
		if(UserSettings.isSet("ExcludedFolders")) {
			String s = UserSettings.getVar("ExcludedFolders");
			if(!s.isEmpty()) listExcluded.setItems(s.split(";"));
			else listExcluded.removeAll();
		}
		if(UserSettings.isSet("AmnesiaDir"))
		{
			textGameDir.setText(UserSettings.getVar("AmnesiaDir"));
		}
		else
		{
			// Attempt to guess where Amnesia might be installed.
			String[] paths = new String[] {
					"C:/Program Files (x86)/Amnesia The Dark Descent",
					"D:/Program Files (x86)/Amnesia The Dark Descent",
					"C:/Program Files (x86)/Steam/steamapps/common/Amnesia The Dark Descent",
					"D:/Program Files (x86)/Steam/steamapps/common/Amnesia The Dark Descent",
					"D:/SteamLibrary/steamapps/common/Amnesia The Dark Descent"
					//System.getProperty("user.home") + "/AppData/Local/Discord/"
			};
			File f;
			for(String path : paths)
			{				
				f = new File(path);
				if(f.isDirectory())
				{
					textGameDir.setText(f.getAbsolutePath());
					break;
				}
			}
		}
	}
	
	private boolean toBool(String value) {
		String val = UserSettings.getVar(value);
		if(val.isEmpty()) return false;
		return Boolean.parseBoolean(val);
	}
	
	public void saveSettings() {
		UserSettings.setVar("AmnesiaDir", 		textGameDir.getText());
		UserSettings.setVar("UseCustomModDir", 	radioCustom.getSelection());
		UserSettings.setVar("ModDir", 			textModDir.getText());
		UserSettings.setVar("ExcludedFolders", 	getExcludedFoldersConcat());
	}

	public String[] getExcludedFolders() {
		return listExcluded.getItems();
	}
	
	public String getExcludedFoldersConcat() {
		try {
			String string = "";
			for(String s : getExcludedFolders())
				string += ";" + s;
			if(string.isEmpty()) return "";
			return string.substring(1, string.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
