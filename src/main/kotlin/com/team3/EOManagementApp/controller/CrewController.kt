package com.team3.EOManagementApp.controller

import com.team3.EOManagementApp.dto.CrewRequest
import com.team3.EOManagementApp.dto.CrewResponse
import com.team3.EOManagementApp.service.CrewService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/** Endpoint HTTP untuk pengelolaan kru dan penugasan alat. */
@RestController
@RequestMapping("/api/crews")
class CrewController(
    private val crewService: CrewService
) {

    /** GET /api/crews: mengembalikan seluruh data kru. */
    @GetMapping
    fun getAllCrews(): ResponseEntity<List<CrewResponse>> {
        val crews = crewService.getAllCrews()
        return ResponseEntity.ok(crews)
    }

    /** GET /api/crews/{id}: mengembalikan detail kru berdasarkan ID. */
    @GetMapping("/{id}")
    fun getCrewById(@PathVariable id: Long): ResponseEntity<CrewResponse> {
        val crew = crewService.getCrewById(id)
        return ResponseEntity.ok(crew)
    }

    /** POST /api/crews: membuat kru dari request dan mengembalikan HTTP 201. */
    @PostMapping
    fun createCrew(@Valid @RequestBody request: CrewRequest): ResponseEntity<CrewResponse> {
        val created = crewService.createCrew(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }

    /** PUT /api/crews/{id}: memperbarui kru berdasarkan ID dan isi request. */
    @PutMapping("/{id}")
    fun updateCrew(
        @PathVariable id: Long,
        @Valid @RequestBody request: CrewRequest
    ): ResponseEntity<CrewResponse> {
        val updated = crewService.updateCrew(id, request)
        return ResponseEntity.ok(updated)
    }

    /** DELETE /api/crews/{id}: menghapus kru dan mengembalikan pesan hasil. */
    @DeleteMapping("/{id}")
    fun deleteCrew(@PathVariable id: Long): ResponseEntity<Map<String, String>> {
        crewService.deleteCrew(id)
        return ResponseEntity.ok(mapOf("message" to "Kru dengan ID $id berhasil dihapus"))
    }

    /** Menugaskan alat dengan equipmentId kepada kru dengan crewId. */
    @PostMapping("/{crewId}/equipments/{equipmentId}")
    fun assignEquipment(
        @PathVariable crewId: Long,
        @PathVariable equipmentId: Long
    ): ResponseEntity<CrewResponse> {
        val updated = crewService.assignEquipmentToCrew(crewId, equipmentId)
        return ResponseEntity.ok(updated)
    }

    /** Melepas penugasan alat dengan equipmentId dari kru dengan crewId. */
    @DeleteMapping("/{crewId}/equipments/{equipmentId}")
    fun removeEquipment(
        @PathVariable crewId: Long,
        @PathVariable equipmentId: Long
    ): ResponseEntity<CrewResponse> {
        val updated = crewService.removeEquipmentFromCrew(crewId, equipmentId)
        return ResponseEntity.ok(updated)
    }

    /** Mengembalikan daftar kru yang ditugaskan pada equipmentId. */
    @GetMapping("/by-equipment/{equipmentId}")
    fun getCrewsByEquipment(@PathVariable equipmentId: Long): ResponseEntity<List<CrewResponse>> {
        val crews = crewService.getCrewsByEquipment(equipmentId)
        return ResponseEntity.ok(crews)
    }
}
