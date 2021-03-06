package es.codeurjc.daw.e2e.selenium.pages;

import static java.lang.String.format;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testcontainers.containers.BrowserWebDriverContainer;

public abstract class Page {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected int port;
    protected BrowserWebDriverContainer container;

    public Page(WebDriver driver, int port, BrowserWebDriverContainer container) {
        this.driver = driver;
        this.port = port;
        this.container = container;
        this.wait = new WebDriverWait(driver, 10);
    }

    public Page(Page page) {
        this.driver = page.driver;
        this.port = page.port;
        this.wait = new WebDriverWait(driver, 10);
    }

    protected void get(String path) {
        String ip = null;
        try {
            InetAddress address = InetAddress.getLocalHost();
            ip = address.getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.get("http://" + ip + ":" + this.port + path);

    }

    protected boolean isElementPresent(String text) {
        try {
            findElementWithText(text);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException e) {
            return false;
        }
    }

    protected void waitForXPathElement(String xpath) {
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
    }

    protected WebElement findElementWithText(String text) {
        return driver.findElement(getConditionForText(text));
    }

    protected List<WebElement> findElementsWithText(String text) {
        return driver.findElements(getConditionForText(text));
    }

    protected By getConditionForText(String text) {
        return By.xpath(format("//*[text()='%s']", text));
    }

}
