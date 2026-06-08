package com.example.ghas.vulnerable

import java.io.File
import java.security.MessageDigest
import java.sql.Connection
import java.sql.DriverManager
import java.util.Random

object VulnerableSamples {
    // SQL injection anti-pattern used for GHAS detection testing.
    fun findUserByName(userInput: String): String {
        val conn: Connection = DriverManager.getConnection("jdbc:h2:mem:test")
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("SELECT id FROM users WHERE name = '$userInput'")
        return if (rs.next()) rs.getString(1) else "not found"
    }

    // Command injection anti-pattern used for GHAS detection testing.
    fun runCommand(userInput: String): Process {
        return Runtime.getRuntime().exec("sh -c \"echo $userInput\"")
    }

    // Weak crypto anti-pattern used for GHAS detection testing.
    fun md5Hex(input: String): String {
        val digest = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    // Path traversal anti-pattern used for GHAS detection testing.
    fun readFileFromUserPath(userPath: String): String {
        return File("/tmp/$userPath").readText()
    }

    // Insecure randomness anti-pattern used for GHAS detection testing.
    fun weakToken(): String {
        val random = Random()
        return List(24) { ('a' + random.nextInt(26)) }.joinToString("")
    }

    // Hardcoded credential anti-pattern used for GHAS detection testing.
    fun basicAuthHeader(): String {
        val username = "admin"
        val password = "hardcoded-password"
        return "Basic $username:$password"
    }
}
