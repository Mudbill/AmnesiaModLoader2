package net.buttology.modloader.util;

import java.net.MalformedURLException;
import java.net.URL;

public enum EOperatingSystem {
	WINDOWS,
	MACOS,
	LINUX,
	UNKNOWN;
	
	public String getExecutableExtension() {
		switch(this) {
		case WINDOWS:
			return ".exe";
		case MACOS:
			return ".app";
		default:
			return "";
		}
	}
	
	public URL getUpdateUrl() {
		URL url = null;
		try {
			switch(this)
			{
			case WINDOWS:
				url = new URL("http://www.buttology.net/assets/other/modloader_version_win.txt");
				break;
			case MACOS:
				url = new URL("http://www.buttology.net/assets/other/modloader_version_mac.txt");
				break;
			default:
				return null;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
}
