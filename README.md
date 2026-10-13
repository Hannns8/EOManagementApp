# EOManagementApp

## Project Name

**EOManagementApp** — aplikasi manajemen event organizer milik Team 3.
Package utama: `com.team3.EOManagementApp`.

## Description

Aplikasi untuk mengelola inventaris peralatan, data kru, dan jadwal event.
Data alat dan kru dapat dihubungkan ke jadwal agar kebutuhan acara tercatat dalam satu sistem.

## Features

- CRUD peralatan: nama, kategori, deskripsi, jumlah stok, dan kondisi.
- Pencarian alat berdasarkan nama dan kategori.
- Perhitungan stok tersedia dari total stok dikurangi pinjaman pada jadwal aktif.
- CRUD kru dan penugasan alat kepada kru.
- CRUD jadwal event beserta tanggal, jam, lokasi, peralatan, dan kru.
- Filter jadwal berdasarkan rentang tanggal atau status.
- Status jadwal: `UPCOMING`, `ONGOING`, `COMPLETED`, dan `CANCELLED`.
- UI web, validasi input, serta dokumentasi API melalui Swagger.

## Tech Stack

| Teknologi | Penggunaan |
| --- | --- |
| Kotlin 2.3.21 dan JDK 25 | Bahasa dan runtime backend |
| Spring Boot 4.1.1 / Spring Web MVC | Aplikasi dan REST API |
| Spring Data JPA / Hibernate | Pemetaan entitas dan akses database |
| MySQL / MySQL Connector/J | Penyimpanan data |
| Spring Security dan Jakarta Validation | Konfigurasi akses dan validasi input |
| HTML, CSS, JavaScript, Bootstrap, Font Awesome | Antarmuka web |
| springdoc OpenAPI / Swagger UI | Dokumentasi API |
| Gradle Wrapper 9.7.1 | Build dan pengelolaan dependency |
| JUnit 5 / Spring Boot Test | Pengujian konteks aplikasi |

## Installation

1. Siapkan JDK 25 dan jalankan server MySQL lokal.
2. Buka folder project yang berisi `build.gradle.kts` dan `gradlew.bat` di IDE atau terminal.
3. Sesuaikan pengaturan database pada bagian **Configuration**.
4. Jalankan melalui PowerShell:

```powershell
# Sesuaikan JAVA_HOME dengan lokasi JDK 25 di komputer Anda.
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-25.0.4.1'
.\gradlew.bat bootRun
```

