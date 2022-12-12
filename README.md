# OnlineChat

## Class echoserver
Responsable l'écoute du client qui souhaite se connecter et lorsqu'il le fait, cela démarrera un nouveau thread
et les gérer.

- Constructeur pour pour configurer le socket du serveur

- Implementation de methode staringtServer pour maintenir les serveurs en cours d'exécution
	- While pour attente que le client se connecte
	- methode accept permettant le programme de s'arreter jusqu'a ce que le client connecte
	- class ClientHandler implementation de runnable, communication client/serveur
	- création de thread contentant de ClientHandler

Main:
- installation de port de chat en ligne (equivalent dans celui de la classe echoclient)
- execution des methodes utilisés dan la classe

## Class ClientHandler
- mise de client dans un arrayList permettant la communication entre clients en bouclant le Array
- affichage de nom de client dans le terminal lorsquils sont connecté
- creation de methode close pour éviter la repetition des conditions de fermeture des buffer et socket .
- rmClientHandler:  évite donc l'envoi et lecture des message lorsqu'un client est deconnecté


## Class echoclient
Ressemble comme clientHandler avec des differences:
- affichage dans le terminal client les infos qui lui sont necessaires comme:
	- les messages des autres user avec leur username grâce au méthode sendMessage()
	- messageListener pour lire les message du groupchat
	- création de méthode closeThis pour fermer les buffer + socket
- Main:
	- demande à l'utilisateur de créer son username
	- création du socket avec le host et le port
	- attribution de socket et username au client, lecture des messages de groupe
	
