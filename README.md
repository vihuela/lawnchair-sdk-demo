# Lawnchair SDK Demo

<div align="right">
  <a href="#readme-zh">中文</a> | <a href="#readme-en">English</a>
</div>

<a id="readme-zh"></a>

## 中文

### 项目说明

这个 demo 用来验证外部宿主接入 `launcher-core` 和 `launcher-sdk-api`。

当前 SDK 分支基于 Lawnchair 15 分支族，来源于 upstream `15-dev` / `15-beta` 的共同基线，并在其上做了适合三方宿主 app 集成的 SDK 化改造。

### 相对原始 Lawnchair 的主要调整

#### 裁剪内容

- 裁剪了一般三方 app 不需要的系统权限、系统组件和部分系统能力。
- 去掉了 root、bugreport、部分 accessibility / sleep / SystemUI 强耦合能力。
- 精简了部分设置页、实验功能入口、关于页和调试入口。

#### 开放能力

- 负一屏能力：
  - 宿主可以注入自己的 Compose 负一屏内容。
  - 支持监听负一屏状态：完全打开、完全关闭、滑动中。
  - 支持桌面启动时直接进入负一屏。
  - 支持通过 SDK API 以动画方式打开负一屏。
- 桌面业务入口能力：
  - 宿主可以向桌面注入自定义 icon。
  - 支持指定页面、列、行锚点。
  - 支持配置 icon 是否允许拖动到删除区。
  - 支持点击后直接启动 `Intent`，或回调到业务侧自定义 action。
- 宿主操作能力：
  - 启动桌面。
  - 判断当前是否已设置为默认桌面。
  - 拉起系统默认桌面设置页。
  - 拉起 launcher 应用详情页。
- 桌面可见回调：
  - 支持监听桌面完全展示时机。
  - 支持回调来源：`ColdStart`、`MinusOneReturn`、`AllAppsReturn`、`OtherUiReturn`。
  - 回调会在桌面确认可见后再延后一拍派发，更适合业务侧做轻量弹框。
- 宿主 `Application` 兼容：
  - 宿主不需要继承 `app.lawnchair.LawnchairApp`。
  - 只需要在自己的 `Application.onCreate()` 里调用 `LawnchairSdkHost.initialize(this)`。

### 接入方式

#### 1. 添加依赖

当前 demo 默认走远程依赖：

```groovy
implementation("com.github.vihuela.lawnchair-sdk:launcher-core:<latest-version>")
implementation("com.github.vihuela.lawnchair-sdk:launcher-sdk-api:<latest-version>")
```

将 `<latest-version>` 替换为 JitPack 页面上的最新版本号：

