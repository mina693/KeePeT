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
 
 
$res = mysqli_query($con,"select * from locations");  
   
$result = array();  


   
while($row = mysqli_fetch_array($res)){  
  array_push($result,  
    array('id'=>$row[0],'latitude'=>$row[1],'longitude'=>$row[2],'date'=>$row[3],'user_email'=>$row[4],'path_name'=>$row[5]
    ));  
}  
   
 
$json = json_encode(array("result"=>$result));  
echo $json;
 
   
mysqli_close($con);  
   
?>
<html>
   <body>
   
      <form action = "<?php $_PHP_SELF ?>" method = "POST">
         latitude: <name = "user_email" />
       longititude: <name = "path_name" />

         
      </form>
   
   </body>
</html>