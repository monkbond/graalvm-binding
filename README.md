# graalvm-binding

GraalVM is pretty awesome; and you want to embed Python in Interlok without trying to use `jython`, or you fancy some R or WASM (the list goes on).

## Installation

The implication of attempting to use this library is that you're going to be executing using GraalVM. The available services are functionally equivalent to the existing `com.adaptris.core.services.EmbeddedScriptingService` and `com.adaptris.core.services.ScriptingService` and are largely similar in configuration.

- Configure GraalVM for your polyglot language of choice.
- Configure and use the service.

## Additional notes

It is possible to use this service within a non-graal JRE but this may only allow you to use javascript as a language (check https://www.graalvm.org/22.0/reference-manual/js/RunOnJDK/). If you only intend on running javascript then you can just stick with graal.js as a ScriptEngine implementation.

If you're using the standard interlok build-parent mechanism then you get access to `GraalConfigChecker` which attempts to validate that the language requested is supported when creating a new Context. Since you may just be building a zip file for execution (rather than building for immediately execution) this only counts as a WARNING.

If you're already passing in system properties that have GraalVM side-effects (such as `-Dpolyglot.js.nashorn-compat=true`) then you may need to configure an explicit builder for each service to enable experimental options (this is done automatically for you by `DefaultContextBuilder` but only on the existence of that system property).