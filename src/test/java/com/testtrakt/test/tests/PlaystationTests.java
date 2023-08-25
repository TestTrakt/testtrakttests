package com.testtrakt.test.tests;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.*;

import com.testtrakt.common.Element;
import com.testtrakt.common.HTMLElement;
import com.testtrakt.common.LocatorRequest;
import com.testtrakt.common.OptionsRequest;
import com.testtrakt.common.enums.LocatorType;
import com.testtrakt.common.exceptions.ElementNotFoundException;
import com.testtrakt.playstationautomator.PlaystationAutomator;
import com.testtrakt.playstationautomator.PlaystationControllerButton;
import com.testtrakt.playstationautomator.PlaystationControllerPress;
import com.testtrakt.playstationautomator.PlaystationStartRequest;

public class PlaystationTests {

	private static final String PLAYSTATION_IP_ADDRESS = "your_playstation_ip_address";
	private static final File APP_PACKAGE = new File("C:\\path\\or\\url\\to\\your\\playstation\\application.pkg");
	private static final String TITLE_ID = "title_id_of_application_to_install";

	private static final File TESSERACT_BIN = new File("path_to_tesseract_binary");
	private static final File TESSERACT_DATA_DIR = new File("path_to_tessdata");
	private static final String TESSERACT_LANGUAGE = "eng";
	private static final File FFMPEG_BIN = new File("path_to_ffmpeg");
	private static final String WEB_PROFILER_ID = "your_web_profiler_id";

	private static final String PLAYSTATION_AUTOMATOR_ADDRESS = "http://localhost:9014";

	PlaystationAutomator playstationAutomator = null;

	@AfterMethod (alwaysRun = true)
	public void afterTest() {
		if (playstationAutomator != null) {
			playstationAutomator.quit();
		}
	}

	@BeforeMethod (alwaysRun = true)
	public void beforeTest() throws Exception {
		
	}

