package monkbond.scripting.graalvm;

import static com.adaptris.util.KeyValuePairBag.asMap;

import com.adaptris.util.KeyValuePairSet;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.io.File;
import java.util.Optional;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.EnvironmentAccess;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.PolyglotAccess;


/**
 * Exposes all the 'simple' configuration that's available in {@code Context.Builder}.
 * <p>The documentation (javadoc or otherwise) associated with {@code Context#Builder} is considered canonical
 * and you should consult that if you don't understand what the fields mean. Not all fields are exposed since it can be
 * difficult to configure arbitrary objects in Interlok configuration. Only members that are non-null will be invoked on
 * the underlying {@code Context#Builder}.</p>
 * <p>{@code ContextBuilder} remains an interface that you can write a custom immplementation for if the reference
 * implementations are not sufficient.
 * </p>
 *
 * @config graalvm-configured-context-builder
 */
@XStreamAlias("graalvm-configured-context-builder")
@NoArgsConstructor
public class ConfiguredContextBuilder extends ContextBuilderImpl {

  @Getter
  @Setter
  private Boolean allowNativeAccess;
  @Getter
  @Setter
  private Boolean allowCreateThread;
  @Getter
  @Setter
  private Boolean allowAllAccess;
  @Getter
  @Setter
  private Boolean allowIo;
  @Getter
  @Setter
  private Boolean allowHostClassLoading;
  @Getter
  @Setter
  private Boolean allowExperimentalOptions;
  @Getter
  @Setter
  private Boolean allowValueSharing;
  @Getter
  @Setter
  private PolyglotAccessEnum polyglotAccess;
  @Getter
  @Setter
  private HostAccessEnum hostAccess;
  @Getter
  @Setter
  private Boolean allowCreateProcess;
  @Getter
  @Setter
  private EnvironmentAccessEnum environmentAccess;
  @Getter
  @Setter
  private KeyValuePairSet environment;
  @Getter
  @Setter
  private KeyValuePairSet options;
  @Getter
  @Setter
  private String currentWorkingDirectory;

  @Override
  protected Context.Builder configure(Context.Builder builder) {
    Optional.ofNullable(getAllowNativeAccess()).ifPresent(builder::allowNativeAccess);
    Optional.ofNullable(getAllowCreateThread()).ifPresent(builder::allowCreateThread);
    Optional.ofNullable(getAllowAllAccess()).ifPresent(builder::allowAllAccess);
    Optional.ofNullable(getAllowIo()).ifPresent(builder::allowIO);
    Optional.ofNullable(getAllowHostClassLoading()).ifPresent(builder::allowHostClassLoading);
    Optional.ofNullable(getAllowExperimentalOptions()).ifPresent(builder::allowExperimentalOptions);
    Optional.ofNullable(getAllowValueSharing()).ifPresent(builder::allowValueSharing);
    Optional.ofNullable(getPolyglotAccess()).ifPresent((b) -> builder.allowPolyglotAccess(b.objectify()));
    Optional.ofNullable(getHostAccess()).ifPresent((b) -> builder.allowHostAccess(b.objectify()));

    Optional.ofNullable(getAllowCreateProcess()).ifPresent(builder::allowCreateProcess);
    Optional.ofNullable(getEnvironmentAccess()).ifPresent((b) -> builder.allowEnvironmentAccess(b.objectify()));
    Optional.ofNullable(getEnvironment()).ifPresent((kvps) -> builder.environment(asMap(kvps)));
    Optional.ofNullable(getOptions()).ifPresent((kvps) -> builder.options(asMap(kvps)));
    Optional.ofNullable(getCurrentWorkingDirectory())
        .ifPresent((p) -> builder.currentWorkingDirectory(new File(p).toPath()));
    return builder;
  }

  /**
   * Maps to the pre-configured enums in {@code PolyglotAccess}.
   */
  public enum PolyglotAccessEnum {
    NONE(PolyglotAccess.NONE),
    ALL(PolyglotAccess.ALL);

    private final PolyglotAccess access;

    PolyglotAccessEnum(PolyglotAccess a) {
      this.access = a;
    }

    PolyglotAccess objectify() {
      return access;
    }
  }

  /**
   * Maps to the pre-configured enums in {@code HostAccess}.
   */
  public enum HostAccessEnum {
    EXPLICIT(HostAccess.EXPLICIT),
    SCOPED(HostAccess.SCOPED),
    ALL(HostAccess.ALL),
    NONE(HostAccess.NONE);
    private final HostAccess access;

    HostAccessEnum(HostAccess a) {
      this.access = a;
    }

    HostAccess objectify() {
      return access;
    }
  }


  /**
   * Maps to the pre-configured enums in {@code EnvironmentAccess}.
   */
  public enum EnvironmentAccessEnum {
    NONE(EnvironmentAccess.NONE),
    INHERIT(EnvironmentAccess.INHERIT);
    private final EnvironmentAccess access;

    EnvironmentAccessEnum(EnvironmentAccess a) {
      this.access = a;
    }

    EnvironmentAccess objectify() {
      return access;
    }
  }
}
