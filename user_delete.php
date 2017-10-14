<?php
$con=mysqli_connect("localhost","root","123123","db");  

mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$user_email= $_POST['user_email'];

$sql="DELETE FROM `users` WHERE `user_email`='$user_email'";
$sql="DELETE FROM `pets` WHERE `user_email`='$user_email'";
$sql="DELETE FROM `petsitters` WHERE `user_email`='$user_email'";
$sql="DELETE FROM `pet_compass` WHERE `user_email`='$user_email'";
$sql="DELETE FROM `pet_gallery` WHERE `user_email`='$user_email'";
$sql="DELETE FROM `pet_health` WHERE `user_email`='$user_email'";
$sql="DELETE FROM `pet_stagram` WHERE `user_email`='$user_email'";
$sql="DELETE FROM `pet_temp` WHERE `user_email`='$user_email'";
$sql="DELETE FROM `location` WHERE `user_email`='$user_email'";


$result = mysqli_query($con,$sql);
   
   if($result){
      echo "success";
   }else {
      echo "failed";
   }

mysqli_close($con);  
?>