	@Test(groups = { "Playstation" })
	public void findElementByTesseractTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// find an element
		List<Element> elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "text to find").withTimeout(40, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() > 90.0f);
		Assert.assertTrue(element.getX() > 700 && element.getX() < 750);
		Assert.assertTrue(element.getY() > 1500 && element.getY() < 1550);
		Assert.assertTrue(element.getWidth() > 180 && element.getWidth() < 220);
		Assert.assertTrue((element.getHeight() > 30 && element.getHeight() < 40));
		Assert.assertEquals(element.getText(), "text to find");
	
	}

	@Test(groups = { "Playstation" })
	public void findElementByImageTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// set an image element similary default
		playstationAutomator.options().set(new OptionsRequest().setDefaultImageFindSimilarity(.90));

		// get the sub screen image of our device to use as a locator for later
		File savedImage = playstationAutomator.display().getImage(0, 0, 200, 200);
		
		// find an element by filepath
		List<Element> elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.IMAGE, savedImage.getAbsolutePath()).withTimeout(10, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 200);
		Assert.assertTrue(element.getHeight() == 200);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertTrue(element.getText().contains("text in image"));

	}
	
	@Test(groups = { "Playstation" })
	public void findElementByCssTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
			.withWebProfilerID(WEB_PROFILER_ID);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// find an element by a css locator within your application
		List<Element> elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.CSS, "p[id='testtraktidentifier']").withTimeout(10, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "TestTrakt Application Web Profiler Test Site");

		// find all elements that match a css locator within your application
		elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.CSS, "p[id*='testtraktidentifier']").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "TestTrakt Application Web Profiler Test Site");

		// converting the element to an html element allows us to get attributes and css details on the element
		HTMLElement htmlElement = element.toHTMLElement();
		Assert.assertEquals(htmlElement.getAttribute("id"), "testtraktidentifier2");
		Assert.assertEquals(htmlElement.getCssValue("align-content"), "normal");
	}
	
	@Test(groups = { "Playstation" })
	public void findElementByXpathTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
			.withWebProfilerID(WEB_PROFILER_ID);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// find an element by ax xpath locator within your application
		List<Element> elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.XPATH, "//p[@id='testtraktidentifier']").withTimeout(10, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "TestTrakt Application Web Profiler Test Site");

		// find all elements that match an xpath locator within your application
		elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.XPATH, "//p[contains(@id, 'testtraktidentifier')]").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "TestTrakt Application Web Profiler Test Site");

		// converting the element to an html element allows us to get attributes and css details on the element
		HTMLElement htmlElement = element.toHTMLElement();
		Assert.assertEquals(htmlElement.getAttribute("id"), "testtraktidentifier2");
		Assert.assertEquals(htmlElement.getCssValue("align-content"), "normal");
	}
	
	@Test(groups = { "Playstation" })
	public void remoteControlInteractTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// find an element
		List<Element> elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "text to find").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertTrue(elements.size() > 0);

		// perform remote control operations against the device
		playstationAutomator.controller().sendCommand(new PlaystationControllerPress(PlaystationControllerButton.DOWN_ARROW));
		playstationAutomator.controller().sendCommand(new PlaystationControllerPress(PlaystationControllerButton.DOWN_ARROW));
		playstationAutomator.controller().sendCommand(new PlaystationControllerPress(PlaystationControllerButton.X));
		
		elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "text to find 2").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertTrue(elements.size() > 0);

		playstationAutomator.controller().sendCommand(new PlaystationControllerPress(PlaystationControllerButton.CIRCLE));
		playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "text to find 3").findAll(false).withTimeout(45, TimeUnit.SECONDS));

		playstationAutomator.controller().sendCommand(new PlaystationControllerPress(PlaystationControllerButton.UP_ARROW));
		playstationAutomator.controller().sendCommand(new PlaystationControllerPress(PlaystationControllerButton.X));

		playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "text to find 4").findAll(false).withTimeout(45, TimeUnit.SECONDS));
	}
	
	@Test(groups = { "Playstation" })
	public void webProfilerTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
			.withWebProfilerID(WEB_PROFILER_ID);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// find an element
		playstationAutomator.locator().locate(new LocatorRequest(LocatorType.XPATH, "//p").withTimeout(45, TimeUnit.SECONDS));
		
		// gets our application page source
		String pageSource = playstationAutomator.webProfiler().getPageSource();
		Assert.assertTrue(pageSource.contains("TestTrakt"));

		// gets the browser console logs of the webkit browser running our application
		String consoleLogs = playstationAutomator.webProfiler().getConsoleLogs();
		Assert.assertTrue(consoleLogs.contains("console log entry"));

		// execute javascript against our application
		String scriptResult = playstationAutomator.webProfiler().executeScript("document.documentElement.innerHTML");
		Assert.assertTrue(scriptResult.contains("<p>"));
	}
	
	@Test(groups = { "Playstation" })
	public void elementTimeoutTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		ElementNotFoundException elementNotFoundException = null;
		long startTime = System.currentTimeMillis();
		try {
			playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere").withTimeout(10, TimeUnit.SECONDS));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertTrue(elementNotFoundException != null);
		long endTime = System.currentTimeMillis();
		long timeDiffSec = (endTime - startTime) / 1000;
		Assert.assertTrue(timeDiffSec >= 10 && timeDiffSec <= 12);

		elementNotFoundException = null;
		startTime = System.currentTimeMillis();
		try {
			playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere"));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertTrue(elementNotFoundException != null);
		endTime = System.currentTimeMillis();
		timeDiffSec = (endTime - startTime) / 1000;
		Assert.assertTrue(timeDiffSec >= 0 && timeDiffSec <= 2);

		playstationAutomator.options().set(new OptionsRequest().setDefaultElementWait(10, TimeUnit.SECONDS));
		elementNotFoundException = null;
		startTime = System.currentTimeMillis();
		try {
			playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere"));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertTrue(elementNotFoundException != null);
		endTime = System.currentTimeMillis();
		timeDiffSec = (endTime - startTime) / 1000;
		Assert.assertTrue(timeDiffSec >= 10 && timeDiffSec <= 12);

		elementNotFoundException = null;
		startTime = System.currentTimeMillis();
		try {
			playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere").withTimeout(15, TimeUnit.SECONDS));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertTrue(elementNotFoundException != null);
		endTime = System.currentTimeMillis();
		timeDiffSec = (endTime - startTime) / 1000;
		Assert.assertTrue(timeDiffSec >= 15 && timeDiffSec <= 18);

		elementNotFoundException = null;
		startTime = System.currentTimeMillis();
		try {
			List<Element> elements = playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere").withTimeout(5, TimeUnit.SECONDS).findAll(true));
			Assert.assertTrue(elements.isEmpty());
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertTrue(elementNotFoundException == null);
		endTime = System.currentTimeMillis();
		timeDiffSec = (endTime - startTime) / 1000;
		Assert.assertTrue(timeDiffSec >= 5 && timeDiffSec <= 8);
	}
	
	@Test(groups = { "Playstation" })
	public void screenTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "text to find").withTimeout(45, TimeUnit.SECONDS));

		// get the image/sub image from our application
		Assert.assertTrue(playstationAutomator.display().getImage().exists());
		Assert.assertTrue(playstationAutomator.display().getImage(400, 300, 300, 300).exists());
		
		// get the resolution width/height
		Assert.assertEquals(playstationAutomator.display().getResolution().getWidth(), 3840.0);
		Assert.assertEquals(playstationAutomator.display().getResolution().getHeight(), 2160.0);

		// get a compiled video recording of our application from test start to now
		File recording = playstationAutomator.display().getRecording();
		Assert.assertTrue(recording.exists());
		System.out.println("Video saved to: " + recording.getAbsolutePath());
	}

	@Test(groups = { "Playstation" })
	public void withCustomProsperoBinLocationTest() throws Exception {
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
			.withProsperoBinDirectoryPath("/custom/path/to/prosper/binary");

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// find an element
		playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "locator").withTimeout(45, TimeUnit.SECONDS));
	
	}

	@Test(groups = { "Playstation" })
	public void withAPKAtUrlTest() throws Exception {
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(new URI("public url to .pkg application package"), TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// find an element
		playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "text to find").withTimeout(45, TimeUnit.SECONDS));
	
	}

	@Test(groups = { "Playstation" })
	public void getConsoleLogsTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		PlaystationStartRequest playstationStartRequest = new PlaystationStartRequest(PLAYSTATION_IP_ADDRESS)
			.withAppPackage(APP_PACKAGE, TITLE_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our playstation session
		playstationAutomator = new PlaystationAutomator(new URI(PLAYSTATION_AUTOMATOR_ADDRESS), playstationStartRequest);

		// find an element
		playstationAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "text to find").withTimeout(40, TimeUnit.SECONDS));
		
		// get the console logs from the test start until now
		String logs = playstationAutomator.system().getConsoleLogs();
		Assert.assertTrue(logs.contains("log entry"));

		playstationAutomator.system().clearConsoleLogs();
	
	}
	
}