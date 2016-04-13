package mage2paradise

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class General extends Simulation {
    val customerLogin = "roni_cost@example.com"
    val custoperPassword = "roni_cost@example.com"

    val httpProtocol = http
        .baseURL("http://dev.mage2.com")
        .inferHtmlResources(BlackList(""".*css\?.*""", """.*\.js\?.*""", """.*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.(t|o)tf""", """.*\.png""",""".*\.svg"""), WhiteList())
        .acceptHeader("image/png,image/*;q=0.8,*/*;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .acceptLanguageHeader("en-US,en;q=0.5")
        .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:42.0) Gecko/20100101 Firefox/42.0")

    val standardHeader = Map("Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    val postHeader = Map(
        "Keep-Alive" -> "115",
        "Content-Type" -> "application/x-www-form-urlencoded",
        "X-Requested-With" -> "XMLHttpRequest"
    );
    val baseUrl = "http://dev.mage2.com/"

    val scn = scenario("Test catalog pages performance")
        .exec(http("Home page")
            .get(baseUrl + "/")
            .headers(standardHeader)
            )

        .pause(10)

        .exec(http("Women Pants category")
            .get(baseUrl + "women/bottoms-women/pants-women.html")
            .headers(standardHeader)
            )

        .pause(10)

        .exec(http("Overnight Duffle product")
            .get(baseUrl + "overnight-duffle.html")
            .headers(standardHeader)
            .check(
                regex("""(?<=form_key" type="hidden" value=")([a-zA-Z0-9]*)(?=")""")
                .saveAs("addCartFormKey")
                )
            )

        .pause(10)

        .exec(http("Add to cart")            
            .post(baseUrl + "checkout/cart/add/uenc/aHR0cDovL2Rldi5tYWdlMi5jb206MTAwNS9vdmVybmlnaHQtZHVmZmxlLmh0bWw,/product/13")
            .headers(postHeader)
            .formParam("product", "13")
            .formParam("selected_configurable_option", "")
            .formParam("related_product", "")
            .formParam("form_key", "${addCartFormKey}")
            .formParam("qty", "1")            
            )

        .pause(10)

        .exec(http("Search for 'band'")
            .get(baseUrl + "catalogsearch/result/?q=band")
            .headers(standardHeader)
            )

        .pause(10)

        .exec(http("User log in page")
            .get(baseUrl + "customer/account/login/")
            .check(
                regex("""(?<=form_key" type="hidden" value=")([a-zA-Z0-9]*)(?=")""")
                .saveAs("loginFormKey")
                )
            )

        .pause(10)

        .exec(http("User log in process")
            .post(baseUrl + "customer/account/loginPost/")
            .headers(postHeader)
            .formParam("form_key", "${loginFormKey}")
            .formParam("login[username]", customerLogin)
            .formParam("login[password]", custoperPassword)
            .formParam("send", "")
            )

    setUp(scn.inject(nothingFor(1 seconds),
            atOnceUsers(20)
            )).protocols(httpProtocol)
}