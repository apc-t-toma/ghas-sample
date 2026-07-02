package com.example.ghas

import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients

/**
 * Demonstrates usage of intentionally old library versions for Dependabot PR Triage verification.
 *
 * Test Scenarios:
 * 1. Apache Commons Lang 3.12.0 - Breaking change scenario (3.x -> 4.x API changes)
 * 2. Log4j 1.2.17 - Known vulnerability scenario (CVE-2021-44228 and related)
 * 3. Gson 2.8.9 - Deprecated API usage scenario
 * 4. OkHttp 3.14.9 - Security patch / minor version scenario
 * 5. HttpClient 4.5.13 - Minor version compatibility scenario (4.5.x series)
 */

class DependencyUsageExamples {

    companion object {
        private val logger = Logger.getLogger(DependencyUsageExamples::class.java)
    }

    /**
     * Scenario 1: Apache Commons Lang 3.12.0
     * Test Case: Breaking change (3.x -> 4.x API changes)
     *
     * Expected Merge Decision: Merge Blocked (API incompatibility requires source code changes)
     * Expected Incident Decision: Not Required (no security vulnerability, only API changes)
     *
     * This demonstrates a scenario where upgrading requires changes to the application code,
     * because the StringUtils API has evolved between major versions.
     */
    fun demonstrateCommonsLang3(): String {
        // StringUtils.isBlank() is available in both 3.x and 4.x (compatible)
        val result1 = StringUtils.isBlank("  ")
        val result2 = StringUtils.isBlank("hello")

        // In Commons Lang 3.x, CharRange.toString() returns something like "a-z"
        // In Commons Lang 4.x, CharRange was moved/removed
        // This serves as a marker for demonstrating breaking changes
        val stringUtilsMethod = "StringUtils.isBlank() is compatible"

        return "Commons Lang 3 Demo: blank check=$result1, non-blank=$result2. Notes: $stringUtilsMethod"
    }

    /**
     * Scenario 2: Log4j 1.2.17
     * Test Case: Known vulnerability (CVE-2021-44228 - Log4Shell)
     *
     * Expected Merge Decision: Conditional Hold or Merge Blocked (depends on exploit feasibility)
     * Expected Incident Decision: Incident Review Required (known severe vulnerability)
     *
     * Log4j 1.2.17 is an old version that may have dependencies with known vulnerabilities.
     * This demonstrates a scenario where the dependency update is critical for security reasons.
     */
    fun demonstrateLog4j1(): String {
        logger.info("Logging with Log4j 1.2.17 - demonstrating old logging implementation")
        logger.warn("This version may have known vulnerabilities")

        // Simulate a log message that would be vulnerable in certain configurations
        val userInput = "test-value"
        logger.info("User submitted: $userInput")

        return "Log4j 1.2 Demo: Logged messages with old API"
    }

    /**
     * Scenario 3: Gson 2.8.9
     * Test Case: Deprecated API usage
     *
     * Expected Merge Decision: Conditional Hold (needs validation for deprecated API migration)
     * Expected Incident Decision: Not Required (no security vulnerability, only API changes)
     *
     * Gson 2.8.9 has certain APIs that were marked as deprecated in newer versions.
     * This demonstrates a scenario where code works but uses APIs that are no longer recommended.
     */
    fun demonstrateGson2(): String {
        val gson = Gson()

        // Define a sample data class for JSON serialization
        data class Person(val name: String, val age: Int)

        val person = Person("Alice", 30)

        // toJson() is available in both 2.8.9 and newer versions (compatible)
        val jsonString = gson.toJson(person)

        // fromJson() is available in both versions (compatible)
        val deserializedPerson = gson.fromJson(jsonString, Person::class.java)

        return "Gson 2.8 Demo: Serialized=$jsonString, Deserialized=${deserializedPerson.name}"
    }

    /**
     * Scenario 4: OkHttp 3.14.9
     * Test Case: Security patch / minor version update
     *
     * Expected Merge Decision: Merge Recommended (minor version upgrade, no breaking changes)
     * Expected Incident Decision: Not Required (security patches are routine updates)
     *
     * OkHttp 3.14.9 demonstrates a stable minor version within a major version series.
     * Upgrades within the 3.x series should be safe without code changes.
     */
    fun demonstrateOkHttp3(): String {
        val client = OkHttpClient()

        try {
            // Create a simple GET request to a public endpoint
            val request = Request.Builder()
                .url("https://www.google.com")
                .build()

            // Execute the request (in real scenarios, this would be async)
            val response = client.newCall(request).execute()

            val statusInfo = if (response.isSuccessful) "success" else "failure"
            return "OkHttp 3.14 Demo: HTTP request $statusInfo"
        } catch (e: Exception) {
            return "OkHttp 3.14 Demo: Request failed (${e.message})"
        }
    }

    /**
     * Scenario 5: HttpClient 4.5.13
     * Test Case: Minor version compatibility (4.5.x series)
     *
     * Expected Merge Decision: Merge Recommended (minor version within stable 4.5.x series)
     * Expected Incident Decision: Not Required (routine patch updates)
     *
     * HttpClient 4.5.13 is part of the stable 4.5.x series. Updates within this series
     * should maintain full compatibility with existing code.
     */
    fun demonstrateHttpClient4(): String {
        val httpClient = HttpClients.createDefault()

        try {
            val request = HttpGet("https://www.google.com")

            val response = httpClient.execute(request)
            val statusCode = response.statusLine.statusCode

            return "HttpClient 4.5 Demo: HTTP Status=$statusCode"
        } catch (e: Exception) {
            return "HttpClient 4.5 Demo: Request failed (${e.message})"
        } finally {
            httpClient.close()
        }
    }

    /**
     * Utility method to run all demonstrations sequentially
     */
    fun runAllDemos(): List<String> {
        val results = mutableListOf<String>()

        results.add("=== Scenario 1: Commons Lang 3.x -> 4.x Breaking Changes ===")
        results.add(demonstrateCommonsLang3())

        results.add("\n=== Scenario 2: Log4j 1.2.17 Known Vulnerability ===")
        results.add(demonstrateLog4j1())

        results.add("\n=== Scenario 3: Gson 2.8.9 Deprecated APIs ===")
        results.add(demonstrateGson2())

        results.add("\n=== Scenario 4: OkHttp 3.14.9 Security Patch ===")
        results.add(demonstrateOkHttp3())

        results.add("\n=== Scenario 5: HttpClient 4.5.13 Minor Version ===")
        results.add(demonstrateHttpClient4())

        return results
    }
}
