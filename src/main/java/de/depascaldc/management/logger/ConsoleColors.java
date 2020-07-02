package de.depascaldc.management.logger;

public class ConsoleColors {

	public static final String RESET = "\u001b[0m";
	public static final String BLACK = "\u001b[0;30m";
	public static final String RED = "\u001b[0;31m";
	public static final String GREEN = "\u001b[0;32m";
	public static final String YELLOW = "\u001b[0;33m";
	public static final String BLUE = "\u001b[0;34m";
	public static final String PURPLE = "\u001b[0;35m";
	public static final String CYAN = "\u001b[0;36m";
	public static final String WHITE = "\u001b[0;37m";
	public static final String BLACK_BOLD = "\u001b[1;30m";
	public static final String RED_BOLD = "\u001b[1;31m";
	public static final String GREEN_BOLD = "\u001b[1;32m";
	public static final String YELLOW_BOLD = "\u001b[1;33m";
	public static final String BLUE_BOLD = "\u001b[1;34m";
	public static final String PURPLE_BOLD = "\u001b[1;35m";
	public static final String CYAN_BOLD = "\u001b[1;36m";
	public static final String WHITE_BOLD = "\u001b[1;37m";
	public static final String BLACK_UNDERLINED = "\u001b[4;30m";
	public static final String RED_UNDERLINED = "\u001b[4;31m";
	public static final String GREEN_UNDERLINED = "\u001b[4;32m";
	public static final String YELLOW_UNDERLINED = "\u001b[4;33m";
	public static final String BLUE_UNDERLINED = "\u001b[4;34m";
	public static final String PURPLE_UNDERLINED = "\u001b[4;35m";
	public static final String CYAN_UNDERLINED = "\u001b[4;36m";
	public static final String WHITE_UNDERLINED = "\u001b[4;37m";
	public static final String BLACK_BACKGROUND = "\u001b[40m";
	public static final String RED_BACKGROUND = "\u001b[41m";
	public static final String GREEN_BACKGROUND = "\u001b[42m";
	public static final String YELLOW_BACKGROUND = "\u001b[43m";
	public static final String BLUE_BACKGROUND = "\u001b[44m";
	public static final String PURPLE_BACKGROUND = "\u001b[45m";
	public static final String CYAN_BACKGROUND = "\u001b[46m";
	public static final String WHITE_BACKGROUND = "\u001b[47m";
	public static final String BLACK_BRIGHT = "\u001b[0;90m";
	public static final String RED_BRIGHT = "\u001b[0;91m";
	public static final String GREEN_BRIGHT = "\u001b[0;92m";
	public static final String YELLOW_BRIGHT = "\u001b[0;93m";
	public static final String BLUE_BRIGHT = "\u001b[0;94m";
	public static final String PURPLE_BRIGHT = "\u001b[0;95m";
	public static final String CYAN_BRIGHT = "\u001b[0;96m";
	public static final String WHITE_BRIGHT = "\u001b[0;97m";
	public static final String BLACK_BOLD_BRIGHT = "\u001b[1;90m";
	public static final String RED_BOLD_BRIGHT = "\u001b[1;91m";
	public static final String GREEN_BOLD_BRIGHT = "\u001b[1;92m";
	public static final String YELLOW_BOLD_BRIGHT = "\u001b[1;93m";
	public static final String BLUE_BOLD_BRIGHT = "\u001b[1;94m";
	public static final String PURPLE_BOLD_BRIGHT = "\u001b[1;95m";
	public static final String CYAN_BOLD_BRIGHT = "\u001b[1;96m";
	public static final String WHITE_BOLD_BRIGHT = "\u001b[1;97m";
	public static final String BLACK_BACKGROUND_BRIGHT = "\u001b[0;100m";
	public static final String RED_BACKGROUND_BRIGHT = "\u001b[0;101m";
	public static final String GREEN_BACKGROUND_BRIGHT = "\u001b[0;102m";
	public static final String YELLOW_BACKGROUND_BRIGHT = "\u001b[0;103m";
	public static final String BLUE_BACKGROUND_BRIGHT = "\u001b[0;104m";
	public static final String PURPLE_BACKGROUND_BRIGHT = "\u001b[0;105m";
	public static final String CYAN_BACKGROUND_BRIGHT = "\u001b[0;106m";
	public static final String WHITE_BACKGROUND_BRIGHT = "\u001b[0;107m";

	public static String colors[] = { RESET, BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, BLACK_BOLD, RED_BOLD,
			GREEN_BOLD, YELLOW_BOLD, BLUE_BOLD, PURPLE_BOLD, CYAN_BOLD, WHITE_BOLD, BLACK_UNDERLINED, RED_UNDERLINED,
			GREEN_UNDERLINED, YELLOW_UNDERLINED, BLUE_UNDERLINED, PURPLE_UNDERLINED, CYAN_UNDERLINED, WHITE_UNDERLINED,
			BLACK_BACKGROUND, RED_BACKGROUND, GREEN_BACKGROUND, YELLOW_BACKGROUND, BLUE_BACKGROUND, PURPLE_BACKGROUND,
			CYAN_BACKGROUND, WHITE_BACKGROUND, BLACK_BRIGHT, RED_BRIGHT, GREEN_BRIGHT, YELLOW_BRIGHT, BLUE_BRIGHT,
			PURPLE_BRIGHT, CYAN_BRIGHT, WHITE_BRIGHT, BLACK_BOLD_BRIGHT, RED_BOLD_BRIGHT, GREEN_BOLD_BRIGHT,
			YELLOW_BOLD_BRIGHT, BLUE_BOLD_BRIGHT, PURPLE_BOLD_BRIGHT, CYAN_BOLD_BRIGHT, WHITE_BOLD_BRIGHT,
			BLACK_BACKGROUND_BRIGHT, RED_BACKGROUND_BRIGHT, GREEN_BACKGROUND_BRIGHT, YELLOW_BACKGROUND_BRIGHT,
			BLUE_BACKGROUND_BRIGHT, PURPLE_BACKGROUND_BRIGHT, CYAN_BACKGROUND_BRIGHT, WHITE_BACKGROUND_BRIGHT };

	public static String stripColors(String message) {
		for (String color : colors) {
			if (message.contains(color))
				message = message.replace(color, "");
		}
		return message;
	}
	
}
