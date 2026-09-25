# AutoLoc — Système de Gestion de Location de Voitures

> Projet développé dans le cadre du cours de Génie Logiciel — ESPRIT  
> Équipe projet | Année universitaire 2025/2026

---

## Description

**AutoLoc** est une application de gestion de location de véhicules destinée aux agences de location. Elle permet de gérer les clients, les contrats de location, la flotte de véhicules, et les opérations administratives de l'agence.

---

## Acteurs du système

| Acteur | Rôle |
|---|---|
| **Client** | Consulte les véhicules disponibles, effectue et suit ses réservations |
| **Agent d'agence** | Gère les contrats de location, enregistre les retours, assiste les clients |
| **Responsable d'agence** | Supervise l'activité de l'agence, gère la flotte et les agents |
| **Administrateur** | Administre l'ensemble du système, gère les comptes et les paramètres |

---

## Cas d'utilisation identifiés (Séance 1)

### Client
- S'inscrire / Se connecter
- Consulter les véhicules disponibles
- Effectuer une réservation
- Annuler une réservation
- Consulter l'historique de ses locations
- Payer en ligne

### Agent d'agence
- Créer / modifier un contrat de location
- Enregistrer le retour d'un véhicule
- Vérifier la disponibilité d'un véhicule
- Gérer les dossiers clients
- Émettre une facture

### Responsable d'agence
- Gérer la flotte de véhicules (ajout, modification, suppression)
- Consulter les statistiques de l'agence
- Gérer les agents (création de comptes, affectation)
- Générer des rapports d'activité

### Administrateur
- Gérer les comptes utilisateurs (tous rôles)
- Configurer les paramètres du système
- Gérer les agences
- Consulter les journaux système (logs)
- Sauvegarder / restaurer les données

---

## Stack technique (prévisionnel)

- **Back-end** : Java (Spring Boot)
- **Base de données** : MySQL
- **Build** : Maven
- **Front-end** : (à définir)
- **Versioning** : Git / GitHub

---

## Installation et lancement

```bash
# Cloner le dépôt
git clone https://github.com/mansournour/AutoLoc.git

# Aller dans le répertoire
cd AutoLoc

# Compiler avec Maven
mvn clean install

# Lancer l'application
mvn spring-boot:run
```

---

## Équipe

| Nom | Rôle |
|---|---|
| Nour Mansour | Développeur / Chef de projet |

---

*Dernier commit : Initialisation du dépôt — Atelier 0*
