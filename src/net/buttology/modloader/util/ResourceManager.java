package net.buttology.modloader.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

import net.buttology.modloader.Modloader;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.SWTResourceManager;

public class ResourceManager {

	private static List<Font> fonts = new ArrayList<Font>();
	private static List<Image> images = new ArrayList<Image>();
	private static FileLock lock;
	
	/**
	 * Create a locked session file in the current directory.
	 * @throws IOException 
	 */
	public static boolean lockSessionFile(String fileName) throws IOException
	{
		File f = new File(fileName);
		if(!f.isFile())
		{
			try
			{
				Log.info("Lock file not found, creating.");
				f.createNewFile();
			}
			catch(IOException e)
			{
				Log.error("Could not create lock file! Missing write privileges in directory?");
				e.printStackTrace();
				return false;
			}
		}
		
		FileChannel channel = null;
		try
		{
			channel = new RandomAccessFile(f, "rw").getChannel();
			lock = channel.tryLock();
			if(lock == null)
			{
				Log.error("Failed to lock session file. Is another instance running?");
				Modloader.promptErrorMessage("Failed to create session. Is another instance running?");
				return false;
//				throw new IOException();
			}
		}
		catch(IOException e)
		{
//			throw e;
		}
		
		return true;
	}
	
	public static void unlockSessionFile()
	{
		Log.info("Unlocking session file.");
		try
		{
			if(lock != null) lock.release();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the system default font, now applied with the given font size and style.
	 * @param fontSize (or -1 if default)
	 * @param fontStyle (or -1 if default)
	 * @return
	 */
	public static Font getSystemFontWithOptions(int fontSize, int fontStyle)
	{
		FontData[] fd = Display.getCurrent().getSystemFont().getFontData();
		if(fontSize != -1) fd[0].setHeight(fontSize);
		if(fontStyle != -1) fd[0].setStyle(fontStyle);
		Font f = new Font(Display.getCurrent(), fd);
		fonts.add(f);
		return f;
	}
	
	/**
	 * Loads a single image from within the package.
	 * @param path
	 * @return
	 */
	public static Image loadInternalImage(String path)
	{
		Image image = SWTResourceManager.getImage(ResourceManager.class, path);
//		images.add(image);
		return image;
	}
	
	public static ImageData[] loadInternalGif(String path)
	{
		ImageLoader loader = new ImageLoader();
		ImageData[] data = loader.load(ResourceManager.class.getResourceAsStream(path));
		return data;
	}
	
	/**
	 * Loads multiple relative images.
	 * @param relativePaths
	 * @return
	 */
	public static Image[] loadInternalImages(String... relativePaths)
	{
		List<Image> imageList = new ArrayList<Image>();
		for(String s : relativePaths)
			imageList.add(loadInternalImage(s));
		Image[] images = new Image[0];
		return imageList.toArray(images);
	}
	
	public static Image loadAbsoluteImage(String path) {
		if(path.isEmpty()) return null;
		Log.info("Loading image: " + path);
		Image image = new Image(Display.getCurrent(), path);
		images.add(image);
		return image;
	}
	
	/**
	 * Scales an image to the specified icon size.
	 * @param inputImage
	 * @param size
	 * @return
	 */
	public static Image scaleImage(Image inputImage, int x, int y) {
		if(inputImage.isDisposed()) return inputImage;
		// Extract the image data and scale it (for purpose of alpha).
		ImageData inputImageData = inputImage.getImageData().scaledTo(x, y);
		// Create a new image with the new size.
		Image tempImage = new Image(inputImage.getDevice(), x, y);
		// Extract the image data from the new image (empty at this point).
		ImageData tempImageData = tempImage.getImageData();
		// Set the alpha data to that of the old image data.
		tempImageData.alphaData = inputImageData.alphaData;
		// Create new image using the new image data (empty but with proper alpha).
		Image finalImage = new Image(inputImage.getDevice(), tempImageData);
		
		// Create a graphics context from the final image.
		GC gc = new GC(finalImage);
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		// Draw the pixels from the original image onto the final image using the new size.
		gc.drawImage(inputImage, 0, 0, inputImage.getBounds().width, inputImage.getBounds().height, 0, 0, x, y);

		// Dispose resources we no longer need.
		if(images.contains(inputImage)) {
			inputImage.dispose();
			images.remove(inputImage);
		}
		tempImage.dispose();
		gc.dispose();
		
		images.add(finalImage);
		return finalImage;
	}
	
	public static Color getSystemColor(int colorId)
	{
		return SWTResourceManager.getColor(colorId);
	}

	/**
	 * Disposes all currently used resources.
	 */
	public static void flush()
	{
		for(Image i : images)
			if(!i.isDisposed()) i.dispose();
		for(Font f : fonts)
			if(!f.isDisposed()) f.dispose();
		images.clear();
		fonts.clear();
	}
	
}
