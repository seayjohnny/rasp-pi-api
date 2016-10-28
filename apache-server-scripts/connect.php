<?php
class DB_CONNECT {
	// constructor
	function __construct() {
		// connecting to db
		$this->connect();
	}

	// destructor
	function __destruct() {
		// closing db conection
		$this->close();
	}

	function connect() {
		// import db connection info
		require_once __DIR__ . '/db_config.php';

		// connecting to mysql db
		$con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD);

		// selecting db
		$db = mysql_select_db(DB_DATABASE);

		// returning connection cursor
		return $con;
	}

	function close() {
		// closing db connection
		mysql_close();
	}

}

?>
