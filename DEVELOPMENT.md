# Developer notes

You don't need to have Graalvm installed to extend this package, but it might help. GraalVM has jsr223 bindings for javascript, so any java 11 with the appropriate jars will work.

## Publishing Artefacts.

[jitpack.io](https://jitpack.io/#monkbond/graalvm-binding) is where all the stuff is published, changes pushed to any branch end up with a version '[branch-name]-SNAPSHOT' which should be usable.


## Releases

Release _aren't yet_ automated, but could be via github actions; the key to that is knowing "when" to do a release, since releases here are probably tied to interlok versions, so things may not as easy as that.

Uses [axion-release-plugin](https://github.com/allegro/axion-release-plugin) to manage versioning; so you should read their [docs](https://axion-release-plugin.readthedocs.io/en/latest/)

Use `-Prelease.localOnly` if you don't want to automatically push changes to remote; basically, do that, otherwise you'll be deleting tags all day long. The two key commands are basically to mark the next version, and to formally tag and release the version. The order in which you do this should be self-explanatory.

- `./gradlew markNextVersion -Prelease.version=4.5.0 [-Prelease.localOnly]`
    - ends up with a semaphore tag of v4.5.0-alpha and ongoing version of 4.5.0-SNAPSHOT for graalvm-binding, and 4.5-SNAPSHOT for the underlying interlok version.
- `./gradlew release [-Prelease.localOnly]` will give you a v4.5.0 tag which you can then do a github release from.
    - Since jitpack releases are predicated on a github release, the tag means nothing, but there is extra logic to cope with `gradle currentVersion` reporting 4.5.0 as the version until there are additional commits. While unncessary, it disambiguates the version because no-one wants to rely on _develop-SNAPSHOT_ and then wonder why they keep getting _4.5.0-RELEASE_

