function hash( securityAnswer )
{ 
	var cleanAnswer = cleanString( securityAnswer );
	if ( cleanAnswer == "" )
	{
		return;
	}
	var answer 	=  md5( securityAnswer ); 	
}

function cleanString( dirtyString )
{
	var cleanString;
	cleanString = dirtyString.replace(/^\s*/, "").replace(/\s*$/, ""); 
	cleanString = cleanString.replace(/\s{2,}/, " "); 
	return cleanString;
}

//MD5
// shift amounts in each round
var r1Shifts 	= [ 7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22 ];
var r2Shifts 	= [ 5, 	9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20 ];
var r3Shifts 	= [ 4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23 ];
var r4Shifts	= [ 6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21 ]; 

// convert number to hex string
var hex_chr = "0123456789abcdef";
function numToHex(num)
{
	str = "";
	for(j = 0; j <= 3; j++)
	{
		str += hex_chr.charAt((num >> (j * 8 + 4)) & 0x0F) + hex_chr.charAt((num >> (j * 8)) & 0x0F);
	}
	return str;
}

// Process message into chunks
function chunkMessage( str )
{
	nblk = ((str.length + 8) >> 6) + 1;
	blks = new Array(nblk * 16);
	
	for(i = 0; i < nblk * 16; i++)
	{
		blks[i] = 0;
	}
	for(i = 0; i < str.length; i++)
	{
		blks[i >> 2] |= str.charCodeAt(i) << ((i % 4) * 8);
	}
	
	blks[i >> 2] |= 0x80 << ((i % 4) * 8);
	blks[nblk * 16 - 2] = str.length * 8;
	
	return blks;
}

function add(x, y)
{
  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
  return (msw << 16) | (lsw & 0xFFFF);
}

// Bitwise rotate 32bit num to left
function bitwiseRotate( x, c )
{
  return ( x << c ) | ( x >>> (32 - c) );
}

// Basic MD5 operations
function cmn(q, a, b, x, s, t)
{
	return add( bitwiseRotate( add( add(a, q), add(x, t) ), s ), b );
}
function md5_f(a, b, c, d, x, s, t)
{
	return cmn( (b & c) | ((~b) & d), a, b, x, s, t );
}
function md5_g(a, b, c, d, x, s, t)
{
	return cmn( (b & d) | (c & (~d)), a, b, x, s, t );
}
function md5_h(a, b, c, d, x, s, t)
{
	return cmn( b ^ c ^ d, a, b, x, s, t );
}
function md5_i(a, b, c, d, x, s, t)
{
	return cmn( c ^ (b | (~d)), a, b, x, s, t );
}

