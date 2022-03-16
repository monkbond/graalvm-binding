package monkbond.scripting.graalvm;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.NoArgsConstructor;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;

/**
 * The default context builder.
 * <p>Is included for completeness, but only contains default behaviour which equates to {@code
 * Context.newBuilder().allowHostAccess(HostAccess.ALL).build()}. The default is {@code allowHostAccess=all} since we
 * are passing the message object as a binding to the script. If the system property {@code polyglot.js.nashorn-compat}
 * is set to true, then `Builder#allowExperimentalOptions` is set to true to avoid complications.
 * </p>
 *
 * @config graalvm-default-context-builder
 */
@XStreamAlias("graalvm-default-context-builder")
@NoArgsConstructor
public class DefaultContextBuilder extends ContextBuilderImpl {

  @Override
  protected Context.Builder configure(Context.Builder builder) {
    return builder.allowHostAccess(HostAccess.ALL);
  }
}
