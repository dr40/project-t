<?php

		
	/*<!-- Show highscore -->*/
	$score = $database->getAllScore(-1,0,-1,"`level` DESC,`score` DESC");
	if ((isset($score)) && (count($score))) {
			echo "<div id='tableView'>";
			echo "<table cellpadding='8px' border='0px' cellspacing='0px' style='width:100%'>";
			echo "<tr>";
			echo "<th>Player</th>";
			echo "<th>Score</th>";
			echo "<th>Level</th>";
			echo "<th>Done on</th>";
			echo "</tr>";
			
			$id = 0;
			foreach($score as $item) {
				echo "<tr class='" . (($id % 2 == 0) ? "evenRow" : "oddRow") . "'>";
				$id++;
				echo "<td>" . $item["username"] . "</td>";
				echo "<td>" . $item["score"] . "</td>";
				echo "<td>" . $item["level"] . "</td>";
				echo "<td>" . @date("Y-m-d H:i:s", @intval($item["score_timestamp"])) . "</td>";
				echo "</tr>";
			}
			echo "</table>";
			echo "</div>";
	} else {
		echo "<div class='jumbotron'><h1>No highscore yet</h1></div>";
	}
