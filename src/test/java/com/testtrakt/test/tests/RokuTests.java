package com.rokuality.test.tests;

import java.awt.Dimension;
import java.io.File;
import java.net.URI;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

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
import com.testtrakt.common.Element;
import com.testtrakt.common.LocatorRequest;
import com.testtrakt.common.enums.LocatorType;
import com.testtrakt.rokuautomator.RokuAutomator;
import com.testtrakt.rokuautomator.RokuControllerButton;
import com.testtrakt.rokuautomator.RokuControllerPress;
import com.testtrakt.rokuautomator.RokuElement;
import com.testtrakt.rokuautomator.RokuLocatorRequest;
import com.testtrakt.rokuautomator.RokuLocatorType;
import com.testtrakt.rokuautomator.RokuMediaPlayerData;
import com.testtrakt.rokuautomator.RokuStartRequest;
import com.testtrakt.rokuautomator.RokuSystemInformation;

public class RokuTests {

	private static final String ROKU_IP_ADDRESS = "{roku_ip_address}";
	private static final String ROKU_USERNAME = "{roku_dev_username}";
	private static final String ROKU_PASSWORD = "{roku_dev_password}";
	private static final String APP_PACKAGE = "https://testtraktresources.s3.amazonaws.com/channelwebcall.zip";
	private static final String ENCODER_URL = "rtsp://{hdmi_ip_address}/0";

	private static final String TESSERACT_BIN = "{path_to_tesseract_binary}";
	private static final String TESSERACT_DATA_DIR = "{path_to_tess_data_directory}";
	private static final String TESSERACT_LANGUAGE = "eng";
	private static final String FFMPEG_BIN = "{path_to_ffmpeg_binary}";

	private static final String ROKU_AUTOMATOR_ADDRESS = "http://localhost:9070";

	RokuAutomator rokuAutomator = null;
	Proxy proxy = null;

	@AfterMethod (alwaysRun = true)
	public void afterTest() {
		if (rokuAutomator != null) {
			rokuAutomator.quit();
		}

		if (proxy != null) {
			proxy.stop();
		}
	}

	@BeforeMethod (alwaysRun = true)
	public void beforeTest() throws Exception {
		
	}

