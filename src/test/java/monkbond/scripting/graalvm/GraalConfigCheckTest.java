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

  @Test
  public void testChecker() throws Exception {
    GraalConfigChecker checker = new GraalConfigChecker();
    ConfigurationCheckReport report = new ConfigurationCheckReport();
    report.setCheckName(checker.getFriendlyName());
    checker.validate(createAdapterConfig("js"), report);
    doLogging(report);
    assertTrue(report.isCheckPassed());
    assertEquals(0, report.getFailureExceptions().size());
    assertEquals(0, report.getWarnings().size());
  }

  @Test
  public void testChecker_NoLang() throws Exception {
    GraalConfigChecker checker = new GraalConfigChecker();
    ConfigurationCheckReport report = new ConfigurationCheckReport();
    report.setCheckName(checker.getFriendlyName());
    // perhaps the end-of-days is upon us, if perl is a graal polyglot lang.
    checker.validate(createAdapterConfig("perl"), report);
    doLogging(report);
    assertFalse(report.isCheckPassed());
    assertEquals(0, report.getFailureExceptions().size());
    assertEquals(1, report.getWarnings().size());
  }

  @Test
  public void testWithException() throws Exception {
    GraalConfigChecker checker = new DefectiveChecker();
    ConfigurationCheckReport report = new ConfigurationCheckReport();
    report.setCheckName(checker.getFriendlyName());
    checker.validate(createAdapterConfig("js"), report);
    assertFalse(report.isCheckPassed());
    assertEquals(1, report.getFailureExceptions().size());
  }

  private void doLogging(ConfigurationCheckReport r) {
    System.err.println("---Warnings");
    System.err.println(r.getWarnings());
    System.err.println("---Exceptions");
    r.getFailureExceptions().forEach(Throwable::printStackTrace);
  }

  private Adapter createAdapterConfig(String lang) {
    Adapter result = new Adapter();
    result.setUniqueId(GUID.safeUUID());
    EmbeddedScriptingService service = new EmbeddedScriptingService().withScript(lang, "");
    service.setUniqueId(GUID.safeUUID());
    result.getSharedComponents().addService(service);
    return result;
  }

  private static class DefectiveChecker extends GraalConfigChecker {
    @Override
    protected ObjectScanner<ScriptingServiceImp> scanner() {
      throw new RuntimeException();
    }
  }



}
