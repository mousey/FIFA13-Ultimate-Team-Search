import java.io.*;
import java.util.*;
import java.net.*;

/**
 * @author pmzipko
 * @date 7/26/2011
 * @version 2.2
 */
public class FUTClient {
    private String user;
    private String password;
    private String securityQ;
    public int lastTradeId;
    //Was having trouble with java's built in cookie handler, so I wrote my own. EASW_KEY and PhishingKey are the two critical cookies needed 
    //to authenticate with the EA servers. They are retrieved durring the connect() and AnswerSecurityQ() methods.
    private static String EASW_KEY;
    private static String PhishingKey;


    public FUTClient(String iniFile){
    //class constructor, takes a string file name. This file is to be a text ini file with the following properties
    //#user = UserName
    //#password = Password
    //#SecurityQuestion = HashedSecurityQ
    //
    //Note, the security question is no the plain text answer to your question, it's the hash that the web app creates and sends to the server.
    //this was easier than bothering to hash it myself here. To get the hash use any tool to intercept http traffic and monitor the data sent
    //when you answer the question. Copy and paste the hash sent to your ini file.
            try{
                Properties p = new Properties();
                p.load(new FileInputStream(iniFile));
                user = p.getProperty("user");
                password = p.getProperty("password");
                securityQ = p.getProperty("SecurityQuestion");
            }catch(IOException e){System.out.println("ini file not found: "+e.toString());}
        }

