# Android Internship Task 2025

## Summary

This project is a **Weather Tracking App** built for the Android Internship Task 2025.  
It meets the following requirements:

✅ Get the user's location using device GPS (latitude, longitude)  
✅ Get current weather and 5-day forecast using a weather API  
✅ Two screens:  
- **Current Weather Screen**  
- **5-Day Weather Forecast Screen**  
✅ Swipe-to-refresh on the current weather screen  
✅ Handles offline state and shows an error message  
✅ **BONUS:** Caches the last retrieved data for offline use  
✅ **BONUS:** Handles configuration changes like screen rotation

---

## Architecture & Design

The project follows **Clean Architecture** with three clear layers:

- **Data Layer**
  - Remote data source using `HttpURLConnection`
  - Local data source using `SQLite`
  - Location service implementation
  - Repository: decides whether to serve data from remote or local source based on network state

- **Domain Layer**
  - Contains use cases
  - Prepares logic and data for presentation

- **Presentation Layer**
  - Contains:
    - Two `ViewModel`s
    - Two `Fragment`s  
  - Uses:
    - `LiveData` for data observation
    - `SwipeRefreshLayout` for refreshing weather data

---

## Technical Details

- **Languages**: Kotlin + XML  
- **Supports**:
  - Screen rotation (configuration changes)
  - Multiple screen sizes
  - Light and dark themes

- **Used Tools**:
  - `HttpURLConnection` (no third-party libraries like Retrofit or Volley)
  - `SQLite` (no Room)

---

## Screenshots

| First Screen - Dark (Landscape) | First Screen - Dark (Portrait) |
|---------------------------------|--------------------------------|
| ![first_dark_landscape](screenshots/first_dark_landscape.jpg) | ![first_dark_portrait](screenshots/first_dark_portrait.jpg) |

| First Screen - Light (Landscape) | First Screen - Light (Portrait) |
|----------------------------------|---------------------------------|
| ![first_light_landscape](screenshots/first_light_landscape.jpg) | ![first_light_portrait](screenshots/first_light_portrait.jpg) |

| Second Screen - Dark (Landscape) | Second Screen - Dark (Portrait) |
|----------------------------------|---------------------------------|
| ![second_dark_landscape](screenshots/second_dark_landscape.jpg) | ![second_dark_portrait](screenshots/second_dark_portrait.jpg) |

| Second Screen - Light (Landscape) | Second Screen - Light (Portrait) |
|-----------------------------------|----------------------------------|
| ![second_light_landscape](screenshots/second_light_landscape.jpg) | ![second_light_protrait](screenshots/second_light_protrait.jpg) |

---


