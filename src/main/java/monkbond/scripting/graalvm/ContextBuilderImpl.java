package monkbond.scripting.graalvm;

import java.util.Properties;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.graalvm.polyglot.Context;

@NoArgsConstructor
public abstract class ContextBuilderImpl implements ContextBuilder {

  @Override
  public Context build() {
    return configure(enforcedOptions(Context.newBuilder(), System.getProperties())).build();
  }

  protected abstract Context.Builder configure(Context.Builder builder);

  /** If properties contains {@code polyglot.js.nashorn-compat=true} then experimental options must be turned on
   * <p>Still true as of GraalVM 22</p>
   */
  protected static Context.Builder enforcedOptions(Context.Builder builder, Properties props) {
    Properties p = ObjectUtils.defaultIfNull(props, System.getProperties());
    if (BooleanUtils.toBoolean(p.getProperty("polyglot.js.nashorn-compat", "false"))) {
      // js.nashorn-compat requires allowExperimental = true.
      builder.allowExperimentalOptions(true);
    }
    return builder;
  }
}
