<?php

	//Info to connect to DB
	$servername = "localhost";
	$dbusername = "jyepe";
	$dbpassword = "9373yepe";
	//$dbusername = "root";
	//$dbpassword = "password";
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

	function addUser()
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
		$sqlCheck = "SELECT * FROM CUSTOMERS WHERE UID = '$username'";
		$hashedPass = '';
		$result = $conn->query($sqlCheck);

		if ($result->num_rows > 0)
		{
			echo "user already exists";
		}
		else
		{
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
	}
	
	function login()
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

	function getItems()
	{
		global $conn;
		$sql = "";

		if (urldecode($_POST['type']) == 'getOnHand')
		{
			$sql = "SELECT NAME, IFNULL( ON_HAND, 0) AS ON_HAND
					FROM INVENTORY";
		}
		else
		{
			$sql = "SELECT * FROM INVENTORY";
		}

		
		$result = $conn->query($sql);

		if ($result->num_rows > 0) 
		{
			echo "start";
	    	// output data of each row
	    	while($row = $result->fetch_assoc()) 
	    	{
        		echo $row["NAME"]. ",";

        		if (urldecode($_POST['type']) == 'getOnHand')
				{
					echo $row["ON_HAND"]. "\n";
				}
    		}

    		echo "end";
		} 
		else 
		{
		    echo "0 results";
		}
		

		$conn->close();
	}

	function getUser()
	{
		global $conn;
		$userName = urldecode($_POST['uid']) ;
		$sql = "SELECT CONTACT_NAME, ID FROM CUSTOMERS WHERE UID = '$userName'";
		$result = $conn->query($sql);

		if ($result->num_rows > 0) 
		{
	    	// output data of each row
	    	while($row = $result->fetch_assoc()) 
	    	{
        		echo "start:CONTACT_NAME" . $row["CONTACT_NAME"] . "end:CONTACT_NAME";
        		echo "start:ID" . $row["ID"] . "end:ID";
    		}
		}
		else
		{
		    echo "0 results";
		}
		$conn->close();
	}

	function insertOrder()
	{
		global $conn;
		
		$count = urldecode($_POST['count']);
		$company = urldecode($_POST['company']);
		$flag = 1;
		
		$sql = "
		CALL NEW_ORDER(" . $company . ", @ORDER_NUM);
		";

		if ($conn->query($sql) === TRUE) 
		{
			//echo "New record created successfully";
		} 
		else 
		{
			$flag = 0;
			echo "Error: " . $sql . "<br>" . $conn->error;
		}

		for ($i = 1; $i <= $count; $i++)
		{
			$item = urldecode($_POST['item'. $i]);
			$qty = urldecode($_POST['qty'. $i]);
			$sql = "
			INSERT INTO ORDERS (ID, ITEM, QUANTITY, PRICE)
			VALUES (@ORDER_NUM, (SELECT ID FROM INVENTORY WHERE NAME = '" . $item . "'), " . $qty . ", IFNULL((SELECT PRICE FROM INVENTORY WHERE NAME = '" . $item . "'), 0.00));
			";

			if ($conn->query($sql) === TRUE) 
			{
				//echo "New record created successfully";
			} 
			else 
			{
				$flag = 0;
				//echo "Error: " . $sql . "<br>" . $conn->error;
			}
				
		}

		if ($flag == 1)
		{
			echo "success";
		}
		else
		{
			echo "error";
		}

		$conn->close();
	}

	function getMasterOrders()
	{
		global $conn;

		$sql = "SELECT CONTACT_NAME, master_orders.ID, sum(PRICE) as TOTAL_PRICE, COMPANY_NAME
				FROM master_orders JOIN customers ON master_orders.CUSTOMER = customers.ID
				                   JOIN orders ON master_orders.ID = orders.ID
                   
				GROUP BY ID";

		$result = $conn->query($sql);

		if ($result->num_rows > 0) 
		{
	    	// output data of each row
	    	while($row = $result->fetch_assoc()) 
	    	{
        		echo $row["CONTACT_NAME"] . ",";
        		echo $row["ID"] . ",";
        		echo $row["TOTAL_PRICE"] . ",";
        		echo $row["COMPANY_NAME"] . "\n";
    		}
		}
		else
		{
		    echo "0 results";
		}

		$conn->close();
	}

	function getOrders()
	{
		global $conn;

		$order_id = urldecode($_POST['orderID']);

		$sql = "SET @ORDER_ID = $order_id;";

		if ($conn->query($sql) === TRUE) 
		{
			//echo "New record created successfully";
		} 
		else 
		{
			echo "Error: " . $sql . "<br>" . $conn->error;
		}

		$sql = "SELECT CONTACT_NAME, orders.PRICE, QUANTITY, NAME
				FROM master_orders JOIN customers ON master_orders.CUSTOMER = customers.ID
                   				   JOIN orders ON orders.ID = master_orders.ID
                                   JOIN inventory ON orders.ITEM = inventory.ID
				WHERE master_orders.CUSTOMER = customers.ID AND orders.ID = @ORDER_ID";

		$result = $conn->query($sql);

		if ($result->num_rows > 0) 
		{
	    	// output data of each row
	    	while($row = $result->fetch_assoc()) 
	    	{
        		echo $row["NAME"] . ",";
        		echo $row["QUANTITY"] . ",";
        		echo $row["PRICE"] . "\n";
    		}
		}
		else
		{
		    echo "0 results";
		}

		$conn->close();

	}

	function updateInventory()
	{
		global $conn;
		$flag = 1;
		
		$count = urldecode($_POST['count']) ;
		for ($i=1; $i <= $count; $i++) 
		{ 
			$quantity = urldecode($_POST['qty' . $i]) ;
			$id = urldecode($_POST['itemID' . $i]) ;
			$sql = "SET @CURRENT_QTY = (SELECT ON_HAND FROM INVENTORY WHERE ID = $id);";
			if ($conn->query($sql) === TRUE) 
			{
	    		//echo "success";
			} 
			else 
			{
				$flag = 0;
	    		//echo "Error updating record: " . $conn->error;
			}
			$sql = "UPDATE INVENTORY
				SET ON_HAND = $quantity + @CURRENT_QTY
				WHERE ID = $id;";
			if ($conn->query($sql) === TRUE) 
			{
	    		//echo "success";
			} 
			else 
			{
				$flag = 0;
	    		//echo "Error updating record: " . $conn->error;
			}
		}
		if ($flag == 1)
		{
			echo "success";
		}
		else
		{
			echo "error";
		}
		$conn->close();
	}

	function getUserOrders()
	{
		global $conn;

		$customer = urldecode($_POST['ID']);

		$sql = "SET @CUSTOMER = $customer;";

		if ($conn->query($sql) === TRUE) 
		{
			//echo "New record created successfully";
		} 
		else 
		{
			echo "Error: " . $sql . "<br>" . $conn->error;
		}

		$sql = "
		SELECT 
			CONCAT('ORDER #: ',  ID, '   ', DATE_FORMAT(TIME, \"%M %D, %Y\")) AS INFO
		FROM
			MASTER_ORDERS
		WHERE
			CUSTOMER = $customer;";

		$result = $conn->query($sql);

		if ($result->num_rows > 0) 
		{
			echo "start:INFO"; 
	    	// output data of each row
	    	while($row = $result->fetch_assoc()) 
	    	{
				echo $row["INFO"] . "-";
    		}
			echo "end:INFO";
		}
		else
		{
		    echo "0 results";
		}

		$conn->close();
	}

	function getSingleOrder()
	{
		global $conn;

		$order = urldecode($_POST['ID']);

		$sql = "
		SET @ORDER_ID = $order;
			";

		if ($conn->query($sql) === FALSE) {
			echo "Error: " . $sql . "<br>" . $conn->error;
		}

		$sql = "
		SELECT
			CONCAT(
			INVENTORY.NAME, '  (',
			ORDERS.QUANTITY, ' x $',
			ORDERS.PRICE, ')   =  $',
			(ORDERS.QUANTITY * ORDERS.PRICE)
			) AS ITEM
		FROM
			ORDERS JOIN INVENTORY ON ORDERS.ITEM = INVENTORY.ID
		WHERE
			ORDERS.ID = @ORDER_ID;";

		$result = $conn->query($sql);

		if ($result->num_rows > 0) 
		{
			echo "start:ITEM"; 
	    	// output data of each row
	    	while($row = $result->fetch_assoc()) 
	    	{
				echo $row["ITEM"] . "-";
    		}
			echo "end:ITEM";
		}
		else
		{
		    echo "0 results";
		}

		$sql = "
		SELECT
			SUM(QUANTITY * PRICE) AS TOTAL
		FROM
			ORDERS
		WHERE
			ORDERS.ID = @ORDER_ID;";
	
		$result = $conn->query($sql);

		if ($result->num_rows > 0) 
		{
			echo "start:TOTAL"; 
	    	// output data of each row
	    	while($row = $result->fetch_assoc()) 
	    	{
				echo $row["TOTAL"];
    		}
			echo "end:TOTAL";
		}
		else
		{
		    echo "0 results";
		}

	}

	function sendNotification()
	{
		
		#API access key from Google API's Console
   		define( 'API_ACCESS_KEY', 'AAAAIBjg0NA:APA91bGQ3U4XKME5PB2vKtGdFdnIOg-JRejdU4R5le_-yRzhHaAJ7ppWYPap4iEBY2ksGqGz88EZvivYgwMcwgeSjUkv3eVYORh42mQa4A-0gXY4ijb8-9b8GAji_FRibtNv73m2TcgEseIOgWCagfxS8tKaTeeBUQ' );

    	$registrationIds = urldecode($_POST['ID']);

		#prep the bundle
     	//$msg = array
        //(
    	//	'body' 	=> 'Body  Of Notification',
		//	'title'	=> 'Title Of Notification'
        //);

	    $fields = array
		(
			'to'		=> $registrationIds
			//'notification'	=> $msg
		);
	
	
		$headers = array
		(
			'Authorization: key=' . API_ACCESS_KEY,
			'Content-Type: application/json'
		);

		#Send Reponse To FireBase Server	
		$ch = curl_init();
		curl_setopt( $ch,CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send' );
		curl_setopt( $ch,CURLOPT_POST, true );
		curl_setopt( $ch,CURLOPT_HTTPHEADER, $headers );
		curl_setopt( $ch,CURLOPT_RETURNTRANSFER, true );
		curl_setopt( $ch,CURLOPT_SSL_VERIFYPEER, false );
		curl_setopt( $ch,CURLOPT_POSTFIELDS, json_encode( $fields ) );
		$result = curl_exec($ch );
		curl_close( $ch );

		#Echo Result Of FireBase Server
		echo $result;

	}

	if ($method == 'login')
	{
		login();
	}
	else if ($method == 'addUser')
	{
		addUser();
	}
	else if ($method == 'getItems')
	{
		getItems();
	}
	else if ($method == 'getUser')
	{
		getUser();
	}
	else if ($method == 'newOrder')
	{
		insertOrder();
	}
	else if ($method == 'getMasterOrders')
	{
		getMasterOrders();
	}
	else if ($method == 'getOrders')
	{
		getOrders();
	}
	else if ($method == 'updateInventory')
	{
		updateInventory();
	}
	else if ($method == 'getUserOrders')
	{
		getUserOrders();
	}
	else if ($method == 'getSingleOrder')
	{
		getSingleOrder();
	}
	else if ($method == 'sendNotification')
	{
		sendNotification();
	}

?>