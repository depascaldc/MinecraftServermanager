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