    public void connect(){
        //logs into the EA web app with the username and password provided in the ini file. This required to establish an EASW_KEY.
             HttpURLConnection   urlConn;
             URL url;
             DataOutputStream  printout;
             DataInputStream  input;
             // URL of CGI-Bin script.
             url = null;
             try{

                url = new URL ("https://www.ea.com/uk/football/services/authenticate/login");}
                catch(java.net.MalformedURLException e){}
             // URL connection channel.
             try{
                urlConn = (HttpURLConnection)url.openConnection();
                // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
                // Let the RTS know that we want to do output.
                urlConn.setDoOutput (true);
                // No caching, we want the real thing.
                urlConn.setUseCaches (false);
                urlConn.setAllowUserInteraction(true);
                // Specify the content type.
                urlConn.setRequestProperty
                      ("Content-Type", "application/x-www-form-urlencoded");
                // Send POST output.
                printout = new DataOutputStream (urlConn.getOutputStream ());
                String content ="email="+user+"&password="+password+"&=Sign+In";
                printout.writeBytes (content);
                // Let the run-time system (RTS) know that we want input.
               printout.flush ();
                printout.close ();
                // Get response data.
               input = new DataInputStream (urlConn.getInputStream ());

                String str;
                while (null != ((str = input.readLine())))
                {
                }

               // System.out.println("reconnected");
                //retrieves the EASW_KEY cookie from the URLConnection
                Map<String,List<String>> cm = urlConn.getHeaderFields();
                List<String> cl = cm.get("Set-Cookie");
                input.close ();
                int cmLength = cl.size();
                if (cmLength>0){
                    for (int i=0; i<cmLength;i++){
                        int index;
                        String cookieS = cl.get(i);
                        if((index=cookieS.indexOf("EASW_KEY=")) != -1){
                            int index2 = cookieS.indexOf(";", index);
                            EASW_KEY = cookieS.substring(index, index2);
                        }
                    }
                }
                AnswerSecurityQ();

             }
                catch(IOException e){System.out.println("connect: connection failed "+ e.toString());}
         }
public void AnswerSecurityQ(){
    //Answers the security question with the hash provided in the ini file. This is required to retrieve the FUTWebPhishing cookie.
  URL url;
             URLConnection   urlConn;
             DataOutputStream  printout;
             DataInputStream  input;
             // URL of CGI-Bin script.
             url = null;
             try{
                url = new URL ("http://www.ea.com/p/fut/a/card/l/en_US/s/p/ut/game/ut11/phishing/validate");}
                catch(java.net.MalformedURLException e){}
             // URL connection channel.
             try{
                urlConn = url.openConnection();
                // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
                // Let the RTS know that we want to do output.
                urlConn.setDoOutput (true);
                // No caching, we want the real thing.
                urlConn.setUseCaches (false);
                // Specify the content type.
                urlConn.setRequestProperty
                      ("Content-Type", "application/x-www-form-urlencoded");
                urlConn.addRequestProperty("Cookie", EASW_KEY);

                // Send POST output.
                printout = new DataOutputStream (urlConn.getOutputStream ());
                String content ="answer="+securityQ;
                printout.writeBytes (content);
                // Let the run-time system (RTS) know that we want input.

                printout.flush ();
                printout.close ();
                // Get response data.
                input = new DataInputStream (urlConn.getInputStream ());
                String str;
                while (null != ((str = input.readLine())))
                {
                }
                input.close ();

                //retrieves the FUTWebPhishing cookie from the URLConnection
                Map<String,List<String>> cm = urlConn.getHeaderFields();
                List<String> cl = cm.get("Set-Cookie");
                input.close ();
                int cmLength = cl.size();
                if (cmLength>0){
                    for (int i=0; i<cmLength;i++){
                        int index;
                        String cookieS = cl.get(i);
                        if((index=cookieS.indexOf("FUTWebPhishing")) != -1){
                            int index2 = cookieS.indexOf(";", index);
                            PhishingKey = cookieS.substring(index, index2);
                        }
                    }
                }

             }
                catch(IOException e){System.out.println("connect: connection failed "+ e.toString());}
         }
    public  String search(int start,int count,String level,String formation,String position,int nationality,int league,int team, int minBid,int maxBid,int minBIN,int maxBIN){
            //This method will search the players trading market based on parameters entered. Any of these parameters may be left blank and default vaules will be used.
            //Nationality, league and team are enumerations. Enter the integer for the appropriate value you want. Some samples are below, but to get more values use
            //any tool to monitor http traffic while performing searches and you'll see what values the web app uses for various selections.
            
            //This search returns an xml string that can be parsed to analyse the results. Note, only 16 or so results are returned max each search, to search larger data sets 
            //increment start. Count is the number of results to retrieve. For example, you can make continuous searches with start=0, count=10, then increment start by 10 each
            //time until no further results are returned.
        
            //Valid enumerations
            //level = "gold", "bronze", "silver"
            //formation = "f433", "f352", "f41212", ect...
            //position = "defense", "midfield", "attacker", other positions are same as app
            //nationality = 51 - Argentina, 54 - Brazil, 14 - England, 18 - France, 21 - Germany, 27 - Italy,
            //38 - Portugal, 40 - Russia, 45 - Spain, 60 - Uraguay
            //league = 13 - EPL, 19 - Bundesliga, 31 - Serie A, 39 - MLS, 53 - BBVA
            //team =
             URL url;
             URLConnection   urlConn;
             DataOutputStream  printout;
             DataInputStream  input;
             StringBuilder returnStr = new StringBuilder();
                    //setup level string
             Date now = new Date();
             Long longTime = now.getTime();

             StringBuilder levelString = new StringBuilder();
             if (level.equals("") || level.equals("any"))
                levelString.append("");
             else
                levelString.append("&lev="+level);

          //setup position string

             StringBuilder positionString = new StringBuilder();
             if (position.equals("") || position.equals("any")){
                positionString.append("");
             }
             else {
                if (position.equals("defense") || position.equals("midfield") || position.equals("attacker"))
                   positionString.append("&zone="+position);
                else
                   positionString.append("&pos="+position);
             }

          //setup formation string

             StringBuilder formationString = new StringBuilder();
             if (formation.equals("") || formation.equals("any")){
                formationString.append("");
             }
             else {
                formationString.append("&form="+formation);
             }

          //setup nationality string

             StringBuilder nationalityString = new StringBuilder();
             if(nationality > 0)
                nationalityString.append("&nat="+nationality);
             else
                nationalityString.append("");

          //setup league string

             StringBuilder leagueString = new StringBuilder();
             if(league > 0)
                leagueString.append("&leag="+league);
             else
                leagueString.append("");

          //setup team string

             StringBuilder teamString = new StringBuilder();
             if(team > 0)
                teamString.append("&team="+team);
             else
                teamString.append("");

          //setup min bid string

             StringBuilder minBidString = new StringBuilder();
             if(minBid > 0)
                minBidString.append("&minr="+minBid);
             else
                minBidString.append("");

          //setup max bid string

             StringBuilder maxBidString = new StringBuilder();
             if(maxBid > 0)
                maxBidString.append("&maxr="+maxBid);
             else
                maxBidString.append("");

          //setup min BIN string

             StringBuilder minBINString = new StringBuilder();
             if(minBIN > 0)
                minBINString.append("&minb="+minBIN);
             else
                minBINString.append("");

          //setup max BIN string

             StringBuilder maxBINString = new StringBuilder();
             if(maxBIN > 0)
                maxBINString.append("&maxb="+maxBIN);
             else
                maxBINString.append("");

          // URL of CGI-Bin script.
             url = null;
             try{
                url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/game/ut11/auctionhouse?type=player&start="+start+"&num="+count+levelString+formationString+positionString+nationalityString+leagueString+teamString+minBidString+maxBidString+minBINString+maxBINString+"&timestamp="+longTime);
             //System.out.println(url);
             }
                catch(java.net.MalformedURLException e){ connect();System.out.println("search: Malformed URL"+e.toString());}
          // URL connection channel.
             try{
                urlConn = url.openConnection();

             // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
             // Let the RTS know that we want to do output.
             //urlConn.setDoOutput (true);
             // No caching, we want the real thing.
                urlConn.setUseCaches (false);
             // Specify the content type.
             //urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             // Send POST output.
             //printout = new DataOutputStream (urlConn.getOutputStream ());
              //printout.writeBytes (content);
             //printout.flush ();
             //printout.close ();
             // Get response data.
                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);
                input = new DataInputStream (urlConn.getInputStream ());
             //input = new DataInputStream (url.openStream());

                String str;
                while (null != (str = input.readLine()))
                {
                   returnStr.append(str);
                }
                input.close ();
             }
                catch(IOException e)
                {
        }

