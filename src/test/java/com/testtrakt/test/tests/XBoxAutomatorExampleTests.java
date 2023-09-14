package com.testtrakt.test.tests;

import java.io.File;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.testng.Assert;
import org.testng.annotations.*;

import com.testtrakt.charlesproxifier.enums.RewriteRuleType;
import com.testtrakt.charlesproxifier.proxy.Location;
import com.testtrakt.charlesproxifier.proxy.Match;
import com.testtrakt.charlesproxifier.proxy.Proxy;
import com.testtrakt.charlesproxifier.proxy.ProxySettings;
import com.testtrakt.charlesproxifier.proxy.ProxyStartupSettings;
import com.testtrakt.charlesproxifier.proxy.ProxyToolStartupSettings;
import com.testtrakt.charlesproxifier.proxy.Replace;
import com.testtrakt.charlesproxifier.proxy.RewriteRule;
import com.testtrakt.charlesproxifier.proxy.SSLProxySettings;
import com.testtrakt.common.Element;
import com.testtrakt.common.HTMLElement;
import com.testtrakt.common.LocatorRequest;
import com.testtrakt.common.OptionsRequest;
import com.testtrakt.common.enums.LocatorType;
import com.testtrakt.xboxautomator.enums.XBoxPackageType;
import com.testtrakt.xboxautomator.XBoxAutomator;
import com.testtrakt.xboxautomator.XBoxControllerButton;
import com.testtrakt.xboxautomator.XBoxControllerPress;
import com.testtrakt.xboxautomator.XBoxStartRequest;
import com.testtrakt.xboxautomator.XBoxSystemInformation;

public class XBoxAutomatorExampleTests {

	private static final String LOCATOR_TEXT_1 = "text to find on screen 1";
	private static final String LOCATOR_TEXT_2 = "text to find on screen 2";

	private static final String XBOX_IP_ADDRESS = "your_xbox_ip_address";
	private static final String APP_URL = "path_or_url_to_your_xbox_appx_or_appxbundle";
	private static final String APP_NAME = "your_app_name";
	private static final String TESSERACT_BIN = "path_to_tesseract_binary";
	private static final String TESSERACT_DATA_DIR = "path_to_tessdata";
	private static final String TESSERACT_LANGUAGE = "eng";
	private static final String FFMPEG_BIN = "path_to_ffmpeg";
	private static final String WEB_PROFILER_ID = "your_web_profiler_id";
	private static final String GOOGLE_VISION_OCR = "path_to_google_vision_credentials";
	private static final String TEXTRACT_OCR = "path_to_texttract_credentials";

	private static final String XBOX_AUTOMATOR_ADDRESS = "http://localhost:9011";
	private static final String CHARLES_PROXIFIER_ADDRESS = "http://localhost:8665";

	XBoxAutomator xboxAutomator = null;

	@AfterTest(alwaysRun = true)
	public void afterTest() {
		if (xboxAutomator != null) {
			xboxAutomator.quit();
		}
	}

	@BeforeTest(alwaysRun = true)
	public void beforeTest() throws Exception {
		startProxyWithEmbeddedScript();
	}

