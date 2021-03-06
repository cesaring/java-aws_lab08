/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package quotes;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import com.google.gson.Gson;
import java.io.Reader;
import java.nio.file.*;

import com.google.gson.reflect.TypeToken;
import quotes.app.QuoteOfTheDay;

public class App {
    public ArrayList<Quote> quotes = new ArrayList<Quote>();

    public void quotesFileToQuotesArray(String filename){
        //get json from file and convert to POJO:
        //note that it doesn't return an Array, but modifies
        //the public quotes array in the class.

        try {
            // create Gson instance
            Gson gson = new Gson();

            // create a reader
            Reader reader = Files.newBufferedReader(Paths.get(filename));


            //This line from googling gets the correct type for an ArrayList<Quote>
            Type QuoteListType = new TypeToken<ArrayList<Quote>>(){}.getType();

            // convert JSON file to ArrayList of Quote object
             quotes = gson.fromJson(reader, QuoteListType);


            // close reader
            reader.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public String getRandomQuote(ArrayList<Quote> _quoteArray){
        int randomIndex = (int) (Math.random() * _quoteArray.stream().count());

        return _quoteArray.get(randomIndex).toString();
    }

    //Populate from API
    public String apiToQuote() throws IOException, InterruptedException {
        String result = new String();

        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create("https://favqs.com/api/qotd"))
                .header("accept", "application/json")
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

   //     System.out.println("HERE:" + response.body());
        try {
            QuoteOfTheDay gotd = gson.fromJson(response.body(), QuoteOfTheDay.class);
            result = gotd.quote.body + "\n"+"--" + gotd.quote.author;

            //adding quote to quotes array
            Quote newquote = gotd.toQuote();
            quotes.add(newquote);

            //write out the new quotes array to file
            System.out.println("writing to file...");
            FileWriter fw = new FileWriter("./src/main/resources/recentquotes.json", StandardCharsets.UTF_8);

            gson.toJson(quotes, fw);
            fw.close();

        } catch (Exception e) {
           result = "";
        }
       // System.out.println("HERE TWO:" + gotd.quote.body);
        return result;
    }



    public String getGreeting(String localOrApi) throws IOException, InterruptedException {

        try {
            quotesFileToQuotesArray( "./src/main/resources/recentquotes.json");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        String greeting = getRandomQuote(quotes);
        String apiResult = apiToQuote();
        System.out.println(localOrApi);
        if (localOrApi.equals("api")) {
            if(apiResult.length()>1){
                greeting = apiResult;
            }
        }


        return greeting;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String parameterValue;

        if (args.length==0) {
            parameterValue = "";
        } else {
            parameterValue = args[0];
        }

        System.out.println("Quote Of The Day:");
        System.out.println(new App().getGreeting( parameterValue));

    }
}
