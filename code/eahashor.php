<?php  
/**
* @llygoden
* @author - Rob McGhee
* @URL - www.robmcghee.com
* @date - 20/09/12
* @version - 1.0
**/

/*	EA are using the same Hashing algorithm as in FIFA 12

/* 	This is EAs implementation of the MD5 hashing algorithm
*	It's identical to the standard MD5 algorithm apart from two changes
*	Line 62 - uses 14 instead of 16 for the shift value
*	Line 96 - they do the last II function twice on 'b' where MD5 only does it once
*	I'm not sure if these are intentional changes
*	or just errors when copy pasting by a programmer
*/

function hash($string){ 
	$a = "67452301"; 
	$b = "EFCDAB89"; 
	$c = "98BADCFE"; 
	$d = "10325476";  

	$words = init($string);  

	for($i = 0; $i <= count($words)/16-1; $i++){         
		$A = $a;         
		$B = $b;         
		$C = $c;         
		$D = $d;           

		/* ROUND 1 */         
		FF ($A, $B, $C, $D, $words[0 + ($i * 16)], 7, "d76aa478");          
		FF ($D, $A, $B, $C, $words[1 + ($i * 16)], 12, "e8c7b756");          
		FF ($C, $D, $A, $B, $words[2 + ($i * 16)], 17, "242070db");          
		FF ($B, $C, $D, $A, $words[3 + ($i * 16)], 22, "c1bdceee");          
		FF ($A, $B, $C, $D, $words[4 + ($i * 16)], 7, "f57c0faf");          
		FF ($D, $A, $B, $C, $words[5 + ($i * 16)], 12, "4787c62a");          
		FF ($C, $D, $A, $B, $words[6 + ($i * 16)], 17, "a8304613");          
		FF ($B, $C, $D, $A, $words[7 + ($i * 16)], 22, "fd469501");          
		FF ($A, $B, $C, $D, $words[8 + ($i * 16)], 7, "698098d8");          
		FF ($D, $A, $B, $C, $words[9 + ($i * 16)], 12, "8b44f7af");          
		FF ($C, $D, $A, $B, $words[10 + ($i * 16)], 17, "ffff5bb1");          
		FF ($B, $C, $D, $A, $words[11 + ($i * 16)], 22, "895cd7be");          
		FF ($A, $B, $C, $D, $words[12 + ($i * 16)], 7, "6b901122");          
		FF ($D, $A, $B, $C, $words[13 + ($i * 16)], 12, "fd987193");          
		FF ($C, $D, $A, $B, $words[14 + ($i * 16)], 17, "a679438e");          
		FF ($B, $C, $D, $A, $words[15 + ($i * 16)], 22, "49b40821");           
		
		/* ROUND 2 */         
		GG ($A, $B, $C, $D, $words[1 + ($i * 16)], 5, "f61e2562");          
		GG ($D, $A, $B, $C, $words[6 + ($i * 16)], 9, "c040b340");          
		GG ($C, $D, $A, $B, $words[11 + ($i * 16)], 14, "265e5a51");          
		GG ($B, $C, $D, $A, $words[0 + ($i * 16)], 20, "e9b6c7aa");          
		GG ($A, $B, $C, $D, $words[5 + ($i * 16)], 5, "d62f105d");          
		GG ($D, $A, $B, $C, $words[10 + ($i * 16)], 9, "02441453");          
		GG ($C, $D, $A, $B, $words[15 + ($i * 16)], 14, "d8a1e681");          
		GG ($B, $C, $D, $A, $words[4 + ($i * 16)], 20, "e7d3fbc8");          
		GG ($A, $B, $C, $D, $words[9 + ($i * 16)], 5, "21e1cde6");          
		GG ($D, $A, $B, $C, $words[14 + ($i * 16)], 9, "c33707d6");          
		GG ($C, $D, $A, $B, $words[3 + ($i * 16)], 14, "f4d50d87");          
		GG ($B, $C, $D, $A, $words[8 + ($i * 16)], 20, "455a14ed");          
		GG ($A, $B, $C, $D, $words[13 + ($i * 16)], 5, "a9e3e905");          
		GG ($D, $A, $B, $C, $words[2 + ($i * 16)], 9, "fcefa3f8");          
		GG ($C, $D, $A, $B, $words[7 + ($i * 16)], 14, "676f02d9");          
		GG ($B, $C, $D, $A, $words[12 + ($i * 16)], 20, "8d2a4c8a");           
		
		/* ROUND 3 */         
		HH ($A, $B, $C, $D, $words[5 + ($i * 16)], 4, "fffa3942");          
		HH ($D, $A, $B, $C, $words[8 + ($i * 16)], 11, "8771f681");          
		//HH ($C, $D, $A, $B, $words[11 + ($i * 16)], 16, "6d9d6122");
		//MD5 would have used the line above here EA use the line below
		//EA change the function to use 14 rather than 16 
		HH ($C, $D, $A, $B, $words[11 + ($i * 16)], 14, "6d9d6122");
		HH ($B, $C, $D, $A, $words[14 + ($i * 16)], 23, "fde5380c");          
		HH ($A, $B, $C, $D, $words[1 + ($i * 16)], 4, "a4beea44");          
		HH ($D, $A, $B, $C, $words[4 + ($i * 16)], 11, "4bdecfa9");          
		HH ($C, $D, $A, $B, $words[7 + ($i * 16)], 16, "f6bb4b60");          
		HH ($B, $C, $D, $A, $words[10 + ($i * 16)], 23, "bebfbc70");         
		HH ($A, $B, $C, $D, $words[13 + ($i * 16)], 4, "289b7ec6");          
		HH ($D, $A, $B, $C, $words[0 + ($i * 16)], 11, "eaa127fa");          
		HH ($C, $D, $A, $B, $words[3 + ($i * 16)], 16, "d4ef3085");          
		HH ($B, $C, $D, $A, $words[6 + ($i * 16)], 23, "04881d05");          
		HH ($A, $B, $C, $D, $words[9 + ($i * 16)], 4, "d9d4d039");          
		HH ($D, $A, $B, $C, $words[12 + ($i * 16)], 11, "e6db99e5");          
		HH ($C, $D, $A, $B, $words[15 + ($i * 16)], 16, "1fa27cf8");          
		HH ($B, $C, $D, $A, $words[2 + ($i * 16)], 23, "c4ac5665");           
		
		/* ROUND 4 */         
		II ($A, $B, $C, $D, $words[0 + ($i * 16)], 6, "f4292244");          
		II ($D, $A, $B, $C, $words[7 + ($i * 16)], 10, "432aff97");          
		II ($C, $D, $A, $B, $words[14 + ($i * 16)], 15, "ab9423a7");          
		II ($B, $C, $D, $A, $words[5 + ($i * 16)], 21, "fc93a039");          
		II ($A, $B, $C, $D, $words[12 + ($i * 16)], 6, "655b59c3");          
		II ($D, $A, $B, $C, $words[3 + ($i * 16)], 10, "8f0ccc92");          
		II ($C, $D, $A, $B, $words[10 + ($i * 16)], 15, "ffeff47d");          
		II ($B, $C, $D, $A, $words[1 + ($i * 16)], 21, "85845dd1");          
		II ($A, $B, $C, $D, $words[8 + ($i * 16)], 6, "6fa87e4f");          
		II ($D, $A, $B, $C, $words[15 + ($i * 16)], 10, "fe2ce6e0");          
		II ($C, $D, $A, $B, $words[6 + ($i * 16)], 15, "a3014314");          
		II ($B, $C, $D, $A, $words[13 + ($i * 16)], 21, "4e0811a1");          
		II ($A, $B, $C, $D, $words[4 + ($i * 16)], 6, "f7537e82");          
		II ($D, $A, $B, $C, $words[11 + ($i * 16)], 10, "bd3af235");          
		II ($C, $D, $A, $B, $words[2 + ($i * 16)], 15, "2ad7d2bb");          
		II ($B, $C, $D, $A, $words[9 + ($i * 16)], 21, "eb86d391");
		//MD5 would finish on the line above
		//EA change this and use this last line twice
		II ($B, $C, $D, $A, $words[9 + ($i * 16)], 21, "eb86d391");
		
		addVars($a, $b, $c, $d, $A, $B, $C, $D);         
	}   
		
	$MD5 = '';   
		
	foreach (array($a, $b, $c, $d) as $x) {       
		$MD5 .= implode('', array_reverse(str_split(leftpad($x, 8), 2)));   
	}          

	return $MD5; 
}

