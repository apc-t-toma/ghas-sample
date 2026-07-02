package com.example.ghas

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.assertContains

/**
 * Test suite for DependencyUsageExamples
 *
 * Verifies that all five test scenarios (corresponding to different Dependabot PR Triage decisions)
 * execute successfully with the intentionally old library versions.
 */
@DisplayName("Dependency Usage Examples Tests")
class DependencyUsageExamplesTest {

    private val examples = DependencyUsageExamples()

    @Test
    @DisplayName("Scenario 1: Apache Commons Lang 3.x StringUtils demonstration")
    fun testCommonsLang3Demonstration() {
        val result = examples.demonstrateCommonsLang3()
        assertNotNull(result)
        assertTrue(result.contains("Commons Lang 3 Demo"))
        assertTrue(result.contains("StringUtils.isBlank()"))
    }

    @Test
    @DisplayName("Scenario 2: Log4j 1.2.17 logging demonstration")
    fun testLog4j1Demonstration() {
        val result = examples.demonstrateLog4j1()
        assertNotNull(result)
        assertTrue(result.contains("Log4j 1.2 Demo"))
        assertTrue(result.contains("old API"))
    }

    @Test
    @DisplayName("Scenario 3: Gson 2.8.9 JSON serialization demonstration")
    fun testGson2Demonstration() {
        val result = examples.demonstrateGson2()
        assertNotNull(result)
        assertTrue(result.contains("Gson 2.8 Demo"))
        assertTrue(result.contains("Serialized="))
    }

    @Test
    @DisplayName("Scenario 4: OkHttp 3.14.9 HTTP client demonstration")
    fun testOkHttp3Demonstration() {
        val result = examples.demonstrateOkHttp3()
        assertNotNull(result)
        assertTrue(result.contains("OkHttp 3.14 Demo"))
    }

    @Test
    @DisplayName("Scenario 5: HttpClient 4.5.13 HTTP client demonstration")
    fun testHttpClient4Demonstration() {
        val result = examples.demonstrateHttpClient4()
        assertNotNull(result)
        assertTrue(result.contains("HttpClient 4.5 Demo"))
    }

    @Test
    @DisplayName("All demonstrations run successfully")
    fun testRunAllDemos() {
        val results = examples.runAllDemos()
        assertNotNull(results)
        assertTrue(results.size >= 5, "Expected at least 5 demo results")

        // Verify each scenario is present
        val allResults = results.joinToString("\n")
        assertContains(allResults, "Commons Lang")
        assertContains(allResults, "Log4j")
        assertContains(allResults, "Gson")
        assertContains(allResults, "OkHttp")
        assertContains(allResults, "HttpClient")
    }
}
