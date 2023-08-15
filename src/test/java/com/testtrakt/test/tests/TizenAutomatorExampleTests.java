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
import com.testtrakt.tizenautomator.TizenAutomator;
import com.testtrakt.tizenautomator.TizenControllerButton;
import com.testtrakt.tizenautomator.TizenControllerPress;
import com.testtrakt.tizenautomator.TizenStartRequest;

public class TizenAutomatorExampleTests {

	private static final String TIZEN_IP_ADDRESS = "{YOUR_TIZEN_TV_IP_ADDRESS}";
	private static final File APP_PACKAGE = new File("/ABSOLUTE/PATH/TO/YOUR/TIZEN/WGT/APP/PACKAGE");
	private static final String REMOTE_API_TOKEN = "{YOUR_TIZEN_REMOTE_API_TOKEN}";

	private static final File TESSERACT_BIN = new File("{ABSOLUTE/PATH/TO/TESSERACT/BINARY}");
	private static final File TESSERACT_DATA_DIR = new File("/ABSOLUTE/PATH/TO/TESSERACT/TESSDATADIR");
	private static final String TESSERACT_LANGUAGE = "eng";
	private static final File FFMPEG_BIN = new File("{/ABSOLUTE/PATH/TO/FFMPEG/BINARY}");
	private static final String WEB_PROFILER_ID = "{YOUR_WEB_PROFILE_TOKEN}";
	private static final File GOOGLE_VISION_OCR = new File("/ABSOLUTE/PATH/TO/GOOGLE/JSON");
	private static final File REKOGNITION_OCR = new File("/ABSOLUTE/PATH/TO/AWS/CREDENTIALS");

	private static final String TIZEN_AUTOMATOR_ADDRESS = "http://localhost:9013";

	TizenAutomator tizenAutomator = null;

	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		if (tizenAutomator != null) {
			tizenAutomator.quit();
		}
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeTest() throws Exception {

	}

