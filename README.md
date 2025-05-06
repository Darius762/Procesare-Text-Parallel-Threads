# Procesare Text - Paralelizare cu Threads

Acest proiect implementează o soluție **paralelă** pentru procesarea fișierelor text de dimensiuni foarte mari (până la 3GB), folosind:

- `ExecutorService` pentru gestionarea threadurilor
- `FileChannel` și `ByteBuffer` pentru citirea rapidă, pe bucăți (16MB)
- `ConcurrentHashMap` pentru contorizarea frecvenței cuvintelor fără blocaje

---

## 📌 Scop

Numărarea frecvenței cuvintelor din fișiere text masive, într-un mod eficient, sigur și scalabil. Programul afișează:

- Dimensiunea fișierului
- Primele 20 cele mai frecvente cuvinte
- Timpul total de execuție

---

## ⚙️ Tehnologii

- Limbaj: Java 21
- Concepte folosite:
  - `ExecutorService` (thread pool)
  - `ConcurrentHashMap`
  - `MappedByteBuffer` (`FileChannel.map`)
  - Procesare paralelă pe bucăți fixe de fișier (chunk-uri de 16MB)

---

## 🚀 Cum se rulează

1. Clonează repository-ul:
   ```bash
   git clone https://github.com/Darius762/Procesare-Text-Parallel-Threads.git
   cd Procesare-Text-Parallel-Threads
