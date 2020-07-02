#!/bin/bash

# ======================================================== #
# ============== Servermanager by depascaldc ============= #
# ============== Copyright © 2020 depascaldc ============= #
# ================= All Rights Reserved! ================= #
# ======================================================== #

SCREEN="screen-name"
SERVICE="*ServerManager*.jar"
INITMEM="1M"
MAXMEM="500M"

if [ "$#" -eq 1 ]; then
	if [ "$1" == "inscreen" ]; then
		# Executed whem Parameter "inscreen"...
		while true
		do
			java -server -Xmx$MAXMEM -Xms$INITMEM -XX:+ExitOnOutOfMemoryError -jar $SERVICE nogui
			echo "Zum Abbrechen des Neustarts bitte Strg+C druecken!"
			echo "To abort the restart press Ctrl+C!"
			echo "Re STart in:"
			for i in 10 9 8 7 6 5 4 3 2 1
			do
				echo "$i..."
				sleep 1
			done
			echo "-- Re - Starting Server --"
		done
	fi
else
	screen -S $SCREEN bash $0 inscreen
fi
