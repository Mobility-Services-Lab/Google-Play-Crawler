import java.util.ArrayList;



public class Application {
    private String id;
    private String name;
    private ArrayList <String> genres = new ArrayList<>();
    private String developer;
    private String price;
    private String numDownloads;
    private boolean hasInAppPurchases;
    private boolean hasADS;
    private ArrayList<String> links = new ArrayList<>();
    private boolean searchedDeep;
    private String description;
    private String rating;
    private String votes;
    private String lastUpdate;

    public Application (String id, String name, ArrayList<String> genres, String developer, String price, String numDownloads,
                        boolean hasInAppPurchases, boolean hasADS, ArrayList<String> links, boolean searchedDeep,
                        String description, String lastUpdate, String rating, String votes){
        this.id = id;
        this.name = name;
        this.genres = genres;
        this.developer = developer;
        this.price = price;
        this.numDownloads = numDownloads;
        this.hasInAppPurchases = hasInAppPurchases;
        this.hasADS = hasADS;
        this.links = links;
        this.searchedDeep = searchedDeep;
        this.description = description;
        this.lastUpdate = lastUpdate;
        this.rating = rating;
        this.votes = votes;
    }

    public ArrayList<String> toArrayList() {
        ArrayList<String> applicationDataRecord = new ArrayList<>();
        applicationDataRecord.add(this.getId());
        applicationDataRecord.add(this.getName());
        applicationDataRecord.add(this.getGenres().toString());
        applicationDataRecord.add(this.getDeveloper());
        applicationDataRecord.add(this.getPrice());
        applicationDataRecord.add(this.getNumDownloads());
        applicationDataRecord.add(this.isHasInAppPurchases()+"");
        applicationDataRecord.add(this.isHasADS()+"");
        applicationDataRecord.add(this.getLinks().toString());
        applicationDataRecord.add(this.isSearchedDeep()+"");
        applicationDataRecord.add(this.getDescription());
        applicationDataRecord.add(this.getLastUpdate());
        applicationDataRecord.add(this.getRating());
        applicationDataRecord.add(this.getVotes());
        return applicationDataRecord;
    }

    public String getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public String getDeveloper() {
        return developer;
    }

    public String getPrice() {
        return price;
    }

    public String getNumDownloads() {
        return numDownloads;
    }

    public boolean isHasInAppPurchases() {
        return hasInAppPurchases;
    }

    public boolean isHasADS() {
        return hasADS;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public boolean isSearchedDeep() {
        return searchedDeep;
    }

    public String getDescription() {
        return description;
    }

    public String getRating() {
        return rating;
    }

    public String getVotes() {
        return votes;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }
}
