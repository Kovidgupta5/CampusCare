<div align="center">
  <img src="app/src/main/res/drawable/nmims_logo1.png" alt="NMIMS Logo" width="100"/>
  <h1><b>CampusCare</b></h1>
  <p><b>A Comprehensive Multi-Role Campus Issue Reporting & Maintenance System</b></p>

  <!-- Badges -->
  <p>
    <img src="https://img.shields.io/badge/Platform-Android-green.svg" alt="Platform"/>
    <img src="https://img.shields.io/badge/Language-Java-orange.svg" alt="Language"/>
    <img src="https://img.shields.io/badge/Database-SQLite-blue.svg" alt="Database"/>
    <img src="https://img.shields.io/badge/Architecture-MVC_%2F_%2F_Multi--Role-yellow.svg" alt="Architecture"/>
  </p>
</div>

<br>

---

## 📖 **About CampusCare**
**CampusCare** is a robust Android application engineered to streamline campus maintenance and issue tracking. Built natively in Android Studio using Java and SQLite, it connects students, faculty, maintenance workers (resolvers), and administrators into a seamless, multi-tier operational workflow.

---

## ✨ **Key Features**

* 🔐 **Role-Based Authentication & Security:** Secure login system supporting four distinct user roles with automated routing and profile password management.
* 📍 **Dynamic Location & Category Forms:** Customized submission portals for Students and Faculty with hierarchical selectors (Academic Block, Hostel, Labs, Lecture Rooms, and Staff Rooms) and smart category filters.
* 📸 **Media Integration:** Built-in support to capture live photos using the device camera or select images from the gallery for issue reporting and resolution proof.
* 🔄 **Multi-Tier Resolution Workflow:** 
  1. **Submit:** User reports an issue (*Pending*).
  2. **Resolve:** Maintenance worker uploads proof of completion (*Work Completed - Awaiting Acceptance*).
  3. **Close:** Original reporter reviews proof and performs final sign-off (*Accepted / Closed*).
* 🛡️ **Admin Master Oversight:** Dedicated control panel to monitor all tickets, view system data, and manage user accounts (adding or removing students, faculty, and resolvers).
* 💾 **Local Persistence:** Fully functional offline data storage leveraging SQLite via `DatabaseHelper`.

---

## 👥 **User Roles & Workflows**

<details>
<summary><b>🎓 Student Panel</b></summary>
<ul>
  <li><b>File Complaint:</b> Report issues across Academic Blocks, Hostels, Washrooms, or Campus landmarks with severity levels and photo attachments.</li>
  <li><b>View Dashboard:</b> Track the live status of all personal submitted tickets.</li>
  <li><b>Profile:</b> Update account password and manage session security.</li>
</ul>
</details>

<details>
<summary><b>‍🏫 Faculty Panel</b></summary>
<ul>
  <li><b>Academic Portal:</b> Report issues specifically for Staff Rooms, Lecture Rooms (LR), and Labs with specialized asset selections (PC numbers, AC, Wi-Fi, switches).</li>
  <li><b>View Dashboard:</b> Monitor academic maintenance tickets.</li>
  <li><b>Profile:</b> Manage credentials and security settings.</li>
</ul>
</details>

<details>
<summary><b>🛠️ Resolver (Worker) Panel</b></summary>
<ul>
  <li><b>Task Portal:</b> View pending campus-wide maintenance tasks.</li>
  <li><b>Proof Upload:</b> Compulsory photo attachment of completed work before marking tasks as done.</li>
  <li><b>Profile:</b> Account and password management.</li>
</ul>
</details>

<details>
<summary><b>👑 Admin Panel</b></summary>
<ul>
  <li><b>Master Oversight:</b> Review, track, or delete any campus complaint across the entire system.</li>
  <li><b>User Management:</b> Add or remove student, faculty, and resolver accounts dynamically.</li>
  <li><b>Profile:</b> Admin security controls.</li>
</ul>
</details>

---

## 🛠️ **Tech Stack & Architecture**

* **Language:** Java
* **IDE:** Android Studio
* **Database:** SQLite (`SQLiteOpenHelper`, `Cursor`, `ContentValues`)
* **UI Components:** `RecyclerView`, `CardView`, `Spinner`, `RadioGroup`, `ScrollView`
* **Version Control:** Git / GitHub (`master` branch workflow)

---

## 🚀 **Getting Started & Installation**

1. **Clone the Repository:**
   ```bash
   git clone [https://github.com/Kovidgupta5/CampusCare.git](https://github.com/Kovidgupta5/CampusCare.git)
