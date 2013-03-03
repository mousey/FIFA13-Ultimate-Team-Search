![FIFA13 Ultimate Team Search](http://i.imgur.com/gUxon.png)

# FIFA13 Ultimate Team Search

Having found [pmzipko's](http://pastebin.com/Zu5uDP7X) code to access the EA FIFA Web APP for FIFA11 and then writting a [wrapper](https://github.com/mousey/FIFA12-Ultimate-Team-Search) for FIFA12. 
I thought I'd update the code to work with EA's latest and greatest FIFA13

## Files
* Connector.php  = Creates a connection to the EA Web App Server.
* Searchor.php   = Searches the database for the players/staff requested.
* Tradeor.php    = Bid on items and view Trade details.
* Functionor.php = Random functions used to return player/staff info and other things.
* Eahashor.php   = Returns the hash value of the secret question answer supplied

## External Files
* pmzipko = The original code for the FIFA 11 FUT that my code is based from.
* ea      = The original JavaScript copied from the EA website for hashing your secret question answer.

## How to read the searched data

Well, the structure of this data is not so simple.

This is an example well formatted:

    stdClass Object ( 
      [bidTokens] => stdClass Object ( 
        [count] => 25 [updateTime] => 0 
      ) 
        [credits] => 5879 
        [duplicateItemIdList] => 
        [auctionInfo] => Array ( 
        [0] => stdClass Object ( 
          [itemData] => stdClass Object ( 
            [id] => 100148111245 
            [timestamp] => 1349017203 
            [itemType] => player 
            [formation] => f442 
            [rating] => 83 
            [teamid] => 448 
            [training] => 0 
            [resourceId] => 1342339411 
            [itemState] => forSale 
            [preferredPosition] => ST 
            [cardsubtypeid] => 3 
            [discardValue] => 664 
            [injuryType] => head 
            [injuryGames] => 0 
            [suspension] => 0 
            [morale] => 50 
            [fitness] => 99 
            [statsList] => Array ( 
              [0] => stdClass Object ( 
                [value] => 0 
                [index] => 0 ) 
              [1] => stdClass Object ( 
                [value] => 0 
                [index] => 1 ) 
              [2] => stdClass Object ( 
                [value] => 0 
                [index] => 2 ) 
              [3] => stdClass Object ( 
                [value] => 0 
                [index] => 3 ) 
              [4] => stdClass Object ( 
                [value] => 0 
                [index] => 4 
              ) 
            ) 

            [attributeList] => Array ( 
              [0] => stdClass Object ( 
                [value] => 61 
                [index] => 0 
              ) 
              [1] => stdClass Object ( 
                [value] => 76 
                [index] => 1 
              ) 
              [2] => stdClass Object ( 
                [value] => 57 
                [index] => 2 
              ) 
              [3] => stdClass Object ( 
                [value] => 75 
                [index] => 3 
              ) 
              [4] => stdClass Object ( 
                [value] => 53 
                [index] => 4 
              ) 
              [5] => stdClass Object ( 
                [value] => 87 
                [index] => 5 
              ) 
            ) 
            [lifetimeStats] => Array ( 
              [0] => stdClass Object ( 
                [value] => 0 
                [index] => 0 
              ) 
              [1] => stdClass Object ( 
                [value] => 0 
                [index] => 1 
              ) 
              [2] => stdClass Object ( 
                [value] => 0 
                [index] => 2 
              ) 
              [3] => stdClass Object ( 
                [value] => 0 
                [index] => 3 )

              [4] => stdClass Object ( 
                [value] => 0 
                [index] => 4 
              ) 
            ) 
            [lastSalePrice] => 0 
            [owners] => 1 
            [contract] => 7 
            [rareflag] => 1 
          )

          [offers] => 0 
          [startingBid] => 6000 
          [buyNowPrice] => 7000 
          [tradeId] => 100222006179 
          [watched] => 
          [bidState] => none 
          [tradeState] => active 
          [expires] => 30 
          [currentBid] => 0 
          [sellerName] => MooglyFC 
          [sellerEstablished] => 1291060593 
          [sellerId] => 0 
        ) 