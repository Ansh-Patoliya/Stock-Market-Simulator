# 📈 Stock Market Simulation System

A real-time stock trading simulator built with Java and JavaFX. It allows users to register, log in, add wallet balance, and simulate buying/selling of stocks with dynamic price updates using multithreading.

---

## 🚀 Features

- 🔐 User Registration & Login
- 💵 Add Wallet Balance
- 📊 Live Stock Price Updates (with `ScheduledExecutorService`)
- 🛒 Buy & Sell Stocks
- 📂 View Portfolio with Current Value
- 📉 Bull/Bear Market Mode (Trend Simulation)
- 🧵 Multithreading to simulate live stock market

---

## 🛠️ Tech Stack

| Technology     | Description                        |
|----------------|------------------------------------|
| Java           | Core logic and backend             |
| JavaFX         | UI for all screens                 |
| PostgreSQL     | Database for user/stock data       |
| JDBC           | Database connectivity              |
| Threads        | Simulate real-time stock movement  |
| IntelliJ IDEA  | Development Environment            |

---

## 📁 Project Structure

src/
│
├── dao/ # Data access layer (UserDAO, StockDAO, etc.)
├── model/ # Java models (User, Stock, PortfolioItem)
├── Manager/ # JavaFX controllers (LoginManager, DashboardManager, etc.)
├── ui/ # Scene navigation helper (Connection.java)
├── util/ # Utilities like DB connection and session
└── XML/ # FXML files for UI layouts


---

## 🔧 How to Run

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/stock-market-simulation.git
2. **Open the Project

Open in IntelliJ IDEA

Ensure JavaFX is configured properly

3. **Set Up PostgreSQL Database

Create a new database, e.g., stock_market_db

Update credentials in DBConnection.java

4. **Run the Application

Start the app via Main.java

🧪 Simulated Stock Price Logic
Every stock is assigned a base price and volatility.

ScheduledExecutorService updates stock prices every 5 seconds.

Bull and Bear market trends influence global price directions.

👨‍💻 Developer Info
Patoliya Ansh Amitbhai
LJIET – B.E. Computer Engineering
Batch: 2024 – 2028

