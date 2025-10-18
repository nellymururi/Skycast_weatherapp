# 🌦️ SkyCast Weather App

**SkyCast** is a sleek and user-friendly weather mobile application built with **Kotlin** and **Jetpack Compose**.  
It allows users to **view real-time weather updates** for their **current location** as well as **search and view weather details** for other cities around the world.

The app fetches live weather data from the **OpenWeather API**, providing accurate and timely updates on temperature, humidity, and weather conditions.

---

## 📱 Features

- 🌍 **Current Location Weather:** Automatically detects your current location and displays up-to-date weather information.  
- 🔍 **City Search:** Search for any city worldwide to view its current weather details.  
- 🌡️ **Detailed Weather Info:** Displays temperature, humidity, and weather conditions.  
- 🎨 **Modern UI:** Clean and elegant interface built using **Jetpack Compose**.  

---

## 🧠 Tech Stack

- **Language:** Kotlin  
- **Framework:** Jetpack Compose  
- **API:** [OpenWeather API](https://openweathermap.org/api)  
- **Architecture:** MVVM (Model-View-ViewModel)  
- **Networking:** Retrofit  
- **Location:** Android Location Services  

---

## 🚀 Getting Started

Follow these steps to get a local copy up and running:

### Prerequisites

- Android Studio (latest version recommended)
- Git installed on your machine
- A valid [OpenWeather API key](https://openweathermap.org/api)

### Installation

1. **Clone the repository**
  ```sh

   git clone https://github.com/nellymururi/Skycast_weatherapp.git
```

2. **Open the project in Android Studio**
Go to File → Open and select the cloned project folder.

3. **Add your API key**
- Navigate to your code where the API key is defined (usually in RetrofitClient.kt).
- Replace the placeholder with your actual API key:
  ```sh
   const val API_KEY = "YOUR_API_KEY"
  ```

4. **Build and Run**
  Connect an Android device or use an emulator, then click Run ▶️ in Android Studio.
**🧩 Folder Structure**
```sh

SkyCast/
│
├── app/src/main/java/com/example/letemps/
│   ├── screens/                # UI Screens (Home, Search, Settings)
│   ├── navigation/             # Bottom navigation and route setup
│   ├── weather/data/           # API, models, and network handling
│   ├── weather/viewmodel/      # ViewModel for data management
│   └── ui/theme/               # Colors, typography, and theme
│
├── app/src/main/res/           # Resources (drawables, layouts, values)
└── build.gradle.kts
```


**💡 Future Enhancements**
- Add hourly and weekly forecasts
- Integrate weather alerts
- Add animations for different weather conditions
- Enable offline caching

**📜 License**
License
This project is licensed under the [MIT License](LICENSE).
