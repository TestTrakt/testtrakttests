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
import com.testtrakt.skytvautomator.SkyTVAutomator;
import com.testtrakt.skytvautomator.SkyTVControllerButton;
import com.testtrakt.skytvautomator.SkyTVControllerPress;
import com.testtrakt.skytvautomator.SkyTVStartRequest;

public class SkyTVTests {

	private static final String SKY_TV_IP_ADDRESS = "{SKY_TV_IP_ADDRESS}";
	private static final String APP_ID = "YouTube";
	private static final String ENCODER_URL = "rtsp://{ENCODER_IP_ADDRESS}/0";

	private static final String TESSERACT_BIN = "{PATH_TO_TESSERACT_BINARY}";
	private static final String TESSERACT_DATA_DIR = "{PATH_TO_TESSDATA_DIR}";
	private static final String TESSERACT_LANGUAGE = "eng";
	private static final String FFMPEG_BIN = "{PATH_TO_FFMPEG_BIN}";
	private static final String WEB_PROFILER_ID = "{WEB_PROFILER_ID}";
	private static final String GOOGLE_VISION_OCR = "{PATH_TO_GOOGLE_CRED_JSON}";
	private static final String REKOGNITION_OCR = "{PATH_TO_AWS_CREDS}";

	private static final String SKY_TV_AUTOMATOR_ADDRESS = "http://localhost:9073";

	SkyTVAutomator skyTVAutomator = null;

	@AfterMethod (alwaysRun = true)
	public void afterTest() {
		if (skyTVAutomator != null) {
			skyTVAutomator.quit();
		}
	}

	@BeforeMethod (alwaysRun = true)
	public void beforeTest() throws Exception {
		
	}

