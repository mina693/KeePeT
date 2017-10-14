<?php  
$con=mysqli_connect("localhost","root","123123","db");  
 
mysqli_set_charset($con,"utf8");
  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
$latitude= $_POST['latitude'];
$longitude= $_POST['longitude'];
$user_email= $_POST['user_email'];
$path_name= $_POST['path_name'];
$date= $_POST['date'];

$result = mysqli_query($con,"insert into locations (latitude,longitude,user_email,path_name,date) values ('$latitude','$longitude','$user_email','$path_name','$date')");         
if($result){
   echo 'success';
   }else {
      echo '상상관 경로가 저장되었습니다.';
      }
      

mysqli_close($con);  
?> 