	@Test(groups = { "Roku" })
	public void findElementByOCRTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// find an element
		List<Element> elements = rokuAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "roku").withTimeout(20, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		Element element = elements.get(0);
		System.out.println("Element: " + element.asJSON());

		Assert.assertTrue(element.getConfidence() > 50.0f);
		Assert.assertEquals(element.getX(), 102);
		Assert.assertEquals(element.getY(), 63);
		Assert.assertEquals(element.getWidth(), 135);
		Assert.assertEquals(element.getHeight(), 42);
		Assert.assertEquals(element.getText(), "Roku");
	
	}

	@Test(groups = { "Roku" })
	public void findElementByRokuNativeLocatorsTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);
	
		// find all roku elements by tag name
		List<Element> elements = rokuAutomator.locator().locate(new RokuLocatorRequest(RokuLocatorType.TAG, "RowListItem").findAll(true).withTimeout(20, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 6);

		// get the first element from the collection
		Element element = elements.get(0);

		// verify the element location/size details
		verifyElementData(element, 100.0f, 165, 44, 1920, 800);

		// convert to a roku element and verify attributes of the element
		RokuElement rokuElement = new RokuElement(element);
		Map<String, String> elementAttributes = rokuElement.getAttributes();
		Assert.assertEquals(elementAttributes.get("index"), "0");
		Assert.assertEquals(rokuElement.getAttribute("index"), "0");

		// find all roku elements by xpath
		elements = rokuAutomator.locator().locate(new RokuLocatorRequest(RokuLocatorType.XPATH, "//RowListItem").findAll(true).withTimeout(10, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 6);
		element = elements.get(0); // get the first element
		verifyElementData(element, 100.0f, 165, 44, 1920, 800);

		// more complicated xpath
		elements = rokuAutomator.locator().locate(new RokuLocatorRequest(RokuLocatorType.XPATH, "//Label[@text='Big Hits']").withTimeout(10, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		element = elements.get(0); // get the first element
		verifyElementData(element, 100.0f, 0, 0, 0, 0);

		// get all the element attribute name/value pairs from the element
		rokuElement = new RokuElement(element);
		elementAttributes = rokuElement.getAttributes();
		Assert.assertEquals(elementAttributes.get("color"), "#ffffffff");

		// get a specific attribute value from the element
		Assert.assertEquals(rokuElement.getAttribute("color"), "#ffffffff");

	}

	@Test(groups = { "Roku" })
	public void getPageSourceTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);
	
		// get the page source
		String pageSource = rokuAutomator.getPageSource();
		Assert.assertTrue(pageSource.contains("Big Hits"));
	}

	@Test(groups = { "Roku" })
	public void getMediaPlayerDataTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);
	
		// get the roku media player data
		RokuMediaPlayerData rokuMediaPlayerData = rokuAutomator.getMediaPlayerData();
		System.out.println(rokuMediaPlayerData.asJSON());
		Assert.assertFalse(rokuMediaPlayerData.isError());
		Assert.assertEquals(rokuMediaPlayerData.getCurrentPlaybackState(), "none");
	}

	@Test(groups = { "Roku" })
	public void sendRemoteControlCommandTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);
	
		// send remote control command
		rokuAutomator.controller().sendCommand(new RokuControllerPress(RokuControllerButton.RIGHT_ARROW));
		rokuAutomator.controller().sendCommand(new RokuControllerPress(RokuControllerButton.LEFT_ARROW));
		rokuAutomator.controller().sendCommand(new RokuControllerPress(RokuControllerButton.SELECT));
	}

	@Test(groups = { "Roku" })
	public void screenArtifactsTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// find an element
		rokuAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "roku").withTimeout(20, TimeUnit.SECONDS));
	
		// get the screen image
		File image = rokuAutomator.display().getImage();
		System.out.println("Image saved to: " + image.getAbsolutePath());
		Assert.assertTrue(image.exists() && image.isFile());

		// get the screen recording
		File recording = rokuAutomator.display().getRecording();
		System.out.println("Recording saved to: " + recording.getAbsolutePath());
		Assert.assertTrue(recording.exists() && recording.isFile());

		// get the screen resolution
		Dimension dimension = rokuAutomator.display().getResolution();
		Assert.assertEquals(dimension.getWidth(), 1920.0);
		Assert.assertEquals(dimension.getHeight(), 1080.0);
	}

	@Test(groups = { "Roku" })
	public void consoleLogsTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// get the device logs
		String consoleLogs = rokuAutomator.system().getConsoleLogs();
		System.out.println("Console logs: " + consoleLogs);

		Assert.assertTrue(consoleLogs.contains("HeroGridChannel"));
	}

	@Test(groups = { "Roku" })
	public void runInBackgroundTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// background our application and then resume
		rokuAutomator.runAppInBackground(Duration.ofSeconds(5));

		// find an element after our application has relaunched
		rokuAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "roku").withTimeout(20, TimeUnit.SECONDS));
	}

	@Test(groups = { "Roku" })
	public void getPerformanceDataTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withPerformanceProfilingEnabled(true);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// find an element
		rokuAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "roku").withTimeout(20, TimeUnit.SECONDS));

		// get the performance data - can be used with the roku brightscript visual profiler tool to evaluate cpu and memory usage - https://developer.roku.com/en-ca/docs/developer-program/dev-tools/brightscript-profiler.md
		File performanceBrsFile = rokuAutomator.getPerformanceData();
		System.out.println("Performance file saved to: " + performanceBrsFile.getAbsolutePath());
		Assert.assertTrue(performanceBrsFile.exists() && performanceBrsFile.isFile());
	}

	@Test(groups = { "Roku" })
	public void encoderTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE)
			.withTesseractOCR(TESSERACT_BIN, TESSERACT_DATA_DIR, TESSERACT_LANGUAGE)
			.withFFMPEG(FFMPEG_BIN)
			.withHDMIEncoder(ENCODER_URL);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// find an element
		List<Element> elements = rokuAutomator.locator().locate(new LocatorRequest(LocatorType.TEXT, "roku").withTimeout(20, TimeUnit.SECONDS));
		Assert.assertEquals(elements.size(), 1);
		
		// get the screen image
		File image = rokuAutomator.display().getImage();
		System.out.println("Image saved to: " + image.getAbsolutePath());
		Assert.assertTrue(image.exists() && image.isFile());

		// get the screen recording
		File recording = rokuAutomator.display().getRecording();
		System.out.println("Recording saved to: " + recording.getAbsolutePath());
		Assert.assertTrue(recording.exists() && recording.isFile());

		// get the screen resolution
		Dimension dimension = rokuAutomator.display().getResolution();
		Assert.assertEquals(dimension.getWidth(), 1920.0);
		Assert.assertEquals(dimension.getHeight(), 1080.0);
	
	}

	@Test(groups = { "Roku" })
	public void rokuSystemInfoTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// get the system information about the roku (device os version, etc)
		RokuSystemInformation rokuSystemInformation = rokuAutomator.system().getSystemInfo();
		Assert.assertEquals(rokuSystemInformation.modelName, "Roku Ultra");
	}

	@Test(groups = { "Roku" })
	public void getActiveElementTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE);

		// start our session
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// get the current active/focused element
		Element activeElement = rokuAutomator.getActiveElement();
		System.out.println("Active element: " + activeElement.asJSON());
		verifyElementData(activeElement, 100, 3260, 0, 1600, 700);
	}

	@Test(groups = { "Roku" })
	public void deepLinkTest() throws Exception {
		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE)
			.withDeepLink("contentID", "mediaType");

		// start our session and open the app using the provided deep link
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		// find an element from our deep link
		rokuAutomator.locator().locate(new RokuLocatorRequest(RokuLocatorType.XPATH, "//RowListItem").withTimeout(10, TimeUnit.SECONDS));
	}

	@Test(groups = { "Roku" })
	public void proxyTest() throws Exception {
		// start our charles proxy and setup our rewrite
		startCharlesProxifierWithRokuRewrite(8888);

		// create a start request with an app url that is a publicly available url
		RokuStartRequest rokuStartRequest = new RokuStartRequest(ROKU_IP_ADDRESS, ROKU_USERNAME, ROKU_PASSWORD)
			.withAppPackage(APP_PACKAGE)
			.withProxy("192.168.1.85", 8888); // provisions our application to send all traffic to a proxy at the address/port

		// start our session and open the app using the provided deep link
		rokuAutomator = new RokuAutomator(new URI(ROKU_AUTOMATOR_ADDRESS), rokuStartRequest);

		Thread.sleep(30000);
	}

	private void verifyElementData(Element element, float confidence, int x, int y, int width, int height) {
		System.out.println("Element: " + element.asJSON());
		Assert.assertEquals(element.getConfidence(), confidence);
		Assert.assertEquals(element.getX(), x);
		Assert.assertEquals(element.getY(), y);
		Assert.assertEquals(element.getWidth(), width);
		Assert.assertEquals(element.getHeight(), height);
	}

	private void startCharlesProxifierWithRokuRewrite(int proxyPort) throws Exception {
		Proxy proxy = new Proxy(new URI("http://localhost:9011"));

		ProxySettings proxySettings = new ProxySettings();
		proxySettings.setPort(proxyPort);
		proxySettings.setHeadless(false);
		proxySettings.setCharlesBinaryLocation("{path_charles_app}/Contents/MacOS/Charles");
		proxySettings.enableTransparentHttpProxying(true);

		RewriteRule modifyURLRule = new RewriteRule();
		modifyURLRule.setRuleType(RewriteRuleType.URL);
		modifyURLRule.setName("Charles Proxy for Roku Automator URL Rewrite");
		Location modifyURLLocation = new Location();
		modifyURLLocation.setProtocol("*");
		modifyURLLocation.setHost("*");
		modifyURLLocation.setPort("*");
		modifyURLLocation.setPath("*");
		modifyURLLocation.setQueryString("*");
		modifyURLRule.setLocation(modifyURLLocation);

		Match modifyURLMatch = new Match();
		modifyURLMatch.setValue(Pattern.compile("(.*);(.*)"));
		modifyURLRule.setMatch(modifyURLMatch);

		Replace modifyURLReplace = new Replace();
		modifyURLReplace.setValue("$2");
		modifyURLRule.setReplace(modifyURLReplace);
		
		ProxyStartupSettings proxyStartupSettings = new ProxyStartupSettings();
		proxyStartupSettings.addProxySettings(proxySettings);

		ProxyToolStartupSettings proxyToolStartupSettings = new ProxyToolStartupSettings();
		proxyToolStartupSettings.addRewriteSettings(Arrays.asList(modifyURLRule));

		proxy.start(proxyStartupSettings, proxyToolStartupSettings);
	}

}
