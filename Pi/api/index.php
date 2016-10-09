<?php
require_once '/var/www/html/api/libs/FirebasePhpClient/firebaseLib.php';
require '/var/www/html/api/libs/Slim/Slim.php';
require '/home/pi/php-gpio/vendor/autoload.php';

//Set up slim framework
\Slim\Slim::registerAutoloader();
$app = new \Slim\Slim();


//Set up firebase lib reference
const DEFAULT_URL = 'https://dashmotic.firebaseio.com/Controls';
const DEFAULT_TOKEN = '**************USE-YOUR-SECRET-TOKEN**************';
const DEFAULT_PATH_LIGHT = '';
const DEFAULT_PATH_BLINDS = '';


//ROUTES#############################################################
$app->get('/', function() use($app) {

echo "Welcome to Cirillo's Home Domotic System API";

}); 


// Light Route
$app->post('/ToggleLight/:name/:pin_number', function($name, $pin_number) use($app) {
	toggleChild("/Lights", $name, $pin_number);
});

// Blinds Route
$app->post('/ToggleBlind/:name/:pin_number', function($name, $pin_number) use($app) {
	toggleChild("/Blinds", $name, $pin_number);
});



/*
	Function for toggling childs
*/
function toggleChild($defaultPath, $child_name, $pin_number){

	shell_exec("/usr/local/bin/gpio -1 mode ".$pin_number." out");

	$firebase = new fireBase(DEFAULT_URL, DEFAULT_TOKEN);
	$child_path_value = $defaultPath.'/'.$child_name."/value";
	$value = $firebase->get($child_path_value);

	if($value == 0){

		shell_exec("/usr/local/bin/gpio -1 write ".$pin_number." 1");
		$firebase->set($child_path_value, 1);

		printResult("Switch OFF name[".$child_name."] and pin_number[".$pin_number."]");
	}else{

		shell_exec("/usr/local/bin/gpio -1 write ".$pin_number." 0");
		$firebase->set($child_path_value, 0);

		printResult("Switch ON name[".$child_name."] and pin_number[".$pin_number."]");
	}
}

function printResult($result){
	
	echo json_encode($result);
}

$app->run();



?>






