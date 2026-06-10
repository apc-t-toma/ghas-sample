package com.example.ghas

import java.io.File
import java.security.MessageDigest
import java.sql.Connection
import java.sql.DriverManager

object CodeScanningIntentionalFindings {
    // Intentionally unsafe SQL construction for CodeQL verification.
    fun lookupUserIdByName(userInput: String): String {
        val conn: Connection = DriverManager.getConnection("jdbc:h2:mem:test")
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery("SELECT id FROM users WHERE name = '$userInput'")
        return if (rs.next()) rs.getString(1) else "not found"
    }

    // Intentionally unsafe command construction for CodeQL verification.
    fun runShellCommand(userInput: String): Process {
        return Runtime.getRuntime().exec("sh -c \"echo $userInput\"")
    }

    // Intentionally weak digest algorithm for CodeQL verification.
    fun md5Hex(input: String): String {
        val digest = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        return digest.joinToString("") { "%02x".format(it) }
    }

    // Intentionally unsafe path composition for CodeQL verification.
    fun readUserRequestedFile(userPath: String): String {
        return File("/tmp/$userPath").readText()
    }

    // Intentionally hardcoded credential for CodeQL verification.
    fun basicCredential(): String {
        val username = "admin"
        val password = "hardcoded-password"
        return "$username:$password"
    }
}
