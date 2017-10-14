<?php 
 
 $con=mysqli_connect("localhost","root","123123","db");  
 
 if($_SERVER['REQUEST_METHOD']=='POST'){
 //Getting values 
 $LED = $_POST['LED'];

 //Creating sql query
 $sql = "SELECT * FROM arduino_module WHERE LED='$LED'";
 
 //importing dbConnect.php script 
 //require_once('../includes/dbConnect.php');
 
 //executing query
 $result = mysqli_query($con,$sql);
 
 //fetching result
 $check = mysqli_fetch_array($result);
 
 //if we got some result 
 if(isset($check)){
 //displaying success 
 echo "success";
 }else{
 //displaying failure
 echo "failure";
 }
 mysqli_close($con);
 }
 
 ?>