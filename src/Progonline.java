
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Progonline {

    private static final int MAX_PAGES_TO_SEARCH = 30;
    private Set<String> pagesVisited = new HashSet<String>();
    private List<String> pagesToVisit = new LinkedList<String>();

    private final WebClient WEB_CLIENT = new WebClient(BrowserVersion.CHROME);



    private final String login;
    private final  String password;

    // Notre constructeur pour fixer le username et le passeword et la configuration des quelques clients
    Progonline (String username, String password){
        this.login= username;
        this.password = password;

        WEB_CLIENT.getCookieManager().setCookiesEnabled(true);
    }


    public void login(){


        String baseUrl = "https://www.progonline.com" ;
        String loginUrl = baseUrl + "/visitor_mypage.php?quoi=deconnexion" ;
        String login = "yosraa";
        String password = "yosra1234yosra" ;

        try {
            System.out.println("Starting autoLogin on " + loginUrl);
            WebClient client = autoLogin(loginUrl, login, password);
            HtmlPage page = client.getPage(baseUrl) ;

            HtmlAnchor logoutLink = page.getFirstByXPath(String.format("//a[@href='mypage.php?quoi=deconnect']", login)) ;
            if(logoutLink != null ){
                System.out.println("Connecté avec succès!");
                // imprimer les cookies
                for(Cookie cookie : client.getCookieManager().getCookies()){
                    System.out.println(cookie.toString());
                }

            }else{
                System.err.println("Mauvaises informations d'identification");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public String get(String URL){
        try {

            return WEB_CLIENT.getPage(URL).getWebResponse().getContentAsString();
        } catch (FailingHttpStatusCodeException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static WebClient autoLogin(String loginUrl, String login, String password) throws FailingHttpStatusCodeException, MalformedURLException, IOException{
        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);

        HtmlPage page = client.getPage(loginUrl);

        HtmlInput inputPassword = page.getFirstByXPath("//input[@type='password']");
        //The first preceding input that is not hidden
        HtmlInput inputLogin = inputPassword.getFirstByXPath(".//preceding::input[not(@type='hidden')]");

        inputLogin.setValueAttribute(login);
        inputPassword.setValueAttribute(password);

        //get the enclosing form
        HtmlForm loginForm = inputPassword.getEnclosingForm() ;

        //submit the form
        page = client.getPage(loginForm.getWebRequest(null));

        //returns the cookies filled client :)
        return client;
    }





    public void search(String url, String searchWord) {


        while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
            String currentUrl;
            ProgonlineFN sear = new ProgonlineFN();
            if (this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            } else {
                currentUrl = this.nextUrl();
            }
            sear.crawl(currentUrl);

            boolean success = sear.searchForWord(searchWord);
            if (success) {
                System.out.println(String.format("** Succès ** le mot %s trouvé at %s", searchWord, currentUrl));
                break;
            }
            this.pagesToVisit.addAll(sear.getLinks());
        }
        System.out.println("\n**Terminé** Visité " + this.pagesVisited.size() + " pages webs)");
    }


    /**
     * Pour retourner le suivant URL à visiter and faire le check pour étre
     * sur que cette méthode ne retourne pas URL qui est deja visté
     * @return
     */
    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        } while (this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }
}