package monkbond.scripting.graalvm;

import com.adaptris.core.Adapter;
import com.adaptris.core.management.config.ConfigurationCheckReport;
import com.adaptris.core.management.config.ValidationCheckerImpl;
import com.adaptris.core.util.LoggingHelper;
import com.adaptris.core.util.ObjectScanner;
import java.util.Collection;
import java.util.function.Function;

/**
 * Checks that the java vendor is GraalVM.
 * <p>Polyglot script functionality depends on actually running interlok using GraalVM. This may actually be a defunct
 * checker class since the services have a hard dependency on the graalvm classes so it may fail ot unmarshal
 * anyway.</p>
 */
public class GraalConfigChecker extends ValidationCheckerImpl {

  private static final String FRIENDLY_NAME = "GraalVM Polyglot Engine check";

  @Override
  protected void validate(Adapter adapter, ConfigurationCheckReport report) {
    // If we're a GraalVM, then we're all good.
    if (isGraalVM()) {
      return;
    }
    try {
      Collection<ScriptingServiceImp> scripts = scanner().scan(adapter);
      // It's a warning, we may be building a zip file for execution inside a GraalVM env.
      for (ScriptingServiceImp s : scripts) {
        report.getWarnings()
            .add(String.format(
                "[%s] relies on Graal polyglot scripting but current 'java.vendor' is not Graal",
                LoggingHelper.friendlyName(s)));
      }
    } catch (Exception ex) {
      report.getFailureExceptions().add(ex);
    }
  }

  @Override
  public String getFriendlyName() {
    return FRIENDLY_NAME;
  }

  protected ObjectScanner<ScriptingServiceImp> scanner() {
    return new ScriptingServiceScanner();
  }

  protected boolean isGraalVM() {
    return System.getProperty("java.vendor").toLowerCase().contains("graalvm");
  }

  private static class ScriptingServiceScanner extends ObjectScanner<ScriptingServiceImp> {

    @Override
    protected Function<Object, Boolean> objectMatcher() {
      return (object) -> ScriptingServiceImp.class.isAssignableFrom(object.getClass());
    }
  }

}
