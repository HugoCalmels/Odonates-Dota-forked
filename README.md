# Squirrel Arena

Copie publique d’un projet fullstack initialement développé dans un repository privé.  
L’historique des commits n’est donc pas visible ici.

Application web de gestion d’équipes et de matchs (projet d’apprentissage fullstack en contexte proche production).

Frontend encore accessible :  
https://app.dota-arena.fr/home  
(Authentication actuellement non fonctionnelle — projet non maintenu depuis 2024)

---

## Stack

Backend  
Java · Spring Boot · JPA / Hibernate · PostgreSQL · JWT / OpenID · Flyway

Frontend  
Angular · TypeScript · RxJS · NgRx

---

## Mon rôle

Développement majoritaire du backend et participation au frontend.  
Conception d’API sécurisée, gestion authentification, logique métier, base de données.

---

## Fonctionnalités principales

- Authentification OpenID + JWT
- Gestion utilisateurs / équipes / matchs
- Planification avec fuseaux horaires (UTC)
- API REST sécurisée
- SSE (temps réel partiel)

---

## Lancer en local (optionnel)

Le projet nécessite des variables d’environnement (DB, JWT, etc.).  
Voir `application.properties` et créer un `application-local.properties`.

---

## Note

Projet réalisé dans un cadre de mentorat technique.  
Ce repository sert uniquement de référence de code.
