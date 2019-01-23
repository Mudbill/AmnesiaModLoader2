package net.buttology.modloader.gui.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

public class BalloonMessage extends Composite {

	private Label icon;
	private Label text;
	private Display display = Display.getCurrent();
	
	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public BalloonMessage(Composite parent, int style) {
		super(parent, SWT.NO_BACKGROUND);
		
		setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		setLayout(new FormLayout());
		
		Label left = new Label(this, SWT.SEPARATOR | SWT.SHADOW_IN);
		FormData fd_left = new FormData();
		fd_left.top = new FormAttachment(0, 1);
		fd_left.bottom = new FormAttachment(100, -2);
		fd_left.left = new FormAttachment(0);
		fd_left.right = new FormAttachment(0, 2);
		left.setLayoutData(fd_left);
		
		Label top = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		FormData fd_top = new FormData();
		fd_top.top = new FormAttachment(0, 0);
		fd_top.bottom = new FormAttachment(0, 2);
		fd_top.left = new FormAttachment(0, 1);
		fd_top.right = new FormAttachment(100, -2);
		top.setLayoutData(fd_top);
		
		Label right = new Label(this, SWT.SEPARATOR | SWT.SHADOW_IN);
		FormData fd_right = new FormData();
		fd_right.top = new FormAttachment(0, 1);
		fd_right.bottom = new FormAttachment(100, -2);
		fd_right.left = new FormAttachment(100, -2);
		fd_right.right = new FormAttachment(100);
		right.setLayoutData(fd_right);
		
		Label bottom = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL | SWT.SHADOW_IN);
		FormData fd_bottom = new FormData();
		fd_bottom.top = new FormAttachment(100, -2);
		fd_bottom.bottom = new FormAttachment(100);
		fd_bottom.right = new FormAttachment(100, -2);
		fd_bottom.left = new FormAttachment(0, 1);
		bottom.setLayoutData(fd_bottom);
		
		Composite bg = new Composite(this, SWT.NONE);
		bg.setBackgroundMode(SWT.INHERIT_FORCE);
		bg.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		bg.setLayout(new FormLayout());
		FormData fd_bg = new FormData();
		fd_bg.bottom = new FormAttachment(bottom);
		fd_bg.right = new FormAttachment(right);
		fd_bg.left = new FormAttachment(left);
		fd_bg.top = new FormAttachment(top);
		bg.setLayoutData(fd_bg);
		
		icon = new Label(bg, SWT.NONE);
		FormData fd_icon = new FormData();
		fd_icon.top = new FormAttachment(0, 10);
		fd_icon.left = new FormAttachment(0, 10);
		icon.setLayoutData(fd_icon);

		if((style & SWT.ICON_INFORMATION) == SWT.ICON_INFORMATION)
		{
			icon.setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		}

		text = new Label(bg, SWT.WRAP);
		FormData fd_text = new FormData();
		fd_text.top = new FormAttachment(icon, 0, SWT.TOP);
		fd_text.left = new FormAttachment(icon, 10, SWT.RIGHT);
		fd_text.right = new FormAttachment(100, -10);
		text.setLayoutData(fd_text);
	}
	
	public void setText(String text) {
		this.text.setText(text);
		this.text.pack();
	}
	
//	public void setIconSize(int size) {
//		Image i = icon.getImage();
//	}
	
//	private Image resizeImage(Image source, int size) {
//		ImageData sourceData = source.getImageData().scaledTo(size, size);
//		Image _image = new Image(source.getDevice(), size, size);
//		ImageData _data = _image.getImageData();
//		_data.alphaData = sourceData.alphaData;
//		Image image = new Image(source.getDevice(), _data);
//		GC gc = new GC(image);
//		gc.setAdvanced(true);
//		gc.setAntialias(SWT.ON);
//		gc.setInterpolation(SWT.HIGH);
//		gc.drawImage(source, 0, 0, source.getBounds().width, source.getBounds().height, 0, 0, size, size);
//		gc.dispose();
//		_image.dispose();
//		return image;
//	}
}
