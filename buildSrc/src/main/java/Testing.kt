object Testing {
    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Hilt.hiltVersion}"
    const val hiltTestCompiler = "com.google.dagger:hilt-android-compiler:${Hilt.hiltVersion}"

    private const val androidxTestExtVersion = "1.1.3"
    const val androidxTestExt = "androidx.test.ext:junit:$androidxTestExtVersion"

    private const val version = "1.3.0"
    const val androidXRunner = "androidx.test:runner:$version"

    private const val mockwebserver_version = "4.9.1"
    const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$mockwebserver_version"

    private const val robolectric_version = "4.6.1"
    const val robolectric = "org.robolectric:robolectric:$robolectric_version"

    private const val androidx_core_testing_version = "2.1.0"
    const val archCoreTesting = "androidx.arch.core:core-testing:$androidx_core_testing_version"

    private const val coroutines_test_version = "1.6.0"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutines_test_version"

    const val paging_test = "androidx.paging:paging-common:${Paging.paging_version}"

    private const val truth_version = "1.1.3"
    const val truth = "com.google.truth:truth:$truth_version"
}

object ComposeTest {
    const val uiTestJunit4 = "androidx.compose.ui:ui-test-junit4:${Compose.composeVersion}"
    const val uiTestManifest = "androidx.compose.ui:ui-test-manifest:${Compose.composeVersion}"
}

object Junit {
    private const val version = "4.13.2"
    const val junit4 = "junit:junit:$version"
}
