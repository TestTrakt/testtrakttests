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
import com.testtrakt.vizioautomator.VizioAutomator;
import com.testtrakt.vizioautomator.VizioControllerButton;
import com.testtrakt.vizioautomator.VizioControllerPress;
import com.testtrakt.vizioautomator.VizioStartRequest;
import com.testtrakt.vizioautomator.VizioSystemInformation;

public class VizioAutomatorExampleTests {

	private static final String VIZIO_IP_ADDRESS = "{YOUR_VIZIO_TV_IP_ADDRESS}";
	private static final String APP_URL = "{YOUR_VIZIO_APPLICATION_URL_UNDER_TEST}";
	private static final String REMOTE_API_TOKEN = "{YOUR_PAIRED_REMOTE_TOKEN}";

	private static final String TESSERACT_BIN = "{ABSOLUTE/PATH/TO/TESSERACT/BINARY}";
	private static final String TESSERACT_DATA_DIR = "/ABSOLUTE/PATH/TO/TESSERACT/TESSDATADIR";
	private static final String TESSERACT_LANGUAGE = "eng";
	private static final String FFMPEG_BIN = "{/ABSOLUTE/PATH/TO/FFMPEG/BINARY}";
	private static final String WEB_PROFILER_ID = "{YOUR_WEB_PROFILE_TOKEN}";
	private static final String GOOGLE_VISION_OCR = "/ABSOLUTE/PATH/TO/GOOGLE/JSON";
	private static final String REKOGNITION_OCR = "/ABSOLUTE/PATH/TO/AWS/CREDENTIALS";

	private static final String VIZIO_AUTOMATOR_ADDRESS = "http://localhost:9012";

	VizioAutomator vizioAutomator = null;