             return returnStr.toString();
          }
    public  String searchTraining(String level, String type, int start, int count, int MaxB){
            //searches through traning cards, much simplier than the player search. Takes a string for bronze, silver, gold and a type for the
            //type of card. Increment start with a fixed count to step through the pages.
             URL url;
             URLConnection   urlConn;
             DataOutputStream  printout;
             DataInputStream  input;
             StringBuilder returnStr = new StringBuilder();

          // URL of CGI-Bin script.
             url = null;
             try{
                url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/game/ut11/auctionhouse?type=training&cat="+type+"&blank=10&start="+start+"&num="+count+"&maxb="+MaxB+"&timestamp=0");
             //System.out.println(url);
             }
             catch(java.net.MalformedURLException e){ connect();System.out.println("search: Malformed URL"+e.toString());}
          // URL connection channel.
             try{
                urlConn = url.openConnection();

             // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
             // Let the RTS know that we want to do output.
             //urlConn.setDoOutput (true);
             // No caching, we want the real thing.
                urlConn.setUseCaches (false);
             // Specify the content type.
             //urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             // Send POST output.
             //printout = new DataOutputStream (urlConn.getOutputStream ());
            //printout.writeBytes (content);
             //printout.flush ();
             //printout.close ();
             // Get response data.

                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);
                input = new DataInputStream (urlConn.getInputStream ());
             //input = new DataInputStream (url.openStream());

                String str;
                while (null != (str = input.readLine()))
                {
                   returnStr.append(str);
                }
                input.close ();
             }
                catch(IOException e)
                {
               
                   try
                   {
                      Thread.sleep(2000);
                   }
                      catch(Exception e2)
                      {
                      }
                  // System.out.println("search: retrieve search "+e.toString());
                }

             return returnStr.toString();
          }

    public  boolean buy(int tradeID, int amount, String playerName){
        //places a bid of the set amount on a particular trade. The string playerName isn't used as part of the transaction, but is passed 
        //so I can message the name of the card bought.
             URL url;
             URLConnection   urlConn;
             DataOutputStream  printout;
             DataInputStream  input;

             StringBuilder returnStr = new StringBuilder();

          // URL of CGI-Bin script.
             url = null;

             try{
                url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/put/game/ut11/trade/"+tradeID+"/bid");}
                catch(java.net.MalformedURLException e){connect();System.out.println("Buy: malformed URL "+ e.toString());}
          // URL connection channel.
             try{
                urlConn = url.openConnection();
             // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
             // Let the RTS know that we want to do output.
                urlConn.setDoOutput (true);
             // No caching, we want the real thing.
                urlConn.setUseCaches (false);
             // Specify the content type.
                urlConn.setRequestProperty
                   ("Content-type", "text/xml");

                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);
             // Send POST output.
                printout = new DataOutputStream (urlConn.getOutputStream ());
                String content ="<auctionInfo tradeId='"+tradeID+"'><bid>"+amount+"</bid></auctionInfo>";
                printout.writeBytes (content);
             // Let the run-time system (RTS) know that we want input.

                printout.flush ();
                printout.close ();
             // Get response data.
                input = new DataInputStream (urlConn.getInputStream ());
                String str;
                while (null != (str = input.readLine()))
                {
                   returnStr.append(str);
                }
                input.close ();
             }
                catch(IOException e){connect();System.out.println("Buy: exception buying "+e.toString());}
             //System.out.println(returnStr.toString());
             
             //somewhat dead code to keep track of if an attempt to buy a player failed and message it. Can be safely removed.
             if(returnStr.toString().indexOf("<error>")==-1){
                lastTradeId = tradeID;
                 return true;
            }else{
                //if(lastTradeId != tradeID)
                   // System.out.println("Trade Failed: "+playerName+" "+tradeID + " " + amount);
                return false;
            }
          }
    public  boolean buyPack(int packId, String payForIt){
        //dead code, this is not the real buy pack method. It's a method I wrote to try messing with the free gift packs, but didn't work.
             URL url;
             URLConnection   urlConn;
             DataOutputStream  printout;
             DataInputStream  input;

             StringBuilder returnStr = new StringBuilder();

          // URL of CGI-Bin script.
             url = null;

             try{
                url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/game/ut11/purchased/items");}
                catch(java.net.MalformedURLException e){connect();System.out.println("Buy: malformed URL "+ e.toString());}
          // URL connection channel.
             try{
                urlConn = url.openConnection();
             // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
             // Let the RTS know that we want to do output.
                urlConn.setDoOutput (true);
             // No caching, we want the real thing.
                urlConn.setUseCaches (false);
             // Specify the content type.

                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);
                urlConn.setRequestProperty
                   ("Content-type", "text/xml");
             // Send POST output.
                printout = new DataOutputStream (urlConn.getOutputStream ());
                String content ="<packTypeId useCredits='"+payForIt+"'>"+packId+"</packTypeId>";
                printout.writeBytes (content);
             // Let the run-time system (RTS) know that we want input.

                printout.flush ();
                printout.close ();
             // Get response data.
                input = new DataInputStream (urlConn.getInputStream ());
                String str;
                while (null != (str = input.readLine()))
                {
                   returnStr.append(str);
                }
                input.close ();
             }
             catch(IOException e){connect();System.out.println("Buy: exception buying "+e.toString());}
             System.out.println(returnStr.toString());
             if(returnStr.toString().indexOf("<error>")==-1){

                 return true;
            }else{
                 return false;
            }
          }
    public  boolean buyTokens(){
        //buys bid tokens. May no longer be functional, changes to the bid tokens on the market probably broke this but I haven't tested it since.
             URL url;
             URLConnection   urlConn;
             DataOutputStream  printout;
             DataInputStream  input;

             StringBuilder returnStr = new StringBuilder();

          // URL of CGI-Bin script.
             url = null;

             try{
                url = new URL ("http://pals.www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/game/ut11/user/bidTokens");}
                catch(java.net.MalformedURLException e){connect();System.out.println("Buy: malformed URL "+ e.toString());}
          // URL connection channel.
             try{
                urlConn = url.openConnection();
             // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
             // Let the RTS know that we want to do output.
                urlConn.setDoOutput (true);
             // No caching, we want the real thing.
                urlConn.setUseCaches (false);
             // Specify the content type.
                urlConn.setRequestProperty
                   ("Content-type", "text/xml");
                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);

             // Send POST output.
                printout = new DataOutputStream (urlConn.getOutputStream ());
                
                //With the change to the market to have 3 different bid token packages, look up the purchase id to find the right one to buy.
                String content ="<purchaseGroup id='token'><purchase id='70'/></purchaseGroup>";
                printout.writeBytes (content);

                // Let the run-time system (RTS) know that we want input.

                printout.flush ();
                printout.close ();
             // Get response data.
                input = new DataInputStream (urlConn.getInputStream ());
                String str;
                while (null != (str = input.readLine()))
                {
                   returnStr.append(str);
                }
                input.close ();
             }
             catch(IOException e){connect();System.out.println("Buy: exception buying "+e.toString());}
             System.out.println(returnStr.toString());
             if(returnStr.toString().indexOf("<error>")==-1){

                 return true;
            }else{
                 return false;
            }
          //return "";
          }
    public void postTrade(String itemID, int startBid, int buyNow, int duration){
        // post a player up for sale. Item ID of the card, start price, BIN price, length of time in seconds.
        //minimum buy price is 50c and minimum duration is 3600 seconds. 
       
        URL url;
        URLConnection   urlConn;
        DataOutputStream  printout;
        DataInputStream  input;

        StringBuilder returnStr = new StringBuilder();

        // URL of CGI-Bin script.
        url = null;

        try{
            url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/game/ut11/auctionhouse");}
        catch(java.net.MalformedURLException e){connect();System.out.println("Buy: malformed URL "+ e.toString());}
        // URL connection channel.
       try{
        urlConn = url.openConnection();
         // Let the run-time system (RTS) know that we want input.
        urlConn.setDoInput (true);
        // Let the RTS know that we want to do output.
        urlConn.setDoOutput (true);
        // No caching, we want the real thing.
        urlConn.setUseCaches (false);
        // Specify the content type.
        urlConn.setRequestProperty
        ("Content-type", "text/xml");
                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);

        // Send POST output.
        printout = new DataOutputStream (urlConn.getOutputStream ());
        String content ="<auctionInfo><itemData id='"+itemID+"'/><startingBid>"+startBid+"</startingBid><buyNowPrice>"+buyNow+"</buyNowPrice><duration>"+duration+"</duration></auctionInfo>";
        //System.out.println(content);
        printout.writeBytes (content);
        // Let the run-time system (RTS) know that we want input.

        printout.flush ();
        printout.close ();
        // Get response data.
        input = new DataInputStream (urlConn.getInputStream ());
        String str;
        while (null != (str = input.readLine()))
        {
            returnStr.append(str);
         }
         input.close ();
        }catch(IOException e){connect();System.out.println("Post Auction: exception buying "+e.toString());}
        //System.out.println("postTrade: " +returnStr.toString());

      }
     public  boolean tradeOffer(int tradeID, int amount, Collection<String> playerIDs){
         //make a trade offer. TradeID is the card you want to offer on. Amount is the amount of coins to offer. playerIDs is a 
         //collection of ItemIDs of the cards you want to offer.
         //NOTE: there used to be an amusing bug where if you listed the same ID multiple times in the collection it would show the persons 
         //seeing the trade multiple versions of that card. It's bugged though so they can't accept the trade. Haven't tested in months 
         //though, don't know if patched.
         
             URL url;
             URLConnection   urlConn;
             DataOutputStream  printout;
             DataInputStream  input;

             StringBuilder returnStr = new StringBuilder();
             StringBuilder content = new StringBuilder();
          // URL of CGI-Bin script.
             url = null;

             try{
                url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/game/ut11/trade/"+tradeID+"/offer");}
                catch(java.net.MalformedURLException e){connect();System.out.println("Buy: malformed URL "+ e.toString());}
          // URL connection channel.
             try{
                urlConn = url.openConnection();
             // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
             // Let the RTS know that we want to do output.
                urlConn.setDoOutput (true);
             // No caching, we want the real thing.
                urlConn.setUseCaches (false);
             // Specify the content type.
                urlConn.setRequestProperty
                   ("Content-type", "text/xml");
                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);

             // Send POST output.
                printout = new DataOutputStream (urlConn.getOutputStream ());
                content.append("<auctionInfo><bid>"+amount+"</bid>");
                for (Iterator it=playerIDs.iterator(); it.hasNext( ); ) {
                    Object anObject = it.next( );
                    content.append("<itemData id='"+anObject.toString()+"'/>");
                }
                content.append("</auctionInfo>");
                printout.writeBytes (content.toString());
             // Let the run-time system (RTS) know that we want input.

                printout.flush ();
                printout.close ();
             // Get response data.
                input = new DataInputStream (urlConn.getInputStream ());
                String str;
                while (null != (str = input.readLine()))
                {
                   returnStr.append(str);
                }
                input.close ();
             }
                catch(IOException e){connect();System.out.println("Trade Offer: exception buying "+e.toString());}
             //System.out.println(returnStr.toString());
             if(returnStr.toString().indexOf("<error>")==-1){

                 return true;
            }else{
                return false;
            }
          //return "";
          }
   public void movePlayer(String itemID, String pile){
       //move a card in your account. trade sends them to your trade pile, club sends them back to the club.
       
        URL url;
        URLConnection   urlConn;
        DataOutputStream  printout;
        DataInputStream  input;
        //valid piles - "trade", "club"
        StringBuilder returnStr = new StringBuilder();

        // URL of CGI-Bin script.
        url = null;

        try{
            url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/put/game/ut11/item");}
        catch(java.net.MalformedURLException e){connect();System.out.println("Buy: malformed URL "+ e.toString());}
        // URL connection channel.
       try{
        urlConn = url.openConnection();
         // Let the run-time system (RTS) know that we want input.
        urlConn.setDoInput (true);
        // Let the RTS know that we want to do output.
        urlConn.setDoOutput (true);
        // No caching, we want the real thing.
        urlConn.setUseCaches (false);
        // Specify the content type.
        urlConn.setRequestProperty
        ("Content-type", "text/xml");
                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);

        // Send POST output.
        printout = new DataOutputStream (urlConn.getOutputStream ());
        String content ="<items><itemData id='"+itemID+"'><pile>"+pile+"</pile></itemData></items>";
        //System.out.println(content);
        printout.writeBytes (content);
        // Let the run-time system (RTS) know that we want input.

        printout.flush ();
        printout.close ();
        // Get response data.
        input = new DataInputStream (urlConn.getInputStream ());
        String str;
        while (null != (str = input.readLine()))
        {
            returnStr.append(str);
         }
         input.close ();
        }catch(IOException e){connect();System.out.println("Post Auction: exception buying "+e.toString());}
        //System.out.println("move: "+returnStr.toString());

      }
    public String GetNewCards(){
        //When you open a pack, this method will retrieve the list of cards you recieved in that pack. 
        
        URL url;
        URLConnection   urlConn;
        DataOutputStream  printout;
        DataInputStream  input;
        StringBuilder returnStr = new StringBuilder();

        // URL of CGI-Bin script.
         url = null;
             try{
                url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/game/ut11/purchased/items?timestamp=0");
             //System.out.println(url);
             }
             catch(java.net.MalformedURLException e){ connect();System.out.println("search: Malformed URL"+e.toString());}
          // URL connection channel.
             try{
                urlConn = url.openConnection();

             // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
             // Let the RTS know that we want to do output.
             //urlConn.setDoOutput (true);
             // No caching, we want the real thing.
                urlConn.setUseCaches (false);
             // Specify the content type.
             //urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             // Send POST output.
             //printout = new DataOutputStream (urlConn.getOutputStream ());
             //String content ="start=0&lev=gold&type=player&num=16&timestamp=1291773340890";
             //printout.writeBytes (content);
             //printout.flush ();
             //printout.close ();
             // Get response data.
                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);

                input = new DataInputStream (urlConn.getInputStream ());
             //input = new DataInputStream (url.openStream());

                String str;
                while (null != (str = input.readLine()))
                {
                   returnStr.append(str);
                }
                input.close ();
        }catch(IOException e){connect();System.out.println("Post Auction: exception buying "+e.toString());}
        return returnStr.toString();

      }
    