	@Test(groups = { "XBox" })
	public void findElementByTesseractTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppPackage(new URI(APP_URL))
				.withAppName(APP_NAME)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		// start our xbox session
		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);

		navToStartElement();

		// find an element
		List<Element> elements = xboxAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, LOCATOR_TEXT_1).withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() > 90.0);
		Assert.assertTrue(element.getWidth() > 30 && element.getWidth() < 40);
		Assert.assertTrue(element.getHeight() > 10 && element.getHeight() < 15);
		Assert.assertTrue(element.getX() > 400 && element.getX() < 420);
		Assert.assertTrue(element.getY() > 400 && element.getY() < 450);

		System.out.println("Element location x: " + element.getX());
		System.out.println("Element location y: " + element.getY());
		System.out.println("Element width: " + element.getWidth());
		System.out.println("Element height: " + element.getHeight());

		elements = xboxAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, LOCATOR_TEXT_1).findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() > 90.0);
		Assert.assertTrue(element.getWidth() > 30 && element.getWidth() < 40);
		Assert.assertTrue(element.getHeight() > 10 && element.getHeight() < 15);
		Assert.assertTrue(element.getX() > 570 && element.getX() < 590);
		Assert.assertTrue(element.getY() > 450 && element.getY() < 470);
	}

	@Test(groups = { "XBox" })
	public void findElementByGoogleVisionTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppPackage(new URI(APP_URL))
				.withAppName(APP_NAME)
				.withGoogleVisionOCR(GOOGLE_VISION_OCR);

		// start our xbox session
		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);

		navToStartElement();

		// find an element
		List<Element> elements = xboxAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, LOCATOR_TEXT_1).withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 0.0);
		Assert.assertTrue(element.getWidth() > 30 && element.getWidth() < 40);
		Assert.assertTrue(element.getHeight() > 10 && element.getHeight() < 20);
		Assert.assertTrue(element.getX() > 400 && element.getX() < 420);
		Assert.assertTrue(element.getY() > 400 && element.getY() < 450);

		elements = xboxAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, LOCATOR_TEXT_1).findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 0.0);
		Assert.assertTrue(element.getWidth() > 30 && element.getWidth() < 40);
		Assert.assertTrue(element.getHeight() > 10 && element.getHeight() < 20);
		Assert.assertTrue(element.getX() > 570 && element.getX() < 590);
		Assert.assertTrue(element.getY() > 450 && element.getY() < 470);
	}

	@Test(groups = { "XBox" })
	public void findElementByTextractTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppPackage(new URI(APP_URL))
				.withAppName(APP_NAME)
				.withAmazonRekognitionOCR(TEXTRACT_OCR);

		// start our xbox session
		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);

		navToStartElement();

		// find an element
		List<Element> elements = xboxAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, LOCATOR_TEXT_1).withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() > 90.0);
		Assert.assertTrue(element.getWidth() > 30 && element.getWidth() < 40);
		Assert.assertTrue(element.getHeight() > 10 && element.getHeight() < 20);
		Assert.assertTrue(element.getX() > 400 && element.getX() < 420);
		Assert.assertTrue(element.getY() > 400 && element.getY() < 450);

		elements = xboxAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, LOCATOR_TEXT_1).findAll(true));
		Assert.assertEquals(elements.size(), 2);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() > 90.0);
		Assert.assertTrue(element.getWidth() > 30 && element.getWidth() < 40);
		Assert.assertTrue(element.getHeight() > 10 && element.getHeight() < 20);
		Assert.assertTrue(element.getX() > 570 && element.getX() < 590);
		Assert.assertTrue(element.getY() > 450 && element.getY() < 470);
	}

	@Test(groups = { "XBox" })
	public void findElementByImageTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppPackage(new URI(APP_URL))
				.withAppName(APP_NAME)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN)
				.withWebProfilerID(WEB_PROFILER_ID);

		// start our xbox session
		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);

		navToStartElement();

		// set an image element similary default
		xboxAutomator.options().set(new OptionsRequest().setDefaultImageFindSimilarity(.90));

		// find an element
		List<Element> elements = xboxAutomator.locator()
				.locate(new LocatorRequest(LocatorType.IMAGE, "/path/to/.png").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() > 0.95);
		Assert.assertTrue(element.getWidth() == 300);
		Assert.assertTrue(element.getHeight() == 300);
		Assert.assertTrue(element.getX() == 400);
		Assert.assertTrue(element.getY() == 300);

	}

	@Test(groups = { "XBox" })
	public void findElementByCssTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppPackage(new URI(APP_URL))
				.withAppName(APP_NAME)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN)
				.withWebProfilerID(WEB_PROFILER_ID);

		// start our xbox session
		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);

		navToStartElement();

		// find an element
		List<Element> elements = xboxAutomator.locator().locate(
				new LocatorRequest(LocatorType.CSS, "div[class='some-class']").withTimeout(45, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "element text");

		elements = xboxAutomator.locator()
				.locate(new LocatorRequest(LocatorType.CSS, "div[class='some-class']").findAll(true));
		Assert.assertEquals(elements.size(), 11);
		element = elements.get(1);
		System.out.println("Element 2: " + element.asJSON());
		Assert.assertTrue(element.getConfidence() == 100.0);
		Assert.assertTrue(element.getWidth() == 0);
		Assert.assertTrue(element.getHeight() == 0);
		Assert.assertTrue(element.getX() == 0);
		Assert.assertTrue(element.getY() == 0);
		Assert.assertEquals(element.getText(), "element text");

		HTMLElement htmlElement = element.toHTMLElement();
		Assert.assertEquals(htmlElement.getAttribute("class"), "featured-title");
		Assert.assertEquals(htmlElement.getCssValue("font-weight"), "900");
	}

	@Test(groups = { "XBox" })
	public void remoteControlInteractTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppPackage(new URI(APP_URL))
				.withAppName(APP_NAME)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE);

		// start our xbox session
		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);

		xboxAutomator.controller().sendCommand(new XBoxControllerPress(XBoxControllerButton.LEFT_ARROW));

	}

	@Test(groups = { "XBox" })
	public void systemInfoTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppPackage(new URI(APP_URL))
				.withAppName(APP_NAME);

		// start our xbox session
		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);

		XBoxSystemInformation info = xboxAutomator.system().getSystemInfo();
		Assert.assertEquals(info.getConsoleType(), "Xbox One");

	}

	@Test(groups = { "XBox" })
	public void launchPreInstalledAppTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppName(APP_NAME)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE);

		// start our xbox session
		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);
	}

	@Test(groups = { "XBox" })
	public void screenTest() throws Exception {
		// create a start request with an app package that is a publicly available url
		XBoxStartRequest xBoxStartRequest = new XBoxStartRequest(XBOX_IP_ADDRESS)
				.withAppName(APP_NAME)
				.withAppPackage(new URI(APP_URL))
				.withAppPackageType(XBoxPackageType.APP_X_BUNDLE)
				.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
				.withFFMPEG(FFMPEG_BIN);

		xboxAutomator = new XBoxAutomator(new URI(XBOX_AUTOMATOR_ADDRESS), xBoxStartRequest);

		Assert.assertTrue(xboxAutomator.display().getImage().exists());
		Assert.assertTrue(xboxAutomator.display().getImage(400, 300, 300, 300).exists());

		Assert.assertEquals(xboxAutomator.display().getResolution().getWidth(), 1280.0);
		Assert.assertEquals(xboxAutomator.display().getResolution().getHeight(), 720.0);

		xboxAutomator.options().set(new OptionsRequest().setDefaultControllerDelay(2, TimeUnit.SECONDS));
		for (int i = 0; i < 5; i++) {
			xboxAutomator.controller().sendCommand(new XBoxControllerPress(XBoxControllerButton.RIGHT_ARROW));
			xboxAutomator.controller().sendCommand(new XBoxControllerPress(XBoxControllerButton.LEFT_ARROW));
		}

		File recording = xboxAutomator.display().getRecording();
		Assert.assertTrue(recording.exists());
		System.out.println("Video saved to: " + recording.getAbsolutePath());
	}

	private void startProxyWithEmbeddedScript() throws Exception {
		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(8889);
		proxySettings.setHeadless(true);
		proxySettings.setCharlesBinaryLocation("/path/to/charles/binary");

		SSLProxySettings sslProxySettings = new SSLProxySettings();
		sslProxySettings.enableSSLProxying(true);
		Location sslIncludeLocation = new Location();
		sslIncludeLocation.setHost("*");
		sslIncludeLocation.setPort("*");
		sslProxySettings.include(Arrays.asList(sslIncludeLocation));

		RewriteRule modifyBodyRule = new RewriteRule();
		modifyBodyRule.setRuleType(RewriteRuleType.BODY);
		modifyBodyRule.setName("Modify Body Rule");

		Location modifyBodyLocation = new Location();
		modifyBodyLocation.setProtocol("*");
		modifyBodyLocation.setHost("host_location");
		modifyBodyLocation.setPort("*");
		modifyBodyLocation.setPath("/");
		modifyBodyLocation.setQueryString("*");
		modifyBodyRule.setLocation(modifyBodyLocation);

		Match modifyBodyMatch = new Match();
		modifyBodyMatch.setValue("</head>");
		modifyBodyMatch.matchRequest(false);
		modifyBodyMatch.matchResponse(true);
		modifyBodyRule.setMatch(modifyBodyMatch);

		Replace modifyBodyReplace = new Replace();
		modifyBodyReplace
				.setValue("<script src=\"https://www.testtraktserver.com/your_capture_id.js\"></script></head>");
		modifyBodyReplace.replaceAll(true);
		modifyBodyRule.setReplace(modifyBodyReplace);

		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);
		proxyStartupSettings.addSSLProxySettings(sslProxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(modifyBodyRule));

		Proxy proxy = new Proxy(new URI(CHARLES_PROXIFIER_ADDRESS));
		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

	private void navToStartElement() {
		xboxAutomator.locator()
				.locate(new LocatorRequest(LocatorType.TEXT, LOCATOR_TEXT_2).withTimeout(45, TimeUnit.SECONDS));
		xboxAutomator.options().set(new OptionsRequest().setDefaultControllerDelay(2, TimeUnit.SECONDS));
		xboxAutomator.controller().sendCommand(new XBoxControllerPress(XBoxControllerButton.RIGHT_ARROW));
		xboxAutomator.controller().sendCommand(new XBoxControllerPress(XBoxControllerButton.RIGHT_ARROW));
	}

}
