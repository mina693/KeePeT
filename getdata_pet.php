<?php  
 
function unistr_to_xnstr($str){ 
    return preg_replace('/\\\u([a-z0-9]{4})/i', "&#x\\1;", $str); 
} 
 
$con=mysqli_connect("localhost","root","123123","db");  

  
if (mysqli_connect_errno($con))  
{  
   echo "Failed to connect to MySQL: " . mysqli_connect_error();  
}  
 
 
mysqli_set_charset($con,"utf8");  
 
 
$res = mysqli_query($con,"select * from pets");  
   
$result = array();  
   
while($row = mysqli_fetch_array($res)){  
  array_push($result,  
    array('id'=>$row[0],'user_email'=>$row[1],'pet_name'=>$row[2],'pet_birth'=>$row[3],'pet_sex'=>$row[4],'pet_species'=>$row[5],'pet_breed'=>$row[6] ,'pet_number'=>$row[7],'pet_image'=>$row[8],'api'=>$row[9],'channel'=>$row[10]
    ));  
}  
   
 
$json = json_encode(array("result"=>$result));  
echo $json;
 
   
mysqli_close($con);  
   
?>