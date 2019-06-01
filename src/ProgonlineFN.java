
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;


import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ProgonlineFN {

    //Utiliser un User_AGENT pour que le serveur web crois que le robot est un web browser

    private static final String USER_AGENT =
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    private List<String> links = new LinkedList<String>();
    private Document htmlDocument;




    public boolean crawl(String Url) {

        try {

            Connection connection = Jsoup.connect(Url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if (connection.response().statusCode() == 200) // 200 est le code d'état HTTP OK indiquant que tout va bien
            {
                System.out.println("\n**En visite** Page Web reçue à " + Url);
            }
            if (!connection.response().contentType().contains("text/html")) {
                System.out.println("**Echec** Récupéré autre chose que HTML");
                return false;
            }
            Elements linksOnPage = htmlDocument.select("a[href]");
            System.out.println("Trouvé (" + linksOnPage.size() + ") links");
            for (Element link : linksOnPage) {
                this.links.add(link.absUrl("href"));
            }
            return true;
        } catch (IOException ioe) {
            return false;
        }
    }



    public boolean searchForWord(String searchWord) {
        //  Codage défensif. Cette méthode ne doit être utilisée qu'après un crawl réussie.
        if (this.htmlDocument == null) {
            System.out.println("ERREUR! Appelez crawl () avant d'effectuer l'analyse sur le document");
            return false;
        }
        System.out.println("À la recherche du mot " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }


    public List<String> getLinks() {
        return this.links;
    }


}
