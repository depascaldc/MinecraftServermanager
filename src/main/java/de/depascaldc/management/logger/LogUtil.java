/**
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   __  __                                                   _   
 *  |  \/  | __ _ _ __   __ _  __ _  ___ _ __ ___   ___ _ __ | |_ 
 *  | |\/| |/ _` | '_ \ / _` |/ _` |/ _ \ '_ ` _ \ / _ \ '_ \| __|
 *  | |  | | (_| | | | | (_| | (_| |  __/ | | | | |  __/ | | | |_ 
 *  |_|  |_|\__,_|_| |_|\__,_|\__, |\___|_| |_| |_|\___|_| |_|\__|
 *                           |___/                               
 * 
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *   
 *   Copyright © 2020 | depascaldc | Discord: [depascaldc]#4093
 *   
 */
package de.depascaldc.management.logger;

import java.text.SimpleDateFormat;
import java.util.Date;

class LogUtil {
	static String translate(LogType lt, String message) {
		Date date = new Date();
		switch (lt) {
		case DEBUG: {
			message = String.valueOf(ConsoleColors.CYAN_BRIGHT) + "[DEBUG | " + formatDateTime(date) + "] " + message;
			break;
		}
		case WARN: {
			message = String.valueOf(ConsoleColors.YELLOW_BOLD_BRIGHT) + "[WARN | " + formatDateTime(date) + "] "
					+ message;
			break;
		}
		case INFO:
		case LOG: {
			message = String.valueOf(ConsoleColors.GREEN) + "[LOG/INFO | " + formatDateTime(date) + "] " + message;
			break;
		}
		case ERROR: {
			message = String.valueOf(ConsoleColors.RED_BOLD) + "[ERROR | " + formatDateTime(date) + "] " + message;
			break;
		}
		}
		return message + String.valueOf(ConsoleColors.RESET);
	}

	static String translate(LogType lt, String message, Exception exception) {
		return translate(lt, message) + " \r\n" + String.valueOf(ConsoleColors.RED_BRIGHT) + exception.getMessage()
				+ " \r\n" + exception.getStackTrace() + String.valueOf(ConsoleColors.RESET);
	}

	static String formatDateTime(Date d) {
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-YYYY / HH:mm");
		return format.format(d);
	}

	static String transLateStackElement(StackTraceElement el) {
		return "Class: " + el.getClassName() + " Method: " + el.getMethodName() + " Line: " + el.getLineNumber();
	}

	enum LogType {
		INFO, DEBUG, WARN, LOG, ERROR;
	}

}