public String getTrade(int TradeID){
    //This method will get the trade data for any card. This includes cards you don't own, so if you want to see what trades 
    //someone has on their card call this method with the auctionID of the card you want to examine.
    
             URL url;
             URLConnection   urlConn;
             DataOutputStream  printout;
             DataInputStream  input;
             StringBuilder returnStr = new StringBuilder();

          // URL of CGI-Bin script.
             url = null;
             try{
                url = new URL ("http://www.ea.com/p/fut/a/card/l/en_GB/s/p/ut/game/ut11/trade/"+TradeID+"?timestamp=0");
             //System.out.println(url);
             }
             catch(java.net.MalformedURLException e){ connect();System.out.println("search: Malformed URL"+e.toString());}
          // URL connection channel.
             try{
                urlConn = url.openConnection();

             // Let the run-time system (RTS) know that we want input.
                urlConn.setDoInput (true);
             // Let the RTS know that we want to do output.
             //urlConn.setDoOutput (true);
             // No caching, we want the real thing.
                urlConn.setUseCaches (false);
             // Specify the content type.
             //urlConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
             // Send POST output.
             //printout = new DataOutputStream (urlConn.getOutputStream ());
             //String content ="start=0&lev=gold&type=player&num=16&timestamp=1291773340890";
             //printout.writeBytes (content);
             //printout.flush ();
             //printout.close ();
             // Get response data.
                urlConn.addRequestProperty("Cookie", EASW_KEY+";"+PhishingKey);

                input = new DataInputStream (urlConn.getInputStream ());
             //input = new DataInputStream (url.openStream());

                String str;
                while (null != (str = input.readLine()))
                {
                   returnStr.append(str);
                }
                input.close ();
             }
                catch(IOException e)
                {
                  /* Date currentDate=new Date();
                   long currentTime=currentDate.getTime();
                   if(Find.connecting==false && (currentTime-Find.lastReconnect)>5000)
                   {
                      Find.lastReconnect=currentTime;
                      Find.connecting=true;
                      connect();
                   }*/
                    System.out.println("Get Trade Exception: "+ e);
                   try
                   {
                      Thread.sleep(2000);
                   }
                      catch(Exception e2)
                      {
                      }
                  // System.out.println("search: retrieve search "+e.toString());
                }
             if(returnStr.indexOf("Invalid Cookie")>0){
                    connect();
                    returnStr = new StringBuilder(getTrade(TradeID));
             }
             return returnStr.toString();
          }
}
