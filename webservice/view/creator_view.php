<?php
	


?>

	<script>

		var chronoMS = 0;

		function updateChrono() {

		}

		function doTap() {
			var textData = document.getElementById("data");
			if (textData) {
				/* Start if value empty - add otherwise time */
				Date d = new Date();
				int time = d.getTime();
				if (textData.value == "") {
					chronoMS = time;
				} else {
					textData.value = textData.value + ',';
				}
				textData.value = textData.value + (time - chronoMS);
			}
		}


	</script>

	<div class='jumbotron'>
		<h1>Rhythm Creator<div id="Timer" style="text-align:center"><small>00:00:00</small></div></h1><br>

		<div>

			<div onClick="javascript:doTap()" style="text-align:center;font-size:26px;font-weight:bold;color:white;text-shadow:#C0C0C0 0 -2px;margin-top:32px;padding-top:32px;background-color:#A0A0A0;display:block;width:20%;height:128px;float:left;">
				Tap
			</div>
			<div style="display:block;width:70%;height:512px;float:right">
				<textarea id="data" style="width:100%;height:100%;font-size:10px"></textarea>
			</div>
			<div style="clear:both"></div>
		</div>

	</div>


	
