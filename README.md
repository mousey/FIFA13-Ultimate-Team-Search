![FIFA12 Ultimate Team Search](http://i.imgur.com/gUxon.png)

# FIFA12 Ultimate Team Search

Having found [pmzipko's](http://pastebin.com/Zu5uDP7X) code to access the EA FIFA Web APP for FIFA11 I've taken a stab at updating it for FIFA12

## Files
* Connector.php  = Creates a connection to the EA Web App Server.
* Searchor.php   = Searches the database for the players/staff requested.
* Tradeor.php    = Bid on items and view Trade details.
* Functionor.php = Random functions used to return player/staff info and other things.
* Eahashor.php   = Returns the hash value of the secret question answer supplied

## External Files
* pmzipko = The original code for the FIFA 11 FUT that my code is based from.
* ea      = The original JavaScript copied from the EA website for hashing your secret question answer.