package es.codeurjc.daw.e2e.selenium;

import static java.lang.String.format;
import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import es.codeurjc.daw.Application;
import es.codeurjc.daw.e2e.selenium.pages.BlogIndexPage;
import io.github.bonigarcia.wdm.WebDriverManager;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SeleniumTest {

	@LocalServerPort
    int port;

	private WebDriver driver;
	private WebDriverWait wait;

	@BeforeAll
	public static void setupClass() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void setupTest() {
		driver = new ChromeDriver();
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
	public void createPostTest() throws Exception{

		BlogIndexPage blog = new BlogIndexPage(driver, port);

		String title = "Mi titulo";
		String content = "Mi contenido";

		blog
			// ENTRAMOS AL BLOG Y NAVEGAMOS AL FORMULARIO
			.get()
			.goToCreatePostPage()
			// CREAMOS UN NUEVO POST
			.fillForm(title, content)
			.submitForm()
			// COMPROBAMOS QUE LA PÁGINA DE RESPUESTA ES CORRECTA
			.assertPostSuccessfullyCreated(title, content)
			// COMPROBAMOS QUE EXISTE EN LA PÁGINA PRINCIPAL
			.goToBlogIndexPage()
			.assertPostExist(title)
			;
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
			.get()
			.goToCreatePostPage()
			// CREAMOS UN NUEVO POST
			.fillForm(title, content)
			.submitForm()
			// COMPROBAMOS QUE LA PÁGINA DE RESPUESTA ES CORRECTA
			.assertPostSuccessfullyCreated(title, content)
			// CREAMOS UN NUEVO COMENTARIO
			.fillCommentForm(author, message)
			.submitCommentForm()
			// COMPROBAMOS QUE SE HA CREADO EL COMENTARIO
			.assertCommentSuccessfullyCreated(author, message)
			;		
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
			.get()
			.goToCreatePostPage()
			// CREAMOS UN NUEVO POST
			.fillForm(title, content)
			.submitForm()
			// COMPROBAMOS QUE LA PÁGINA DE RESPUESTA ES CORRECTA
			.assertPostSuccessfullyCreated(title, content)
			// CREAMOS UN NUEVO COMENTARIO
			.fillCommentForm(author, message)
			.submitCommentForm()
			// COMPROBAMOS QUE SE HA CREADO EL COMENTARIO
			.assertCommentSuccessfullyCreated(author, message)
			// BORRAMOS EL COMENTARIO
			.deletComment()
			// COMPROBAMOS QUE YA NO EXISTE
			.assertCommentSuccessfullyDeleted(author, message)
			;	

	}

}