Gradle akan mengunduh dependency pada penggunaan pertama; koneksi internet diperlukan.
Buka UI di [http://localhost:8080](http://localhost:8080).

Untuk menjalankan test dan membuat JAR:

```powershell
.\gradlew.bat test bootJar
```

Test konteks memakai konfigurasi database aplikasi, sehingga MySQL harus aktif.
JAR dihasilkan pada `build/libs/EOManagementApp-0.0.1-SNAPSHOT.jar`.

## Configuration

Konfigurasi publik terdapat pada `src/main/resources/application.properties`:

```properties
spring.application.name=EOManagementApp
spring.config.import=optional:file:./application-local.properties
server.port=8080

spring.datasource.url=${DB_URL:jdbc:mysql://localhost:3306/eo_management_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true}
spring.datasource.username=${DB_USERNAME:root}
spring.datasource.password=${DB_PASSWORD:}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.open-in-view=false
```

Untuk pengaturan komputer sendiri, buat file lokal dari template:

```powershell
Copy-Item .\application-local.properties.example .\application-local.properties
```

Isi alamat server, username, dan password pada `application-local.properties` di folder project.
File ini diabaikan Git dan berada di luar resource yang dikemas ke JAR.
Template `.example` aman dibagikan karena tidak berisi password.

Database `eo_management_db` dibuat otomatis saat koneksi berhasil dan akun memiliki izin membuat database.
Hibernate membuat atau memperbarui tabel melalui `ddl-auto=update`.
Data dari database sebelumnya tidak otomatis dipindahkan.

`JAVA_HOME` digunakan untuk memilih JDK. Jika tidak memakai file lokal, pengaturan database
dapat diisi melalui environment variable `DB_URL`, `DB_USERNAME`, dan `DB_PASSWORD`.
Jika port 8080 sudah dipakai, hentikan instance sebelumnya atau ubah `server.port`.

`.gitignore` mengecualikan kredensial lokal, `.env`, kunci privat, log, database lokal,
unggahan pengguna, folder IDE, serta hasil build/cache.

## Project Structure

```text
EOManagementApp/
├── build.gradle.kts
├── settings.gradle.kts
├── gradlew / gradlew.bat
├── gradle/
├── README.md
├── application-local.properties.example
└── src/
    ├── main/
    │   ├── kotlin/com/team3/EOManagementApp/
    │   │   ├── EoManagementAppApplication.kt
    │   │   ├── config/       # Keamanan dan CORS
    │   │   ├── controller/   # Endpoint HTTP
    │   │   ├── dto/          # Data request dan response
    │   │   ├── entity/       # Pemetaan tabel database
    │   │   ├── exception/    # Penanganan kesalahan API
    │   │   ├── repository/   # Akses database
    │   │   └── service/      # Logika aplikasi
    │   └── resources/
    │       ├── application.properties
    │       └── static/index.html
    └── test/kotlin/com/team3/EOManagementApp/
        └── EoManagementAppApplicationTests.kt
```

## API Documentation

Base URL: `http://localhost:8080`. Body POST/PUT menggunakan JSON dengan header `Content-Type: application/json`.
Endpoint saat ini dapat diakses tanpa login.

- [Swagger UI](http://localhost:8080/swagger-ui/index.html)
- [OpenAPI JSON](http://localhost:8080/v3/api-docs)

### Equipment Tools

| Method | Endpoint | Fungsi |
| --- | --- | --- |
| GET | `/api/equipment` | Daftar alat; filter opsional `category` dan `search` |
| GET | `/api/equipment/{id}` | Detail alat |
| POST | `/api/equipment` | Tambah alat |
| PUT | `/api/equipment/{id}` | Ubah alat |
| DELETE | `/api/equipment/{id}` | Hapus alat |

Contoh body POST/PUT:

```json
{
  "name": "Speaker Aktif",
  "category": "Audio",
  "description": "Speaker untuk acara kampus",
  "totalQuantity": 10,
  "conditionStatus": "Bagus"
}
```

### Crew

| Method | Endpoint | Fungsi |
| --- | --- | --- |
| GET | `/api/crews` | Daftar kru |
| GET | `/api/crews/{id}` | Detail kru |
| POST | `/api/crews` | Tambah kru |
| PUT | `/api/crews/{id}` | Ubah kru |
| DELETE | `/api/crews/{id}` | Hapus kru |
| POST | `/api/crews/{crewId}/equipments/{equipmentId}` | Tugaskan alat kepada kru |
| DELETE | `/api/crews/{crewId}/equipments/{equipmentId}` | Lepas penugasan alat |
| GET | `/api/crews/by-equipment/{equipmentId}` | Cari kru berdasarkan alat |

Contoh body POST/PUT:

```json
{
  "name": "Kru Audio",
  "phone": "081234567890",
  "role": "Operator Audio",
  "equipmentIds": [1]
}
```

### Calendar Schedule

| Method | Endpoint | Fungsi |
| --- | --- | --- |
| GET | `/api/schedules` | Daftar jadwal; filter opsional `startDate`, `endDate`, atau `status` |
| GET | `/api/schedules/{id}` | Detail jadwal |
| POST | `/api/schedules` | Tambah jadwal |
| PUT | `/api/schedules/{id}` | Ubah jadwal, alat, dan kru |
| PATCH | `/api/schedules/{id}/status` | Ubah status melalui query, misalnya `?status=ONGOING` |
| DELETE | `/api/schedules/{id}` | Hapus jadwal |

Filter tanggal membutuhkan `startDate` dan `endDate`. Jika keduanya diisi, rentang tanggal diprioritaskan atas filter status.
Format tanggal: `YYYY-MM-DD`; format jam: `HH:mm:ss`.

Contoh body POST/PUT:

```json
{
  "eventName": "Seminar Kampus",
  "description": "Seminar untuk mahasiswa",
  "location": "Auditorium",
  "startDate": "2026-11-15",
  "endDate": "2026-11-15",
  "startTime": "09:00:00",
  "endTime": "17:00:00",
  "status": "UPCOMING",
  "equipmentBorrowings": [
    { "equipmentId": 1, "quantityBorrowed": 2 }
  ],
  "crewIds": [1]
}
```

Buat alat dan kru terlebih dahulu. Ganti ID contoh dengan ID yang dikembalikan API.
POST mengembalikan **201 Created**; GET, PUT, PATCH, dan DELETE mengembalikan **200 OK**.
Validasi field menghasilkan **400**, data tidak ditemukan menghasilkan **404**, dan kesalahan internal menghasilkan **500**.

## Contributors

| Developer | Tanggung jawab |
| --- | --- |
| Randy Jeremi Tua Putra LGaol | Equipment Tools: pengelolaan peralatan dan stok |
| Frederich Irawan | Crew: pengelolaan data kru dan penugasan alat |
| Hananta Maulana | Calendar Schedule: pengelolaan kalender dan jadwal event |

## Contribution Guidelines

1. Ikuti package `com.team3.EOManagementApp` dan pola controller → service → repository.
2. Gunakan PascalCase untuk nama kelas dan camelCase untuk fungsi serta variabel.
3. Tambahkan komentar singkat pada fungsi baru atau yang diubah.
4. Pastikan build berhasil dan uji endpoint yang terdampak.
5. Jelaskan perubahan dan koordinasikan dengan developer modul terkait sebelum digabungkan.
6. Simpan kredensial pada file lokal/environment dan pastikan tidak masuk commit.

Repository publik dapat dilihat semua orang. Pemilik repository dapat menambahkan developer
melalui **Settings → Collaborators → Add people**. Developer perlu menerima undangan sebelum
dapat melakukan push; setelah itu gunakan branch fitur dan pull request untuk bekerja bersama.
