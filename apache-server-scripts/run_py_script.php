<?php
// create JSON array for response
$response = array();

//$script_name = $_POST['name'];
//$script_params = json_decode($_POST['parmas'], true);

// check if file exists
$filename = "/home/pi/py_scripts/test.py";
if(file_exists($filename)){
	// run python script and store raw output
	$r_output = array();
	exec("python {$filename}", $r_output);

	// format output
	$f_output = array();
	$f_output["message"] = $r_output[0];
	$f_output["data"] = $r_output[1];
	
	$content["type"] = "py_script";
	$content["data"] = $f_output;
	
	$response["success"] = 1;
	$response["message"] = "Python Script successfully ran.";
	$response["content"] = $content;
	echo json_encode($response);

} else {
	$content["type"] = "py_script";
	$content["data"] = array();
	
	$response["success"] = 0;
	$response["message"] = "Could not find {$filename}";
	$response["content"] = $content;
	echo json_encode($response);
}
?>