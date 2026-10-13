package com.team3.EOManagementApp.dto

import jakarta.validation.constraints.NotBlank

/** Data input pembuatan atau perubahan kru dan penugasan alat. */
data class CrewRequest(
    @field:NotBlank(message = "Nama kru tidak boleh kosong")
    val name: String,

    @field:NotBlank(message = "Nomor telepon tidak boleh kosong")
    val phone: String,

    @field:NotBlank(message = "Role / Jabatan tidak boleh kosong")
    val role: String,

    val equipmentIds: List<Long>? = emptyList()
)

/** Data kru dan daftar alat yang dikirim melalui API. */
data class CrewResponse(
    val id: Long,
    val name: String,
    val phone: String,
    val role: String,
    val assignedEquipments: List<SimpleEquipmentResponse> = emptyList()
)

/** Ringkasan kru untuk ditampilkan dalam relasi data lain. */
data class SimpleCrewResponse(
    val id: Long,
    val name: String,
    val phone: String,
    val role: String
)
