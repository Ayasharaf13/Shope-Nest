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

----------------

## 💡 Technical Decisions & Challenges

* **Challenge:** Complex and deeply nested Shopify API responses were causing runtime crash risks due to missing or null data fields.
* **Solution:** Built a dedicated data mapping layer with default fallback values to safely parse nested API models and prevent `NullPointerException` crashes.
