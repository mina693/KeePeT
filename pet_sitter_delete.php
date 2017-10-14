<?php
$con=mysqli_connect("localhost","root","123123","db");  

mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$id= $_POST['id'];

$sql="DELETE FROM `petsitters` WHERE `id`='$id'";
      
$result = mysqli_query($con,$sql);
   
   if($result){
      echo "success";
   }else {
      echo "failed";
   }

mysqli_close($con);  
?>