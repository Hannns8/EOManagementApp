package com.team3.EOManagementApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/** Konfigurasi utama dan pemindaian komponen Spring Boot. */
@SpringBootApplication
class EoManagementAppApplication

/** Menjalankan aplikasi Spring Boot dengan argumen dari terminal. */
fun main(args: Array<String>) {
	runApplication<EoManagementAppApplication>(*args)
}
