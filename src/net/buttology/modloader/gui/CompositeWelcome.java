package net.buttology.modloader.gui;

import net.buttology.modloader.util.ResourceManager;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;

public class CompositeWelcome extends Composite {

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public CompositeWelcome(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());
		
		Label lblWelcome = new Label(this, SWT.WRAP | SWT.CENTER);
		lblWelcome.setForeground(ResourceManager.getSystemColor(SWT.COLOR_DARK_BLUE));
		lblWelcome.setFont(ResourceManager.getSystemFontWithOptions(11, SWT.BOLD));
		FormData fd_lblWelcome = new FormData();
		fd_lblWelcome.top = new FormAttachment(0);
		fd_lblWelcome.left = new FormAttachment(0);
		fd_lblWelcome.right = new FormAttachment(100);
		lblWelcome.setLayoutData(fd_lblWelcome);
		lblWelcome.setText("Welcome");
		
		Label lblLooksLikeThere = new Label(this, SWT.WRAP | SWT.CENTER);
		FormData fd_lblLooksLikeThere = new FormData();
		fd_lblLooksLikeThere.left = new FormAttachment(0);
		fd_lblLooksLikeThere.right = new FormAttachment(100);
		fd_lblLooksLikeThere.top = new FormAttachment(lblWelcome, 6);
		lblLooksLikeThere.setLayoutData(fd_lblLooksLikeThere);
		lblLooksLikeThere.setText("Looks like there are no mods here. How about searching for some?");
		
		Label lblMakeSureYouve = new Label(this, SWT.WRAP | SWT.CENTER);
		lblMakeSureYouve.setAlignment(SWT.LEFT);
		FormData fd_lblMakeSureYouve = new FormData();
		fd_lblMakeSureYouve.left = new FormAttachment(0, 64);
		fd_lblMakeSureYouve.top = new FormAttachment(lblLooksLikeThere, 16);
		lblMakeSureYouve.setLayoutData(fd_lblMakeSureYouve);
		lblMakeSureYouve.setText("Make sure you've first set the correct settings down there.");
		
		Label iconArrowDR = new Label(this, SWT.NONE);
		fd_lblMakeSureYouve.right = new FormAttachment(iconArrowDR, -6);
		iconArrowDR.setImage(ResourceManager.loadInternalImage("/res/icons/arrow_dr.png"));
		FormData fd_iconArrowDR = new FormData();
		fd_iconArrowDR.top = new FormAttachment(lblLooksLikeThere, 6);
		fd_iconArrowDR.right = new FormAttachment(100, -10);
		iconArrowDR.setLayoutData(fd_iconArrowDR);
		
		Label iconArrowU = new Label(this, SWT.NONE);
		iconArrowU.setImage(ResourceManager.loadInternalImage("/res/icons/arrow_bend.png"));
		FormData fd_iconArrowU = new FormData();
		fd_iconArrowU.top = new FormAttachment(lblMakeSureYouve, 6);
		iconArrowU.setLayoutData(fd_iconArrowU);
		
		Label lblThenStartSearching = new Label(this, SWT.WRAP);
		fd_iconArrowU.right = new FormAttachment(lblThenStartSearching, -6);
		FormData fd_lblThenStartSearching = new FormData();
		fd_lblThenStartSearching.right = new FormAttachment(100, -64);
		fd_lblThenStartSearching.left = new FormAttachment(0, 80);
		fd_lblThenStartSearching.top = new FormAttachment(lblMakeSureYouve, 49);
		lblThenStartSearching.setLayoutData(fd_lblThenStartSearching);
		lblThenStartSearching.setText("Then start searching for them up there.");
	}
}
