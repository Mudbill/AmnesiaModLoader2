package net.buttology.modloader.gui;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

import net.buttology.modloader.Mod;
import net.buttology.modloader.Modloader;
import net.buttology.modloader.util.ResourceManager;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;

public class DialogModInfo extends Dialog {

	protected Object result;
	protected Shell shell;
	private Table table;
	private Label labelTitle;
	private Label labelAuthor;
	private Label icon;
	private TableColumn colProperty;
	private TableColumn colValue;
	private ControlAdapter resizeAdapter = new ControlAdapter() {
		@Override
		public void controlResized(ControlEvent e) {
			colValue.setWidth(table.getClientArea().width - colProperty.getWidth() - 1);
		}
	};

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogModInfo(Shell parent, int style) {
		super(parent, style);
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
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.RESIZE | SWT.PRIMARY_MODAL);
		shell.setImage(SWTResourceManager.getImage(DialogModInfo.class, "/res/icons/app_16.png"));
		shell.setMinimumSize(new Point(400, 300));
		shell.setSize(509, 332);
		shell.setText("Mod info");
		
		Point pRect = getParent().getLocation();
		shell.setLocation(pRect.x + (getParent().getSize().x - shell.getSize().x) / 2, pRect.y);
		shell.setLayout(new FormLayout());
		
		icon = new Label(shell, SWT.NONE);
		icon.setImage(SWTResourceManager.getImage(DialogModInfo.class, "/res/icons/default.png"));
		FormData fd_icon = new FormData();
		fd_icon.top = new FormAttachment(0, 10);
		fd_icon.left = new FormAttachment(0, 10);
		fd_icon.bottom = new FormAttachment(0, 74);
		fd_icon.right = new FormAttachment(0, 74);
		icon.setLayoutData(fd_icon);
		
		labelTitle = new Label(shell, SWT.NONE);
		FormData fd_labelTitle = new FormData();
		fd_labelTitle.top = new FormAttachment(icon, 0, SWT.TOP);
		fd_labelTitle.left = new FormAttachment(icon, 12);
		labelTitle.setLayoutData(fd_labelTitle);
		labelTitle.setText("<mod title>");
		labelTitle.setFont(ResourceManager.getSystemFontWithOptions(12, SWT.BOLD));
		
		labelAuthor = new Label(shell, SWT.NONE);
		FormData fd_labelAuthor = new FormData();
		fd_labelAuthor.top = new FormAttachment(labelTitle, 6);
		fd_labelAuthor.left = new FormAttachment(icon, 12);
		labelAuthor.setLayoutData(fd_labelAuthor);
		labelAuthor.setText("<mod author>");
		
		Label sep_1 = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		FormData fd_sep_1 = new FormData();
		fd_sep_1.top = new FormAttachment(icon, 6);
		fd_sep_1.left = new FormAttachment(0, 10);
		fd_sep_1.right = new FormAttachment(100, -10);
		sep_1.setLayoutData(fd_sep_1);
		
		Button buttonClose = new Button(shell, SWT.NONE);
		buttonClose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		FormData fd_buttonClose = new FormData();
		fd_buttonClose.bottom = new FormAttachment(100, -10);
		fd_buttonClose.left = new FormAttachment(sep_1, -90, SWT.RIGHT);
		fd_buttonClose.right = new FormAttachment(sep_1, 0, SWT.RIGHT);
		buttonClose.setLayoutData(fd_buttonClose);
		buttonClose.setText("Close");
		shell.setDefaultButton(buttonClose);
		
		table = new Table(shell, SWT.BORDER | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(buttonClose, -6);
		fd_table.right = new FormAttachment(sep_1, 0, SWT.RIGHT);
		fd_table.top = new FormAttachment(sep_1, 6);
		fd_table.left = new FormAttachment(icon, 0, SWT.LEFT);
		table.setLayoutData(fd_table);
		table.setLinesVisible(true);
		
		colProperty = new TableColumn(table, SWT.NONE);
		colProperty.setWidth(230);
		colProperty.setText("Property");
		
		colValue = new TableColumn(table, SWT.NONE);
		colValue.setResizable(false);
		colValue.setWidth(239);
		colValue.setText("Value");
		
		colProperty.addControlListener(resizeAdapter);
		shell.addControlListener(resizeAdapter);
		
		addItems(Modloader.getForm().getModList().getSelectedMod());
		setInfo(Modloader.getForm().getModList().getSelectedMod());
	}
	
	private void setInfo(Mod mod) {
		if(mod == null) return;
		labelTitle.setText(mod.getName());
		labelAuthor.setText("By " + mod.getAuthor());
		File iconFile = mod.getIcon();
		if(iconFile != null && iconFile.isFile())
		{
			Image image = ResourceManager.loadAbsoluteImage(iconFile.getAbsolutePath());
			if(image != null)
			{
				image = ResourceManager.scaleImage(image, 64, 64);
				icon.setImage(image);
			}
		}
	}
	
	private void addItems(Mod mod) {
		if(mod == null) return;
		
		Map<String, String> map = new LinkedHashMap<String, String>();
		map.put("Title", mod.getName());
		map.put("Author", mod.getAuthor());
		map.put("Start file", mod.getRelativeStartFilePath());
		map.put("Required game version", mod.getRequiredVersion());
//		map.put("Isolated", "Yes");
		map.put("Custom shaders", mod.hasCustomShaders() ? "Yes" : "No");
		map.put("Custom executable", mod.getCustomExecutableName());
		
		for(String key : map.keySet())
		{
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(new String[] {key, map.get(key)});
		}
	}
}
