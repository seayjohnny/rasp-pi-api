<?php
// create JSON array for response
$response = array();
$content = array();
$data = array();
// set parameters
$name = $_POST['name'];
$val = $_POST['value'];
$type = $_POST['type'];

// check for valid parameters
if (isset($name) && isset($val) && isset($type)){
	// include databse credentials
	
	require_once  __DIR__.'/db_config.php';

	// connect to MySQL server and select database
	$con = mysql_connect(HOST, USER, PASS);
	$db = mysql_select_db(NAME);

	// get all IDs from vars table
	$result = mysql_query("SELECT id FROM vars");

	// array (1, 2, 3, ..., 48, 49, 50)
	$numlist = range(100, 500);
	//empty array to store collected IDs
	$idlist = array();

	// put IDs into an array
	while ($row = mysql_fetch_array($result)) {
			//push ID into idlist array
			array_push($idlist, $row["id"]);
		}
	
	//get free ID
	foreach ($numlist as &$num){
		if( in_array($num, $idlist)){
		} else {
			$id = $num;
			break;
		}
	}	

	// send a MySQL query to insert variable into table
	mysql_query("INSERT INTO vars(id, name, type, value)
		     VALUES('$id', '$name', '$type', '$val')");
	
	$data["id"] = $id;
	
	$content["type"] = "variable";
	$content["data"] = $data;
	
	$response["success"] = 1;
	$response["message"] = "Successfully stored variable.";
	$response["content"] = $content;
	echo json_encode($response);
}
?>
