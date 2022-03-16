/*
 * Copyright 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package monkbond.scripting.graalvm;

import static monkbond.scripting.graalvm.ScriptingServiceTest.BROKEN_SCRIPT;
import static monkbond.scripting.graalvm.ScriptingServiceTest.LANGUAGE;
import static monkbond.scripting.graalvm.ScriptingServiceTest.METADATA_SCRIPT;
import static monkbond.scripting.graalvm.ScriptingServiceTest.MODIFIED_VALUE;
import static monkbond.scripting.graalvm.ScriptingServiceTest.MY_METADATA_KEY;
import static monkbond.scripting.graalvm.ScriptingServiceTest.MY_METADATA_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.AdaptrisMessageFactory;
import com.adaptris.core.ServiceException;
import com.adaptris.interlok.junit.scaffolding.services.ExampleServiceCase;
import org.junit.jupiter.api.Test;

public class EmbeddedScriptingServiceTest extends ExampleServiceCase {

  @Test
  public void testScripting_File() throws Exception {
    EmbeddedScriptingService service = new EmbeddedScriptingService().withScript(LANGUAGE, METADATA_SCRIPT);
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    msg.addMetadata(MY_METADATA_KEY, MY_METADATA_VALUE);
    execute(service, msg);
    assertTrue(msg.headersContainsKey(MY_METADATA_KEY));
    assertNotSame(MY_METADATA_VALUE, msg.getMetadataValue(MY_METADATA_KEY));
    assertEquals(MODIFIED_VALUE, msg.getMetadataValue(MY_METADATA_KEY));
  }

  @Test
  public void testScripting_Broken() throws Exception {
    EmbeddedScriptingService service = new EmbeddedScriptingService().withScript(LANGUAGE, BROKEN_SCRIPT);
    AdaptrisMessage msg = AdaptrisMessageFactory.getDefaultInstance().newMessage();
    msg.addMetadata(MY_METADATA_KEY, MY_METADATA_VALUE);
    assertThrows(ServiceException.class, () ->execute(service, msg));
  }

  @Override
  protected Object retrieveObjectForSampleConfig() {
    return new EmbeddedScriptingService().withScript("js", "some javascript content");
  }
}