	@Test(groups = { "SkyTV" })
	public void findElementByTesseractTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withAppID(APP_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
			.withHDMIEncoder(ENCODER_URL);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		// find an element
		List<Element> elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "uploaded").withTimeout(40, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() > 50.0f);
		// Assert.assertTrue(element.getX() > 10 && element.getX() < 15);
		// Assert.assertTrue(element.getY() > 170 && element.getY() < 180);
		// Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		// Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 45));
		Assert.assertEquals(element.getText(), "uploaded");
	
	}

	@Test(groups = { "SkyTV" })
	public void findElementByGoogleVisionTest() throws Exception {
		
		// create a start request with an app url that is a publicly available url
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withWebProfilerID(WEB_PROFILER_ID)
			.withAppID(APP_ID)
			.withGoogleVisionOCR(GOOGLE_VISION_OCR)
      .withHDMIEncoder(ENCODER_URL);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		// find an element
		List<Element> elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "TestTrakt Application Web Profiler Test Site").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() == 0.0);
		Assert.assertTrue(element.getX() > 8 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 170 && element.getY() < 180);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 50));
		Assert.assertEquals(element.getText(), "TestTrakt Application Web Profiler Test Site");

		// find multiple elements
		elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "TestTrakt Application Web Profiler Test Site").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		
		Assert.assertTrue(element.getConfidence() == 0.0);
		Assert.assertTrue(element.getX() > 8 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 320 && element.getY() < 325);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 55));
		Assert.assertEquals(element.getText(), "TestTrakt Application Web Profiler Test Site");
	}

	@Test(groups = { "SkyTV" })
	public void findElementByRekognitionTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withWebProfilerID(WEB_PROFILER_ID)
			.withAppID(APP_ID)
			.withAmazonRekognitionOCR(REKOGNITION_OCR)
      .withHDMIEncoder(ENCODER_URL);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		// find an element
		List<Element> elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "TestTrakt Application Web Profiler Test Site").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getX() > 8 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 170 && element.getY() < 180);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 50));
		Assert.assertEquals(element.getText(), "TestTrakt Application Web Profiler Test Site");

		// find multiple elements
		elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "TestTrakt Application Web Profiler Test Site").findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getX() > 8 && element.getX() < 15);
		Assert.assertTrue(element.getY() > 320 && element.getY() < 325);
		Assert.assertTrue(element.getWidth() > 310 && element.getWidth() < 330);
		Assert.assertTrue((element.getHeight() > 40 && element.getHeight() < 55));
		Assert.assertEquals(element.getText(), "TestTrakt Application Web Profiler Test Site");
	}
	
	@Test(groups = { "SkyTV" })
	public void findElementByImageTest() throws Exception {
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withWebProfilerID(WEB_PROFILER_ID)
			.withAppID(APP_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
      .withHDMIEncoder(ENCODER_URL);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		// set an image element similary default
		skyTVAutomator.options().set(new OptionsRequest().setDefaultImageFindSimilarity(.90));

		// get the sub screen image of our device to use as a locator for later
		File savedImage = skyTVAutomator.display().getImage(0, 0, 200, 200);
		
		// find an element by filepath
		List<Element> elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.IMAGE, savedImage.getAbsolutePath()).withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 200);
		Assert.assertTrue(element.getHeight() == 200);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertTrue(element.getText().contains("Offline"));

		// an image element that's NOT findAll and NOT found should return an empty collection
		ElementNotFoundException elementNotFoundException = null;
		try {
			skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.IMAGE, "https://testtraktresources.s3.amazonaws.com/TestTraktLogo.png").withTimeout(10, TimeUnit.SECONDS));
		} catch (ElementNotFoundException e) {
			elementNotFoundException = e;
		}
		Assert.assertNotNull(elementNotFoundException);

	}
	
	@Test(groups = { "SkyTV" })
	public void findElementByCssTest() throws Exception {
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withWebProfilerID(WEB_PROFILER_ID)
			.withAppID(APP_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		for (int i = 0; i < 100; i++) {
			skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.CSS, "p[id='testtraktidentifier']").withTimeout(10, TimeUnit.SECONDS));
		}

		// find an element by a css locator within your application
		List<Element> elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.CSS, "p[id='testtraktidentifier']").withTimeout(10, TimeUnit.SECONDS));
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
		elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.CSS, "p[id*='testtraktidentifier']").findAll(true));
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
	
	@Test(groups = { "SkyTV" })
	public void findElementByXpathTest() throws Exception {
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withWebProfilerID(WEB_PROFILER_ID)
			.withAppID(APP_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		// find an element by ax xpath locator within your application
		List<Element> elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.XPATH, "//p[@id='testtraktidentifier']").withTimeout(10, TimeUnit.SECONDS));
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
		elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.XPATH, "//p[contains(@id, 'testtraktidentifier')]").findAll(true));
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
	
	@Test(groups = { "SkyTV" })
	public void remoteControlInteractTest() throws Exception {
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withAppID(APP_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
			.withHDMIEncoder(ENCODER_URL);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		// find an element
		List<Element> elements = skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "uploaded").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertTrue(elements.size() > 0);

		// set a remote control delay
		skyTVAutomator.options().set(new OptionsRequest().setDefaultControllerDelay(1000, TimeUnit.MILLISECONDS));

		// perform remote control operations against the device
		skyTVAutomator.controller().sendCommand(new SkyTVControllerPress(SkyTVControllerButton.DOWN));
		skyTVAutomator.controller().sendCommand(new SkyTVControllerPress(SkyTVControllerButton.LEFT));
		skyTVAutomator.controller().sendCommand(new SkyTVControllerPress(SkyTVControllerButton.SELECT));
		
	}
	
	@Test(groups = { "SkyTV" })
	public void webProfilerTest() throws Exception {
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withWebProfilerID(WEB_PROFILER_ID)
			.withAppID(APP_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		// find an element
		skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.XPATH, "//p").withTimeout(45, TimeUnit.SECONDS));
		
		// gets our application page source
		String pageSource = skyTVAutomator.webProfiler().getPageSource();
		Assert.assertTrue(pageSource.contains("TestTrakt"));

		// gets the browser console logs of the webkit browser running our application
		String consoleLogs = skyTVAutomator.webProfiler().getConsoleLogs();
		Assert.assertTrue(consoleLogs.contains("Request received from testtrakt server"));

		// execute javascript against our application
		String scriptResult = skyTVAutomator.webProfiler().executeScript("document.documentElement.innerHTML");
		Assert.assertTrue(scriptResult.contains("<p>"));
	}

	@Test(groups = { "SkyTV" })
	public void screenTest() throws Exception {
		SkyTVStartRequest skyTVStartRequest = new SkyTVStartRequest(SKY_TV_IP_ADDRESS)
			.withWebProfilerID(WEB_PROFILER_ID)
			.withAppID(APP_ID)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
      .withHDMIEncoder(ENCODER_URL);

		// start our sky session
		skyTVAutomator = new SkyTVAutomator(new URI(SKY_TV_AUTOMATOR_ADDRESS), skyTVStartRequest);

		skyTVAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "TestTrakt").withTimeout(45, TimeUnit.SECONDS));

		// get the image/sub image from our application
		Assert.assertTrue(skyTVAutomator.display().getImage().exists());
		Assert.assertTrue(skyTVAutomator.display().getImage(400, 300, 300, 300).exists());
		
		// get the resolution width/height
		Assert.assertEquals(skyTVAutomator.display().getResolution().getWidth(), 1920.0);
		Assert.assertEquals(skyTVAutomator.display().getResolution().getHeight(), 823.0);

		// get a compiled video recording of our application from test start to now
		File recording = skyTVAutomator.display().getRecording();
		Assert.assertTrue(recording.exists());
		System.out.println("Video saved to: " + recording.getAbsolutePath());
	}

}
