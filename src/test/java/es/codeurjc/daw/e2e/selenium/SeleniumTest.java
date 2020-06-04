package es.codeurjc.daw.e2e.selenium;

import java.net.MalformedURLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.containers.BrowserWebDriverContainer;
import org.testcontainers.junit.jupiter.Container;

import es.codeurjc.daw.Application;
import es.codeurjc.daw.e2e.selenium.pages.BlogIndexPage;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @Testcontainers
public class SeleniumTest {

	@LocalServerPort
	int port;

	@Container
	BrowserWebDriverContainer chrome = new BrowserWebDriverContainer().withCapabilities(new ChromeOptions());

	private RemoteWebDriver driver;
	private WebDriverWait wait;

	@BeforeEach
	public void setupTest() throws MalformedURLException {
		chrome.start();
		driver = chrome.getWebDriver();
		wait = new WebDriverWait(driver, 10);
	}

	@AfterEach
	public void teardown() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	@DisplayName("Crear un post y verificar que se crea correctamente")
	public void createPostTest() throws Exception {

		BlogIndexPage blog = new BlogIndexPage(driver, port, chrome);

		String title = "Mi titulo";
		String content = "Mi contenido";

		blog
				// ENTRAMOS AL BLOG Y NAVEGAMOS AL FORMULARIO
				.get().goToCreatePostPage()
				// CREAMOS UN NUEVO POST
				.fillForm(title, content).submitForm()
				// COMPROBAMOS QUE LA PÁGINA DE RESPUESTA ES CORRECTA
				.assertPostSuccessfullyCreated(title, content)
				// COMPROBAMOS QUE EXISTE EN LA PÁGINA PRINCIPAL
				.goToBlogIndexPage().assertPostExist(title);
	}

	@Test
	@DisplayName("Añadir un comentario a un post y verificar que se añade el comentario")
	public void createCommentTest() throws Exception {

		BlogIndexPage blog = new BlogIndexPage(driver, port, chrome);

		String title = "Mi titulo";
		String content = "Mi contenido";

		String author = "Juan";
		String message = "Buen comentario";

		blog
				// ENTRAMOS AL BLOG Y NAVEGAMOS AL FORMULARIO
				.get().goToCreatePostPage()
				// CREAMOS UN NUEVO POST
				.fillForm(title, content).submitForm()
				// COMPROBAMOS QUE LA PÁGINA DE RESPUESTA ES CORRECTA
				.assertPostSuccessfullyCreated(title, content)
				// CREAMOS UN NUEVO COMENTARIO
				.fillCommentForm(author, message).submitCommentForm()
				// COMPROBAMOS QUE SE HA CREADO EL COMENTARIO
				.assertCommentSuccessfullyCreated(author, message);
	}

	@Test
	@DisplayName("Borrar un comentario de un post y verificar que no aparece el comentario")
	public void deleteCommentTest() throws Exception {

		BlogIndexPage blog = new BlogIndexPage(driver, port, chrome);

		String title = "Mi titulo";
		String content = "Mi contenido";

		String author = "Juan";
		String message = "Buen comentario";

		blog
				// ENTRAMOS AL BLOG Y NAVEGAMOS AL FORMULARIO
				.get().goToCreatePostPage()
				// CREAMOS UN NUEVO POST
				.fillForm(title, content).submitForm()
				// COMPROBAMOS QUE LA PÁGINA DE RESPUESTA ES CORRECTA
				.assertPostSuccessfullyCreated(title, content)
				// CREAMOS UN NUEVO COMENTARIO
				.fillCommentForm(author, message).submitCommentForm()
				// COMPROBAMOS QUE SE HA CREADO EL COMENTARIO
				.assertCommentSuccessfullyCreated(author, message)
				// BORRAMOS EL COMENTARIO
				.deletComment()
				// COMPROBAMOS QUE YA NO EXISTE
				.assertCommentSuccessfullyDeleted(author, message);

	}

}