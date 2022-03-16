/*
 * Copyright 2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package monkbond.scripting.graalvm;

import com.adaptris.annotation.AdapterComponent;
import com.adaptris.annotation.ComponentProfile;
import com.adaptris.annotation.DisplayOrder;
import com.adaptris.annotation.MarshallingCDATA;
import com.adaptris.core.AdaptrisMessage;
import com.adaptris.core.CoreException;
import com.adaptris.core.util.Args;
import com.adaptris.core.util.LoggingHelper;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.graalvm.polyglot.Source;

/**
 * Execute a script using the polyglot features of GraalVM.
 * <p>This is functionally equivalent to {@link com.adaptris.core.services.EmbeddedScriptingService}.</p>
 *
 * @config graalvm-embedded-scripting-service
 */
@XStreamAlias("graalvm-embedded-scripting-service")
@AdapterComponent
@ComponentProfile(summary = "Execute an embedded script via the GraalVM Polyglot engine",
    tag = "graalvm,scripting", branchSelector = true, since = "4.5.0")
@DisplayOrder(order = {"script", "language", "branchingEnabled"})
@NoArgsConstructor
public class EmbeddedScriptingService extends ScriptingServiceImp {

  @Getter
  @Setter
  @NotBlank
  @MarshallingCDATA
  private String script;

  @Override
  public void prepare() throws CoreException {
    Args.notBlank(getLanguage(), "language");
    Args.notBlank(getScript(), "script");
  }

  public EmbeddedScriptingService withScript(String lang, String script) {
    setLanguage(lang);
    setScript(script);
    return this;
  }

  @Override
  protected Source compile(AdaptrisMessage msg) throws Exception {
    return Source.newBuilder(getLanguage(), getScript(), LoggingHelper.friendlyName(this)).build();
  }
}
