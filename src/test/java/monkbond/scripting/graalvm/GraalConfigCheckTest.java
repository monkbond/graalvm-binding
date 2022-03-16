package monkbond.scripting.graalvm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.adaptris.core.Adapter;
import com.adaptris.core.management.config.ConfigurationCheckReport;
import com.adaptris.core.util.ObjectScanner;
import com.adaptris.util.GuidGenerator;
import org.junit.Test;

public class GraalConfigCheckTest {
  private static final GuidGenerator GUID = new GuidGenerator();

  // This should pass because build.gradle already checks the java.vendor so we are
  // almost certainly being tested under the auspices of GraalVM.
  @Test
  public void testChecker() throws Exception {
    GraalConfigChecker checker = new GraalConfigChecker();
    ConfigurationCheckReport report = new ConfigurationCheckReport();
    report.setCheckName(checker.getFriendlyName());
    checker.validate(createAdapterConfig(), report);
    assertTrue(report.isCheckPassed());
    assertEquals(0, report.getFailureExceptions().size());
    assertEquals(0, report.getWarnings().size());
  }

  @Test
  public void testChecker_NotGraalVM() throws Exception {
    GraalConfigChecker checker = new NotGraal();
    ConfigurationCheckReport report = new ConfigurationCheckReport();
    report.setCheckName(checker.getFriendlyName());
    checker.validate(createAdapterConfig(), report);
    assertFalse(report.isCheckPassed());
    assertEquals(0, report.getFailureExceptions().size());
    assertEquals(1, report.getWarnings().size());
  }

  @Test
  public void testWithException() throws Exception {
    GraalConfigChecker checker = new DefectiveChecker();
    ConfigurationCheckReport report = new ConfigurationCheckReport();
    report.setCheckName(checker.getFriendlyName());
    checker.validate(createAdapterConfig(), report);
    assertFalse(report.isCheckPassed());
    assertEquals(1, report.getFailureExceptions().size());
  }

  private Adapter createAdapterConfig() {
    Adapter result = new Adapter();
    result.setUniqueId(GUID.safeUUID());
    EmbeddedScriptingService service = new EmbeddedScriptingService().withScript("js", "");
    service.setUniqueId(GUID.safeUUID());
    result.getSharedComponents().addService(service);
    return result;
  }

  private static class DefectiveChecker extends NotGraal {
    @Override
    protected ObjectScanner<ScriptingServiceImp> scanner() {
      throw new RuntimeException();
    }
  }

  // build.gradle does a check already, which means that we're graal, so this is here to spoof the branch.
  private static class NotGraal extends GraalConfigChecker {
    @Override
    protected boolean isGraalVM() {
      return false;
    }
  }


}
