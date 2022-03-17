package monkbond.scripting.graalvm;

import static lombok.AccessLevel.PRIVATE;

import com.adaptris.core.Adapter;
import com.adaptris.core.management.config.ConfigurationCheckReport;
import com.adaptris.core.management.config.ValidationCheckerImpl;
import com.adaptris.core.util.LoggingHelper;
import com.adaptris.core.util.ObjectScanner;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import lombok.NoArgsConstructor;

/**
 * Checks that the java vendor is GraalVM.
 * <p>Polyglot script functionality depends on actually running interlok using GraalVM. This may actually be a defunct
 * checker class since the services have a hard dependency on the graalvm classes so it may fail ot unmarshal
 * anyway.</p>
 */
@NoArgsConstructor
public class GraalConfigChecker extends ValidationCheckerImpl {

  private static final String FRIENDLY_NAME = "GraalVM Polyglot Engine check";

  private static String buildWarningMsg(ScriptingServiceImp s) {
    return String.format(
        "[%s] relies on Graal polyglot scripting but [%s] is not installed",
        LoggingHelper.friendlyName(s), s.getLanguage());
  }

  @Override
  protected void validate(Adapter adapter, ConfigurationCheckReport report) {
    try {
      Collection<ScriptingServiceImp> scripts = scanner().scan(adapter);
      Collection<String> langs = installedLangs();
      // It's a warning, we may be building a zip file for execution inside a GraalVM env.
      for (ScriptingServiceImp s : scripts) {
        if (!langs.contains(s.getLanguage())) {
          report.getWarnings().add(buildWarningMsg(s));
        }
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

  private Collection<String> installedLangs() {
    return List.copyOf(new DefaultContextBuilder().build().getEngine().getLanguages().keySet());
  }

  @NoArgsConstructor(access = PRIVATE)
  private static class ScriptingServiceScanner extends ObjectScanner<ScriptingServiceImp> {

    @Override
    protected Function<Object, Boolean> objectMatcher() {
      return (object) -> ScriptingServiceImp.class.isAssignableFrom(object.getClass());
    }
  }

}
