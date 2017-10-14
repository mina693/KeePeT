<?php
$con=mysqli_connect("localhost","root","123123","db");  

mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$user_image= $_POST['user_image'];
$user_email= $_POST['user_email'];
$image= $_POST['image'];
$content= $_POST['content'];

if (empty($image)){
	echo 'You have to put all of them';
}else {
$sql="INSERT INTO `pet_stagram` (user_image,user_email,image,content) values ('$user_image','$user_email','$image','$content')";
		
$result = mysqli_query($con,$sql);
	
	if($result){
		echo 'success';
	}else {
		echo "failed";
	}
}
mysqli_close($con);  
?> 
