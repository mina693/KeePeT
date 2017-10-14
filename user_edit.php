<?php
$con=mysqli_connect("localhost","root","123123","db");  

mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$user_name = $_POST['user_name'];
$user_email = $_POST['user_email'];
$user_image= $_POST['user_image'];
$sql="UPDATE `users` SET `user_name`='$user_name',`user_email`='$user_email',`user_image`='$user_image'";


$result = mysqli_query($con,$sql);
   
   if($result){
      echo 'success';
   }else {
      echo "failed";
   }

mysqli_close($con);  
?>
