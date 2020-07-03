package de.depascaldc.management.console;

import org.jline.builtins.Widgets;
import org.jline.reader.LineReader;
import org.jline.reader.Reference;

public class JLTCLI_KeypressWidget extends Widgets {
	private LineReader reader;
	enum Action {
		Up, Left, Right, Down, Quit, Retry
	};

	public JLTCLI_KeypressWidget(LineReader reader) {
		super(reader);
		this.reader = reader;
//		getKeyMap().bind(Action.Up, "\033[0A");
//		getKeyMap().bind(Action.Left, "\033[0B");
//		getKeyMap().bind(Action.Right, "\033[0C");
//		getKeyMap().bind(Action.Down, "\033[0D");
		char ctrlC = 0x3;
		String controlC = Character.toString(ctrlC);
		addWidget("exit-widget", this::runExit);
		getKeyMap().bind(new Reference("exit-widget"), controlC);
	}

	public boolean runExit() {
        try {
        	System.exit(0);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
	public LineReader getReader() {
		return reader;
	}
}
