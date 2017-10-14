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
 
 
$res = mysqli_query($con,"select * from pet_stagram");  
   
$result = array();  
   
while($row = mysqli_fetch_array($res)){  
  array_push($result,  
    array('id'=>$row[0],'user_image'=>$row[1],'user_email'=>$row[2],'image'=>$row[3],'text'=>$row[4]  ));  
}  
   
 
$json = json_encode(array("result"=>$result));  
echo $json;
 
   
mysqli_close($con);  
   
?>