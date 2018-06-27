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


	function createUser()
	{
		global $conn;
		$compName = urldecode($_POST['compName']) ;
		$username = urldecode($_POST['username']) ;
		$password = urldecode($_POST['password']) ;
		$name = urldecode($_POST['name']) ;
		$address1 = urldecode($_POST['address1']) ;
		$address2 = urldecode($_POST['address2']) ;
		$city = urldecode($_POST['city']) ;
		$county = urldecode($_POST['county']) ;
		$state = urldecode($_POST['state']) ;
		$zip = urldecode($_POST['zip']) ;
		$phone = urldecode($_POST['phone']) ;
		$email = urldecode($_POST['email']) ;
		$hashed_password = password_hash($password, PASSWORD_DEFAULT);
		//todo add columns of the DB

		$sql = "INSERT INTO
					CUSTOMERS
						(COMPANY_NAME
						, UID
						, PASSWORD
						, CONTACT_NAME
						, ADDRESS1
						, ADDRESS2
						, CITY
						, COUNTY
						, STATE
						, ZIP
						, PHONE
						, EMAIL)
				VALUES
					('$compName'
					, '$username'
					, '$hashed_password'
					, '$name'
					, '$address1'
					, '$address2'
					, '$city'
					, '$county'
					, '$state'
					, '$zip'
					, '$phone'
					, '$email')
					";

		if ($conn->query($sql) === TRUE) 
		{
		    echo "user created successfully";
		} 
		else 
		{
		    echo "Error: " . $sql . "<br>" . $conn->error;
		}

		$conn->close();

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
	else if ($method == 'addUser')
	{
		createUser();
	}

?>