<?php
$con=mysqli_connect("localhost","root","123123","db");  

mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$user_email= $_POST['user_email'];
$sitter_image= $_POST['sitter_image'];
$sitter_name= $_POST['sitter_name'];
$sitter_age= $_POST['sitter_age'];
$sitter_sex= $_POST['sitter_sex'];
$sitter_phone_1= $_POST['sitter_phone_1'];
$sitter_phone_2= $_POST['sitter_phone_2'];
$sitter_phone_3= $_POST['sitter_phone_3'];
$sitter_adr= $_POST['sitter_adr'];
$sitter_career_year= $_POST['sitter_career_year'];
$sitter_career_month= $_POST['sitter_career_month'];
$sitter_pet= $_POST['sitter_pet'];
$sitter_price= $_POST['sitter_price'];
$sitter_word= $_POST['sitter_word'];

if (empty($user_email)){
	echo 'You have to put all of them';
}else {
$sql="INSERT INTO `petsitters` (`user_email`, `sitter_image`, `sitter_name`, `sitter_age`, `sitter_sex`, `sitter_phone_1`, `sitter_phone_2`, `sitter_phone_3`, `sitter_adr`, `sitter_career_year`, `sitter_career_month`, `sitter_pet`, `sitter_price`, `sitter_word`) VALUES ('$user_email','$sitter_image','$sitter_name','$sitter_age','$sitter_sex','$sitter_phone_1','$sitter_phone_2','$sitter_phone_3','$sitter_adr','$sitter_career_year','$sitter_career_month','$sitter_pet','$sitter_price','$sitter_word')";
		
$result = mysqli_query($con,$sql);
	if($result){
		echo 'success';
	}else {
		echo "failed";
	}
}
mysqli_close($con);  
?> 
