# ZeroDelta - Core API & Trading Engine ⚙️

ZeroDelta is a robust backend trading engine and portfolio management API. It features automated real-time market data ingestion, algorithmic portfolio drift scanning, and secure, fault-tolerant REST API endpoints.

🔗 **Live API Health Check:** [ZeroDelta API on Render](https://zerodelta-api.onrender.com/api/v1/health)  
🔗 **Frontend Client Repository:** [ZeroDelta-Frontend](https://github.com/Sahilll10/ZeroDelta-Frontend)  

## 🏗️ System Architecture

```mermaid
graph TD;
    A[External Market API <br/> CoinGecko] -->|JSON Market Data| B(MarketDataService <br/> @Scheduled Job);
    B -->|Type-Safe Number Casting| C[(Neon Serverless <br/> PostgreSQL)];
    C -->|Hibernate / JPA| D[DashboardController <br/> REST API];
    D -->|CORS Enabled| E[Vercel Frontend Client];
    
    subgraph Spring Boot Backend
    B
    D
    end
