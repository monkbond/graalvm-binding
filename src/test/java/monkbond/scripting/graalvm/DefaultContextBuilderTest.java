package monkbond.scripting.graalvm;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Properties;
import org.graalvm.polyglot.Context;
import org.junit.jupiter.api.Test;

public class DefaultContextBuilderTest {

  @Test
  public void testBuilder() throws Exception {
    DefaultContextBuilder builder = new DefaultContextBuilder();
    assertNotNull(builder.build());
  }

  @Test
  public void testConfigureBuilder() {
    Context.Builder builder = Context.newBuilder();
    Context.Builder b2 = DefaultContextBuilder.enforcedOptions(builder, new Properties());
    // You can't get the value out of the builder, so bleh
    assertEquals(builder, b2);
  }

  @Test
  public void testConfigureBuilder_NashornCompat() {
    Context.Builder builder = Context.newBuilder();
    Properties p = new Properties();
    p.setProperty("polyglot.js.nashorn-compat", "true");
    Context.Builder b2 = DefaultContextBuilder.enforcedOptions(builder, p);
    // You can't get the value out of the builder, so bleh
    assertEquals(builder, b2);
  }
}
