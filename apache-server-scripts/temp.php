<?php

$numlist = range(1, 50);
//empty array to store collected IDs
$idlist = array();

require_once  __DIR__.'/db_config.php';

	// connect to MySQL server and select database
$con = mysql_connect(HOST, USER, PASS);
$db = mysql_select_db(NAME);

// send a MySQL query to retrieve list of IDs
// get all IDs from vars table
$result = mysql_query("SELECT id FROM vars");


while ($row = mysql_fetch_array($result)) {
		//push ID into idlist array
		array_push($idlist, $row["id"]);
	}

foreach ($numlist as &$num){
	if( in_array($num, $idlist)){
	} else {
		$id = $num;
		break;
	}
}	

echo json_encode($idlist)."<br>";
echo $id."<br>";
?>
