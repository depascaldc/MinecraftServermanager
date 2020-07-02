package de.depascaldc.management.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import de.depascaldc.management.annotations.Categorie;
import de.depascaldc.management.logger.Logger;
import de.depascaldc.management.main.exceptions.ServerException;
import de.depascaldc.management.util.HastebinUtils;

public class General_Tests {

	private Logger l;

	public General_Tests() {
		l = new Logger(true);
	}

	@Categorie("General")
	@DisplayName("General Tests")
	@Test
	void testLogger() {
		Assertions.assertAll(() -> {
			l.debug("Loggertest DEBUG");
			l.debug("Loggertest T_DEBUG", new ServerException("Tests__ Exception throwable..."));
		}, () -> {
			l.error("Loggertest ERROR");
			l.error("Loggertest T_ERROR", new ServerException("Tests__ Exception throwable..."));
		}, () -> {
			l.warn("Loggertest WARN");
			l.warn("Loggertest T_WARN", new ServerException("Tests__ Exception throwable..."));
		}, () -> {
			l.info("Loggertest INFO");
			l.info("Loggertest T_INFO", new ServerException("Tests__ Exception throwable..."));
		}, () -> {
			l.log("Loggertest LOG/INFO");
			l.log("Loggertest T_LOG/INFO", new ServerException("Tests__ Exception throwable..."));
		}, () -> {
			l.out(">> Loggertest OUT");
		}, () -> {
			l.log("TESTING HastebinUtil");
			StringBuilder sb = new StringBuilder();
			for (String line : Logger.TESTS_CACHED_LOG) {
				sb.append(line);
				sb.append("\n");
			}
			String url = HastebinUtils.upload(sb.toString());
			l.log("HastebinUtil PASTE = " + url + ".log");
		});
	}

}
