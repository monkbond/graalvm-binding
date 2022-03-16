package monkbond.scripting.graalvm;

import org.graalvm.polyglot.Context;

@FunctionalInterface
public interface ContextBuilder {

  /** Build a {@code  org.graalvm.polyglot.Context}.
   *
   * @return a {@code org.graalvm.polyglot.Context}
   */
  Context build();

}
