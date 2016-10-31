<?php
// create JSON array for response
$response = array();
$content = array();
$data = array();

$id = $_POST["id"];

// include databse credentials
require_once  __DIR__.'/db_config.php';

// connect to MySQL server and select database
$con = mysql_connect(HOST, USER, PASS);
$db = mysql_select_db(NAME);

// load result from vars table
$result= mysql_query("SELECT * FROM vars WHERE id='$id'");

// check for empty result
if (mysql_num_rows($result) > 0) {
	while ($row = mysql_fetch_array($result)) {
		$data["id"] = $row["id"];
		$data["name"] = $row["name"];
		$data["type"] = $row["type"];
		$data["value"] = $row["value"];
		$data["protected"] = $row["protected"];
	}
	
	if($data["protected"] < 3){
		// success
		$content["type"] = "variable";
		$content["data"] = $data;
		
		$response["success"] = 1;
		$response["message"] = "Variable successfully retrieved";
		$response["content"] = $content;
		echo json_encode($response);
	} else {
		// protection failure
		$content["type"] = "variable";
		$content["data"] = array();
		
		$response["success"] = 0;
		$response["message"] = "Attempted to retrieve inaccessible variable";
		$response["content"] = $content;
		echo json_encode($response);
	}
} else {
	// no variables found
	$content["type"] = "variable";
	$content["data"] = array();
	
    $response["success"] = 0;
	$response["message"] = "No variable with requested ID found";
	$response["content"] = $content;
	echo json_encode($response);
	
}
?>
