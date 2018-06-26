<?php
	$var = urldecode($_POST['name']) ;
	
	print " ==== POST DATA =====
       Name  : $var";



    //Info to connect to DB
	$servername = "localhost";
	$username = "jyepe";
	$password = "9373yepe";
	$dbname = "mydb";

	// Create connection
	$conn = new mysqli($servername, $username, $password, $dbname);

	// Check connection
	if ($conn->connect_error) 
	{
	    die("Connection failed: " . $conn->connect_error);
	}
	

	$sql = "INSERT INTO FAVORITES (CUSTOMER, ITEM) 
						VALUES (1, 2)";


	if ($conn->query($sql) === TRUE) {
    echo "New record created successfully";
} else {
    echo "Error: " . $sql . "<br>" . $conn->error;
}
	$conn->close();
?>