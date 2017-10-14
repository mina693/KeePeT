<?php  
$con=mysqli_connect("localhost","root","123123","db");  
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
$user_name = $_POST['user_name'];
$password = $_POST['password'];
$user_image= $_POST['user_image'];
$user_email = $_POST['user_email'];

if (empty($user_name) || empty($password) || empty($user_email)){
	echo 'You have to put all of them';
}else {
	if(!preg_match("/^[a-zA-Z0-9_\-]+@(([a-zA-Z_\-])+\.)+[a-zA-Z]{2,4}$/",$user_email)){
		echo 'failure';
	}else{
		$sql="SELECT * FROM users WHERE user_email='$user_email'";

		$check = mysqli_fetch_array(mysqli_query($con,$sql));
		
		if(isset($check)){
			echo 'email is already exist';
		}else {
			$result = mysqli_query($con,"insert into users (user_name,password,user_email,user_image) values ('$user_name','$password','$user_email','$user_image')");
			
			if($result){
				echo 'success';
			}else {
				echo 'Connect is failure';
			}
		}
	}
}
mysqli_close($con);  
?> 
 