/* General functions */  
function hexbin($str){         
	$hexbinmap = array(	"0" => "0000", 
						"1" => "0001", 
						"2" => "0010", 
						"3" => "0011"
						, "4" => "0100" 
						, "5" => "0101" 
						, "6" => "0110"                                                 
						, "7" => "0111"                                                 
						, "8" => "1000"                                                
						, "9" => "1001"                                                
						, "A" => "1010"                                                
						, "a" => "1010"                                                
						, "B" => "1011"                                                
						, "b" => "1011"                                                
						, "C" => "1100"                                                
						, "c" => "1100"                                                
						, "D" => "1101"                                                
						, "d" => "1101"                                                
						, "E" => "1110"                                                 
						, "e" => "1110"                                              
						, "F" => "1111"                                              
						, "f" => "1111");         					
	$bin = "";     

	for ($i = 0; $i < strlen($str); $i++){         
		$bin .= $hexbinmap[$str[$i]];     
	}     

	$bin = ltrim($bin, '0');            
	return $bin; 
}  

function strhex($str){     
	$hex = "";     
	
	for ($i = 0; $i < strlen($str); $i++){         
		$hex = $hex.leftpad(dechex(ord($str[$i])), 2);     
	}     
	
	return $hex; 
}   

/* MD5-specific functions */  
function init($string){         
	$len = strlen($string) * 8;         
	$hex = strhex($string); // convert ascii string to hex         
	$bin = leftpad(hexbin($hex), $len); // convert hex string to bin         
	$padded = pad($bin);         
	$padded = pad($padded, 1, $len);         
	$block = str_split($padded, 32);          
	
	foreach ($block as &$b) {             
		$b = implode('', array_reverse(str_split($b, 8)));         
	}          
	
	return $block; 
}  

