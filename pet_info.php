<?php  
$con=mysqli_connect("localhost","root","1346798","testdb");  
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
$user_email= $_POST['user_email'];
$pet_name= $_POST['pet_name'];
$pet_age= $_POST['pet_age'];
$pet_species= $_POST['pet_species'];
$pet_kinds= $_POST['pet_kinds'];
$pet_birthday= $_POST['pet_birthday'];
$pet_sex= $_POST['pet_sex'];


if (empty($pet_name) ||	empty($pet_sex)){
	echo 'You have to put all of them';
}else {
	
		$sql="SELECT * FROM pets WHERE user_email='$user_email'";


			$result = mysqli_query($con,"insert into pets (user_email,pet_name,pet_age,pet_species,pet_kinds,pet_birthday,pet_sex) values ('$user_email','$pet_name','$pet_age','$pet_species','$pet_kinds','$pet_birthday','$pet_sex')");			
			if($result){
				echo 'success';
			}else {
				echo 'Connect is failure';
			}
		
	
}
mysqli_close($con);  
?> 

