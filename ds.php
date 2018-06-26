<?php

	//Info to connect to DB
	$servername = "localhost";
	$dbusername = "root";
	$dbpassword = "password";
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
		//$sql = "INSERT INTO CUSTOMERS SELECT 1, 'hello', '$username', '$hashed_password', 'Adrian Serrano', '16423 Ruby Lake', '', 'Weston', 'Broward', 'FL', '33331', '954-560-3284', 'adrianserrano15@gmail.com'";
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
			while($row = mysqli_fetch_assoc($result)) {
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