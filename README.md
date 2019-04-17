# lang-kotlin
A Kotlin language runtime module for NetKernel.

## Example Scripts

`res:/experiment/one.nk.kts`:
```kotlin
val req = context.createRequest("active:kotlin")
req.addArgument("operator", "res:/experiment/two.nk.kts")
req.addArgumentByValue("recipient", "world")

context.createResponseFrom(context.issueRequestForResponse(req))
```

`res:/experiment/two.nk.kts`:
```kotlin
val recipient = context.source("arg:recipient", String::class.java)

context.createResponseFrom("Hello $recipient")
```

Running `active:kotlin+operator@res:/experiment/one.nk.kts` will result in:
```
Hello world
```

## IDE Code Completion

### IntelliJ

In order to get Code Completion working correctly, you will need to:

* use `.nk.kts` as the extension for your Kotlin Script files,
* have `urn.org.netkernel.lang.kotlin-1.0.0.jar` as a compile dependency,
* in *Preferences > Build, Execution, Deployment > Compiler > Kotlin Compiler*,
  set the following settings in the 'Kotlin Scripting' section:
  * 'Script template classes': `org.netkernel.lang.kotlin.NetKernelKotlinScript`,
  * 'Script template classpath': `/path/to/urn.org.netkernel.lang.kotlin-1.0.0.jar`.
* The above should lead to a row for 'NetKernel Kotlin Script' appearing in
  *Language & Frameworks > Kotlin > Kotlin Scripting*
