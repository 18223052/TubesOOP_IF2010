# Tugas Besar IF2010 Pemrograman Berorientasi Objek

## Anggota Kelompok 4 / K-05
| Nama                             | NIM       |
|----------------------------------|-----------|
| Mochamad Ikhbar Adiwinangun      | 18223050  |
| Fathimah Nurhumaida Ramadhani    | 18223052  |
| Irdina Ilmuna Yosapat            | 18223060  |
| Sebastian Albern Nugroho         | 18223074  |


# Spakbor Hills

## Deskripsi Game

# 🌾 That Time I Became a Farming Game Dev to Save the World

## 📖 Latar Belakang

Setelah kegagalan ke-420 dalam usahanya menguasai Danville, Dr. Asep Spakbor—seorang ilmuwan jenius (dan agak gila)—akhirnya menyerah. Tak ada ledakan. Tak ada kekacauan. Hanya kesunyian, kelelahan, dan rasa kalah yang menyengat. 

Dunia tidak jatuh ke tangan Dr. Asep. Yang jatuh justru nilai tukar mata uang Danville, harga bahan baku inator, dan saldo rekeningnya.

Merasa dunianya hancur, Dr. Asep memutuskan untuk pensiun dari dunia kejahatan dan banting setir: menjadi petani.

> “Kalau aku nggak bisa menguasai dunia, setidaknya bisa lah menguasai sawah!”  
> — *Dr. Asep Spakbor*

Sayangnya, pertanian itu lebih kejam dari dunia supervillain. Tanamannya gagal tumbuh, alat pertanian rusak, dan bahkan cacing pun menolak tinggal. Yang tumbuh hanya rasa frustrasi.

Di sinilah Organisasi Warga Cool Abiez (O.W.C.A.) melihat celah. Untuk pertama kalinya, Dr. Asep tidak ingin menciptakan alat penghancur dunia. Ia hanya ingin... bertani. Dan mungkin, ini adalah satu-satunya kesempatan untuk membawa perdamaian sejati ke Danville.

### 👤 Peran Pemain

Kamu adalah seorang agen biasa di O.W.C.A. yang entah bagaimana dipercaya untuk menyelamatkan dunia. Track record-mu? Main Minesweeper di jam kerja. Tapi malam itu, kamu dipanggil langsung oleh Agen Purry dalam sebuah briefing rahasia.

### 🎯 Misi Kamu

Buatlah sebuah **game bertani** untuk Dr. Asep Spakbor.  
Game ini harus:
- Cukup **seru** untuk membuat Asep mau belajar.
- Cukup **aman** agar tidak jadi inator baru.
- Cukup **mendidik** untuk mengubah mantan ilmuwan jahat menjadi petani sejati.

Kamu tidak tahu kenapa kamu yang dipilih. Tapi kamu tahu satu hal:  
**Jika seekor platipus sudah mengangguk serius, berarti dunia benar-benar butuh bantuan.**

---

> 🎮 **Selamat datang di Spakbor Hills.**  
> Tempat di mana kamu tidak hanya menanam kentang, tapi juga harapan terakhir umat manusia.


**Tujuan Permainan:**
Meskipun permainan ini dirancang untuk berjalan tanpa akhir (infinite gameplay), terdapat beberapa *milestone* yang ingin dicapai pemain:
1.  Memiliki gold sebesar 17.209g.
2.  Sudah menikah.

Jika salah satu milestone tercapai, game akan menampilkan statistik akhir permainan.

**Fitur Utama (Berdasarkan Spesifikasi):**
* **Player:** Karakter utama yang akan anda mainkan
* **Farm & World Map:** Area permainan dengan berbagai lokasi termasuk rumah pemain, rumah NPC, area farm map, dan area world map.
* **NPC:** Terdapat 6 NPC (Mayor Tadi, Caroline, Perry, Dasco, Emily, Abigail) dengan preferensi item dan sistem relationship.
* **Items:** Beragam item seperti benih, ikan, crops, makanan, dan peralatan.
* **Aktivitas:** Bertani (membajak, menanam, menyiram, memanen), memancing, memasak, berinteraksi dengan NPC, tidur, dll.
* **Sistem Waktu, Musim, Cuaca:** Mempengaruhi gameplay dan ketersediaan sumber daya.

## Prasyarat

Sebelum Anda dapat mengompilasi dan menjalankan proyek ini, pastikan Anda memiliki perangkat lunak berikut terinstal di sistem Anda:

1.  **Java Development Kit (JDK)**: Versi 8 atau yang lebih baru direkomendasikan (sesuaikan jika proyek menggunakan fitur dari versi yang lebih tinggi, misal JDK 11 atau 17). Anda bisa mengunduhnya dari [Oracle JDK](https://www.oracle.com/java/technologies/javase-downloads.html) atau menggunakan alternatif seperti [OpenJDK](https://openjdk.java.net/).
2.  **Gradle**: Meskipun proyek ini menyertakan Gradle Wrapper (`gradlew`) yang akan mengunduh versi Gradle yang sesuai secara otomatis, memiliki pemahaman dasar tentang Gradle bisa membantu.

## Cara Kompilasi

Proyek ini menggunakan Gradle untuk manajemen dependensi dan proses build. Anda tidak perlu menginstal Gradle secara manual jika menggunakan Gradle Wrapper.

1.  **Buka Terminal atau Command Prompt.**
2.  **Navigasi ke direktori root proyek** (direktori yang berisi file `gradlew` dan `build.gradle`, yaitu `TubesOOP_IF2010`).
    ```bash
    cd path/ke/TubesOOP_IF2010
    ```
3.  **Untuk mengompilasi proyek dan membuat file JAR yang dapat dieksekusi (executable JAR):**
    * Pada Linux atau macOS:
        ```bash
        ./gradlew build
        ```
    * Pada Windows:
        ```bash
        gradlew build
        ```

## Cara Menjalankan Program

Ada beberapa cara untuk menjalankan program setelah berhasil dikompilasi:

### 1. Menggunakan Perintah `run` dari Gradle (Cara paling mudah)

Gradle biasanya dikonfigurasi untuk menjalankan aplikasi secara langsung:

* Pada Linux atau macOS:
    ```bash
    ./gradlew run
    ```
* Pada Windows:
    ```bash
    gradlew run
    ```

## Catatan Tambahan

* Jika Anda mengalami masalah terkait izin saat menjalankan `./gradlew` pada Linux/macOS, pastikan file tersebut memiliki izin eksekusi: `chmod +x gradlew`.
* Proses build pertama kali mungkin memerlukan waktu lebih lama karena Gradle akan mengunduh dependensi yang dibutuhkan.
* Pastikan path ke JDK sudah terkonfigurasi dengan benar di variabel lingkungan sistem Anda (`JAVA_HOME`).
* Game ini memiliki fitur simpan/muat data permainan menggunakan file `savegame.json` di direktori `app/`.
* Link untuk booklet dapat dilihat pada file [canva](https://www.canva.com/design/DAGo6biqQfc/-lwdmv0niFD3v1NWxHoOCQ/watch?utm_content=DAGo6biqQfc&utm_campaign=designshare&utm_medium=link2&utm_source=uniquelinks&utlId=hc87d41c26d) ini.
---

