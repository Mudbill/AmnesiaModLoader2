package net.buttology.modloader.util;

import java.util.HashMap;
import java.util.Map;

public class LangList {

	private Map<String, String> langs = new HashMap<String, String>();
	private String defaultLang;
	
	public LangList(Map<String, String> langs, String defaultLang)
	{
		this.langs = langs;
		this.defaultLang = defaultLang;
	}

	public Map<String, String> getLangs() {
		return langs;
	}

	public String getDefaultLang() {
		return defaultLang;
	}
	
}
