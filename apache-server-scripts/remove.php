<?php
// create JSON array for response
$response = array();
$content = array();
$data = array();

// set parameters
$id = $_POST['id'];

// check for parameters
if (isset($id)) {

	// include databse credentials
	require_once __DIR__.'/db_config.php';

	// connect to MySQL server and select database
	$con = mysql_connect(HOST, USER, PASS);
	$db = mysql_select_db(NAME);
	
	// load result from vars table
	$result= mysql_query("SELECT * FROM vars WHERE id='$id'");
	
	// check for empty result
	if (mysql_num_rows($result) > 0) {
		// fetch protection level
		while ($row = mysql_fetch_array($result)) {
			$protected = $row["protected"];
		}
		
		// if variable can be removed, remove
		if($protected < 1){
			// send MySQL query to remove variable
			mysql_query("DELETE FROM vars WHERE id='$id'");
			
			// success
			$content["type"] = "variable";
			$content["data"] = $data;
			
			$response["success"] = 1;
			$response["message"] = "Variable successfully removed";
			$response["content"] = $content;
			echo json_encode($response);
		} else {
			// protection failure
			$content["type"] = "variable";
			$content["data"] = array();
			
			$response["success"] = 0;
			$response["message"] = "Attempted to remove protected variable";
			$response["content"] = $content;
			echo json_encode($response);
		}
	} else {
		// can't find variable
		$content["type"] = "variable";
		$content["data"] = array();
		
		$response["success"] = 0;
		$response["message"] = "Variable with request ID not found";
		$response["content"] = $content;
		echo json_encode($response);
	}
} else {
	// invalid or missing parameters
	$content["type"] = "variable";
	$content["data"] = array();
	
	$response["success"] = 0;
	$response["message"] = "Variable with request ID not found";
	$response["content"] = $content;
	echo json_encode($response);
}
?>