function pad($bin, $type=0, $len = 0){         
	if($type == 0){         
		$bin = $bin."1";         
		$buff = strlen($bin) % 512;         
		if($buff != 448){                 
			while(strlen($bin) % 512 != 448){                          
				$bin = $bin."0";                 
			}   
		}
	}         
	// append length (b) of string to latter 64 bits         
	elseif($type == 1){             
		$bLen = leftpad(decbin($len), 64);        
		$bin .= implode('', array_reverse(str_split($bLen, 8)));       
	}   
	return $bin; 
}  

/* MD5 base functions */  
function F($X, $Y, $Z){         
	$X = hexdec($X);
	$Y = hexdec($Y); 
	$Z = hexdec($Z);
	$calc = (($X & $Y) | ((~ $X) & $Z)); // X AND Y OR NOT X AND Z   
	return  $calc;  
}  

function G($X, $Y, $Z){ 
	$X = hexdec($X);
	$Y = hexdec($Y);
	$Z = hexdec($Z);
	$calc = (($X & $Z) | ($Y & (~ $Z))); // X AND Z OR Y AND NOT Z
	return  $calc;  
}  

function H($X, $Y, $Z){
    $X = hexdec($X);
	$Y = hexdec($Y);
	$Z = hexdec($Z);
	$calc = ($X ^ $Y ^ $Z); // X XOR Y XOR Z
	return  $calc;  
}  

function I($X, $Y, $Z){         
	$X = hexdec($X);
	$Y = hexdec($Y);
	$Z = hexdec($Z);
	$calc = ($Y ^ ($X | (~ $Z))) ; // Y XOR (X OR NOT Z)
	return  $calc;  
}  

/* MD5 round functions */  
/* $A - hex, $B - hex, $C - hex, $D - hex (F - dec) 
$M - binary 
$s - decimal 
$t - hex 
*/ 
function FF(&$A, $B, $C, $D, $M, $s, $t){         
	$A = hexdec($A);
	$t = hexdec($t);
	$M = bindec($M);
	$A = ($A + F($B, $C, $D) + $M + $t) & 0xffffffff; //decimal
	$A = rotate($A, $s);
	$A = dechex((hexdec($B) + hexdec($A)) & 0xffffffff); 
}  

function GG(&$A, $B, $C, $D, $M, $s, $t){ 
	$A = hexdec($A);
	$t = hexdec($t);
	$M = bindec($M);
	$A = ($A + G($B, $C, $D) + $M + $t) & 0xffffffff; //decimal
	$A = rotate($A, $s);
	$A = dechex((hexdec($B) + hexdec($A)) & 0xffffffff); 
}  

function HH(&$A, $B, $C, $D, $M, $s, $t){
    $A = hexdec($A);
	$t = hexdec($t);
	$M = bindec($M);
	$A = ($A + H($B, $C, $D) + $M + $t) & 0xffffffff; //decimal
	$A = rotate($A, $s);
	$A = dechex((hexdec($B) + hexdec($A)) & 0xffffffff); 
}  

function II(&$A, $B, $C, $D, $M, $s, $t){
    $A = hexdec($A);
	$t = hexdec($t);
	$M = bindec($M);
	$A = ($A + I($B, $C, $D) + $M + $t) & 0xffffffff; //decimal
	$A = rotate($A, $s);
	$A = dechex((hexdec($B) + hexdec($A)) & 0xffffffff); 
}  

// shift 
function rotate ($decimal, $bits) { //returns hex     
	return dechex((($decimal << $bits) |  ($decimal >> (32 - $bits))) & 0xffffffff); 
}  

function addVars(&$a, &$b, &$c, &$d, $A, $B, $C, $D){    
    $A = hexdec($A);
	$B = hexdec($B);
	$C = hexdec($C);
	$D = hexdec($D); 
	$aa = hexdec($a);
	$bb = hexdec($b);
	$cc = hexdec($c);
	$dd = hexdec($d);
	
	$aa = ($aa + $A) & 0xffffffff;
	$bb = ($bb + $B) & 0xffffffff;
	$cc = ($cc + $C) & 0xffffffff;
	$dd = ($dd + $D) & 0xffffffff;
	
	$a = dechex($aa); 
	$b = dechex($bb);
	$c = dechex($cc);
	$d = dechex($dd); 
}
  
function leftpad($needs_padding, $alignment) {
    if (strlen($needs_padding) % $alignment) {
		$pad_amount    = $alignment - strlen($needs_padding) % $alignment;
		$left_pad      = implode('', array_fill(0, $pad_amount, '0'));
		$needs_padding = $left_pad . $needs_padding;   
	}   
	
	return $needs_padding; 
}
 
?>