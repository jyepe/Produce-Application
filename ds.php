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
	    die("connection failed");
	}
	else
	{
		echo "database connection successful";
	}


	function createUser()
	{
		global $conn;
		$username = urldecode($_POST['username']) ;
		$password = urldecode($_POST['password']) ;
		$hashed_password = password_hash($password, PASSWORD_DEFAULT);
		var_dump($hashed_password);
	}
	
	function checkLogin ()
	{
		global $conn;
		$username = urldecode($_POST['username']) ;
		$password = urldecode($_POST['password']) ;
		
		$sql = "SELECT * FROM CUSTOMERS WHERE UID = '$username'";

		$hashedPass = '';

		$result = $conn->query($sql);


		if ($result->num_rows > 0)
		{
			while($row = mysqli_fetch_assoc($result)) 
			{
				$hashedPass = $row["PASSWORD"];
				break;
			}

			if (password_verify(($password), ($hashedPass)))
			{
				echo "login successful";
			}
			else
			{
				echo "wrong credentials"; //Username and pass do not match
			}
		}
		else
		{
			echo "user does not exist"; //Username may or may not exist
		}

		$conn->close();

	}



	if ($method == 'login')
	{
		checkLogin();
	}

?>