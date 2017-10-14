<?php 
 
 $con=mysqli_connect("localhost","root","123123","db");  
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 //Getting values 
 $user_name = $_POST['user_email'];
 $password = $_POST['password'];
 
 //Creating sql query
 $sql = "SELECT user_image FROM users WHERE user_email='$user_name' AND password='$password'";
 //executing query
 $result = mysqli_query($con,$sql);
 //fetching result
 $check = mysqli_fetch_array($result);

 //if we got some result 
 if(isset($check)){
	echo $check[0];
	
 }else{
	//displaying failure
	echo "failure";
 }
 mysqli_close($con);
 }
 
 ?>
