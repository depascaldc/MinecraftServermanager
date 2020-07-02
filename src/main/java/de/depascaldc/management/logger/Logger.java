package de.depascaldc.management.logger;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.depascaldc.management.Initializer;
import de.depascaldc.management.logger.LogUtil.LogType;
import de.depascaldc.management.main.ServerManager;

public class Logger {
	private String dataPath;

	private boolean isTestRun = false;

	public static List<String> TESTS_CACHED_LOG = new ArrayList<String>();

	public Logger() {
		this.dataPath = new File(Initializer.MAIN_PATH).getAbsolutePath() + "/";
		if (!new File(dataPath + "managerlogs/").exists()) {
			new File(dataPath + "managerlogs/").mkdirs();
		}
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "==========================================================");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "============== ServerInstaller by depascaldc =============");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "=============== Copyright © 2020 depascaldc ==============");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "================== All Rights Reserved! ==================");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "==========================================================");
	}

	public Logger(boolean isTestRun) {
		this.isTestRun = isTestRun;
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "==========================================================");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "=============== Logger - TESTS ServerManager =============");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "=============== Copyright © 2020 depascaldc ==============");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "================== All Rights Reserved! ==================");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "==========================================================");
	}

	public Logger(String PATH) {
		this.dataPath = new File(PATH).getAbsolutePath() + "/";
		if (!new File(dataPath + "managerlogs/").exists()) {
			new File(dataPath + "managerlogs/").mkdirs();
		}
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "==========================================================");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "=============== ServerManager by depascaldc ==============");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "=============== Copyright © 2020 depascaldc ==============");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "================== All Rights Reserved! ==================");
		info(ConsoleColors.CYAN_BOLD_BRIGHT + "==========================================================");
	}

	public void log(String message) {
		System.out.println(saveline(LogUtil.translate(LogType.LOG, message)));
	}

	public void out(String message) {
		String message_final = ConsoleColors.WHITE + "[OUT] > " + ConsoleColors.RESET + message + ConsoleColors.RESET;
		if(isTestRun) {
			TESTS_CACHED_LOG.add(ConsoleColors.stripColors(message_final));
		}
		System.out.println(message_final);
	}

	public void info(String message) {
		System.out.println(saveline(LogUtil.translate(LogType.INFO, message)));
	}

	public void debug(String message) {
		boolean show = true;
		if (ServerManager.getProperties() != null) {
			if (ServerManager.getProperties().getProperty("loglevel") != null) {
				int lv = Integer.valueOf(ServerManager.getProperties().getProperty("loglevel"));
				if (lv < 0)
					show = false;
			}
		}
		if (show) {
			System.out.println(saveline(LogUtil.translate(LogType.DEBUG, message)));
		}
	}

	public void warn(String message) {
		System.out.print(saveline(LogUtil.translate(LogType.WARN, message)));
		System.out.println();
	}

	public void error(String message) {
		System.out.println(saveline(LogUtil.translate(LogType.ERROR, message)));
	}

	public void log(String message, Exception exception) {
		System.out.println(saveline(LogUtil.translate(LogType.LOG, message)));
		if (exception.getMessage() != null) {
			System.out.println(saveline(LogUtil.translate(LogType.LOG, exception.getMessage())));
		}
		if (exception.getStackTrace() != null) {
			int i = 0;
			for (StackTraceElement element : exception.getStackTrace()) {
				if (i > 1 && isTestRun) {
					break;
				} else {
					if (i > 19) {
						break;
					}
				}
				i++;
				System.out.println(saveline(LogUtil.translate(LogType.LOG, LogUtil.transLateStackElement(element))));
			}
		}
	}

	public void info(String message, Exception exception) {
		System.out.println(saveline(LogUtil.translate(LogType.INFO, message)));
		if (exception.getMessage() != null) {
			System.out.println(saveline(LogUtil.translate(LogType.INFO, exception.getMessage())));
		}
		if (exception.getStackTrace() != null) {
			int i = 0;
			for (StackTraceElement element : exception.getStackTrace()) {
				if (i > 1 && isTestRun) {
					break;
				} else {
					if (i > 19) {
						break;
					}
				}
				i++;
				System.out.println(saveline(LogUtil.translate(LogType.INFO, LogUtil.transLateStackElement(element))));
			}
		}
	}

	public void debug(String message, Exception exception) {
		System.out.println(saveline(LogUtil.translate(LogType.DEBUG, message)));
		if (exception.getMessage() != null) {
			System.out.println(saveline(LogUtil.translate(LogType.DEBUG, exception.getMessage())));
		}
		if (exception.getStackTrace() != null) {
			int i = 0;
			for (StackTraceElement element : exception.getStackTrace()) {
				if (i > 1 && isTestRun) {
					break;
				} else {
					if (i > 19) {
						break;
					}
				}
				i++;
				System.out.println(saveline(LogUtil.translate(LogType.DEBUG, LogUtil.transLateStackElement(element))));
			}
		}
	}

	public void warn(String message, Exception exception) {
		System.out.println(saveline(LogUtil.translate(LogType.WARN, message)));
		if (exception.getMessage() != null) {
			System.out.println(saveline(LogUtil.translate(LogType.WARN, exception.getMessage())));
		}
		if (exception.getStackTrace() != null) {
			int i = 0;
			for (StackTraceElement element : exception.getStackTrace()) {
				if (i > 1 && isTestRun) {
					break;
				} else {
					if (i > 19) {
						break;
					}
				}
				i++;
				System.out.println(saveline(LogUtil.translate(LogType.WARN, LogUtil.transLateStackElement(element))));
			}
		}
	}

	public void error(String message, Exception exception) {
		System.out.println(saveline(LogUtil.translate(LogType.ERROR, message)));
		if (exception.getMessage() != null) {
			System.out.println(saveline(LogUtil.translate(LogType.ERROR, exception.getMessage())));
		}
		if (exception.getStackTrace() != null) {
			int i = 0;
			for (StackTraceElement element : exception.getStackTrace()) {
				if (i > 1 && isTestRun) {
					break;
				} else {
					if (i > 19) {
						break;
					}
				}
				i++;
				System.out.println(saveline(LogUtil.translate(LogType.ERROR, LogUtil.transLateStackElement(element))));
			}
		}
	}

	private String saveline(String message) {
		if (!isTestRun) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			Calendar cal = Calendar.getInstance();
			try {
				File file = new File("managerlogs/log_" + dateFormat.format(cal.getTime()) + ".log");
				FileWriter fr = new FileWriter(file, true);
				fr.write(String.valueOf(ConsoleColors.stripColors(message) + "\n"));
				fr.close();
			} catch (Exception folder) {
			}
		} else {
			TESTS_CACHED_LOG.add(ConsoleColors.stripColors(message));
		}
		return message;
	}

}
