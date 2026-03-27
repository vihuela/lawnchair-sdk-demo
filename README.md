# Lawnchair SDK Demo

这个 demo 用来验证外部宿主接入 `launcher-core` 和 `launcher-sdk-api`。

当前接法：

1. `app/build.gradle` 里声明远程依赖坐标。
2. `settings.gradle` 通过 `includeBuild("/Users/rickyyao/github/lawnchair/sdk-local")` 把这两个坐标替换成本地源码。
3. 宿主只需要提供：
   - 一个 `LawnchairSdkEntry`
   - 一个业务侧 `MinusOneContentProvider`
   - Manifest 里的 `app.lawnchair.sdk.entry` 元数据
4. 如果宿主已经有自己的 `Application`，建议继承 `app.lawnchair.LawnchairApp`。
   - demo 里的 `DemoApplication` 就是这个演示用法。

当前这版接法不需要业务方额外声明 `Application` 子类，`launcher-core` 的 manifest 会合入 `app.lawnchair.LawnchairApp`。

后面如果要切到真正远程依赖，只需要把 `includeBuild(...)` 去掉，并把依赖指向发布后的 Maven 坐标。
