# Energy Service backend (Demo)

Progetto realizzato con Java 21.

## Descrizione 

Simulazione di web app per la gestione di clienti e fatture per società di energia. Offre un'interfaccia intuitiva sia per gli operatori interni che per i clienti finali e la consultazione di una dashboard.

Permette:
- operazioni CRUD
- ricerca clienti con filtri
- ricerca fatture con filtri
- registrazione nuovi utenti e login
- creazione nuove fatture
- visualizzazione dashboard con report

Tecnologie utilizzate:
- Java (interazione db con JPA, controller protetti con Spring Security, controlli di validazione)
- JWT per sistema di autenticazione

Dati:
- Rest API con salvataggio dati su db PostgreSQL su neon.tech

## ENV FILE => NECESSARIO PER L'AVVIO DELL'APPLICAZIONE
  Creare file .env dentro la root del progetto e definire le seguenti variabili d'ambiente: 

  `DB_NAME=[nome db]` (crea un nuovo db locale o remoto ed inserisci i dati richiesti \
  ATTENZIONE!!! la variabile DB_NAME deve includere anche l'host: es. localhost\nome_db) \
  `DB_USER=[utente db]` \
  `DB_PASSWORD=[password db]` \
  `JWT_SECRET=[segreto jwt]` (è possibile ottenerne una qui [JwtSecret.com](https://jwtsecret.com/generate)) \
  `CLOUD_NAME=dgevh7ksg` (creare un account gratuito su [cloudinary.com](https://cloudinary.com/) ed inserire i dati richiesti ) \
  `CLOUD_API_KEY=745387371774771` \
  `CLOUD_API_SECRET=fcAbCakLGdV1i60mX4WN7HJ5YnI`
  `MAIL_HOST=[smtp server di posta]` \
  `MAIL_PASSWORD=[password utente server di posta]` \
  `MAIL_USERNAME=[username server di posta]` \
  `FRONTEND_URL=[url frontend - es. http:\\localhost:4200]`

## Eseguire l'applicazione con Docker (consigliato)

- Se non hai Docker installato, segui le istruzioni dal sito ufficiale: [Docker](https://docs.docker.com/desktop/)
- Esegui `docker compose up -d` per creare ed eseguire il container per avviare l'applicazione con i parametri specificati nel file docker-compose.yml 

  oppure
- Esegui `docker build . -t energyservice-be-app` per creare l'immagine dell'applicazione con tutte le dipendenze necessarie al suo funzionamento
- Esegui `docker run -d -p 8080:8080 --env-file .env energyservice-be-app` per creare ed avviare il container basato sull'immagine appena creata e specificando il file .env con le variabili necessarie
- Naviga `http://localhost:8080/swagger-ui/index.html` per consultare la documentazione degli endpoint e testarli

## Eseguire l'applicazione sulla tua macchina locale (sconsigliato)
- Assicurati di eseguire il progetto con la versione 21 di Java
- Imposta le variabili d'ambiente (specificate sopra) all'interno del tuo IDE (es. IntelliJ) o sostiiscile dentro il file src/main/resources/application.properties
- Avvia l'applicazione
- Naviga `http://localhost:8080/swagger-ui/index.html` per consultare la documentazione degli endpoint e testarli
- L'applicazione si aggiornarà automaticamente ad ogni modifica del file sorgente.

## Link al frontend
- [EnergyService Frontend Web App](https://energy-services.netlify.app)
- [EnergyService Frontend GitHub](https://github.com/adrianagaglio/EnergyServices-Angular)
