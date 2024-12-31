import com.microsoft.playwright.*;
import com.microsoft.playwright.options.WaitForSelectorState;

import java.awt.*;
import java.nio.file.Paths;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class TrendYolUrunAramaSepeteEkleme {

    public static void main(String[] args) {

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) dimension.getWidth();
        int height = (int) dimension.getHeight();

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(120));
            Page page = browser.newPage();
            page.navigate("https://www.trendyol.com/");
            page.setViewportSize(width,height);
            System.out.println(page.title());

            // Expect a title "to contain" a substring.
            assertThat(page).hasTitle(Pattern.compile("En Trend Ürünler Türkiye'nin Online Alışveriş Sitesi Trendyol'da"));
            assertThat(page).hasTitle("En Trend Ürünler Türkiye'nin Online Alışveriş Sitesi Trendyol'da");


            playwright.selectors().setTestIdAttribute("id");
            assertThat(page.getByTestId("Rating-Review")).isVisible();
            page.getByTestId("Rating-Review").click();

            Locator lc = page.locator("p", new Page.LocatorOptions().setHasText("Giriş Yap"));
            assertThat(lc).isVisible();
            lc.hover();
            lc = page.locator(".login-button");
            lc.click();

            String emailID = "#login-email", password = "#login-password-input", menuTitle ="Fiyat";
            page.locator(emailID).fill("emrecnnn5@gmail.com");
            page.locator(password).fill("TestTest1234*");
            assertThat(page.locator(".q-primary.q-fluid.q-button-medium.q-button.submit")).isVisible();
            page.locator(".q-primary.q-fluid.q-button-medium.q-button.submit").click();
            page.locator(".q-loader-parent").waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.DETACHED));
            playwright.selectors().setTestIdAttribute("data-testid");
            page.getByTestId("suggestion").fill("Oyuncu Bilgisayarı");
            page.getByTestId("search-icon").click();
            page.getByPlaceholder("Marka ara").fill("Monster");
            page.locator("//div[@data-title='Marka']//..//div[@class='chckbox']").click();
            Locator element = page.locator("//div[text()='" + menuTitle + "']");
            element.evaluate("element => element.scrollIntoView()");
            element.click();
            page.getByPlaceholder("En Az").fill("3000");
            page.getByPlaceholder("En Çok").fill("10000");
            page.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshot.png")));

            //Yeni sekmeye geçme
            Page newPage = page.context().waitForPage(() -> {
                page.locator(".prdct-desc-cntnr-ttl").filter(new Locator.FilterOptions().setHasText("Monster")).first().click();; // Opens a new tab
            });
            // Interact with the new page normally
            newPage.waitForLoadState();
            newPage.setViewportSize(width,height);
            System.out.println(newPage.title());

            newPage.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshot2.png")));



//            page.screenshot(new Page.ScreenshotOptions()
//                    .setPath(Paths.get("screenshot3.png")));


            if (newPage.locator("text='Anladım'").isVisible()) newPage.locator("text='Anladım'").click();

            newPage.getByText("Sepete Ekle").first().click();
            newPage.getByText("Sepetim").click();
            newPage.locator("text='Anladım'").waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.ATTACHED));
            if (newPage.locator("text='Anladım'").isVisible()) newPage.locator("text='Anladım'").click();
            newPage.getByText("Sil").first().click();
            newPage.screenshot(new Page.ScreenshotOptions()
                    .setPath(Paths.get("screenshot4.png")));
            newPage.close();
            //ilk sekmeyi öne getirir
             page.bringToFront();
        }
    }
}
