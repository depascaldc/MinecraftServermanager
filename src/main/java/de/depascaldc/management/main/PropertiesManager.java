package de.depascaldc.management.main;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import de.depascaldc.management.util.Utils;

public class PropertiesManager {

	private File propertiesFile;

	private Properties properties;

	public PropertiesManager(File propertiesFile) {
		properties = new Properties();
		if (!propertiesFile.exists()) {
			try {
				InputStream inputStream = new ByteArrayInputStream(
						getDefaultPropertiesString().getBytes(Charset.forName("UTF-8")));
				ServerManager.saveFile(propertiesFile, inputStream);
				properties.load(new FileReader(propertiesFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				properties.load(new FileReader(propertiesFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private String getDefaultPropertiesString() throws IOException {
		InputStream inputStream = ServerManager.class.getResourceAsStream("/servermanager.properties");
		String text = IOUtils.toString(inputStream, StandardCharsets.UTF_8.name());
		boolean textHasMorePlaceholder = text.contains("%randomPassword%");
		while (textHasMorePlaceholder) {
			text = text.replaceFirst("%randomPassword%", Utils.generateRandomPasswordString(32));
			textHasMorePlaceholder = text.contains("%randomPassword%");
		}
		return text;
	}

	public Properties getProperties() {
		return properties;
	}

	public File getPropertiesFile() {
		return propertiesFile;
	}

}
