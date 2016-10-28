<?php
// create JSON array for response
$response = array();
$content = array();
$data = array();

// set parameters
$id = $_POST['id'];
$name = $_POST['name'];
$val = $_POST['value'];
$type = $_POST['type'];

// check for valid parameters
if (isset($id) && isset($name) && isset($val)){
	// include databse credentials
	require_once  __DIR__.'/db_config.php';

	// connect to MySQL server and select database
	$con = mysql_connect(HOST, USER, PASS);
	$db = mysql_select_db(NAME);

	// load result from vars table
	$result= mysql_query("SELECT * FROM vars WHERE id='$id'");
	
	// check for empty result
	if (mysql_num_rows($result) > 0) {
		// fetch protection level
		while ($row = mysql_fetch_array($result)) {
			$protected = intval($row["protected"]);
		}
		
		// if variable can be written to, update
		if($protected < 2){
			// send MySQL query to update variable
			mysql_query("UPDATE vars SET name='$name', type='$type', value='$val' WHERE id='$id'");
			
			// success
			$content["type"] = "variable";
			$content["data"] = array();
			
			$response["success"] = 1;
			$response["message"] = "Variable successfully updated";
			$response["content"] = $content;
			echo json_encode($response);
		} else {
			// protection failure
			$content["type"] = "variable";
			$content["data"] = array();
			
			$response["success"] = 0;
			$response["message"] = "Attempted to update protected variable";
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
	$response["message"] = "Invalid or missing parameters";
	$response["content"] = $content;
	echo json_encode($response);
	
}
?>
