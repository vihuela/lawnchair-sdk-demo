# Lawnchair SDK Demo

这个 demo 用来验证外部宿主接入 `launcher-core` 和 `launcher-sdk-api`。

当前默认接法是远程依赖：

1. `app/build.gradle` 里直接依赖 JitPack 坐标。
2. `settings.gradle` 已经保留了本地 `includeBuild(...)` 示例，但默认是注释状态。
3. 宿主只需要提供：
   - 一个 `LawnchairSdkEntry`
   - 一个业务侧 `MinusOneContentProvider`
   - Manifest 里的 `app.lawnchair.sdk.entry` 元数据
4. 如果宿主已经有自己的 `Application`，建议继承 `app.lawnchair.LawnchairApp`。
   - demo 里的 `DemoApplication` 就是这个演示用法。

当前远程依赖坐标：

```groovy
implementation("com.github.vihuela.lawnchair-sdk:launcher-core:v0.1.3")
implementation("com.github.vihuela.lawnchair-sdk:launcher-sdk-api:v0.1.3")
```

如果你要本地联调 Lawnchair 源码，可以取消 `settings.gradle` 里的 `includeBuild(...)` 注释。