function md5(str)
{
	x = chunkMessage(str);
	
	a =  1732584193;
	b = -271733879;
	c = -1732584194;
	d =  271733878;
	for ( i = 0; i < x.length; i += 16 )
	{
		tempA = a;
		tempB = b;
		tempC = c;
		tempD = d;
		a = md5_f(a, b, c, d, x[i+ 0], r1Shifts[0] , -680876936);
		d = md5_f(d, a, b, c, x[i+ 1], r1Shifts[1] , -389564586);
		c = md5_f(c, d, a, b, x[i+ 2], r1Shifts[2] ,  606105819);
		b = md5_f(b, c, d, a, x[i+ 3], r1Shifts[3] , -1044525330);
		a = md5_f(a, b, c, d, x[i+ 4], r1Shifts[4] , -176418897);
		d = md5_f(d, a, b, c, x[i+ 5], r1Shifts[5] ,  1200080426);
		c = md5_f(c, d, a, b, x[i+ 6], r1Shifts[6] , -1473231341);
		b = md5_f(b, c, d, a, x[i+ 7], r1Shifts[7] , -45705983);
		a = md5_f(a, b, c, d, x[i+ 8], r1Shifts[8] ,  1770035416);
		d = md5_f(d, a, b, c, x[i+ 9], r1Shifts[9] , -1958414417);
		c = md5_f(c, d, a, b, x[i+10], r1Shifts[10], -42063);
		b = md5_f(b, c, d, a, x[i+11], r1Shifts[11], -1990404162);
		a = md5_f(a, b, c, d, x[i+12], r1Shifts[12],  1804603682);
		d = md5_f(d, a, b, c, x[i+13], r1Shifts[13], -40341101);
		c = md5_f(c, d, a, b, x[i+14], r1Shifts[14], -1502002290);
		b = md5_f(b, c, d, a, x[i+15], r1Shifts[15],  1236535329);    
		a = md5_g(a, b, c, d, x[i+ 1], r2Shifts[0] , -165796510);
		d = md5_g(d, a, b, c, x[i+ 6], r2Shifts[1] , -1069501632);
		c = md5_g(c, d, a, b, x[i+11], r2Shifts[2] ,  643717713);
		b = md5_g(b, c, d, a, x[i+ 0], r2Shifts[3] , -373897302);
		a = md5_g(a, b, c, d, x[i+ 5], r2Shifts[4] , -701558691);
		d = md5_g(d, a, b, c, x[i+10], r2Shifts[5] ,  38016083);
		c = md5_g(c, d, a, b, x[i+15], r2Shifts[6] , -660478335);
		b = md5_g(b, c, d, a, x[i+ 4], r2Shifts[7] , -405537848);
		a = md5_g(a, b, c, d, x[i+ 9], r2Shifts[8] ,  568446438);
		d = md5_g(d, a, b, c, x[i+14], r2Shifts[9] , -1019803690);
		c = md5_g(c, d, a, b, x[i+ 3], r2Shifts[10], -187363961);
		b = md5_g(b, c, d, a, x[i+ 8], r2Shifts[11],  1163531501);
		a = md5_g(a, b, c, d, x[i+13], r2Shifts[12], -1444681467);
		d = md5_g(d, a, b, c, x[i+ 2], r2Shifts[13], -51403784);
		c = md5_g(c, d, a, b, x[i+ 7], r2Shifts[14],  1735328473);
		b = md5_g(b, c, d, a, x[i+12], r2Shifts[15], -1926607734);
		
		a = md5_h(a, b, c, d, x[i+ 5], r3Shifts[0] , -378558);
		d = md5_h(d, a, b, c, x[i+ 8], r3Shifts[1] , -2022574463);
		//line below uses r2Shifts[2] where as MD5 would use r3Shifts[2] 
		c = md5_h(c, d, a, b, x[i+11], r2Shifts[2] ,  1839030562);
		b = md5_h(b, c, d, a, x[i+14], r3Shifts[3] , -35309556);
		a = md5_h(a, b, c, d, x[i+ 1], r3Shifts[4] , -1530992060);
		d = md5_h(d, a, b, c, x[i+ 4], r3Shifts[5] ,  1272893353);
		c = md5_h(c, d, a, b, x[i+ 7], r3Shifts[6] , -155497632);
		b = md5_h(b, c, d, a, x[i+10], r3Shifts[7] , -1094730640);
		a = md5_h(a, b, c, d, x[i+13], r3Shifts[8] ,  681279174);
		d = md5_h(d, a, b, c, x[i+ 0], r3Shifts[9] , -358537222);
		c = md5_h(c, d, a, b, x[i+ 3], r3Shifts[10], -722521979);
		b = md5_h(b, c, d, a, x[i+ 6], r3Shifts[11],  76029189);
		a = md5_h(a, b, c, d, x[i+ 9], r3Shifts[12], -640364487);
		d = md5_h(d, a, b, c, x[i+12], r3Shifts[13], -421815835);
		c = md5_h(c, d, a, b, x[i+15], r3Shifts[14],  530742520);
		b = md5_h(b, c, d, a, x[i+ 2], r3Shifts[15], -995338651);
		a = md5_i(a, b, c, d, x[i+ 0], r4Shifts[0] , -198630844);
		d = md5_i(d, a, b, c, x[i+ 7], r4Shifts[1] ,  1126891415);
		c = md5_i(c, d, a, b, x[i+14], r4Shifts[2] , -1416354905);
		b = md5_i(b, c, d, a, x[i+ 5], r4Shifts[3] , -57434055);
		a = md5_i(a, b, c, d, x[i+12], r4Shifts[4] ,  1700485571);
		d = md5_i(d, a, b, c, x[i+ 3], r4Shifts[5] , -1894986606);
		c = md5_i(c, d, a, b, x[i+10], r4Shifts[6] , -1051523);
		b = md5_i(b, c, d, a, x[i+ 1], r4Shifts[7] , -2054922799);
		a = md5_i(a, b, c, d, x[i+ 8], r4Shifts[8] ,  1873313359);
		d = md5_i(d, a, b, c, x[i+15], r4Shifts[9] , -30611744);
		c = md5_i(c, d, a, b, x[i+ 6], r4Shifts[10], -1560198380);
		b = md5_i(b, c, d, a, x[i+13], r4Shifts[11],  1309151649);
		a = md5_i(a, b, c, d, x[i+ 4], r4Shifts[12], -145523070);
		d = md5_i(d, a, b, c, x[i+11], r4Shifts[13], -1120210379);
		c = md5_i(c, d, a, b, x[i+ 2], r4Shifts[14],  718787259);
		b = md5_i(b, c, d, a, x[i+ 9], r4Shifts[15], -343485551);
		//This line is doubled for some reason, line below is not in the MD5 version
		b = md5_i(b, c, d, a, x[i+ 9], r4Shifts[15], -343485551);
		a = add(a, tempA);
		b = add(b, tempB);
		c = add(c, tempC);
		d = add(d, tempD);
	}
	
	return numToHex(a) + numToHex(b) + numToHex(c) + numToHex(d);
}