import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class CrawlForApps {
    private static HashMap<String, Application> Data;
    private static boolean printResults = false;
    private static boolean searchDeep = true;
    private static int Depth = 0;
    private static int currentDepth = 0;
    private static int appsAddedToDB = 0;
    private static int appsInDBatTheStart = 0;
    private static int maxAppsToGatherAtATime = 100000;
    private static boolean continueAfterError = true;
    private static String CSVName = "You should choose a name for your CSV.csv";
    private static int maxAttemptsForEachApp = 10;
    private static String lastAddress = "";
    private static int attemptsForLastAddress = 0;

    private static void parsePage(String address){
        if (!lastAddress.equals(address)) {
            attemptsForLastAddress=0;
            lastAddress=address;
        } else attemptsForLastAddress++;
        if ((Data.size()-appsInDBatTheStart)>= maxAppsToGatherAtATime) return;
        Document page = null;
        String inApp = "";
        String ads = "";
        boolean hasInAppPurchases=false;
        boolean hasAds = false;
        ArrayList<String> links;
        boolean searchedForLinks = false;
        //to avoid making same thing for several times
        if (Data.containsKey(address) && Data.get(address).isSearchedDeep()) return;
        try {
            //get page
            page = Jsoup.connect("https://play.google.com/store/apps/details?id="+address).get();
            //parse page

            String name = getName(page);
            String developer = getDeveloper(page);
            ArrayList<String> genres = getGenre(page);
            String numDownloads = getNumDownloads(page);
            String price = getPrice(page);
            String description = getDescription(page);
            String rating = getRating(page);
            String votes = getVotes(page);
            String lastUpdate = getLastUpdate(page);

            //Adding some Strings for better result representation
            if (price.equals("0")) {
                price = "free";
            }
            if (!page.getElementsByClass("inapp-msg").toString().equals("")) {
                inApp = "\nin-app purchases present";
                hasInAppPurchases = true;
            }
            if (!page.getElementsByClass("ads-supported-label-msg").toString().equals("")) {
                ads = "\nads are present";
                hasAds = true;
            }
            //print results
            if (printResults) {
                System.out.println("\nName: " + name +
                        "\nGenre: " + genres +
                        "\nDeveloper: " + developer +
                        "\nPrice: " + price +
                        "\nNumber of downloads: " + numDownloads +
                        inApp + ads+"\nLast Update: "+lastUpdate+
                        "\nRating: "+rating+"\nVotes: "+votes);
            }
            //search for links to similar apps on the page
            links = searchForLinks(page);
            if (searchDeep) {
                if (currentDepth<Depth) {
                    currentDepth++;
                    searchedForLinks = true;
                    for (String link:links) {
                        parsePage(link);
                    }
                    currentDepth--;
                }
            }
            Application app = new Application(address, name,genres,developer,price,numDownloads,hasInAppPurchases,hasAds, links, searchedForLinks,description,lastUpdate,rating,votes);
            Data.put(address, app);
            appsAddedToDB++;
            if (appsAddedToDB%10==0) System.out.println(Data.size()+ " entries in DB.");
            //save every 100 operations
            if (appsAddedToDB%100==0) {
                Save(Data, CSVName);
                System.out.println(((float)Math.round((float)(Data.size()-appsInDBatTheStart)/ maxAppsToGatherAtATime *1000)/10)+"% accomplished. "+(Data.size()-appsInDBatTheStart) + " app pages proceeded out of "+ maxAppsToGatherAtATime +".");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Save(Data, CSVName);
            if (continueAfterError&attemptsForLastAddress<maxAttemptsForEachApp) parsePage(address);
            System.out.println("Caught an IOException. If you see this  - then the catch-block worked, if you don't - you won't know that you don't))");
        } catch (NullPointerException n){
            n.printStackTrace();
            Save(Data, CSVName);
            if (continueAfterError&attemptsForLastAddress<maxAttemptsForEachApp) parsePage(address);
            System.out.println("Caught a nullPointerException. If you see this  - then the catch-block worked, if you don't - you won't know that you don't))");
        }
    }
    private static String getName(Document page){
        return page.getElementsByClass("id-app-title").text();
    }
    private static String getDeveloper(Document page){
        return page.getElementsByClass("document-subtitle primary").text();
    }
    private static ArrayList<String> getGenre(Document page){
        ArrayList<Element> genresRaw = page.getElementsByClass("document-subtitle category");
        ArrayList<String> genres = new ArrayList<>();
        for (Element aGenresRaw : genresRaw) {
            genres.add(aGenresRaw.attr("href").split("/")[4]);
        }
        return genres;
    }
    private static String getNumDownloads(Document page){
        String numberOfDownloads = "";
        Elements aContent = page.select("div[itemprop=numDownloads]");
        return aContent.text();
    }
    private static String getPrice(Document page) throws NullPointerException {
        Elements installButton = page.getElementsByClass("price buy id-track-click id-track-impression");
        Elements priceElement = installButton.select("meta[itemprop=price]");
        return priceElement.attr("content");
    }
    private static ArrayList <String> searchForLinks(Document page){
        Elements similar = page.getElementsByClass("cards id-card-list");
        //System.out.println(similar);
        ArrayList <String> LinksList = new ArrayList<>();
        Elements links1 = similar.select("a.card-click-target");
        //Elements links2 = links1.select("a[href]");
        //Elements links3 = links2.select("a[aria-label]");
        for (Element links : links1) {
            String app = links.attr("href").split("=")[1];
            if (!LinksList.contains(app)) {
                LinksList.add(app);
                //System.out.println(app);
            }
        }
        return LinksList;
    }
    private static String getDescription(Document page){
        Elements els = page.select("div[class=show-more-content text-body]");
        Elements div = els.select("div[jsname]");
        return div.text();
    }
    private static String getRating(Document page){
        Elements score = page.select("div.score");
        return score.text();
    }
    private static String getVotes(Document page){
        Elements votes = page.select("span[class=reviews-num]");
        return votes.text();
    }
    private static String getLastUpdate(Document page){
        Elements date = page.select("div[itemprop=datePublished]");
        return date.text();
    }
    private static void Save(HashMap<String, Application> Map, String CSVName){
        ArrayList <Application> ListToSave = convertToArrayList(Map);
        SaveNLoad.saveToCSV(ListToSave, CSVName);
    }
    private static HashMap<String, Application> Load(String CSVName){
        ArrayList <Application> LoadedList = SaveNLoad.loadFromCSV(CSVName);
        HashMap temp = convertToHashMap(LoadedList);
        appsInDBatTheStart = temp.size();
        return temp;
    }
    private static ArrayList<Application> convertToArrayList(HashMap<String, Application> Data){
        ArrayList <Application> TempList = new ArrayList<>();
        for (String key: Data.keySet()){
            TempList.add(Data.get(key));
        }
        return TempList;
    }
    private static HashMap<String, Application> convertToHashMap(ArrayList<Application> Data){
        HashMap<String, Application> TempMap = new HashMap<>();
        for (Application app: Data){
            TempMap.put(app.getId(), app);
        }
        return TempMap;
    }
    private static void runTheMapThrough(HashMap<String, Application> Map, int depth){
        Depth = depth;
        ArrayList<String> Temp = new ArrayList<>();
        Temp.addAll(Map.keySet());
        try {
            for (String key : Temp) {
                parsePage(key);
            }
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Exception caught by runTheMapThrough();");

        }
    }
    private static void crawlForApps(int depth){
        Depth = depth;
        String[] list = {"com.ultimateguitar.tabs","com.media.saturn","de.mcdonalds.mcdonaldsinfoapp","com.ea.games.r3_row",
                "com.expedia.bookings","in.softecks.economics","clone.whatsapp","jp.gr.java_conf.matchama.SceneSwitch",
                "com.blendr.mobile","net.booksy.customer","com.sparkpeople.androidtracker","com.tweber.stickfighter.activities",
                "com.globalexperts.jobsearch","com.anmol.vachnanquotes"};
        for (int a=0;a<list.length;a++){
            parsePage(list[a]);
        }
    }

    public static void main(String args[]){
        CSVName = "Data.csv";                   //name of CSV file in which the data will be saved/ from which will be read
        Data =  Load(CSVName);                  //loads already gathered App data from CSV, if given
        crawlForApps(6);                  //gatheres data recursively, starting with apps stored in the method. needs depth of recursive search to be given
        runTheMapThrough(Data,3);         //gatheres data further - same as crawlForApps, but instead of "starting apps" uses all apps in List Data
        Save(Data, CSVName);                    //save to CSV
    }
}
