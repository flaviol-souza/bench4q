package org.bench4Q.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class Bench4QBuild {

	private static final String s_versionString;
	private static final String s_dateString;

	static {
		try {
			final InputStream buildPropertiesStream = Bench4QBuild.class.getClassLoader()
					.getResourceAsStream("org/bench4Q/resources/build.properties");

			if (buildPropertiesStream == null) {
				throw new IOException("Could not find build.properties");
			}

			final Properties properties = new Properties();
			properties.load(buildPropertiesStream);

			s_versionString = properties.getProperty("version");
			s_dateString = properties.getProperty("date");
		} catch (IOException e) {
			UncheckedInterruptedException.ioException(e);
			throw new ExceptionInInitializerError(e);
		}
	}

	private Bench4QBuild() {
	}


	public static String getName() {
		return "The Bench4Q " + getVersionString();
	}

	public static String getVersionString() {
		return s_versionString;
	}

	public static String getDateString() {
		return s_dateString;
	}
}
