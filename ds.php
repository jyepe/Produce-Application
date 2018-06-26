<?php

	//Info to connect to DB
	$servername = "localhost";
	$dbusername = "jyepe";
	$dbpassword = "9373yepe";
	$dbname = "mydb";

	//what method to execute
	$method = urldecode($_POST['method']) ;

	

	// Create connection
	$conn = new mysqli($servername, $dbusername, $dbpassword, $dbname);

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	}
	else
	{
		echo "success";
	}

	

	
	function checkLogin ()
	{
		global $conn;
		$username = urldecode($_POST['username']) ;
		$password = urldecode($_POST['password']) ;
		

		$sql = "SELECT * FROM CUSTOMERS WHERE PASSWORD = $password AND UID = $username";
		$result = $conn->query($sql);

		if ($result->num_rows > 0)
		{
			echo "true";
		}
		else
		{
			echo "false";
		}

		$conn->close();

	}



	if ($method == 'login')
	{
		checkLogin();
	}

?>