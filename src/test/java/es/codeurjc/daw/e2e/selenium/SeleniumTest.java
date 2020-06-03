package es.codeurjc.daw.e2e.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import es.codeurjc.daw.Application;
import es.codeurjc.daw.e2e.selenium.pages.BlogIndexPage;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class SeleniumTest {

	@LocalServerPort
	int port;
	// @Container
	// public GenericContainer hub = new GenericContainer<>("selenium/hub:3.141.59-20200525").withExposedPorts(4444);
	// @Container
	// public GenericContainer nodeChrome = new GenericContainer<>("selenium/node-chrome:3.141.59-20200525");
	// @Container
	// public GenericContainer nodeFirefox = new GenericContainer<>("selenium/node-firefox:3.141.59-20200525");

	private RemoteWebDriver driver;
	private WebDriverWait wait;

	@BeforeEach
	public void setupTest() throws MalformedURLException {
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		// capabilities.setCapability("version", "latest");
		// hub.dependsOn(nodeChrome,nodeFirefox);
		// String address = hub.getHost();
		capabilities.setCapability("network", true); // To enable network logs
		capabilities.setCapability("visual", true); // To enable step by step screenshot
		capabilities.setCapability("video", true); // To enable video recording
		capabilities.setCapability("console", true); // To capture console logs
		capabilities.setCapability("platform", Platform.LINUX);
		capabilities.setCapability("name", "Testing Selenium");
		driver = new RemoteWebDriver(new URL( "http://localhost:4444/wd/hub"), capabilities);
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

		BlogIndexPage blog = new BlogIndexPage(driver, port);

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

		BlogIndexPage blog = new BlogIndexPage(driver, port);

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

		BlogIndexPage blog = new BlogIndexPage(driver, port);

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