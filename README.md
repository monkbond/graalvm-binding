# graalvm-binding

GraalVM is pretty awesome; and you want to embed Python in Interlok without trying to use `jython`, or R or WASM (the list goes on).

## Installation

The implication of attempting to use this library is that you're going to be executing using GraalVM. The available services are functionally equivalent to the existing `com.adaptris.core.services.EmbeddedScriptingService` and `com.adaptris.core.services.ScriptingService` and are largely similar in configuration.

- Configure GraalVM for your polyglot language of choice.
- Configure and use the service.


## Additional notes

If you're using the standard interlok build-parent mechanism then you get access to `GraalConfigChecker` which attempts to validate that you are in fact running under a GraalVM. Since you may just be building a zip file for execution (rather than building for immediately execution) this only counts as a WARNING.

If you're already passing in system properties that control GraalVM (such as `-Dpolyglot.js.nashorn-compat=true`) then you may need to configure an explicit builder for each service to enable experimental options.