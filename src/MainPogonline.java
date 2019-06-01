
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;


public class MainPogonline {

    public static void main(String[] args)
    {

        try {


            Progonline prog = new Progonline("yosraa","yosra1234yosra");
            prog.login();
            prog.search("https://www.progonline.com/mypage.php?quoi=my_demandes4","Votre préséléction");



        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}