# Shared Database Configuration Guide

This guide explains how to set up a shared PostgreSQL database that all three environments (Windows, WSL2, and Kubernetes) can access.

## Architecture

```
Windows App (localhost:8081) ─┐
WSL2 Docker (localhost:8084) ─┼─→ Docker Postgres (localhost:5432)
Kubernetes Cluster ───────────┘
```

## Prerequisites

- Docker and Docker Compose installed
- WSL2 with Docker
- Kubernetes cluster (minikube/kind/k3s)

---

## Step 1: Start Shared Postgres Database

### Option A: Using Docker Compose (Recommended)

```bash
# Navigate to your project root
cd /path/to/TasklistApi

# Start Postgres
docker-compose up -d postgres

# Verify it's running
docker ps | grep tasklist-postgres

# Check logs
docker logs tasklist-postgres
```

### Option B: Using Docker CLI

```bash
docker run -d \
  --name tasklist-postgres \
  -p 5432:5432 \
  -e POSTGRES_DB=tasklist_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=mypassword \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:15
```

---

## Step 2: Configure Each Environment

### 🪟 Windows Application (Port 8081)

**application.properties** or **application.yml**:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tasklist_db
spring.datasource.username=postgres
spring.datasource.password=mypassword
spring.jpa.hibernate.ddl-auto=update
server.port=8081
```

**Test connection:**
```bash
# From Windows
psql -h localhost -p 5432 -U postgres -d tasklist_db
```

---

### 🐧 WSL2 Docker Container (Port 8084)

The GitHub Actions workflow handles this automatically, but for manual deployment:

**Get your Docker bridge IP:**
```bash
ip -4 addr show docker0 | grep -oP '(?<=inet\s)\d+(\.\d+){3}'
# Typically: 172.17.0.1
```

**Run container:**
```bash
docker run -d \
  --name tasklist-api \
  -p 8084:8081 \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://172.17.0.1:5432/tasklist_db" \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=mypassword \
  -e JWT_SECRET=your-secret-here \
  --add-host=host.docker.internal:host-gateway \
  ghcr.io/your-repo/tasklistapi:latest
```

**Alternative: Use Docker network:**
```bash
# Connect both containers to same network
docker network create tasklist-network
docker network connect tasklist-network tasklist-postgres
docker network connect tasklist-network tasklist-api

# Then use container name in URL:
# jdbc:postgresql://tasklist-postgres:5432/tasklist_db
```

---

### ☸️ Kubernetes Cluster

**Find your WSL2 IP address:**
```bash
# From WSL2
ip addr show eth0 | grep -oP '(?<=inet\s)\d+(\.\d+){3}'
# Example: 172.24.208.1
```

**Update the Endpoints in k8s/postgres-external-service.yaml:**
```yaml
apiVersion: v1
kind: Endpoints
metadata:
  name: postgres-db-endpoint
  namespace: tasklist
subsets:
- addresses:
  - ip: 172.24.208.1  # ← Your actual WSL2 IP
  ports:
  - port: 5432
```

**Apply Kubernetes manifests:**
```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/secret.yaml
kubectl apply -f k8s/postgres-external-service.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

**Test connection from a pod:**
```bash
kubectl run -it --rm debug --image=postgres:15 --restart=Never -n tasklist -- \
  psql -h postgres-db-endpoint -U postgres -d tasklist_db
```

---

## Step 3: Verify All Connections

### 1. Check Postgres is accessible

```bash
# From WSL2
docker exec -it tasklist-postgres psql -U postgres -d tasklist_db -c "\dt"
```

### 2. Check Windows app

```bash
curl http://localhost:8081/actuator/health
```

### 3. Check WSL2 Docker

```bash
curl http://localhost:8084/actuator/health
```

### 4. Check Kubernetes

```bash
kubectl port-forward -n tasklist svc/tasklist-api-service 8080:80
curl http://localhost:8080/actuator/health
```

---

## Troubleshooting

### Issue: Kubernetes pods can't connect to Postgres

**Check if port 5432 is accessible from pods:**
```bash
kubectl run -it --rm debug --image=busybox --restart=Never -n tasklist -- \
  nc -zv <your-wsl2-ip> 5432
```

**If it fails, check firewall:**
```bash
# From WSL2
sudo ufw allow 5432/tcp
# Or disable firewall temporarily
sudo ufw disable
```

### Issue: "Connection refused" from Docker container

**Solution 1: Use host.docker.internal**
```bash
-e SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/tasklist_db"
```

**Solution 2: Use Docker bridge IP**
```bash
-e SPRING_DATASOURCE_URL="jdbc:postgresql://172.17.0.1:5432/tasklist_db"
```

**Solution 3: Use Docker network**
```bash
docker network connect tasklist-network tasklist-api
-e SPRING_DATASOURCE_URL="jdbc:postgresql://tasklist-postgres:5432/tasklist_db"
```

### Issue: Stale connections

```sql
-- Check active connections
SELECT * FROM pg_stat_activity WHERE datname = 'tasklist_db';

-- Kill connections if needed
SELECT pg_terminate_backend(pid) 
FROM pg_stat_activity 
WHERE datname = 'tasklist_db' AND pid <> pg_backend_pid();
```

---

## GitHub Secrets Configuration

Ensure these secrets are set in your GitHub repository:

- `POSTGRES_PASSWORD`: Your database password
- `SPRING_DATASOURCE_USERNAME`: postgres
- `SPRING_DATASOURCE_URL`: jdbc:postgresql://172.17.0.1:5432/tasklist_db (for WSL2)
- `JWT_SECRET`: Your JWT secret key

---

## Network Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    Host Machine (WSL2)                  │
│                                                         │
│  ┌──────────────┐         Docker Network              │
│  │   Windows    │         (bridge: 172.17.0.0/16)      │
│  │     App      │                                       │
│  │  Port: 8081  │         ┌────────────────┐           │
│  └──────┬───────┘         │   Postgres     │           │
│         │                 │   Container    │           │
│         │                 │  Port: 5432    │           │
│         │                 │  IP: 172.17.0.2│           │
│         │                 └────────────────┘           │
│         │                         ▲                     │
│         │                         │                     │
│         └─────────────────────────┤                     │
│                                   │                     │
│  ┌──────────────┐                 │                     │
│  │  WSL2 Docker │                 │                     │
│  │  Container   │─────────────────┘                     │
│  │  Port: 8084  │                                       │
│  └──────────────┘                                       │
│                                                         │
└─────────────────────────────────────────────────────────┘
                         ▲
                         │
                         │ External Service
                         │
              ┌──────────┴──────────┐
              │   Kubernetes Pods   │
              │   (via ExternalName │
              │    or Endpoints)    │
              └─────────────────────┘
```

---

## Best Practices

1. **Use connection pooling** in your Spring Boot configuration:
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=5
```

2. **Enable health checks** to monitor database connectivity

3. **Use environment-specific configs** rather than hardcoding

4. **Backup your database regularly**:
```bash
docker exec tasklist-postgres pg_dump -U postgres tasklist_db > backup.sql
```

5. **Monitor connections**:
```sql
SELECT count(*) FROM pg_stat_activity WHERE datname = 'tasklist_db';
```