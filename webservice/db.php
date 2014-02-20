<?php

	class Database {
	
		///////////////////////////////////////////////////////////////////////
		// Private members
		///////////////////////////////////////////////////////////////////////
		
		private $db;
		private $isLoaded;
		private $dbFile;
		
		
		///////////////////////////////////////////////////////////////////////
		// Public constructors
		///////////////////////////////////////////////////////////////////////
		
		public function __construct($file) {
			$this->dbFile = $file;
			$this->connectToDatabase();
			/* Check if need to reinstall */
			if ($this->getDBVersion() < DATABASE_VERSION) {
				$this->reinstall();
			}
		}
		public function connectToDatabase() {
			/* Connect to Database */
			try {
				$this->db = new PDO("sqlite:" . $this->dbFile);
				$this->isLoaded = true;
				/* Install db */
				$this->installIfRequired();
			} catch (PDOException $e) {
				$this->errorResponse("Connection failed: " . $e->getMessage());
			}
		}
		
		public function reinstall() {
			if ($this->isLoaded) {
				$this->db = null;
				$this->isLoaded = false;
			}
			if (file_exists($this->dbFile)) {
				@chmod($this->dbFile, 0777);
				unlink($this->dbFile);
			}
			$this->connectToDatabase();
			if ($this->isLoaded) {
				$sql = "INSERT INTO `version` (`version`) VALUES (" . DATABASE_VERSION . ")";
				if ($this->db->exec($sql) === false) {
					$this->errorResponse("Insert failed: " . print_r($this->db->errorInfo(), true));
				}	
			}
			return $this->installIfRequired();
		}
		public function installIfRequired() {
			if ($this->isLoaded) {
				
				$sql = "CREATE TABLE IF NOT EXISTS `score` (
					id INTEGER PRIMARY KEY, 
					username TEXT NOT NULL, 
					score INTEGER NOT NULL, 
					level INTEGER NOT NULL,
					score_timestamp DOUBLE
				)";
				if ($this->db->exec($sql) === false) {
					$this->errorResponse("Create failed: " . print_r($this->db->errorInfo(), true));
				}	
				
				$sql = "CREATE TABLE IF NOT EXISTS `version` (version INTEGER)";
				if ($this->db->exec($sql) === false) {
					$this->errorResponse("Create failed: " . print_r($this->db->errorInfo(), true));
				}	
				
			}
		}
		
		public function getDBVersion() {
			if ($this->isLoaded) {
				/* Execute */
				$s = $this->db->prepare("SELECT `version` FROM `version`");
				if ($s === false) {
					$this->logEcho($sql);
					$this->logEcho($this->db->errorInfo());
					return 0;
				}				
				if (!($s->execute())) return 0;
				/* Done: return result */
				$result = $s->fetchAll(PDO::FETCH_ASSOC);
				if ((is_array($result)) && (count($result) > 0) && (array_key_exists("version", $result[0]))) {
					return intval($result[0]["version"]);
				}
			}
			return -1;
		}
		
		///////////////////////////////////////////////////////////////////////
		// Response methods
		///////////////////////////////////////////////////////////////////////
		
		public function okResponse($desc="", $code=200) {
			echo json_encode(array("code" => $code, "status" => "OK", "message" => $desc));
			exit();
		}
		
		public function errorResponse($desc="", $code=501) {
			echo json_encode(array("code" => $code, "status" => "FAIL", "message" => $desc));
			exit();
		}
		
		
		///////////////////////////////////////////////////////////////////////
		// Methods
		///////////////////////////////////////////////////////////////////////
		
		/**
		 * Return a JSON response if OK with
		 */
		public function setHighscore($username, $score, $level) {
			if ($this->isLoaded) {
				/* Get highscore of user */
				$currentScore = $this->getScore($username, $level);
				$sql = "";
				$params = null;
				if (isset($currentScore)) {
					if (intval($currentScore["score"]) >= $score) {
						$this->okResponse("UNDER");
						return true;
					}
					$sql = "UPDATE `score` 
							SET 
								`score`=?,
								`score_timestamp`=?
							WHERE `username`=? AND `level`=?";
					$params = array($score, time(), $username, $level);
				} else {
					$sql = "INSERT INTO `score` (`username`, `score`, `level`, `score_timestamp`) VALUES (?,?,?,?)";
					$params = array($username, $score, $level, time());
				}
				/* Prepare SQL */
				$s = $this->db->prepare($sql);
				if ($s === false) {
					$this->logEcho($sql);
					$this->logEcho($this->db->errorInfo());
					exit();
					return null;
				}
				/* Execute & response */
				if ($s->execute($params)) {
					$this->okResponse(((isset($currentScore)) ? "BEAT" : "NEW"));
				} else {
					exit();
					//$this->errorResponse("Not inserted> " . print_r($this->db->errorInfo(), true));
				}
			}
		}
		public function getScore($username, $level) {
			if ($this->isLoaded) {
				/* Seek user */
				$sql = "SELECT * FROM `score` WHERE `username`=? AND `level`=?";
				$s = $this->db->prepare($sql);
				if ($s === false) {
					$this->logEcho($sql);
					$this->logEcho($this->db->errorInfo());
					return null;
				}
				/* Execute & response */
				if ($s->execute(array($username, $level))) {
					$result = ($s->fetchAll(PDO::FETCH_ASSOC));
					if (count($result) > 0) {
						return $result[0];
					}
				}
			}
			return null;
		}
		public function getAllScore($level = -1, $start=0, $count=-1, $sortByColumn="", $sortOrder=SORT_ASC) {
			if ($this->isLoaded) {
				/* Prepare SQL */
				$sql = "SELECT * FROM `score`";
				if ($level >= 0) {
					$sql .= " WHERE `level`=" . $level;
				}
				/* Add order */
				if (strlen($sortByColumn) > 0) {
					/* Have order */
					$sql .= " ORDER BY " . $sortByColumn;
				}
				/* Add Limit */
				if ($start < 0) $start = 0;
				if ($count > 0) {
					/* Use Limit */
					$sql .= " LIMIT " . $start . ", " . $count;
				} else if ($start > 0) {
					/* Use Limit */
					$sql .= " LIMIT " . $start . ", 2147483647";
				}
				/* Execute */
				$s = $this->db->prepare($sql);
				if ($s === false) {
					$this->logEcho($sql);
					$this->logEcho($this->db->errorInfo());
					return null;
				}				
				if (!($s->execute())) return null;
				/* Done: return result */
				return ($s->fetchAll(PDO::FETCH_ASSOC));
			} else {
				return null;
			}
		}
		
		
		
		
		///////////////////////////////////////////////////////////////////////
		// Log
		///////////////////////////////////////////////////////////////////////
		
		/**
		 * Write in log file "out.log" messages
		 * @param string $msg Message to log
		 */
		public function logEcho($msg)
		{
			if ((defined("DATABASE_LOG_ENABLED")) && (DATABASE_LOG_ENABLED)) {
				$date = date('d.m.Y h:i:s');
				@chmod('out.log', 0777);
				error_log($date . " - Database>" . print_r($msg, true) . "\n", 3, 'out.log');
			}
		} 
		
		
	}
	
	