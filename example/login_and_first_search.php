<?php 

/**
 * Include the respective files
 */
require_once('lib/connector.php');
require_once('lib/tradeor.php');
require_once('lib/eahashor.php');
require_once('lib/searchor.php');
require_once('lib/functionor.php');


 //enter your username, password, secret answer in the variables below
 //should look something like
 //$user = "ea@ea.com";
 //$password = "password";
 //$secret = "secretquestion";
 $user = "";
 $password = "";
 $secret = "";
 
//we call the eaEncode function from the EAHashor file
 $e = new EAHashor();
 $hash = $e->eaEncode($secret);
 //display the hash on the screen
 echo "Your Hash: " . $hash . "<br />";
 
 $c = new Connector($user, $password, $hash);
 $info = $c->connect();
 
 //display the connection info on the screen
 echo "<br />Your Connection Details:<br />";
 echo $info['EASW_KEY'] . "<br />";
 echo $info['EASF_SESS'] . "<br />";
 echo $info['PHISHKEY'] . "<br />";
 echo $info['XSID'] . "<br />";
 
 //we call the playersearch function from the Searchor file
 $s = new Searchor($info['EASW_KEY'], $info['EASF_SESS'], $info['PHISHKEY'], $info['XSID']);
 //we pass $s->playersearch a lot of variables
 //1. what number to start searching at
 //2. how many results do I want to get back (max 15)
 //3. what level is the player
 //4. what formation am I looking for
 //5. what position do they play
 //6. what nationality are they
 //7. what league do they play in
 //8. what team do they play for
 //9. minimum bid (this is not your offer)
 //10. maximum bid (this is not your offer)
 //11. minimum Buy It Now (this is not your offer)
 //12. maximum Buy It Now (this is not your offer)
 $search = $s->playersearch(0,1,'gold','f442','attacker','9','13','144','','','','');
 //display the search results on the screen
 echo "<br />A Single Search Result: <br />" ;
 var_dump($search);

 ?>