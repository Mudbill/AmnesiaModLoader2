package net.buttology.modloader.gui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.buttology.modloader.Mod;
import net.buttology.modloader.Modloader;
import net.buttology.modloader.file.FileHandler;
import net.buttology.modloader.file.UserSettings;
import net.buttology.modloader.util.Log;
import net.buttology.modloader.util.ResourceManager;
import net.buttology.modloader.util.Util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;

public class CompositeModList extends Composite {
	
	private static final int ICON_SIZE = 48;
	
	private Table table;
	private Menu menu;
	private TableColumn colIcon;
	private TableColumn colModTitle;
	private ScrolledComposite scrolledComposite;
	private int iconSize = ICON_SIZE;
	private List<Mod> mods = new ArrayList<Mod>();
	private Listener scrollListener = new Listener() {
	    public void handleEvent(Event e) {
	    	int count = scrolledComposite.getVerticalBar().getSelection();
	    	scrolledComposite.getVerticalBar().setSelection(count - e.count*16);
	    	scrolledComposite.getVerticalBar().notifyListeners(SWT.Selection, e);
	    }
	};

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeModList(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		scrolledComposite = new ScrolledComposite(this, SWT.V_SCROLL);
		scrolledComposite.setAlwaysShowScrollBars(true);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		try {
			if(UserSettings.isSet("IconSize")) 
				iconSize = Integer.parseInt(UserSettings.getVar("IconSize"));
		} catch(NumberFormatException e) { Log.error("Invalid IconSize value."); }
		
		table = new Table(scrolledComposite, SWT.FULL_SELECTION);
		table.addMenuDetectListener(new MenuDetectListener() {
			public void menuDetected(MenuDetectEvent e) {
				if(table.getSelectionIndex() == -1) e.doit = false;
			}
		});
		table.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event e) {
				int index = table.getSelectionIndex();
				if(mods.size() >= index)
				{					
					Mod mod = mods.get(index);
					Modloader.getForm().setModInfo(mod);
				}
				Modloader.getForm().setStartButtonEnabled(true);
			}
		});
		
		colIcon = new TableColumn(table, SWT.NONE);
		colIcon.setResizable(false);
		colIcon.setWidth(10 + iconSize);
		colIcon.setText("Icon");
		
		colModTitle = new TableColumn(table, SWT.NONE);
		colModTitle.setResizable(false);
		colModTitle.setWidth(200);
		colModTitle.setText("Mod title");
		
		menu = new Menu(table);
		table.setMenu(menu);
		
		MenuItem mntmLaunchMod = new MenuItem(menu, SWT.NONE);
		mntmLaunchMod.setImage(SWTResourceManager.getImage(CompositeModList.class, "/res/icons/game.png"));
		mntmLaunchMod.setText("Play mod");
		mntmLaunchMod.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Modloader.prepareLaunchMod(getSelectedMod());
			}
		});
		
		MenuItem mntmEditSettings = new MenuItem(menu, SWT.NONE);
		mntmEditSettings.setImage(SWTResourceManager.getImage(CompositeModList.class, "/res/icons/launcher.png"));
		mntmEditSettings.setText("Edit settings");
		mntmEditSettings.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Mod mod = getSelectedMod();
				if(mod == null) return;
				DialogEditSettings d = new DialogEditSettings(getShell(), SWT.DIALOG_TRIM | SWT.PRIMARY_MODAL, mod);
				d.open();
			}
		});
		
		MenuItem mntmCreateShortcut = new MenuItem(menu, SWT.NONE);
		mntmCreateShortcut.setImage(SWTResourceManager.getImage(CompositeModList.class, "/res/icons/shortcut.png"));
		mntmCreateShortcut.setText("Create shortcut");
		mntmCreateShortcut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Mod mod = getSelectedMod();
				if(mod == null) return;
				
				FileDialog fd = new FileDialog(getShell(), SWT.SAVE);
				fd.setFilterExtensions(new String[]{"*.lnk"});
				fd.setFilterNames(new String[]{"Shortcuts"});
				fd.setOverwrite(true);
				fd.setText("Select where to save your shortcut");
				fd.setFileName(mod.getName());
				String destination = fd.open();
				if(destination != null) 
					Util.createShortcut(mod.getAbsoluteStartFilePath(), destination);
			}
		});
		
		new MenuItem(menu, SWT.SEPARATOR);
		
		MenuItem mntmStartOfficialLauncher = new MenuItem(menu, SWT.NONE);
		mntmStartOfficialLauncher.setText("Start official launcher");
		mntmStartOfficialLauncher.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Modloader.startOfficialLauncher(getSelectedMod());
			}
		});
		scrolledComposite.setContent(table);
		scrolledComposite.setMinSize(scrolledComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		
//		scrolledComposite.addListener(SWT.MouseWheel, scrollListener);
		table.addListener(SWT.MouseWheel, scrollListener);
		table.setFocus();
		
		loadCache();
	}
	
	private void loadCache() {
		if("true".equals(UserSettings.getVar("UseCache")))
		{
			Mod[] mods = FileHandler.readCache();
			for(Mod mod : mods)
			{
				addModToList(mod);
			}
		}
	}
	
	public void setTitleWidth(int width) {
		colModTitle.setWidth(width - colIcon.getWidth() - 17);
	}
	
	public void setIconSize(int size) {
		iconSize = size;
	}
	
	public int getIconSize() {
		return iconSize;
	}
	
	public void addModToList(Mod mod) {
		TableItem ti = new TableItem(table, SWT.NONE);
		ti.setText(new String[] {"", mod.getName()});
		File iconFile = mod.getIcon();
		Image icon = ResourceManager.loadInternalImage("/res/icons/default.png");
		if(iconFile != null && iconFile.isFile())
			icon = ResourceManager.loadAbsoluteImage(iconFile.getAbsolutePath());
		if(!icon.isDisposed())
			ti.setImage(ResourceManager.scaleImage(icon, iconSize, iconSize));
		ti.setFont(ResourceManager.getSystemFontWithOptions(10, SWT.BOLD));
		scrolledComposite.setMinSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT, true));
		mods.add(mod);
	}
	
	public void clearMods() {
		table.removeAll();
		mods.clear();
	}
	
	public Mod getSelectedMod() {
		int index = table.getSelectionIndex();
		if(index != -1 && mods.size() > index) return mods.get(index);
		return null;
	}
	
	public Mod[] getAllMods() {
		Mod[] m = new Mod[0];
		return mods.toArray(m);
	}
	
	public Menu getMenu() {
		return menu;
	}
}
