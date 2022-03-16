package monkbond.scripting.graalvm;

import com.adaptris.annotation.AdvancedConfig;
import com.adaptris.annotation.InputFieldDefault;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.BranchingServiceCollection;
import com.adaptris.core.CoreException;
import com.adaptris.core.DynamicPollingTemplate;
import com.adaptris.core.ServiceException;
import com.adaptris.core.ServiceImp;
import com.adaptris.core.util.ExceptionHelper;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

public abstract class ScriptingServiceImp extends ServiceImp implements DynamicPollingTemplate.TemplateProvider {

  private static final ContextBuilder DEFAULT_BUILDER = new DefaultContextBuilder();

  /**
   * The language that is going to used.
   */
  @Getter
  @Setter
  @NotBlank
  private String language;

  /**
   * Whether or not this service is branching so it can be used as part of a {@link BranchingServiceCollection}.
   */
  @InputFieldDefault(value = "false")
  @AdvancedConfig
  @Getter
  @Setter
  private Boolean branchingEnabled;

  /**
   * An explicit {@code ContextBuilder} that configures the polyglot context prior to script evaluation.
   * <p>The default of not explicitly configured is a builder that eseentially does {@code
   * Context.newBuilder().allowHostAccess(HostAccess.ALL).build()}. The default is always {@code allowHostAccess=all}
   * since we are passing the message object as a binding to the script.
   * </p>
   */
  @AdvancedConfig
  @Getter
  @Setter
  private ContextBuilder contextBuilder;

  @Override
  protected void initService() throws CoreException {  }

  @Override
  protected void closeService() {  }

  @Override
  public boolean isBranching() {
    return BooleanUtils.toBooleanDefaultIfNull(getBranchingEnabled(), false);
  }

  @Override
  public void doService(AdaptrisMessage msg) throws ServiceException {
    // We create a new context every time because we add msg in as a binding.
    try (Context context = createContext(msg)) {
      context.eval(compile(msg));
    } catch (Exception e) {
      throw ExceptionHelper.wrapServiceException(e);
    }
  }

  /** Compile into a {@code Source} for evaluation.
   *
   */
  protected abstract Source compile(AdaptrisMessage msg) throws Exception;

  /** Convenience method to create the context.
   *
   */
  protected Context createContext(AdaptrisMessage msg) {
    return addBindings(contextBuilder().build(), msg);
  }

  /** Binds various objects to the context.
   *  <p>
   *    <ul>
   *      <li>Binds the {@link AdaptrisMessage} against {@code message}</li>
   *      <li>Binds a SLF4J Logger against {@code log}</li>
   *    </ul>
   *  </p>
   */
  protected Context addBindings(Context context, AdaptrisMessage msg) {
    Value bindings = context.getBindings(getLanguage());
    bindings.putMember("message", msg);
    bindings.putMember("log", log);
    return context;
  }

  private ContextBuilder contextBuilder() {
    return ObjectUtils.defaultIfNull(getContextBuilder(), DEFAULT_BUILDER);
  }
}
