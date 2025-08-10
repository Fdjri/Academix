# 🎓 Academix - Proyek UAS Pemrograman Berorientasi Objek (OOP)

Selamat datang di repositori Academix, sebuah proyek aplikasi desktop yang dikembangkan untuk memenuhi tugas Ujian Akhir Semester (Uas) mata kuliah Pemrograman Berorientasi Objek (PBO/OOP).

# 📜 Deskripsi Proyek

Academix adalah prototipe Sistem Informasi Akademik berbasis desktop. Aplikasi ini dirancang untuk mengelola data fundamental dalam sebuah lingkungan akademik, seperti data mahasiswa, mata kuliah, dan nilai.

Proyek ini dibangun sepenuhnya menggunakan bahasa pemrograman Java dengan memanfaatkan Apache NetBeans IDE. Fokus utama dari proyek ini adalah penerapan konsep-konsep inti dari Pemrograman Berorientasi Objek (OOP), antara lain:

- Encapsulation: Membungkus data dan metode dalam sebuah class.

- Inheritance: Membuat class turunan untuk mewarisi properti dan metode dari class induk.

- Polymorphism: Menggunakan objek dari class yang berbeda melalui satu antarmuka yang sama.

- Abstraction: Menyembunyikan detail implementasi yang kompleks dan hanya menunjukkan fungsionalitas yang esensial.

## ✨ Fitur Utama

Aplikasi ini memiliki beberapa fungsionalitas dasar, seperti:

- Sistem Login untuk otentikasi pengguna.

- Manajemen Data Mahasiswa (Tambah, Baca, Ubah, Hapus - CRUD).

- Manajemen Data Mata Kuliah (CRUD).

- Pengelolaan dan Input Nilai Mahasiswa.

## 🛠️ Teknologi yang Digunakan

- Bahasa Pemrograman: Java

- IDE: Apache NetBeans IDE

- UI Toolkit: Java Swing (GUI Builder)

- Database: MySQL (dikelola melalui XAMPP)

- Konektor Database: JDBC (Java Database Connectivity)

## 🚀 Panduan Menjalankan Proyek

Berikut adalah langkah-langkah untuk mengompilasi dan menjalankan proyek ini menggunakan NetBeans IDE.

### Prasyarat

Pastikan Anda telah menginstal perangkat lunak berikut:

1. JDK (Java Development Kit)

2. Apache NetBeans IDE

3. XAMPP (untuk Web Server Apache dan Database MySQL)

### Langkah-langkah Setup

**1. Clone Repositori**

```
git clone https://github.com/Fdjri/Academix.git
```

**2. Setup Database**

- Jalankan modul Apache dan MySQL dari XAMPP Control Panel.

- Buka browser dan akses ``http://localhost/phpmyadmin``.

- Buat database baru dengan nama (misalnya) ``db_academix``.

- Pilih database yang baru dibuat, lalu klik tab Import.

- Pilih file ``.sql`` yang tersedia di dalam folder proyek ini (misalnya di dalam folder ``database/`` atau di root). Klik Go untuk mengimpor.

**3. Buka Proyek di NetBeans**

- Jalankan Apache NetBeans IDE.

- Pilih menu File > Open Project....

- Arahkan ke folder ``Academix`` yang telah Anda clone, lalu klik Open Project.

**4. Konfigurasi Koneksi Database di Java**

- Di dalam NetBeans, cari file yang bertanggung jawab untuk koneksi database (misalnya ``Koneksi.java`` atau ``DatabaseConnection.java``).

- Pastikan detail koneksi (nama database, user, password) sesuai dengan konfigurasi XAMPP Anda.
```
// Contoh file koneksi
String url = "jdbc:mysql://localhost:3306/db_academix"; // Pastikan nama DB sesuai
String user = "root";
String password = "";
```

**5. Tambahkan Library JDBC (Jika Diperlukan)**
- Jika NetBeans menunjukkan error terkait ``mysql-connector``, klik kanan pada folder Libraries di panel proyek.

- Pilih Add JAR/Folder....

- Arahkan ke file ``mysql-connector-java-x.x.x.jar``(Anda mungkin perlu mengunduhnya terlebih dahulu jika belum ada).

**6. Jalankan Proyek**

- Klik kanan pada file utama yang berisi method ``main()`` (misalnya ``Main.java`` atau ``Login.java``).

- Pilih Run File atau tekan ``Shift + F6``.

- Alternatif lain, klik tombol Run Project (ikon ▶ hijau) pada toolbar NetBeans.
