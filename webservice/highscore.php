<?php


	ini_set('display_errors',true);
	error_reporting(E_ALL|E_STRICT);
	
	require_once("config.php");
	require_once("db.php");
	
	///////////////////////////////////////////////////////////////////////
	// Response methods
	///////////////////////////////////////////////////////////////////////
	
	function okResponse($desc="", $code=200) {
		echo json_encode(array("code" => $code, "status" => "OK", "message" => $desc));
		exit();
	}
	
	function errorResponse($desc="", $code=501) {
		echo json_encode(array("code" => $code, "status" => "FAIL", "message" => $desc));
		exit();
	}
	
		
	
	if ((array_key_exists("secure_code", $_GET)) && (strcmp($_GET['secure_code'], SECURE_CODE) == 0)) {
		$db = new Database("score.sqlite");
		if (array_key_exists("action", $_GET)) {
			if ((strcmp($_GET['action'], "setScore") == 0) 
				&& (array_key_exists("username", $_GET)) 
				&& (array_key_exists("score", $_GET))
				&& (array_key_exists("level", $_GET))
				) {
				$level = intval($_GET['level']);
				if ($level < 0) $level = 0;
				/* Add score */
				$db->setHighscore($_GET["username"], $_GET["score"], $_GET["level"]);
			} else if (strcmp($_GET['action'], "listScore") == 0) {
				$entries = $db->getAllScore();
				if ($entries != null) {
					echo json_encode($entries);
					exit();
				}
			}
		}
	}


