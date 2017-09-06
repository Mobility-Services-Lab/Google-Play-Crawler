import com.opencsv.CSVReader;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class SaveNLoad {

    private static final String CSV_FILE_NAME = "test.csv";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static String[] csvFileHeader =
            {"id" ,"name" ,"genres", "developer"
                    , "price", "numDownloads"
                    , "hasInAppPurchases", "hasADS"
                    , "links", "searchedDeep", "description"
                    , "lastUpdate", "rating", "votes"};


    public static void saveToCSV(ArrayList<Application> apps, String csvFileName){
        FileWriter fileWriter = null;
        CSVPrinter csvPrinter = null;

        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);

        try {
            //initialize FileWriter object
            fileWriter = new FileWriter(csvFileName);

            //initialize CSVPrinter object
            csvPrinter = new CSVPrinter(fileWriter, csvFileFormat);

            //Create CSV file header
            csvPrinter.printRecord(csvFileHeader);

            //Write a new object list to the CSV file
            for (Application application : apps) {
                csvPrinter.printRecord(application.toArrayList());
            }

            System.out.println("\nCSV file was created successfully! "+apps.size()+" Entries saved.");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvPrinter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
                e.printStackTrace();
            }
        }
    }
    public static ArrayList<Application> loadFromCSV(String fileName){
        System.out.println("Loading previously saved data.");
        ArrayList<Application> Temp = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(new FileReader(fileName));
            reader.readNext();
            while (true){
                String[] nextLine = reader.readNext();
                if (nextLine!=null){
                    ArrayList<String> genres = new ArrayList<String>(Arrays.asList(nextLine[2].substring(1, nextLine[2].length()-1).split(", ")));
                    ArrayList<String> links = new ArrayList<String>(Arrays.asList(nextLine[8].substring(1, nextLine[8].length()-1).split(", ")));
                    boolean hasAds = false, hasInApp = false, searchedDeep = false;
                    if (nextLine[6].equals("true")) hasInApp = true;
                    if (nextLine[7].equals("true")) hasAds = true;
                    if (nextLine[9].equals("true")) searchedDeep = true;
                    Application app = new Application(nextLine[0], nextLine[1], genres ,nextLine[3],nextLine[4],nextLine[5],
                            hasInApp,hasAds,links,searchedDeep, nextLine[10],nextLine[11],nextLine[12],nextLine[13]);
                    Temp.add(app);
                } else break;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Loaded "+ Temp.size()+" entries.");
        return Temp;
    }





//    public static void main(String[] args) {
//        ArrayList<String> genres = new ArrayList<>();
//        genres.add("RACING");
//        genres.add("TEST");
//
//        ArrayList<String> links = new ArrayList<>();
//        links.add("test.com.org");
//
//        ArrayList<Application> apps = new ArrayList<>();
//        apps.add(new Application("1",
//                "Test1",
//                genres,
//                "DEVELOPER",
//                "565",
//                "898",
//                false,
//                true,
//                links,
//                true));
//
//        apps.add(new Application("2",
//                "Test2",
//                genres,
//                "DEVELOPER2",
//                "565",
//                "898",
//                false,
//                false,
//                links,
//                true));
//
//        saveToCSV(apps, CSV_FILE_NAME);
//    }

}
