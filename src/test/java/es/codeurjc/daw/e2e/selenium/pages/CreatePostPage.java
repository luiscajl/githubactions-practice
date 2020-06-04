package es.codeurjc.daw.e2e.selenium.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testcontainers.containers.BrowserWebDriverContainer;

public class CreatePostPage extends Page {

    public CreatePostPage(WebDriver driver, int port,BrowserWebDriverContainer container) {
        super(driver, port,container);
    }

    public CreatePostPage(Page page) {
        super(page);
    }

    public CreatePostPage fillForm(final String title, final String content){
        driver.findElement(By.name("title")).sendKeys(title);
        driver.findElement(By.name("content")).sendKeys(content);
        return this;
    }

    public PostPage submitForm(){
        driver.findElement(By.tagName("form")).submit();
        return new PostPage(this);
    }

    

    

}