# sh-server-service

Ce service à pour principale tache d'orchestrer l'infra MC. Il va offrir un système d'inter connexion entre chaque instance des serveurs minecraft finaux.
Il aura également la responsabilité de créer des serveurs à la demande pour les prochaines mises à jours.
Il jouera également le role de HealthChecker, de façon a surveiller en permanence l'état des anciens/nouveaux serveurs.
Si un serveur tombe, il est automatiquement retiré du réseau proxy de nos serveurs d'entrée (Bungeecord like).
