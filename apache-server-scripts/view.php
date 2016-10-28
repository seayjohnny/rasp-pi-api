<?php
// create JSON array for response
$response = array();

// include databse credentials
require_once  __DIR__.'/db_config.php';

// connect to MySQL server and select database
$con = mysql_connect(HOST, USER, PASS);
$db = mysql_select_db(NAME);

// get all variables from vars table
$result= mysql_query("SELECT *FROM vars");

// check for empty result
if (mysql_num_rows($result) > 0) {
	// looping through all results
	$response["vars"] = array();

	while ($row = mysql_fetch_array($result)) {
		// temp user array
		$variable = array(); // var is predefined keyword, thus the use of variable
		$variable["id"] = $row["id"];
		$variable["name"] = $row["name"];
		$variable["type"] = $row["type"];
		$variable["value"] = $row["value"];

		// push variable into final response array
		array_push($response["vars"], $variable);
	}
	// success
        $response["success"] = 1;
	$response["message"] = "Variables successfully retrieved.";
	echo json_encode($response);
} else {
	// no variables found
	$response["success"] = 0;
	$response["message"] = "No variables have been stored.";
	echo json_encode($response);
}
?>
