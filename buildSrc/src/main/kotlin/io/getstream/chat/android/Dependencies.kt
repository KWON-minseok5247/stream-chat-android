@file:Suppress("RedundantVisibilityModifier")

package io.getstream.chat.android

object Versions {
    internal const val ANDROID_GRADLE_PLUGIN = "8.2.0"
    internal const val ANDROID_JUNIT5_GRADLE_PLUGIN = "1.9.3.0"
    internal const val ANDROID_LEGACY_SUPPORT = "1.0.0"
    internal const val ANDROIDX_ACTIVITY_COMPOSE = "1.7.2"
    internal const val ANDROIDX_ACTIVITY_KTX = "1.7.2"
    internal const val ANDROIDX_ANNOTATIONS = "1.6.0"
    internal const val ANDROIDX_APPCOMPAT = "1.6.1"
    internal const val ANDROIDX_COMPOSE = "1.6.2"
    public const val ANDROIDX_COMPOSE_COMPILER = "1.5.10"
    internal const val ANDROIDX_CORE_TEST = "2.2.0"
    internal const val ANDROIDX_FRAGMENT = "1.6.1"
    internal const val ANDROIDX_KTX = "1.10.1"
    internal const val ANDROIDX_LIFECYCLE = "2.6.1"
    internal const val ANDROIDX_NAVIGATION = "2.5.3"
    internal const val ANDROIDX_PREFERENCES = "1.2.1"
    internal const val ANDROIDX_RECYCLERVIEW = "1.3.1"
    internal const val ANDROIDX_STARTUP = "1.1.1"
    internal const val ANDROIDX_TEST_CORE = "1.5.0"
    internal const val ANDROIDX_TEST_JUNIT = "1.1.5"
    internal const val ANDROIDX_VIEW_PAGER_2 = "1.0.0"
    internal const val ANDROIDX_UI_AUTOMATOR = "2.2.0"
    internal const val ANDROIDX_TEST = "1.5.2"
    internal const val BASE_PROFILE = "1.3.1"
    internal const val COIL = "2.4.0"
    internal const val COMPOSE_ACCOMPANIST = "0.32.0"
    internal const val COMPOSE_STABLE_MARKER = "1.0.2"
    internal const val CONSTRAINT_LAYOUT = "2.1.4"
    internal const val COROUTINES = "1.7.3"
    internal const val DETEKT_PLUGIN = "1.23.1"
    internal const val DOKKA = "1.9.0"
    internal const val DOKKASAURUS = "0.1.10"
    internal const val ESPRESSO = "3.5.1"
    internal const val FIREBASE_ANALYTICS = "21.3.0"
    internal const val FIREBASE_CRASHLYTICS = "18.4.0"
    internal const val FIREBASE_CRASHLYTICS_PLUGIN = "2.9.7"
    internal const val FIREBASE_MESSAGING = "23.3.1"
    internal const val FLIPPER = "0.174.0"
    internal const val FLIPPER_SO_LOADER = "0.10.4"
    internal const val GITVERSIONER = "0.5.0"
    internal const val GOOGLE_SERVICES = "4.3.14"
    internal const val GRADLE_NEXUS_PUBLISH_PLUGIN = "1.3.0"
    internal const val GRADLE_VERSIONS_PLUGIN = "0.47.0"
    internal const val HUAWEI_PUSH = "6.11.0.300"
    internal const val ITU_DATE_VERSION = "1.7.3"
    internal const val JSON = "20230227"
    internal const val JUNIT4 = "4.13.2"
    internal const val JUNIT5 = "5.10.0"
    internal const val KEYBOARD_VISIBILITY_EVENT = "2.3.0"
    internal const val KLUENT = "1.73"
    internal const val KOTLIN = "1.9.22"
    internal const val KOTLIN_BINARY_VALIDATOR = "0.13.2"
    internal const val KSP = "1.9.22-1.0.17"
    internal const val LEAK_CANARY = "2.4"
    internal const val MATERIAL_COMPONENTS = "1.8.0"
    internal const val MACRO_BENCHMARK = "1.2.0"
    internal const val MARKWON = "4.6.2"
    internal const val MOCKITO_KOTLIN = "5.0.0"
    internal const val MOCKITO = "5.4.0"
    internal const val MOSHI = "1.15.0"
    internal const val OK2CURL = "0.8.0"
    internal const val OKHTTP = "4.11.0"
    internal const val PERMISSIONX = "1.7.1"
    internal const val PHOTOVIEW = "1.0.1"
    internal const val RETROFIT = "2.9.0"
    internal const val ROBOLECTRIC = "4.10.3"
    internal const val ROOM = "2.5.2"
    internal const val SHIMMER = "0.5.0"
    internal const val SHOT = "5.14.1"
    internal const val SPOTLESS = "6.20.0"
    internal const val STFALCON_IMAGE_VIEWER = "1.0.1"
    internal const val STREAM_LOG = "1.1.4"
    internal const val STREAM_PUSH = "1.1.7"
    internal const val STREAM_RESULT = "1.1.0"
    internal const val TEST_PARAMETER_INJECTOR = "1.12"
    internal const val THREETENBP = "1.6.8"
    internal const val TIMBER = "5.0.1"
    internal const val TURBINE = "1.0.0"
    internal const val WORK = "2.8.1"
}

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.ANDROID_GRADLE_PLUGIN}"
    const val androidJunit5GradlePlugin =
        "de.mannodermaus.gradle.plugins:android-junit5:${Versions.ANDROID_JUNIT5_GRADLE_PLUGIN}"
    const val androidLegacySupport = "androidx.legacy:legacy-support-v4:${Versions.ANDROID_LEGACY_SUPPORT}"
    const val androidxActivityKtx = "androidx.activity:activity-ktx:${Versions.ANDROIDX_ACTIVITY_KTX}"
    const val androidxAnnotations = "androidx.annotation:annotation:${Versions.ANDROIDX_ANNOTATIONS}"
    const val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.ANDROIDX_APPCOMPAT}"
    const val androidxArchCoreTest = "androidx.arch.core:core-testing:${Versions.ANDROIDX_CORE_TEST}"
    const val androidxCoreKtx = "androidx.core:core-ktx:${Versions.ANDROIDX_KTX}"
    const val androidxFragmentKtx = "androidx.fragment:fragment-ktx:${Versions.ANDROIDX_FRAGMENT}"
    const val androidxLifecycleProcess = "androidx.lifecycle:lifecycle-process:${Versions.ANDROIDX_LIFECYCLE}"
    const val androidxLifecycleLiveDataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.ANDROIDX_LIFECYCLE}"
    const val androidxLifecycleViewModelKtx =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.ANDROIDX_LIFECYCLE}"
    const val androidxPreferences = "androidx.preference:preference:${Versions.ANDROIDX_PREFERENCES}"
    const val androidxRecyclerview = "androidx.recyclerview:recyclerview:${Versions.ANDROIDX_RECYCLERVIEW}"
    const val androidxStartup = "androidx.startup:startup-runtime:${Versions.ANDROIDX_STARTUP}"
    const val androidxTest = "androidx.test:core:${Versions.ANDROIDX_TEST_CORE}"
    const val androidxTestRunner = "androidx.test:runner:${Versions.ANDROIDX_TEST}"
    const val androidxUiAutomator = "androidx.test.uiautomator:uiautomator:${Versions.ANDROIDX_UI_AUTOMATOR}"
    const val androidxTestKtx = "androidx.test:core-ktx:${Versions.ANDROIDX_TEST_CORE}"
    const val androidxLifecycleTesting = "androidx.lifecycle:lifecycle-runtime-testing:${Versions.ANDROIDX_LIFECYCLE}"
    const val androidxTestJunit = "androidx.test.ext:junit:${Versions.ANDROIDX_TEST_JUNIT}"
    const val androidxTestJunitKtx = "androidx.test.ext:junit-ktx:${Versions.ANDROIDX_TEST_JUNIT}"
    const val androidxViewPager2 = "androidx.viewpager2:viewpager2:${Versions.ANDROIDX_VIEW_PAGER_2}"
    const val baseProfile = "androidx.profileinstaller:profileinstaller:${Versions.BASE_PROFILE}"
    const val baselineProfilePlugin =
        "androidx.benchmark:benchmark-baseline-profile-gradle-plugin:${Versions.MACRO_BENCHMARK}"
    const val coil = "io.coil-kt:coil:${Versions.COIL}"
    const val coilGif = "io.coil-kt:coil-gif:${Versions.COIL}"
    const val coilVideo = "io.coil-kt:coil-video:${Versions.COIL}"
    const val composeCoil = "io.coil-kt:coil-compose:${Versions.COIL}"
    const val composeCompiler = "androidx.compose.compiler:compiler:${Versions.ANDROIDX_COMPOSE_COMPILER}"
    const val composeRuntime = "androidx.compose.runtime:runtime:${Versions.ANDROIDX_COMPOSE}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.ANDROIDX_COMPOSE}"
    const val composeUiTest = "androidx.compose.ui:ui-test-junit4:${Versions.ANDROIDX_COMPOSE}"
    const val composeUiTestManifest = "androidx.compose.ui:ui-test-manifest:${Versions.ANDROIDX_COMPOSE}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.ANDROIDX_COMPOSE}"
    const val composeFoundation = "androidx.compose.foundation:foundation:${Versions.ANDROIDX_COMPOSE}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.ANDROIDX_COMPOSE}"
    const val composeAccompanistPermissions =
        "com.google.accompanist:accompanist-permissions:${Versions.COMPOSE_ACCOMPANIST}"
    const val composeAccompanistPager = "com.google.accompanist:accompanist-pager:${Versions.COMPOSE_ACCOMPANIST}"
    const val composeAccompanistSystemUiController =
        "com.google.accompanist:accompanist-systemuicontroller:${Versions.COMPOSE_ACCOMPANIST}"
    const val composeActivity = "androidx.activity:activity-compose:${Versions.ANDROIDX_ACTIVITY_COMPOSE}"
    const val composeViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.ANDROIDX_LIFECYCLE}"
    const val composeStableMarker = "com.github.skydoves:compose-stable-marker:${Versions.COMPOSE_STABLE_MARKER}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINES}"
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINES}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINES}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}"
    const val detektPlugin = "io.gitlab.arturbosch.detekt:detekt-gradle-plugin:${Versions.DETEKT_PLUGIN}"
    const val detektFormatting = "io.gitlab.arturbosch.detekt:detekt-formatting:${Versions.DETEKT_PLUGIN}"
    const val dokka = "org.jetbrains.dokka:dokka-gradle-plugin:${Versions.DOKKA}"
    const val dokkasaurus = "io.getstream:dokkasaurus:${Versions.DOKKASAURUS}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.ESPRESSO}"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:${Versions.ESPRESSO}"
    const val espressoIdlingResources = "androidx.test.espresso:espresso-idling-resource:${Versions.ESPRESSO}"
    const val fragmentTest = "androidx.fragment:fragment-testing:${Versions.ANDROIDX_FRAGMENT}"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx:${Versions.FIREBASE_ANALYTICS}"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics:${Versions.FIREBASE_CRASHLYTICS}"
    const val firebaseCrashlyticsPlugin =
        "com.google.firebase:firebase-crashlytics-gradle:${Versions.FIREBASE_CRASHLYTICS_PLUGIN}"
    const val firebaseMessaging = "com.google.firebase:firebase-messaging:${Versions.FIREBASE_MESSAGING}"
    const val flipper = "com.facebook.flipper:flipper:${Versions.FLIPPER}"
    const val flipperNetwork = "com.facebook.flipper:flipper-network-plugin:${Versions.FLIPPER}"
    const val flipperLoader = "com.facebook.soloader:soloader:${Versions.FLIPPER_SO_LOADER}"
    const val gitversionerPlugin = "com.pascalwelsch.gitversioner:gitversioner:${Versions.GITVERSIONER}"
    const val googleServicesPlugin = "com.google.gms:google-services:${Versions.GOOGLE_SERVICES}"
    const val gradleNexusPublishPlugin = "io.github.gradle-nexus:publish-plugin:${Versions.GRADLE_NEXUS_PUBLISH_PLUGIN}"
    const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.GRADLE_VERSIONS_PLUGIN}"
    const val huaweiPush = "com.huawei.hms:push:${Versions.HUAWEI_PUSH}"
    const val ituDate = "com.ethlo.time:itu:${Versions.ITU_DATE_VERSION}"
    const val json = "org.json:json:${Versions.JSON}"
    const val junit4 = "junit:junit:${Versions.JUNIT4}"
    const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:${Versions.JUNIT5}"
    const val junitJupiterParams = "org.junit.jupiter:junit-jupiter-params:${Versions.JUNIT5}"
    const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:${Versions.JUNIT5}"
    const val junitVintageEngine = "org.junit.vintage:junit-vintage-engine:${Versions.JUNIT5}"
    const val keyboardVisibilityEvent =
        "net.yslibrary.keyboardvisibilityevent:keyboardvisibilityevent:${Versions.KEYBOARD_VISIBILITY_EVENT}"
    const val kluent = "org.amshove.kluent:kluent:${Versions.KLUENT}"
    const val kotlinBinaryValidator =
        "org.jetbrains.kotlinx:binary-compatibility-validator:${Versions.KOTLIN_BINARY_VALIDATOR}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.KOTLIN}"
    const val kotlinReflect = "org.jetbrains.kotlin:kotlin-reflect:${Versions.KOTLIN}"
    const val ksp = "com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:${Versions.KSP}"
    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.LEAK_CANARY}"
    const val macroBenchmark = "androidx.benchmark:benchmark-macro-junit4:${Versions.MACRO_BENCHMARK}"
    const val materialComponents = "com.google.android.material:material:${Versions.MATERIAL_COMPONENTS}"
    const val markwonCore = "io.noties.markwon:core:${Versions.MARKWON}"
    const val markwonLinkify = "io.noties.markwon:linkify:${Versions.MARKWON}"
    const val markwonextStrikethrough = "io.noties.markwon:ext-strikethrough:${Versions.MARKWON}"
    const val markwonImage = "io.noties.markwon:image:${Versions.MARKWON}"
    const val mockito = "org.mockito:mockito-core:${Versions.MOCKITO}"
    const val mockitoKotlin = "org.mockito.kotlin:mockito-kotlin:${Versions.MOCKITO_KOTLIN}"
    const val moshi = "com.squareup.moshi:moshi:${Versions.MOSHI}"
    const val moshiCodegen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.MOSHI}"
    const val moshiKotlin = "com.squareup.moshi:moshi-kotlin:${Versions.MOSHI}"
    const val moshiAdapters = "com.squareup.moshi:moshi-adapters:${Versions.MOSHI}"
    const val navigationFragmentKTX = "androidx.navigation:navigation-fragment-ktx:${Versions.ANDROIDX_NAVIGATION}"
    const val navigationSafeArgsGradlePlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.ANDROIDX_NAVIGATION}"
    const val navigationRuntimeKTX = "androidx.navigation:navigation-runtime-ktx:${Versions.ANDROIDX_NAVIGATION}"
    const val navigationTest = "androidx.navigation:navigation-testing:${Versions.ANDROIDX_NAVIGATION}"
    const val navigationUIKTX = "androidx.navigation:navigation-ui-ktx:${Versions.ANDROIDX_NAVIGATION}"
    const val ok2curl = "com.github.mrmike:ok2curl:${Versions.OK2CURL}"
    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.OKHTTP}"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.OKHTTP}"
    const val okhttpMockWebserver = "com.squareup.okhttp3:mockwebserver:${Versions.OKHTTP}"
    const val permissionx = "com.guolindev.permissionx:permissionx:${Versions.PERMISSIONX}"
    const val photoview = "io.getstream:photoview:${Versions.PHOTOVIEW}"
    const val photoviewDialog = "io.getstream:photoview-dialog:${Versions.PHOTOVIEW}"
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.RETROFIT}"
    const val retrofitMoshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.RETROFIT}"
    const val robolectric = "org.robolectric:robolectric:${Versions.ROBOLECTRIC}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.ROOM}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.ROOM}"
    const val roomRuntime = "androidx.room:room-runtime:${Versions.ROOM}"
    const val roomTesting = "androidx.room:room-testing:${Versions.ROOM}"
    const val shimmer = "com.facebook.shimmer:shimmer:${Versions.SHIMMER}"
    const val shot = "com.karumi:shot:${Versions.SHOT}"
    const val spotlessGradlePlugin = "com.diffplug.spotless:spotless-plugin-gradle:${Versions.SPOTLESS}"
    const val streamLog = "io.getstream:stream-log:${Versions.STREAM_LOG}"
    const val streamLogAndroid = "io.getstream:stream-log-android:${Versions.STREAM_LOG}"
    const val streamLogAndroidFile = "io.getstream:stream-log-android-file:${Versions.STREAM_LOG}"
    const val streamPush = "io.getstream:stream-android-push:${Versions.STREAM_PUSH}"
    const val streamPushPermissions = "io.getstream:stream-android-push-permissions:${Versions.STREAM_PUSH}"
    const val streamPushPermissionsSnackbar =
        "io.getstream:stream-android-push-permissions-snackbar:${Versions.STREAM_PUSH}"
    const val streamPushDelegate = "io.getstream:stream-android-push-delegate:${Versions.STREAM_PUSH}"
    const val streamPushFirebase = "io.getstream:stream-android-push-firebase:${Versions.STREAM_PUSH}"
    const val streamPushHuawei = "io.getstream:stream-android-push-huawei:${Versions.STREAM_PUSH}"
    const val streamPushXiaomi = "io.getstream:stream-android-push-xiaomi:${Versions.STREAM_PUSH}"
    const val streamResult = "io.getstream:stream-result:${Versions.STREAM_RESULT}"
    const val streamResultCall = "io.getstream:stream-result-call:${Versions.STREAM_RESULT}"
    const val testParameterInjector =
        "com.google.testparameterinjector:test-parameter-injector:${Versions.TEST_PARAMETER_INJECTOR}"
    const val threeTenBp = "org.threeten:threetenbp:${Versions.THREETENBP}"
    const val timber = "com.jakewharton.timber:timber:${Versions.TIMBER}"
    const val turbine = "app.cash.turbine:turbine:${Versions.TURBINE}"
    const val workRuntimeKtx = "androidx.work:work-runtime-ktx:${Versions.WORK}"
    const val workTesting = "androidx.work:work-testing:${Versions.WORK}"

    @JvmStatic
    fun isNonStable(version: String): Boolean = isStable(version).not()

    @JvmStatic
    fun isStable(version: String): Boolean = ("^[0-9.-]+$").toRegex().matches(version)
}
