<?php
$con=mysqli_connect("localhost","root","123123","db");  

mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$password= $_POST['password'];
$user_email= $_POST['user_email'];

$sql="UPDATE `users` SET `password`='$password' WHERE `user_email`='$user_email'";

$result = mysqli_query($con,$sql);
   
   if($result){
      echo 'success';
   }else {
      echo "failed";
   }

mysqli_close($con);  
?>