- [https://jitpack.io/#com.github.vihuela/lawnchair-sdk](https://jitpack.io/#com.github.vihuela/lawnchair-sdk)

#### 2. 初始化 SDK

```kotlin
class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LawnchairSdkHost.initialize(this)
    }
}
```

#### 3. 提供宿主入口

宿主至少需要提供：

- 一个 `LawnchairSdkEntry`
- 一个业务侧 `MinusOneContentProvider`
- Manifest 中的 `app.lawnchair.sdk.entry` 元数据

#### 4. 常见能力用法

- 负一屏内容注入：实现 `MinusOneContentProvider`
- 负一屏状态监听：实现 `MinusOneOverlayStateListener`
- 桌面 icon 注入：实现 `DesktopItemProvider`
- 桌面 icon 禁止拖到删除区：给 `DesktopItemSpec` 传入 `allowDragToDelete = false`
- 桌面完全展示监听：实现 `DesktopVisibleListener`
- 打开负一屏动画：`LawnchairOperations.openMinusOneAnimated(context)`
- 拉起桌面：`LawnchairOperations.launchDesktop(context)`
- 请求设置默认桌面：`LawnchairOperations.requestSetDefaultLauncher(context)`

桌面 icon 注入示例：

```kotlin
DesktopItemSpec(
    title = "Demo Entry",
    iconResId = R.drawable.ic_demo_desktop_entry,
    clickAction = DesktopItemClickAction.LaunchIntent(intent),
    placement = DesktopItemPlacement(
        page = 1,
        column = 0,
        row = DesktopItemRowAnchor.FromBottom(0),
    ),
    allowDragToDelete = false,
)
```

桌面可见监听示例：

```kotlin
override val desktopVisibleListener: DesktopVisibleListener =
    DesktopVisibleListener { source ->
        Log.d("DemoLawnchairSdk", "Desktop fully visible, source=$source")
    }
```

### 本地源码联调

当前 demo 默认配置：

```properties
useLocalSourceSdk=false
```

也就是默认总是走远程依赖。

如果本机默认源码目录 `/Users/rickyyao/github/lawnchair/sdk-local` 存在，并且你显式传入 `-PuseLocalSourceSdk=true`，就会自动切到本地源码联调。

如果你想临时启用本地源码联调：

```bash
./gradlew -PuseLocalSourceSdk=true :app:assembleDebug
```

---

<a id="readme-en"></a>

## English

### Overview

This demo shows how a host Android app can integrate `launcher-core` and `launcher-sdk-api`.

The current SDK fork is based on the Lawnchair 15 branch family, starting from the shared upstream base of `15-dev` / `15-beta`, and then reshaped into an embeddable SDK for normal host apps.

### Main changes compared with upstream Lawnchair

#### Trimmed parts

- Removed permissions, manifest entries, and system-facing features that are usually unnecessary for a normal third-party app.
- Removed root, bugreport, and some accessibility / sleep / SystemUI-coupled capabilities.
- Simplified parts of settings, experimental entries, about pages, and debug surfaces.

#### Exposed capabilities

- Minus-one screen:
  - Host apps can provide their own Compose minus-one content.
  - Host apps can observe minus-one states: opened, closed, and sliding.
  - Launcher can optionally start directly on minus-one.
  - SDK API can open minus-one with launcher animation.
- Desktop business entries:
  - Host apps can inject custom workspace icons.
  - Placement supports page, column, and row anchor.
  - Each injected icon can control whether dragging to the delete area is allowed.
  - Click behavior can either launch an `Intent` directly or dispatch back to host-defined actions.
- Host operations:
  - Launch desktop.
  - Check whether the launcher is the current default home app.
  - Open the system default-home settings screen.
  - Open launcher app info.
- Desktop visible callback:
  - Host apps can observe when the desktop is fully visible.
  - Supported sources are `ColdStart`, `MinusOneReturn`, `AllAppsReturn`, and `OtherUiReturn`.
  - The callback is posted one main-thread turn after visibility is confirmed, which makes it safer for lightweight dialogs.
- Host `Application` compatibility:
  - The host app does not need to subclass `app.lawnchair.LawnchairApp`.
  - It only needs to call `LawnchairSdkHost.initialize(this)` from its own `Application.onCreate()`.

### Usage

#### 1. Add dependencies

This demo uses remote artifacts by default:

```groovy
implementation("com.github.vihuela.lawnchair-sdk:launcher-core:<latest-version>")
implementation("com.github.vihuela.lawnchair-sdk:launcher-sdk-api:<latest-version>")
```

Replace `<latest-version>` with the latest tag published on JitPack:

- [https://jitpack.io/#com.github.vihuela/lawnchair-sdk](https://jitpack.io/#com.github.vihuela/lawnchair-sdk)

#### 2. Initialize the SDK

```kotlin
class DemoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        LawnchairSdkHost.initialize(this)
    }
}
```

#### 3. Provide a host SDK entry

The host app should provide:

- A `LawnchairSdkEntry`
- A business-side `MinusOneContentProvider`
- An `app.lawnchair.sdk.entry` manifest metadata entry

#### 4. Common capability examples

- Provide minus-one UI: implement `MinusOneContentProvider`
- Observe minus-one state: implement `MinusOneOverlayStateListener`
- Inject desktop icons: implement `DesktopItemProvider`
- Prevent an injected desktop icon from being dragged to the delete area: set `allowDragToDelete = false` on `DesktopItemSpec`
- Observe full desktop visibility: implement `DesktopVisibleListener`
- Open minus-one with animation: `LawnchairOperations.openMinusOneAnimated(context)`
- Launch desktop: `LawnchairOperations.launchDesktop(context)`
- Request default launcher setup: `LawnchairOperations.requestSetDefaultLauncher(context)`

Desktop item example:

```kotlin
DesktopItemSpec(
    title = "Demo Entry",
    iconResId = R.drawable.ic_demo_desktop_entry,
    clickAction = DesktopItemClickAction.LaunchIntent(intent),
    placement = DesktopItemPlacement(
        page = 1,
        column = 0,
        row = DesktopItemRowAnchor.FromBottom(0),
    ),
    allowDragToDelete = false,
)
```

Desktop visible callback example:

```kotlin
override val desktopVisibleListener: DesktopVisibleListener =
    DesktopVisibleListener { source ->
        Log.d("DemoLawnchairSdk", "Desktop fully visible, source=$source")
    }
```

### Local source debugging

The demo currently defaults to:

```properties
useLocalSourceSdk=false
```

That means it uses remote dependencies by default.

If the default local source directory `/Users/rickyyao/github/lawnchair/sdk-local` exists, passing `-PuseLocalSourceSdk=true` will automatically switch the demo to local source integration.

To enable local `includeBuild` source debugging temporarily:

```bash
./gradlew -PuseLocalSourceSdk=true :app:assembleDebug
```