	@Test(groups = { "Tizen" })
	public void findElementByTesseractTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element
		List<Element> elements = tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(40, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() > 90.0f);
		Assert.assertTrue(element.getX() > 10 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 170 && element.getY() < 180);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 45));
		Assert.assertEquals(element.getText(), "ElementTextToFind");

	}

	@Test(groups = { "Tizen" })
	public void findElementByGoogleVisionTest() throws Exception {

		// create a start request with an app url that is a publicly available url
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withGoogleVisionOCR(GOOGLE_VISION_OCR);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element
		List<Element> elements = tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() == 0.0);
		Assert.assertTrue(element.getX() > 8 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 170 && element.getY() < 180);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 50));
		Assert.assertEquals(element.getText(), "ElementTextToFind");

		// find multiple elements
		elements = tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() == 0.0);
		Assert.assertTrue(element.getX() > 8 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 320 && element.getY() < 325);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 55));
		Assert.assertEquals(element.getText(), "ElementTextToFind");
	}

	@Test(groups = { "Tizen" })
	public void findElementByRekognitionTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withAmazonRekognitionOCR(REKOGNITION_OCR);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element
		List<Element> elements = tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getX() > 8 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 170 && element.getY() < 180);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 50));
		Assert.assertEquals(element.getText(), "ElementTextToFind");

		// find multiple elements
		elements = tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getX() > 8 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 320 && element.getY() < 325);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 55));
		Assert.assertEquals(element.getText(), "ElementTextToFind");
	}

	@Test(groups = { "Tizen" })
	public void findElementByImageTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// set an image element similary default
		tizenAutomator.options().set(new OptionsRequest().setDefaultImageFindSimilarity(.90));

		// get the sub screen image of our device to use as a locator for later
		File savedImage = tizenAutomator.display().getImage(0, 0, 200, 200);

		// find an element by filepath
		List<Element> elements = tizenAutomator.locator().locate(
				new LocatorRequest(LocatorType.IMAGE, savedImage.getAbsolutePath()).withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 200);
		Assert.assertTrue(element.getHeight() == 200);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertTrue(element.getText().contains("Offline"));

		// an image element that's NOT findAll and NOT found should return an empty
		// collection
		ElementNotFoundException elementNotFoundException = null;
		try {
			tizenAutomator.locator()
					.locate(new LocatorRequest(LocatorType.IMAGE,
							"https://testtraktresources.s3.amazonaws.com/TestTraktLogo.png")
							.withTimeout(10, TimeUnit.SECONDS));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertNotNull(elementNotFoundException);

	}

	@Test(groups = { "Tizen" })
	public void findElementByCssTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element by a css locator within your application
		List<Element> elements = tizenAutomator.locator().locate(
				new LocatorRequest(LocatorType.CSS, "p[id='testtraktidentifier']").withTimeout(10, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "ElementTextToFind");

		// find all elements that match a css locator within your application
		elements = tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.CSS, "p[id*='testtraktidentifier']").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "ElementTextToFind");

		// converting the element to an html element allows us to get attributes and css
		// details on the element
		HTMLElement htmlElement = element.toHTMLElement();
		Assert.assertEquals(htmlElement.getAttribute("id"), "testtraktidentifier2");
		Assert.assertEquals(htmlElement.getCssValue("align-content"), "normal");
	}

	@Test(groups = { "Tizen" })
	public void findElementByXpathTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element by ax xpath locator within your application
		List<Element> elements = tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.XPATH, "//p[@id='testtraktidentifier']").withTimeout(10,
						TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "ElementTextToFind");

		// find all elements that match an xpath locator within your application
		elements = tizenAutomator.locator().locate(
				new LocatorRequest(LocatorType.XPATH, "//p[contains(@id, 'testtraktidentifier')]").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "ElementTextToFind");

		// converting the element to an html element allows us to get attributes and css
		// details on the element
		HTMLElement htmlElement = element.toHTMLElement();
		Assert.assertEquals(htmlElement.getAttribute("id"), "testtraktidentifier2");
		Assert.assertEquals(htmlElement.getCssValue("align-content"), "normal");
	}

	@Test(groups = { "Tizen" })
	public void remoteControlInteractTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element
		List<Element> elements = tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertTrue(elements.size() > 0);

		// perform remote control operations against the device
		tizenAutomator.controller().sendCommand(new TizenControllerPress(TizenControllerButton.MENU));
		tizenAutomator.controller().sendCommand(new TizenControllerPress(TizenControllerButton.LEFT_ARROW));
		tizenAutomator.controller().sendCommand(new TizenControllerPress(TizenControllerButton.DOWN_ARROW));

	}

	@Test(groups = { "Tizen" })
	public void webProfilerTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element
		tizenAutomator.locator().locate(new LocatorRequest(LocatorType.XPATH, "//p").withTimeout(45, TimeUnit.SECONDS));

		// gets our application page source
		String pageSource = tizenAutomator.webProfiler().getPageSource();
		Assert.assertTrue(pageSource.contains("TestTrakt"));

		// gets the browser console logs of the webkit browser running our application
		String consoleLogs = tizenAutomator.webProfiler().getConsoleLogs();
		Assert.assertTrue(consoleLogs.contains("Request received from testtrakt server"));

		// execute javascript against our application
		String scriptResult = tizenAutomator.webProfiler().executeScript("document.documentElement.innerHTML");
		Assert.assertTrue(scriptResult.contains("<p>"));
	}

	@Test(groups = { "Tizen" })
	public void elementTimeoutTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		ElementNotFoundException elementNotFoundException = null;
		long startTime = System.currentTimeMillis();
		try {
			tizenAutomator.locator()
					.locate(new LocatorRequest(LocatorType.TEXT, "noelementhere").withTimeout(10, TimeUnit.SECONDS));
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
			tizenAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere"));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertTrue(elementNotFoundException != null);
		endTime = System.currentTimeMillis();
		timeDiffSec = (endTime - startTime) / 1000;
		Assert.assertTrue(timeDiffSec >= 0 && timeDiffSec <= 2);

		tizenAutomator.options().set(new OptionsRequest().setDefaultElementWait(10, TimeUnit.SECONDS));
		elementNotFoundException = null;
		startTime = System.currentTimeMillis();
		try {
			tizenAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere"));
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
			tizenAutomator.locator()
					.locate(new LocatorRequest(LocatorType.TEXT, "noelementhere").withTimeout(15, TimeUnit.SECONDS));
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
			List<Element> elements = tizenAutomator.locator()
					.locate(new LocatorRequest(LocatorType.TEXT, "noelementhere").withTimeout(5, TimeUnit.SECONDS)
							.findAll(true));
			Assert.assertTrue(elements.isEmpty());
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertTrue(elementNotFoundException == null);
		endTime = System.currentTimeMillis();
		timeDiffSec = (endTime - startTime) / 1000;
		Assert.assertTrue(timeDiffSec >= 5 && timeDiffSec <= 8);
	}

	@Test(groups = { "Tizen" })
	public void screenTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "TestTrakt").withTimeout(45, TimeUnit.SECONDS));

		// get the image/sub image from our application
		Assert.assertTrue(tizenAutomator.display().getImage().exists());
		Assert.assertTrue(tizenAutomator.display().getImage(400, 300, 300, 300).exists());

		// get the resolution width/height
		Assert.assertEquals(tizenAutomator.display().getResolution().getWidth(), 1920.0);
		Assert.assertEquals(tizenAutomator.display().getResolution().getHeight(), 823.0);

		// get a compiled video recording of our application from test start to now
		File recording = tizenAutomator.display().getRecording();
		Assert.assertTrue(recording.exists());
		System.out.println("Video saved to: " + recording.getAbsolutePath());
	}

	@Test(groups = { "Tizen" })
	public void withCustomProfileTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN)
				.withProfile("TestTraktCustomCertificateProfile"); // if you wish to use a custom certificate profile to
																	// install/launch your app

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element
		tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));

	}

	@Test(groups = { "Tizen" })
	public void withTizenAndSDBBinaryLocationTest() throws Exception {
		TizenStartRequest tizenStartRequest = new TizenStartRequest(TIZEN_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppPackage(APP_PACKAGE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN)
				.withTizenBinaryPath("/Users/baclark77/tizen-studio/tools/ide/bin/tizen")
				.withSDBBinaryPath("/Users/baclark77/tizen-studio/tools/sdb");

		// start our tizen session
		tizenAutomator = new TizenAutomator(new URI(TIZEN_AUTOMATOR_ADDRESS), tizenStartRequest);

		// find an element
		tizenAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));

	}

}