package net.buttology.modloader.gui;

import net.buttology.modloader.Modloader;
import net.buttology.modloader.util.ResourceManager;
import net.buttology.modloader.util.Util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class DialogGameRunning extends Dialog {

	protected Object result;
	protected Shell shell;
	private int gifIndex = 0;
	private Thread gifThread;
	private ShellAdapter noclose = new ShellAdapter() {
		@Override
		public void shellClosed(ShellEvent e) {
			e.doit = false;
		}
	};

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public DialogGameRunning(Shell parent, int style) {
		super(parent, style);
		createContents();
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		shell.open();
//		gifThread.start();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	public void close() {
		shell.removeShellListener(noclose);
		shell.close();
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.addShellListener(noclose);
		shell.setSize(389, 206);
		shell.setText("Game running");
		shell.setLayout(new FormLayout());
		Util.centerShellOnParent(shell);
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);
		composite.setBackground(ResourceManager.getSystemColor(SWT.COLOR_WHITE));
		composite.setLayout(new FormLayout());
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, -44);
		fd_composite.right = new FormAttachment(100);
		fd_composite.top = new FormAttachment(0);
		fd_composite.left = new FormAttachment(0);
		composite.setLayoutData(fd_composite);
		
		Label separator = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_separator = new FormData();
		fd_separator.top = new FormAttachment(composite, 0, SWT.BOTTOM);
		fd_separator.left = new FormAttachment(0);
		fd_separator.right = new FormAttachment(100);
		separator.setLayoutData(fd_separator);
		
		Button buttonForceQuit = new Button(shell, SWT.NONE);
		FormData fd_buttonForceQuit = new FormData();
		fd_buttonForceQuit.bottom = new FormAttachment(100, -10);
		fd_buttonForceQuit.right = new FormAttachment(100, -10);
		fd_buttonForceQuit.left = new FormAttachment(100, -90);
		buttonForceQuit.setLayoutData(fd_buttonForceQuit);
		buttonForceQuit.setText("Force quit");
		
		Button buttonIgnore = new Button(shell, SWT.NONE);
		buttonIgnore.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox m = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				m.setText("Ignore?");
				m.setMessage("Are you sure you want to ignore the running process? This will return control to the modloader,"
						+ " however if custom shaders are applied, these won't be unapplied.\n"
						+ "The modloader is designed to wait for the process to shut down on its own.");
				if(m.open() == SWT.YES) {
					close();
				}
			}
		});
		FormData fd_buttonIgnore = new FormData();
		fd_buttonIgnore.bottom = new FormAttachment(buttonForceQuit, 0, SWT.BOTTOM);
		fd_buttonIgnore.right = new FormAttachment(buttonForceQuit, -6, SWT.LEFT);
		fd_buttonIgnore.left = new FormAttachment(buttonForceQuit, -86, SWT.LEFT);
		buttonIgnore.setLayoutData(fd_buttonIgnore);
		buttonIgnore.setText("Ignore");
		
		Label label = new Label(composite, SWT.NONE);
		FormData fd_label = new FormData();
		label.setLayoutData(fd_label);
		label.setText("Waiting for Amnesia to shut down...");
		
		Canvas canvas = new Canvas(composite, SWT.NONE);
		fd_label.top = new FormAttachment(canvas, 6, SWT.TOP);
		fd_label.left = new FormAttachment(canvas, 17);
		FormData fd_canvas = new FormData();
		fd_canvas.top = new FormAttachment(0, 20);
		fd_canvas.left = new FormAttachment(0, 20);
		fd_canvas.right = new FormAttachment(0, 44);
		fd_canvas.bottom = new FormAttachment(0, 44);
		canvas.setLayoutData(fd_canvas);

		ImageLoader loader = new ImageLoader();
		loader.load(getClass().getResourceAsStream("/res/icons/spinner.gif"));
		Image image = new Image(shell.getDisplay(), loader.data[0]);
		final GC cg = new GC(image);
		canvas.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.drawImage(image, 0, 0);
				gifThread = new Thread() {
					public void run() {
						long time = System.currentTimeMillis();
						int delay = loader.data[gifIndex].delayTime;
						while(time + delay * 10 > System.currentTimeMillis())
						{}
						
						if(!shell.isDisposed()) {
							shell.getDisplay().asyncExec(new Runnable() {
								@Override
								public void run() {
									gifIndex = gifIndex == loader.data.length - 1 ? 0 : gifIndex + 1;
									ImageData imageData = loader.data[gifIndex];
									Image currentImage = new Image(shell.getDisplay(), imageData);
									cg.drawImage(currentImage, imageData.x, imageData.y);
									currentImage.dispose();
									canvas.redraw();
								}
							});
						}
					}
				};
				gifThread.start();
			}
		});

		
		
		buttonForceQuit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox m = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
				m.setText("Force quit?");
				m.setMessage("Are you sure you want to force quit the game process?");
				if(m.open() == SWT.YES)
				{
					buttonForceQuit.setEnabled(false);
					buttonIgnore.setEnabled(false);
					Modloader.forceQuitGame();
				}
			}
		});
	}
}
