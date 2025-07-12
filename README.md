# 🎧 Music Listening Data Analysis using Hadoop MapReduce

This project analyzes large-scale music listening data from **XYZ.com** using **Hadoop MapReduce** to extract meaningful insights about user interactions with tracks.

## 📌 Project Overview

The aim of this project is to process and analyze massive datasets of music listening behavior using the Hadoop MapReduce framework. By leveraging distributed computing, the program efficiently computes the following metrics:

- ✅ Total number of **unique listeners**
- 📤 Count of **track shares**
- 📻 Number of **radio plays**
- ▶️ Total number of **track listens**
- ⏭️ Count of **track skips**

These metrics help in understanding user engagement, track popularity, and listening patterns, which can be valuable for improving user experience, personalizing recommendations, and driving business decisions.

## 🛠️ Technologies Used

- **Java** for writing MapReduce jobs  
- **Hadoop** for distributed data processing  
- **HDFS** (Hadoop Distributed File System) for storage  
- **Linux CLI** for job execution and data handling  

## 📂 Dataset

The dataset used includes logs of user interactions with various tracks, stored in `music_log.csv`:

```
user_id, track_id, action_type
```
Where `action_type` can be one of: `listen`, `skip`, `radio`, `share`.

> **Note**: The dataset is anonymized and used for educational purposes only.

## ⚙️ How It Works

1. **Mapper**: Processes each line from `music_log.csv` and emits key-value pairs based on the action type.
2. **Reducer**: Aggregates the counts per `track_id` to compute total listens, shares, skips, and unique listeners.
3. **Output**: Final results per track with all aggregated insights.

## 📈 Sample Output

```
TrackID: 12345
Total Listens: 1023
Radio Plays: 438
Skips: 87
Shares: 56
Unique Listeners: 912
```

## 🚀 Getting Started

To run the project:

```bash
# Compile the Java code
hadoop com.sun.tools.javac.Main MusicStats.java

# Create a JAR
jar cf musicstats.jar MusicStats*.class

# Run on Hadoop
hadoop jar musicstats.jar MusicStats /input/music_log.csv /output
```

## 📌 Folder Structure

```
.
├── MusicStats.java          # Main MapReduce code
├── music_log.csv            # Input dataset
├── output/                  # Output after job execution
└── README.md                # Project description
```

## 📜 License

This project is intended for educational and non-commercial use only.
