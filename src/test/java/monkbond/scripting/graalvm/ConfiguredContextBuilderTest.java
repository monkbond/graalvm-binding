package monkbond.scripting.graalvm;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.adaptris.util.KeyValuePair;
import com.adaptris.util.KeyValuePairSet;
import monkbond.scripting.graalvm.ConfiguredContextBuilder.EnvironmentAccessEnum;
import monkbond.scripting.graalvm.ConfiguredContextBuilder.HostAccessEnum;
import monkbond.scripting.graalvm.ConfiguredContextBuilder.PolyglotAccessEnum;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ConfiguredContextBuilderTest {

  @Test
  public void testBuilder() throws Exception {
    ConfiguredContextBuilder builder = createBuilder();
    assertNotNull(builder.build());
  }

  @Test
  public void testWorkingDirectory() throws Exception {
    ConfiguredContextBuilder builder = createBuilder();
    builder.setCurrentWorkingDirectory(new File(".").getCanonicalPath());
    assertNotNull(builder.build());
  }

  @Test
  public void testMaps() throws Exception {
    ConfiguredContextBuilder builder = createBuilder();
    builder.setEnvironment(new KeyValuePairSet(List.of(new KeyValuePair("HELLO", "World"))));
    builder.setOptions(new KeyValuePairSet(List.of(new KeyValuePair("engine.WarnInterpreterOnly", "false"))));
    assertNotNull(builder.build());
  }

  @Test
  public void testEnvironmentAccess() throws Exception {
    for (EnvironmentAccessEnum a : EnvironmentAccessEnum.values()) {
      ConfiguredContextBuilder builder = createBuilder();
      builder.setEnvironmentAccess(a);
      assertNotNull(builder.build());
    }
  }

  @Test
  public void testHostAccess() throws Exception {
    for (HostAccessEnum a : HostAccessEnum.values()) {
      ConfiguredContextBuilder builder = createBuilder();
      builder.setHostAccess(a);
      assertNotNull(builder.build());
    }
  }

  @Test
  public void testPolyglotAccess() throws Exception {
    for (PolyglotAccessEnum a : PolyglotAccessEnum.values()) {
      ConfiguredContextBuilder builder = createBuilder();
      builder.setPolyglotAccess(a);
      assertNotNull(builder.build());
    }
  }

  private ConfiguredContextBuilder createBuilder() {
    ConfiguredContextBuilder builder = new ConfiguredContextBuilder();
    builder.setAllowExperimentalOptions(Boolean.TRUE);
    return builder;
  }
}
