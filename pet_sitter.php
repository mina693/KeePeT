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
 
 
$res = mysqli_query($con,"select * from `petsitters`");  
   
$result = array();  
   
while($row = mysqli_fetch_array($res)){  
  array_push($result,  
    array('id'=>$row[0],'user_email'=>$row[1],'sitter_image'=>$row[2],'sitter_name'=>$row[3],'sitter_age'=>$row[4],'sitter_sex'=>$row[5],'sitter_phone_1'=>$row[6],'sitter_phone_2'=>$row[7],'sitter_phone_3'=>$row[8],'sitter_adr'=>$row[9],'sitter_career_year'=>$row[10],'sitter_career_month'=>$row[11],'sitter_pet'=>$row[12],'sitter_price'=>$row[13],'sitter_word'=>$row[14] ));  
}  
 
$json = json_encode(array("result"=>$result));  
echo $json;
 
   
mysqli_close($con);  
   
?>