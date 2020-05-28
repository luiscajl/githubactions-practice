package es.codeurjc.daw.e2e.selenium.pages;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.openqa.selenium.WebDriver;

public class BlogIndexPage extends Page {

    public BlogIndexPage(WebDriver driver, int port) {
        super(driver, port);
    }

    public BlogIndexPage(Page page) {
        super(page);
    }

    public BlogIndexPage get() {
        this.get("/");
        this.waitForXPathElement("/html/body/a[text()='Nuevo post']");
        return this;
    }

    public CreatePostPage goToCreatePostPage() {
        this.findElementWithText("Nuevo post").click();
        this.waitForXPathElement("/html/body/h1[text()='New post']");
        return new CreatePostPage(this);
    }

    public BlogIndexPage assertPostExist(String title) {
        assertNotNull(this.findElementWithText(title));
        return this;
    }

}