<?php  
$con=mysqli_connect("localhost","root","123123","db");  
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
$user_email= $_POST['user_email'];
$pet_name= $_POST['pet_name'];
$pet_birth= $_POST['pet_birth'];
$pet_sex= $_POST['pet_sex'];
$pet_species= $_POST['pet_species'];
$pet_breed= $_POST['pet_breed'];
$pet_number= $_POST['pet_number'];
$pet_image= $_POST['pet_image'];
$api= $_POST['api'];
$channel= $_POST['channel'];

if (empty($pet_name) ||	empty($pet_sex)){
	
	echo 'You have to put all of them';
	
}else {

	$result = mysqli_query($con,"insert into pets (user_email,pet_name,pet_birth,pet_species,pet_breed,pet_sex,pet_number,pet_image,api,channel) values ('$user_email','$pet_name','$pet_birth','$pet_species','$pet_breed','$pet_sex','$pet_number','$pet_image','$api','$channel')");			
	if($result){
		echo 'success';
	}else {
		echo 'Connect is failure';
	}	
	
}
mysqli_close($con);  
?> 

