package monkbond.scripting.graalvm;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.fs.FsHelper;
import com.adaptris.core.util.Args;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.graalvm.polyglot.Source;

/**
 * Execute a script using the polyglot features of GraalVM.
 * <p>This is functionally equivalent to {@link com.adaptris.core.services.ScriptingService}.</p>
 *
 * @config graalvm-scripting-service
 */
@XStreamAlias("graalvm-scripting-service")
@AdapterComponent
@ComponentProfile(summary = "Execute a script stored on the filesystem via the GraalVM Polyglot engine",
    tag = "graalvm,scripting", branchSelector = true, since = "4.5.0")
@DisplayOrder(order = {"filename", "language", "branchingEnabled"})
@NoArgsConstructor
public class ScriptingService extends ScriptingServiceImp {

  /**
   * The file that contains the script to be executed.
   */
  @Getter
  @Setter
  @NotBlank
  private String filename;

  @Override
  public void prepare() throws CoreException {
    Args.notBlank(getLanguage(), "language");
    Args.notBlank(getFilename(), "filename");
  }

  public ScriptingService withScript(String lang, String file) {
    setLanguage(lang);
    setFilename(file);
    return this;
  }

  @Override
  protected Source compile(AdaptrisMessage msg) throws Exception {
    return Source.newBuilder(getLanguage(), FsHelper.toFile(getFilename())).build();
  }
}
