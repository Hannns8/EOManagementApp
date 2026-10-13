package com.team3.EOManagementApp.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/** Kesalahan HTTP 404 untuk data yang tidak ditemukan. */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)
