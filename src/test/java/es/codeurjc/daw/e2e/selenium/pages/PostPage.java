package es.codeurjc.daw.e2e.selenium.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PostPage extends Page {

    public PostPage(WebDriver driver, int port) {
        super(driver, port);
    }

    public PostPage(Page page) {
        super(page);
    }

    public PostPage assertPostSuccessfullyCreated(String title, String content) {
        wait.until(ExpectedConditions.textToBe(By.id("postTitle"), title));
        wait.until(ExpectedConditions.textToBe(By.id("postContent"), content));
        return this;
    }

    public PostPage fillCommentForm(String author, String message) {
        driver.findElement(By.name("author")).sendKeys(author);
		driver.findElement(By.name("message")).sendKeys(message);
        return this;
    }

    public PostPage submitCommentForm(){
        driver.findElement(By.id("newCommentForm")).submit();
        return new PostPage(this);
    }

    public PostPage deletComment(){
        driver.findElement(By.id("deleteCommentForm")).submit();
        return new PostPage(this);
    }

    public PostPage assertCommentSuccessfullyDeleted(String author, String message) {
        String fullComment = author+": "+message;
		wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.tagName("li"), fullComment)));
        return this;
    } 

    public PostPage assertCommentSuccessfullyCreated(String author, String message) {
        String fullComment = author+": "+message;
		wait.until(ExpectedConditions.textToBe(By.tagName("li"), fullComment));
        return this;
    } 

    public BlogIndexPage goToBlogIndexPage() {
        wait.withTimeout(Duration.ofSeconds(10));
        this.findElementWithText(" Atrás ").click();
        return new BlogIndexPage(this);
    }

}
