<?php
$con=mysqli_connect("localhost","root","123123","db");  

mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  

$user_email= $_POST['user_email'];
$pet_id= $_POST['pet_id'];
$pet_name= $_POST['pet_name'];
$pet_birth= $_POST['pet_birth'];
$pet_species= $_POST['pet_species'];
$pet_breed= $_POST['pet_breed'];
$pet_sex= $_POST['pet_sex'];
$pet_number= $_POST['pet_number'];
$pet_image=$_POST['pet_image'];
$api= $_POST['api'];
$channel= $_POST['channel'];

$sql="UPDATE `pets` SET `pet_name`='$pet_name',`pet_birth`='$pet_birth',`pet_species`='$pet_species',`pet_breed`='$pet_breed',`pet_sex`='$pet_sex',`pet_number`='$pet_number',`pet_image`='$pet_image',`api`='$api',`channel`='$channel' WHERE `id`='$pet_id'";
$result = mysqli_query($con,$sql);
   
   if($result){
      echo 'success';
   }else {
      echo "failed";
   }

mysqli_close($con);  
?>
