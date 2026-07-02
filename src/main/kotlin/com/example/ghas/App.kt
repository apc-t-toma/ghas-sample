package com.example.ghas

object App {
    @JvmStatic
    fun main(args: Array<String>) {
        println("GHAS Kotlin sample project")
        println()
        println("Running Dependabot PR Triage test scenarios...")
        println()

        val examples = DependencyUsageExamples()
        val results = examples.runAllDemos()

        results.forEach { result ->
            println(result)
        }

        println()
        println("All test scenarios completed successfully.")
    }
}
