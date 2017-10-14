<?php
$con=mysqli_connect("localhost","root","123123","db");  

mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$image= $_POST['image'];
$content= $_POST['content'];
$id= $_POST['id'];

$sql="UPDATE `pet_stagram` SET `image`='$image',`content`='$content' WHERE `id`='$id'";

$result = mysqli_query($con,$sql);
   
   if($result){
      echo 'success';
   }else {
      echo "failed";
   }

mysqli_close($con);  
?>
