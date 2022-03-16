package monkbond.scripting.graalvm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.ServiceException;
import com.adaptris.core.stubs.TempFileUtils;
import com.adaptris.interlok.junit.scaffolding.services.ExampleServiceCase;
import java.io.File;
import java.net.URI;
import org.junit.jupiter.api.Test;

public class ScriptingServiceTest extends ExampleServiceCase {

  public static final String METADATA_SCRIPT = "message.addMetadata('MyMetadataKey', 'different');";
  public static final String BROKEN_SCRIPT = "This Has a Syntax Error";
  public static final String LANGUAGE = "js";

  public static final String MODIFIED_VALUE = "different";
  public static final String MY_METADATA_VALUE = "MyMetadataValue";
  public static final String MY_METADATA_KEY = "MyMetadataKey";

  @Test
  public void testScripting_URL() throws Exception {
    File scriptFile = TempFileUtils.createTrackedFile("script", ".js", null, this, () -> METADATA_SCRIPT);
    String fileUrl = "file:///" + new URI(null, scriptFile.getCanonicalPath(), null).toASCIIString();
    ScriptingService service = new ScriptingService().withScript(LANGUAGE, fileUrl);
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    msg.addMetadata(MY_METADATA_KEY, MY_METADATA_VALUE);
    execute(service, msg);
    assertTrue(msg.headersContainsKey(MY_METADATA_KEY));
    assertNotSame(MY_METADATA_VALUE, msg.getMetadataValue(MY_METADATA_KEY));
    assertEquals(MODIFIED_VALUE, msg.getMetadataValue(MY_METADATA_KEY));
  }

  @Test
  public void testScripting_File() throws Exception {
    File scriptFile = TempFileUtils.createTrackedFile("script", ".js", null, this, () -> METADATA_SCRIPT);
    ScriptingService service = new ScriptingService().withScript(LANGUAGE, scriptFile.getCanonicalPath());
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    msg.addMetadata(MY_METADATA_KEY, MY_METADATA_VALUE);
    execute(service, msg);
    assertTrue(msg.headersContainsKey(MY_METADATA_KEY));
    assertNotSame(MY_METADATA_VALUE, msg.getMetadataValue(MY_METADATA_KEY));
    assertEquals(MODIFIED_VALUE, msg.getMetadataValue(MY_METADATA_KEY));
  }

  @Test
  public void testScripting_Broken() throws Exception {
    File scriptFile = TempFileUtils.createTrackedFile("script", ".js", null, this, () -> BROKEN_SCRIPT);
    ScriptingService service = new ScriptingService().withScript(LANGUAGE, scriptFile.getCanonicalPath());
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    msg.addMetadata(MY_METADATA_KEY, MY_METADATA_VALUE);
    assertThrows(ServiceException.class, () ->execute(service, msg));
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    return new ScriptingService().withScript("js", "/path/to/script.js");
  }
}