	@AfterMethod(alwaysRun = true)
	public void afterTest() {
		if (vizioAutomator != null) {
			vizioAutomator.quit();
		}
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeTest() throws Exception {

	}

	@Test(groups = { "VizioExample" })
	public void findElementByTesseractTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		// find an element
		List<Element> elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() > 90.0f);
		Assert.assertTrue(element.getX() > 10 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 15 && element.getY() < 20);
		Assert.assertTrue(element.getWidth() > 13 && element.getWidth() < 140);
		Assert.assertTrue((element.getHeight() > 10 && element.getHeight() < 20));
		Assert.assertEquals(element.getText(), "ElementTextToFind");

	}

	@Test(groups = { "VizioExample" })
	public void findElementByGoogleVisionTest() throws Exception {

		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withGoogleVisionOCR(GOOGLE_VISION_OCR)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		// find an element
		List<Element> elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() == 0.0);
		Assert.assertTrue(element.getX() > 10 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 15 && element.getY() < 20);
		Assert.assertTrue(element.getWidth() > 13 && element.getWidth() < 140);
		Assert.assertTrue((element.getHeight() > 15 && element.getHeight() < 25));
		Assert.assertEquals(element.getText(), "ElementTextToFind");

		// find multiple elements
		elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 0.0);
		Assert.assertTrue(element.getX() > 10 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 40 && element.getY() < 50);
		Assert.assertTrue(element.getWidth() > 13 && element.getWidth() < 140);
		Assert.assertTrue((element.getHeight() > 15 && element.getHeight() < 25));
		Assert.assertEquals(element.getText(), "ElementTextToFind");
	}

	@Test(groups = { "VizioExample" })
	public void findElementByRekognitionTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withAmazonRekognitionOCR(REKOGNITION_OCR)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		// find an element
		List<Element> elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() > 90.0);
		Assert.assertTrue(element.getX() > 10 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 15 && element.getY() < 20);
		Assert.assertTrue(element.getWidth() > 13 && element.getWidth() < 140);
		Assert.assertTrue((element.getHeight() > 15 && element.getHeight() < 25));
		Assert.assertEquals(element.getText(), "ElementTextToFind");

		// find multiple elements
		elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() > 90.0);
		Assert.assertTrue(element.getX() > 10 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 40 && element.getY() < 50);
		Assert.assertTrue(element.getWidth() > 13 && element.getWidth() < 140);
		Assert.assertTrue((element.getHeight() > 15 && element.getHeight() < 25));
		Assert.assertEquals(element.getText(), "ElementTextToFind");
	}

	@Test(groups = { "VizioExample" })
	public void findElementByImageTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withGoogleVisionOCR(GOOGLE_VISION_OCR);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		// set an image element similary default
		vizioAutomator.options().set(new OptionsRequest().setDefaultImageFindSimilarity(.90));

		// get the sub screen image of our device to use as a locator for later
		File savedImage = vizioAutomator.display().getImage(0, 0, 200, 200);

		// find an element by filepath
		List<Element> elements = vizioAutomator.locator().locate(
				new LocatorRequest(LocatorType.IMAGE, savedImage.getAbsolutePath()).withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 200);
		Assert.assertTrue(element.getHeight() == 200);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertTrue(element.getText().contains("TestTrakt Applicat\n"));

		// find an element by a url to a sub image
		elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.IMAGE,
						"https://testtraktresources.s3.amazonaws.com/TestTraktVizioSubLocator.png")
						.withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 200);
		Assert.assertTrue(element.getHeight() == 200);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertTrue(element.getText().contains("TestTrakt Applicat\n"));

		// an image element that's findAll and NOT found should return an empty
		// collection
		elements = vizioAutomator.locator().locate(
				new LocatorRequest(LocatorType.IMAGE, "https://testtraktresources.s3.amazonaws.com/TestTraktLogo.png")
						.findAll(true).withTimeout(10, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 0);

		// an image element that's NOT findAll and NOT found should return an empty
		// collection
		ElementNotFoundException elementNotFoundException = null;
		try {
			vizioAutomator.locator()
					.locate(new LocatorRequest(LocatorType.IMAGE,
							"https://testtraktresources.s3.amazonaws.com/TestTraktLogo.png")
							.withTimeout(10, TimeUnit.SECONDS));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertNotNull(elementNotFoundException);

	}

	@Test(groups = { "VizioExample" })
	public void findElementByCssTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		// find an element by a css locator within your application
		List<Element> elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.CSS, "span[id='testtraktidentifier']").withTimeout(10,
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

		// find all elements that match a css locator within your application
		elements = vizioAutomator.locator().locate(new LocatorRequest(LocatorType.CSS, "span").findAll(true));
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

	@Test(groups = { "VizioExample" })
	public void findElementByXpathTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		// find an element by ax xpath locator within your application
		List<Element> elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.XPATH, "//span[@id='testtraktidentifier']").withTimeout(10,
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
		elements = vizioAutomator.locator().locate(new LocatorRequest(LocatorType.XPATH, "//span").findAll(true));
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

	@Test(groups = { "VizioExample" })
	public void remoteControlInteractTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		// find an element
		List<Element> elements = vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "ElementTextToFind").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertTrue(elements.size() > 0);

		// perform remote control operations against the device
		vizioAutomator.controller().sendCommand(new VizioControllerPress(VizioControllerButton.MENU));
		vizioAutomator.controller().sendCommand(new VizioControllerPress(VizioControllerButton.LEFT_ARROW));
		vizioAutomator.controller().sendCommand(new VizioControllerPress(VizioControllerButton.DOWN_ARROW));

	}

	@Test(groups = { "VizioExample" })
	public void webProfilerTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		// find an element
		vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.XPATH, "//span").withTimeout(45, TimeUnit.SECONDS));

		// gets our application page source
		String pageSource = vizioAutomator.webProfiler().getPageSource();
		Assert.assertTrue(pageSource.contains("TestTrakt"));

		// gets the browser console logs of the webkit browser running our application
		String consoleLogs = vizioAutomator.webProfiler().getConsoleLogs();
		Assert.assertTrue(consoleLogs.contains("Request received from testtrakt server"));

		// execute javascript against our application
		String scriptResult = vizioAutomator.webProfiler().executeScript("document.documentElement.innerHTML");
		Assert.assertTrue(scriptResult.contains("<span"));
	}

	@Test(groups = { "VizioExample" })
	public void elementTimeoutTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		ElementNotFoundException elementNotFoundException = null;
		long startTime = System.currentTimeMillis();
		try {
			vizioAutomator.locator()
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
			vizioAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere"));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertTrue(elementNotFoundException != null);
		endTime = System.currentTimeMillis();
		timeDiffSec = (endTime - startTime) / 1000;
		Assert.assertTrue(timeDiffSec >= 0 && timeDiffSec <= 2);

		vizioAutomator.options().set(new OptionsRequest().setDefaultElementWait(10, TimeUnit.SECONDS));
		elementNotFoundException = null;
		startTime = System.currentTimeMillis();
		try {
			vizioAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "noelementhere"));
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
			vizioAutomator.locator()
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
			List<Element> elements = vizioAutomator.locator()
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

	@Test(groups = { "VizioExample" })
	public void systemInfoTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		VizioSystemInformation info = vizioAutomator.system().getSystemInfo();
		Assert.assertEquals(info.getModelName(), "D24f-G1");

	}

	@Test(groups = { "VizioExample" })
	public void screenTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withAppURL(APP_URL)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

		vizioAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, "TestTrakt").withTimeout(45, TimeUnit.SECONDS));

		// get the image/sub image from our application
		Assert.assertTrue(vizioAutomator.display().getImage().exists());
		Assert.assertTrue(vizioAutomator.display().getImage(400, 300, 300, 300).exists());

		// get the resolution width/height
		Assert.assertEquals(vizioAutomator.display().getResolution().getWidth(), 1920.0);
		Assert.assertEquals(vizioAutomator.display().getResolution().getHeight(), 1080.0);

		// get a compiled video recording of our application from test start to now
		File recording = vizioAutomator.display().getRecording();
		Assert.assertTrue(recording.exists());
		System.out.println("Video saved to: " + recording.getAbsolutePath());
	}

	@Test(groups = { "VizioExample" })
	public void launchPreinstalledAppTest() throws Exception {
		VizioStartRequest vizioStartRequest = new VizioStartRequest(VIZIO_IP_ADDRESS)
				.withWebProfilerID(WEB_PROFILER_ID)
				.withRemoteAPIToken(REMOTE_API_TOKEN)
				.withApp("{APP_ID}", "{APP_NAMESPACE}"); // Indicates we want to launch an already installed application
															// on the device

		// start our vizio session
		vizioAutomator = new VizioAutomator(new URI(VIZIO_AUTOMATOR_ADDRESS), vizioStartRequest);

	}

}
