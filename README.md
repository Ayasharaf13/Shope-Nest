# 🛍️ E-Commerce ShpeNest App

> **A modern Android e-commerce application delivering a streamlined fashion catalog for men, women, and kids across top global brands with real-time cart and inventory integration.**


## 🛠️ Tech Stack & Architecture

* **Language:** Kotlin
* **UI Framework:** Android SDK (XML Layouts)
* **Architecture:** MVVM (Model-View-ViewModel) 
* **Navigation:** Navigation Component 
* **Networking:** Retrofit Library
* **Payment Integration:** Payment Gateway API Services
* **Location & Maps:** Google Maps SDK
* **Local Database:** Room Database
* **Asynchronous Programming:** Kotlin Coroutines

------------------------

## 💡 Technical Decisions & Challenges

* **Challenge:** Complex and deeply nested Shopify API responses were causing runtime crash risks due to missing or null data fields.
* **Solution:** Built a dedicated data mapping layer with default fallback values to safely parse nested API models and prevent `NullPointerException` crashes.

-----------------------------------

## 🎬 App Flow Demo & Screenshots

<p align="center">
<img width="300" alt="video-shopenest_done-ezgif com-video-to-gif-converter" src="https://github.com/user-attachments/assets/be9322dc-a237-4f48-912e-5688e725aa6a" />
</p>

|<img src="https://github.com/user-attachments/assets/a1971b82-111f-4b4b-8c3f-de752f38d80c" width="220"/> | <img src="https://github.com/user-attachments/assets/2e689eb6-cca0-4f1d-86c8-e07c37c6e5f4" width="220"/> | <img src="https://github.com/user-attachments/assets/a36ce953-32a6-40d3-bf23-de5fcc82a890" width="220"/> |
| :---: | :---: | :---: |
|<img src="https://github.com/user-attachments/assets/67933d13-2213-4253-8f74-fd7b3d38d960" width="220"/> | <img src="https://github.com/user-attachments/assets/c5e7e294-7dce-4392-a504-548e80a2db86" width="220"/> | <img src="https://github.com/user-attachments/assets/24985fdc-e21f-4818-bf4d-4dff851deef6" width="220"/> |
|<img src="https://github.com/user-attachments/assets/82a2d5c4-472e-436e-a232-b4c59b741a12" width="220"/> | <img src="https://github.com/user-attachments/assets/800f0435-fbc2-4ae9-a37d-7bae1d4830e0" width="220"/> | <img src="https://github.com/user-attachments/assets/775d90e6-de89-4929-a26a-7dc046afadfd" width="220"/> |
|<img src="https://github.com/user-attachments/assets/92c3146b-cfd0-42b0-912a-096c45e5a23d" width="220"/> | <img src="https://github.com/user-attachments/assets/04f62a6d-581d-4a47-9479-c0e61f8796c2" width="220"/> | <img src="https://github.com/user-attachments/assets/b71accfa-6127-4bb5-a724-35d1dba5140c" width="220"/> |

<p align="center">
  <img src="https://github.com/user-attachments/assets/a5f534a3-9f90-4a4c-985f-54113c701ce9" width="220"/>
</p>



